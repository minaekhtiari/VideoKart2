package com.hillavas.filmvazhe.screen.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hillavas.filmvazhe.adapters.BooksAdapter;
import com.hillavas.filmvazhe.adapters.MostClipAdapter;
import com.hillavas.filmvazhe.model.Book;
import com.hillavas.filmvazhe.model.Clip;
import com.hillavas.filmvazhe.model.Movie;
import com.hillavas.filmvazhe.screen.FavoriteClipsListActivity;
import com.hillavas.filmvazhe.screen.LessonsActivity;
import com.hillavas.filmvazhe.screen.ProfileActivity;
import com.hillavas.filmvazhe.MyApplication;
import com.hillavas.filmvazhe.R;
import com.hillavas.filmvazhe.adapters.MoviesAdapter;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.listeners.ActionClickListener;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.BindView;
import cz.msebera.android.httpclient.Header;

public class MainFragment extends Fragment implements View.OnClickListener {


    //    @BindView(R.id.list_books)
//    RecyclerView listBook;
    @BindView(R.id.list_book)
    RecyclerView listBook;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.lbl_title)
    TextView lblTitle;

    @BindView(R.id.btn_fav)
    ImageButton btnFav;
    @BindView(R.id.btn_profile)
    ImageButton btnProfile;
    @BindView(R.id.fab_store)
    FloatingActionButton floatingActionButton;
    @BindView(R.id.layout_loading)
    FrameLayout layoutLoading;
    @BindView(R.id.layout_main)
    RelativeLayout layoutMain;

    BooksAdapter booksAdapter;
    MostClipAdapter mostLikedClipAdapter, MostVisitedClipAdapter;

    ArrayList<Book> bookArrayList = new ArrayList<>();
    ArrayList<Clip> likedArrayList = new ArrayList<>();
    ArrayList<Clip> visitedArrayList = new ArrayList<>();


    MoviesAdapter moviesAdapter;

    ArrayList<Movie> movieArrayList = new ArrayList<>();
    private Integer startPosition = 0;
    private boolean notMoreAnyMovie = false;
    private boolean startState = true;


    public MainFragment() {
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
    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, v);

        floatingActionButton.setVisibility(View.GONE);

        LinearLayoutManager bookManagerLayout=new LinearLayoutManager(MyApplication.getContext(), LinearLayoutManager.VERTICAL, false);

        listBook.setLayoutManager(bookManagerLayout);

        btnFav.setOnClickListener(this);
        btnProfile.setOnClickListener(this);
        floatingActionButton.setOnClickListener(this);

        toolbar.setBackgroundColor(Color.parseColor(MyApplication.getSharedPreferences().getString("colorMain","#fafafa")));
        btnFav.setColorFilter(Color.parseColor(MyApplication.getSharedPreferences().getString("colorSecond","#212121")));
        btnProfile.setColorFilter(Color.parseColor(MyApplication.getSharedPreferences().getString("colorSecond","#212121")));
        lblTitle.setTextColor(Color.parseColor(MyApplication.getSharedPreferences().getString("colorSecond","#212121")));

        if (!getContext().getPackageName().equals("com.hillavas.videokart")) {

           btnProfile.setVisibility(View.GONE);
        }


        getBooks();


        return v;
    }

    void getBooks() {


        layoutLoading.setVisibility(View.VISIBLE);
       // floatingActionButton.setVisibility(View.GONE);

        bookArrayList.clear();

        MyApplication.apiVideokart.getBook(new AsyncHttpResponseHandler() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                //Log.v("result", content);

                JsonObject result = new JsonParser().parse(new String(responseBody)).getAsJsonObject();
                JsonObject status = result.getAsJsonObject("status");

                if (status.get("code").getAsInt() == 200) {

                    JsonObject data = result.get("data").getAsJsonObject();

                    JsonArray booksArray = data.get("books").getAsJsonArray();
                    for (int i = 0; i < booksArray.size(); i++) {
                        bookArrayList.add(new Book(booksArray.get(i).getAsJsonObject()));
                    }

                    booksAdapter = new BooksAdapter(bookArrayList, new BooksAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(Book item) {

                            Intent in = new Intent(getActivity(), LessonsActivity.class);
                            in.putExtra("book", (Serializable) item);

                            startActivity(in);

                        }

                    });
                    listBook.setAdapter(booksAdapter);

                    layoutMain.setVisibility(View.VISIBLE);
                    layoutLoading.setVisibility(View.GONE);
                   // floatingActionButton.setVisibility(View.VISIBLE);

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

                layoutLoading.setVisibility(View.GONE);

                SnackbarManager.show(
                        Snackbar.with(getContext()) // context
                                .duration(Snackbar.SnackbarDuration.LENGTH_SHORT)
                                .swipeToDismiss(false)
                                .text("خطا در برقراری ارتباط") // text to display
                                .actionLabel("تلاش مجدد") // action button label
                                .actionListener(new ActionClickListener() {
                                    @Override
                                    public void onActionClicked(Snackbar snackbar) {
                                        getBooks();
                                    }
                                }) // action button's ActionClickListener
                        , getActivity()); // activity where it is displayed

            }

            @Override
            public void onFinish() {
                super.onFinish();




            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_profile:
                startActivity(new Intent(getContext(), ProfileActivity.class));
                break;
            case R.id.btn_fav:
                startActivity(new Intent(getContext(), FavoriteClipsListActivity.class));
                break;
            case R.id.fab_store:


                new MaterialDialog.Builder(getActivity())
                        .typeface(MyApplication.getTypeFace(),MyApplication.getTypeFace())
                        .title("فعال سازی")
                        .contentGravity(GravityEnum.END)
                        .titleGravity(GravityEnum.END)
                        .contentColor(getResources().getColor(R.color.md_grey_700))
                        .positiveColor(getResources().getColor(R.color.md_blue_700))
                        .negativeColor(getResources().getColor(R.color.md_green_700))

                        .content("مایل به فعال سازی بسته هستید؟")
                        .positiveText("بله")
                        .negativeText("بعدا")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                Uri uri = Uri.parse("smsto:307566");
                                Intent it = new Intent(Intent.ACTION_SENDTO, uri);
                                it.putExtra("sms_body", "1");
                                startActivity(it);

                                dialog.dismiss();
                            }
                        })
                        .show();


                break;
        }
    }
}
