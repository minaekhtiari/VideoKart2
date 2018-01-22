package com.hillavas.filmvazhe.screen;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hillavas.filmvazhe.MyApplication;
import com.hillavas.filmvazhe.R;
import com.hillavas.filmvazhe.adapters.LessonsAdapter;
import com.hillavas.filmvazhe.model.Book;
import com.hillavas.filmvazhe.model.Lesson;
import com.hillavas.filmvazhe.model.LessonStatus;
import com.hillavas.filmvazhe.screen.core.BaseActivity;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.listeners.ActionClickListener;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.BindView;
import cz.msebera.android.httpclient.Header;

public class LessonsActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.list_lessons)
    RecyclerView listLessons;
    @BindView(R.id.progress)
    ProgressBar progressBar;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    LessonsAdapter lessonsAdapter;
    ArrayList<Lesson> lessonArrayList = new ArrayList<>();

    Book book;
    private TextView lblTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);

        ButterKnife.bind(this);

        this.setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View custom_action_bar = getLayoutInflater().inflate(R.layout.actionbar_back, null);
        custom_action_bar.findViewById(R.id.btn_back).setOnClickListener(this);
        lblTitle = (TextView) custom_action_bar.findViewById(R.id.lbl_title);

        toolbar.setBackgroundColor(Color.parseColor(MyApplication.getSharedPreferences().getString("colorMain", "#fafafa")));
        ((ImageButton) custom_action_bar.findViewById(R.id.btn_back)).setColorFilter(Color.parseColor(MyApplication.getSharedPreferences().getString("colorSecond", "#212121")));
        ((TextView) custom_action_bar.findViewById(R.id.lbl_title)).setTextColor(Color.parseColor(MyApplication.getSharedPreferences().getString("colorSecond", "#212121")));

        getSupportActionBar().setCustomView(custom_action_bar);

        book = (Book) getIntent().getSerializableExtra("book");

        lblTitle.setText(book.getName());

        LinearLayoutManager llm = new LinearLayoutManager(MyApplication.getContext());
        listLessons.setLayoutManager(llm);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(listLessons.getContext(),
                llm.getOrientation());
        listLessons.addItemDecoration(dividerItemDecoration);

        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // progressBar.setVisibility(View.VISIBLE);
                getLessons();
            }
        });



        getLessons();

    }


    void getLessons() {


        lessonArrayList.clear();


        MyApplication.apiVideokart.getLesson(book.getId(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                //Log.v("result", content);

                JsonObject result = new JsonParser().parse(new String(responseBody)).getAsJsonObject();
                JsonObject status = result.getAsJsonObject("status");

                if (status.get("code").getAsInt() == 200) {

                    JsonArray array = result.get("data").getAsJsonArray();
                    for (int i = 0; i < array.size(); i++) {
                        lessonArrayList.add(new Lesson(array.get(i).getAsJsonObject()));
                    }

                    lessonsAdapter = new LessonsAdapter(lessonArrayList, new LessonsAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(final Lesson item) {

                            if (item.getStatus() == LessonStatus.Free) {

                                Intent in = new Intent(LessonsActivity.this, LessonWordsListActivity.class);
                                in.putExtra("lesson", item);
                                startActivity(in);

                            } else {


                                new MaterialDialog.Builder(LessonsActivity.this)
                                        .typeface(MyApplication.getTypeFace(), MyApplication.getTypeFace())
                                        .title("بسته ویژه")
                                        .contentGravity(GravityEnum.END)
                                        .titleGravity(GravityEnum.END)
                                        .contentColor(getResources().getColor(R.color.md_grey_700))
                                        .positiveColor(getResources().getColor(R.color.md_blue_700))
                                        .negativeColor(getResources().getColor(R.color.md_green_700))

                                        .content("جهت دسترسی به این درس نیاز است بسته ویژه را فعال سازی نمائید.")
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
                        }
                    }
                    );
                    listLessons.setAdapter(lessonsAdapter);

                } else {
                    SnackbarManager.show(
                            Snackbar.with(MyApplication.getContext()) // context
                                    .textTypeface(MyApplication.getTypeFace())
                                    .duration(Snackbar.SnackbarDuration.LENGTH_SHORT)
                                    .swipeToDismiss(true)
                                    .text(status.get("message").getAsString()) // text to display

                            , LessonsActivity.this); // activity where it is dis
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                SnackbarManager.show(
                        Snackbar.with(LessonsActivity.this) // context
                                .textTypeface(MyApplication.getTypeFace())
                                .duration(Snackbar.SnackbarDuration.LENGTH_SHORT)
                                .swipeToDismiss(false)
                                .text("Connection Error!") // text to display
                                .actionLabel("Try Again") // action button label
                                .actionListener(new ActionClickListener() {
                                    @Override
                                    public void onActionClicked(Snackbar snackbar) {
                                        getLessons();
                                    }
                                }) // action button's ActionClickListener
                        , LessonsActivity.this); // activity where it is displayed

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

        }
    }
}
