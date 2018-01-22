package com.hillavas.filmvazhe.screen;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
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
import com.hillavas.filmvazhe.adapters.WordsAdapter;
import com.hillavas.filmvazhe.MyApplication;
import com.hillavas.filmvazhe.R;
import com.hillavas.filmvazhe.model.Word;
import com.hillavas.filmvazhe.model.Lesson;
import com.hillavas.filmvazhe.screen.core.BaseActivity;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.listeners.ActionClickListener;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.BindView;
import cz.msebera.android.httpclient.Header;

import static com.hillavas.filmvazhe.MyApplication.getContext;

/**
 * Created by Arash on 16/06/26.
 */
public class LessonWordsListActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.grid_movies)
    RecyclerView listCard;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.progress)
    ProgressBar progressBar;



    WordsAdapter wordsAdapter;
    ArrayList<Word> wordArrayList = new ArrayList<>();
    Lesson lesson;

    //filter dialog
    CharSequence[] filterDialogItems;
    ArrayList filterDialogSeletedItems;// arraylist to keep the selected items
    AlertDialog filterDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_words_list);

        ButterKnife.bind(this);

        lesson = (Lesson) getIntent().getSerializableExtra("lesson");

        this.setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View custom_action_bar = getLayoutInflater().inflate(R.layout.actionbar_movie_words, null);

        getSupportActionBar().setCustomView(custom_action_bar);

        ((ImageButton) custom_action_bar.findViewById(R.id.btn_back)).setOnClickListener(this);
        ((ImageButton) custom_action_bar.findViewById(R.id.btn_filter)).setOnClickListener(this);
        TextView lblTitle = (TextView) custom_action_bar.findViewById(R.id.lbl_title);

        lblTitle.setText(lesson.getName());

        toolbar.setBackgroundColor(Color.parseColor(MyApplication.getSharedPreferences().getString("colorMain","#fafafa")));
        ((ImageButton) custom_action_bar.findViewById(R.id.btn_back)).setColorFilter(Color.parseColor(MyApplication.getSharedPreferences().getString("colorSecond","#212121")));
        ((TextView) custom_action_bar.findViewById(R.id.lbl_title)).setTextColor(Color.parseColor(MyApplication.getSharedPreferences().getString("colorSecond","#212121")));

        LinearLayoutManager llm = new LinearLayoutManager(MyApplication.getContext());

        listCard.setLayoutManager(llm);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(listCard.getContext(),
                llm.getOrientation());
        listCard.addItemDecoration(dividerItemDecoration);

        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //  progressBar.setVisibility(View.VISIBLE);

                getWords();
            }
        });
        getWords();


    }


    void getWords() {

        if (wordArrayList.size() == 0)
            progressBar.setVisibility(View.VISIBLE);

        MyApplication.apiVideokart.getLessonWords(lesson.getId(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                //Log.v("result", content);

                JsonObject result = new JsonParser().parse(new String(responseBody)).getAsJsonObject();
                JsonObject status = result.getAsJsonObject("status");

                if (status.get("code").getAsInt() == 200) {

                    JsonArray array = result.get("data").getAsJsonArray();
                    wordArrayList.clear();
                    for (int i = 0; i < array.size(); i++) {
                        wordArrayList.add(new Word(array.get(i).getAsJsonObject()));
                    }

                    wordsAdapter = new WordsAdapter(wordArrayList, new WordsAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(final Word item) {

                            MyApplication.apiVideokart.getMovieWords(null, item.getId(), new AsyncHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                    JsonObject result = new JsonParser().parse(new String(responseBody)).getAsJsonObject();
                                    JsonObject status = result.getAsJsonObject("status");

                                    if (status.get("code").getAsInt() == 200) {
                                        JsonArray array = result.getAsJsonArray("data");
                                        if (array.size() > 0) {

                                            Intent intent = new Intent(LessonWordsListActivity.this, BookWordDialougeListActivity.class);
                                            intent.putExtra("word", item);
                                            intent.putExtra("searchResult", new String(responseBody));
                                            startActivity(intent);

                                        } else {
                                            SnackbarManager.show(
                                                    Snackbar.with(getContext()) // context
                                                            .textTypeface(MyApplication.getTypeFace())
                                                            .duration(Snackbar.SnackbarDuration.LENGTH_SHORT)
                                                            .swipeToDismiss(true)
                                                            .text("دیالوگی یافت نشد.") // text to display

                                                    , LessonWordsListActivity.this); // activity where it is dis
                                            // TODO: 16/07/08 cant find any word
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                                    SnackbarManager.show(
                                            Snackbar.with(getContext()) // context
                                                    .duration(Snackbar.SnackbarDuration.LENGTH_SHORT)
                                                    .swipeToDismiss(false)
                                                    .text("خطا در برقراری ارتباط") // text to display
                                                    .actionLabel("تلاش مجدد") // action button label
                                                    .actionListener(new ActionClickListener() {
                                                        @Override
                                                        public void onActionClicked(Snackbar snackbar) {
                                                            getWords();
                                                        }
                                                    }) // action button's ActionClickListener
                                            , LessonWordsListActivity.this); // activity where it is displayed                                }
                                }
                            });

                        }
                    });
                    listCard.setAdapter(wordsAdapter);


                } else {

                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                SnackbarManager.show(
                        Snackbar.with(LessonWordsListActivity.this) // context
                                .duration(Snackbar.SnackbarDuration.LENGTH_SHORT)
                                .textTypeface(MyApplication.getTypeFace())

                                .swipeToDismiss(true)
                                .text("Connection Error!") // text to display
                                .actionLabel("Try Again") // action button label
                                .actionListener(new ActionClickListener() {
                                    @Override
                                    public void onActionClicked(Snackbar snackbar) {
                                        progressBar.setVisibility(View.VISIBLE);
                                        getWords();
                                    }
                                }) // action button's ActionClickListener
                        , LessonWordsListActivity.this); // activity where it is displayed
            }

            @Override
            public void onFinish() {
                super.onFinish();
                swipeRefreshLayout.setRefreshing(false);
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.btn_filter:
                filterDialog.show();
                break;

        }
    }

}
