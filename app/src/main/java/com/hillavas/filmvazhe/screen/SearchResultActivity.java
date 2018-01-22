package com.hillavas.filmvazhe.screen;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hillavas.filmvazhe.MyApplication;
import com.hillavas.filmvazhe.adapters.WordsAdapter;
import com.hillavas.filmvazhe.model.LessonStatus;
import com.hillavas.filmvazhe.model.Word;
import com.hillavas.filmvazhe.screen.core.BaseActivity;
import com.hillavas.filmvazhe.utils.StringUtils;
import com.hillavas.filmvazhe.utils.ToastHandler;
import com.hillavas.filmvazhe.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.listeners.ActionClickListener;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.BindView;
import cz.msebera.android.httpclient.Header;

public class SearchResultActivity extends BaseActivity implements View.OnClickListener {


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
    private String json, word;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_dialog_list);

        ButterKnife.bind(this);

        word = getIntent().getStringExtra("word");
        json = getIntent().getStringExtra("searchResult");

        this.setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View custom_action_bar = getLayoutInflater().inflate(R.layout.actionbar_back, null);

        getSupportActionBar().setCustomView(custom_action_bar);

        ((ImageButton) custom_action_bar.findViewById(R.id.btn_back)).setOnClickListener(this);
        TextView lblTitle = (TextView) custom_action_bar.findViewById(R.id.lbl_title);

        lblTitle.setText(StringUtils.capitalizeFirstLetter(word) + " Word");

        toolbar.setBackgroundColor(Color.parseColor(MyApplication.getSharedPreferences().getString("colorMain","#fafafa")));
        ((ImageButton) custom_action_bar.findViewById(R.id.btn_back)).setColorFilter(Color.parseColor(MyApplication.getSharedPreferences().getString("colorSecond","#212121")));
        ((TextView) custom_action_bar.findViewById(R.id.lbl_title)).setTextColor(Color.parseColor(MyApplication.getSharedPreferences().getString("colorSecond","#212121")));

        LinearLayoutManager llm = new LinearLayoutManager(MyApplication.getContext());
        listCard.setLayoutManager(llm);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(listCard.getContext(),
                llm.getOrientation());
        listCard.addItemDecoration(dividerItemDecoration);

        swipeRefreshLayout.setEnabled(false);
        progressBar.setVisibility(View.GONE);

        getWords();

    }


    void getWords() {

        JsonObject result = new JsonParser().parse(json).getAsJsonObject();
        JsonArray array = result.get("data").getAsJsonArray();
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

                                if (item.getLesson().getStatus() == LessonStatus.Free) {

                                    Intent intent = new Intent(SearchResultActivity.this, BookWordDialougeListActivity.class);
                                    intent.putExtra("word", item);
                                    intent.putExtra("searchResult", new String(responseBody));
                                    startActivity(intent);
                                } else {

                                    new MaterialDialog.Builder(SearchResultActivity.this)
                                            .typeface(MyApplication.getTypeFace(),MyApplication.getTypeFace())
                                            .title("بسته ویژه")
                                            .contentGravity(GravityEnum.END)
                                            .titleGravity(GravityEnum.END)
                                            .contentColor(getResources().getColor(R.color.md_grey_700))
                                            .positiveColor(getResources().getColor(R.color.md_blue_700))
                                            .negativeColor(getResources().getColor(R.color.md_green_700))

                                            .content("جهت دسترسی به این کلمه نیاز است بسته ویژه را فعال سازی نمائید.")
                                            .positiveText("فعال سازی")
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
                                }

                            } else {
                                SnackbarManager.show(
                                        Snackbar.with(MyApplication.getContext()) // context
                                                .textTypeface(MyApplication.getTypeFace())
                                                .duration(Snackbar.SnackbarDuration.LENGTH_SHORT)
                                                .swipeToDismiss(true)
                                                .text("کلمه ای یافت نشد.") // text to display

                                        , SearchResultActivity.this); // activity where it is dis                                // TODO: 16/07/08 cant find any word
                            }
                        } else {
                            ToastHandler.onShow(SearchResultActivity.this, status.get("message").getAsString(), Toast.LENGTH_SHORT);

                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                        SnackbarManager.show(
                                Snackbar.with(SearchResultActivity.this) // context
                                        .duration(Snackbar.SnackbarDuration.LENGTH_SHORT)
                                        .textTypeface(MyApplication.getTypeFace())

                                        .swipeToDismiss(false)
                                        .text("خطا در برقراری ارتباط") // text to display
                                        .actionLabel("تلاش مجدد") // action button label
                                        .actionListener(new ActionClickListener() {
                                            @Override
                                            public void onActionClicked(Snackbar snackbar) {
                                                getWords();
                                            }
                                        }) // action button's ActionClickListener
                                , SearchResultActivity.this); // activity where it is displayed
                    }
                });

            }
        });
        listCard.setAdapter(wordsAdapter);


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                onBackPressed();
                break;
        }
    }
}
