package com.hillavas.filmvazhe.screen;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hillavas.filmvazhe.MyApplication;
import com.hillavas.filmvazhe.R;
import com.hillavas.filmvazhe.adapters.DialoguesBanerAdapter;
import com.hillavas.filmvazhe.model.Clip;
import com.hillavas.filmvazhe.model.Word;
import com.hillavas.filmvazhe.screen.core.BaseActivity;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.listeners.ActionClickListener;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.BindView;
import cz.msebera.android.httpclient.Header;

import static com.hillavas.filmvazhe.MyApplication.getContext;

public class FavoriteClipsListActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.grid_movies)
    RecyclerView listCard;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.progress)
    ProgressBar progressBar;


    DialoguesBanerAdapter cardsAdapter;
    public static ArrayList<Clip> clipArrayList;
    private String json;
    Word word;


    public static ArrayList<Clip> getClipArrayList() {
        if(clipArrayList == null){
            clipArrayList = new ArrayList<>();
        }
        return clipArrayList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_words_list);

        ButterKnife.bind(this);

        this.setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View custom_action_bar = getLayoutInflater().inflate(R.layout.actionbar_back, null);

        getSupportActionBar().setCustomView(custom_action_bar);

        ((ImageButton) custom_action_bar.findViewById(R.id.btn_back)).setOnClickListener(this);
        TextView lblTitle = (TextView) custom_action_bar.findViewById(R.id.lbl_title);

        lblTitle.setText("علاقه مندی ها");

        toolbar.setBackgroundColor(Color.parseColor(MyApplication.getSharedPreferences().getString("colorMain", "#fafafa")));
        ((ImageButton) custom_action_bar.findViewById(R.id.btn_back)).setColorFilter(Color.parseColor(MyApplication.getSharedPreferences().getString("colorSecond", "#212121")));
        lblTitle.setTextColor(Color.parseColor(MyApplication.getSharedPreferences().getString("colorSecond", "#212121")));

        swipeRefreshLayout.setEnabled(false);
        progressBar.setVisibility(View.GONE);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        listCard.setLayoutManager(llm);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(listCard.getContext(),
                llm.getOrientation());
        listCard.addItemDecoration(dividerItemDecoration);

        cardsAdapter = new DialoguesBanerAdapter(getClipArrayList(), new DialoguesBanerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Clip item) {
                Intent in = new Intent(FavoriteClipsListActivity.this, DialogueDetailActivity.class);
                in.putExtra("clip", (Serializable) item);
                //in.putExtra("movieId", item.getMovieId());
                //  in.putExtra("movieName", item.getMovieName());
                startActivity(in);
            }
        });
        listCard.setAdapter(cardsAdapter);
    }


    void getFavClips() {

        if (clipArrayList.size() > 0) {
            clipArrayList.clear();
            cardsAdapter.notifyDataSetChanged();
        }

        MyApplication.apiVideokart.userFavoriteClips(new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                JsonObject result = new JsonParser().parse(new String(responseBody)).getAsJsonObject();
                JsonObject status = result.getAsJsonObject("status");

                if (status.get("code").getAsInt() == 200) {
                    JsonArray array = result.getAsJsonArray("data");
                    if (array.size() > 0) {

                        for (int i = 0; i < array.size(); i++) {
                            clipArrayList.add(new Clip(array.get(i).getAsJsonObject()));
                        }

                        cardsAdapter.notifyDataSetChanged();

                    } else {
                        SnackbarManager.show(
                                Snackbar.with(getContext()) // context
                                        .textTypeface(MyApplication.getTypeFace())
                                        .duration(Snackbar.SnackbarDuration.LENGTH_SHORT)
                                        .swipeToDismiss(true)
                                        .text("شما موردی را به علاقمندی ها اضافه نکرده اید") // text to display

                                , FavoriteClipsListActivity.this); // activity where it is dis
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                SnackbarManager.show(
                        Snackbar.with(FavoriteClipsListActivity.this) // context
                                .textTypeface(MyApplication.getTypeFace())

                                .duration(Snackbar.SnackbarDuration.LENGTH_SHORT)
                                .swipeToDismiss(false)
                                .text("خطا در برقراری ارتباط") // text to display
                                .actionLabel("تلاش مجدد") // action button label
                                .actionListener(new ActionClickListener() {
                                    @Override
                                    public void onActionClicked(Snackbar snackbar) {
                                        getFavClips();
                                    }
                                }) // action button's ActionClickListener
                        , FavoriteClipsListActivity.this); // activity where it is displayed            }

            }
        });


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                onBackPressed();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (getClipArrayList().size() == 0)
            getFavClips();
        else {
            cardsAdapter.notifyDataSetChanged();
        }
    }

}
