package com.hillavas.filmvazhe.screen.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hillavas.filmvazhe.screen.MovieDialogListActivity;
import com.hillavas.filmvazhe.MyApplication;
import com.hillavas.filmvazhe.R;
import com.hillavas.filmvazhe.adapters.MoviesAdapter;
import com.hillavas.filmvazhe.model.Movie;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.BindView;
import cz.msebera.android.httpclient.Header;

public class MoviesListFragment extends Fragment {


    private static final String ARG_PARAM1 = "type";
    @BindView(R.id.grid_movies)
    RecyclerView gridMovie;


    @BindView(R.id.layout_load_more)
    RelativeLayout layoutLoadMore;

    @BindView(R.id.layout_progress)
    FrameLayout layoutProgress;


    MoviesAdapter moviesAdapter;
    ArrayList<Movie> movieArrayList = new ArrayList<>();
    Snackbar snackbar;
    private Integer startPosition = 0;
    private boolean notMoreAnyMovie = false;
    private boolean startState = false;
    private Integer type;


    public MoviesListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MoviesListFragment newInstance(Integer param1) {
        MoviesListFragment fragment = new MoviesListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
       // args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getInt(ARG_PARAM1);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_movies_list, container, false);
        ButterKnife.bind(this, v);

        GridLayoutManager gridLayoutManager=new GridLayoutManager(getContext(),3);
        gridMovie.setLayoutManager(gridLayoutManager);

        gridMovie.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    startState = false;
                    getMovies();
                    //Call your method here for next set of data
                }
            }
        });

//        gridMovies.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView absListView, int i) {
//
//            }
//
//            @Override
//            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
//                if (i + i1 >= i2 && !notMoreAnyMovie && startState) {
//
//                    startState = false;
//                    getMovies();
//
//                }
//            }
//        });

//        gridMovie.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//            }
//        });


        getMovies();

        return v;

    }

    void getMovies() {

        MyApplication.apiVideokart.getMovies(type, movieArrayList.size(), 12, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
                layoutLoadMore.setVisibility(View.VISIBLE);

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                //Log.v("result", content);

                JsonObject result = new JsonParser().parse(new String(responseBody)).getAsJsonObject();
                JsonObject status = result.getAsJsonObject("status");

                if (status.get("code").getAsInt() == 200) {


                    JsonArray array = result.get("data").getAsJsonArray();

                    if (array.size() == 0) {
                        notMoreAnyMovie = true;
                        layoutLoadMore.setVisibility(View.GONE);
                    } else {

                        for (int i = 0; i < array.size(); i++) {
                            movieArrayList.add(new Movie(array.get(i).getAsJsonObject()));
                        }
                        if (moviesAdapter == null) {
                            moviesAdapter = new MoviesAdapter(movieArrayList, new MoviesAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(Movie item) {
                                    Intent in = new Intent(getContext(), MovieDialogListActivity.class);
                                    in.putExtra("movie", item);
                                    startActivity(in);
                                }
                            });
                            gridMovie.setAdapter(moviesAdapter);
                        } else {
                            moviesAdapter.notifyDataSetChanged();
                        }
                    }

                } else {
                    SnackbarManager.show(
                            Snackbar.with(getContext()) // context
                                    .textTypeface(MyApplication.getTypeFace())
                                    .duration(Snackbar.SnackbarDuration.LENGTH_SHORT)
                                    .swipeToDismiss(true)
                                    .text(status.get("message").getAsString()) // text to display

                            , getActivity()); // activity where it is dis
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }

            @Override
            public void onFinish() {
                super.onFinish();
                layoutLoadMore.setVisibility(View.GONE);
                if (layoutProgress.getVisibility() == View.VISIBLE)
                    layoutProgress.setVisibility(View.GONE);
                startState = true;

            }
        });

    }

}
