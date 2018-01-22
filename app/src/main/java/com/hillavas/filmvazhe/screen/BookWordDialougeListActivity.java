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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hillavas.filmvazhe.adapters.DialoguesBanerAdapter;
import com.hillavas.filmvazhe.screen.core.BaseActivity;
import com.hillavas.filmvazhe.MyApplication;
import com.hillavas.filmvazhe.R;
import com.hillavas.filmvazhe.model.Clip;
import com.hillavas.filmvazhe.model.Word;
import com.hillavas.filmvazhe.utils.StringUtils;
import com.hillavas.filmvazhe.utils.ToastHandler;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class BookWordDialougeListActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.grid_movies)
    RecyclerView listCard;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.progress)
    ProgressBar progressBar;
    @BindView(R.id.lbl_study_state)
    TextView lblStudyState;
    @BindView(R.id.layout_state)
    RelativeLayout layoutState;
    @BindView(R.id.progress_state)
    ProgressBar progressBarState;


    DialoguesBanerAdapter cardsAdapter;
    ArrayList<Clip> clipArrayList = new ArrayList<>();
    private String json;
    Word word;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_dialog_list);

        ButterKnife.bind(this);

        word = (Word) getIntent().getSerializableExtra("word");
        json = getIntent().getStringExtra("searchResult");

        this.setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View custom_action_bar = getLayoutInflater().inflate(R.layout.actionbar_back, null);

        getSupportActionBar().setCustomView(custom_action_bar);
        ImageButton btnBack=(ImageButton) custom_action_bar.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(this);
        TextView lblTitle = (TextView) custom_action_bar.findViewById(R.id.lbl_title);

        lblTitle.setText(StringUtils.capitalizeFirstLetter(word.getName()) + " Word");

        toolbar.setBackgroundColor(Color.parseColor(MyApplication.getSharedPreferences().getString("colorMain","#fafafa")));
        btnBack.setColorFilter(Color.parseColor(MyApplication.getSharedPreferences().getString("colorSecond","#212121")));
        lblTitle.setTextColor(Color.parseColor(MyApplication.getSharedPreferences().getString("colorSecond","#212121")));

        lblStudyState.setOnClickListener(this);

        swipeRefreshLayout.setEnabled(false);
        progressBar.setVisibility(View.GONE);

        LinearLayoutManager llm = new LinearLayoutManager(MyApplication.getContext());
        listCard.setLayoutManager(llm);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(listCard.getContext(),
                llm.getOrientation());
        listCard.addItemDecoration(dividerItemDecoration);

       // getWords();

        layoutState.setVisibility(View.VISIBLE);
        if (word.isStudied()) {
            lblStudyState.setText("آره، این لغت رو یاد گرفته\u200Cام.");
            layoutState.setBackgroundColor(getResources().getColor(R.color.md_green_500));
        } else {
            lblStudyState.setText(String.format("میخوای %s به مجموعه لغاتی که یادگرفتی اضافه بشه؟", word.getName()));
            layoutState.setBackgroundColor(getResources().getColor(R.color.md_light_blue_500));
        }

        getWords();


    }


    void getWords() {

        if(clipArrayList.size()>0){
            clipArrayList.clear();
            cardsAdapter.notifyDataSetChanged();
        }

        MyApplication.apiVideokart.getMovieWords(null, word.getId(), new AsyncHttpResponseHandler() {
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

                        cardsAdapter = new DialoguesBanerAdapter(clipArrayList, new DialoguesBanerAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(Clip item) {
                                Intent in = new Intent(BookWordDialougeListActivity.this, DialogueDetailActivity.class);
                                in.putExtra("clip", (Serializable) item);
                                //in.putExtra("movieId", item.getMovieId());
                                //  in.putExtra("movieName", item.getMovieName());
                                startActivity(in);
                            }
                        });
                        listCard.setAdapter(cardsAdapter);

                    } else {
                        SnackbarManager.show(
                                Snackbar.with(MyApplication.getContext()) // context
                                        .textTypeface(MyApplication.getTypeFace())
                                        .duration(Snackbar.SnackbarDuration.LENGTH_SHORT)
                                        .swipeToDismiss(true)
                                        .text("کلمه ای یافت نشد.") // text to display

                                , BookWordDialougeListActivity.this); // activity where it is dis
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                ToastHandler.onShow(BookWordDialougeListActivity.this, "Connection Error", Toast.LENGTH_SHORT);
            }
        });







    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.lbl_study_state:

                progressBarState.setVisibility(View.VISIBLE);
                lblStudyState.setVisibility(View.INVISIBLE);
                MyApplication.apiVideokart.setStudy(word.getId(), new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                        JsonObject result = new JsonParser().parse(new String(responseBody)).getAsJsonObject();
                        JsonObject status = result.getAsJsonObject("status");

                        if (status.get("code").getAsInt() == 200) {

                            String action = result.get("data").getAsJsonObject().get("action").getAsString();

                            if (action.toLowerCase().equals("created")) {

                                lblStudyState.setText("آره، این لغت رو یاد گرفته\u200Cام.");
                                layoutState.setBackgroundColor(getResources().getColor(R.color.md_green_500));
                            } else {
                                lblStudyState.setText(String.format("میخوای %s به مجموعه لغاتی که یادگرفتی اضافه بشه؟", word.getName()));
                                layoutState.setBackgroundColor(getResources().getColor(R.color.md_light_blue_500));
                            }

                        } else {
                            SnackbarManager.show(
                                    Snackbar.with(MyApplication.getContext()) // context
                                            .textTypeface(MyApplication.getTypeFace())
                                            .duration(Snackbar.SnackbarDuration.LENGTH_SHORT)
                                            .swipeToDismiss(true)
                                            .text(status.get("message").getAsString()) // text to display

                                    , BookWordDialougeListActivity.this); // activity where it is dis
                            // TODO: 16/07/08 cant find any word
                        }


                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();

                        lblStudyState.setVisibility(View.VISIBLE);
                        progressBarState.setVisibility(View.GONE);

                    }
                });


                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
