package com.hillavas.filmvazhe.screen;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.Util;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hillavas.filmvazhe.utils.PersianUtils;
import com.hillavas.filmvazhe.MyApplication;
import com.hillavas.filmvazhe.R;
import com.hillavas.filmvazhe.api.VideoCardApi;
import com.hillavas.filmvazhe.model.Clip;
import com.hillavas.filmvazhe.model.Subtitle;
import com.hillavas.filmvazhe.screen.core.BaseActivity;
import com.hillavas.filmvazhe.utils.StringUtils;
import com.hillavas.filmvazhe.utils.TTS;
import com.hillavas.filmvazhe.utils.ToastHandler;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.bumptech.glide.Glide;
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
public class DialogueDetailActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;

    @BindView(R.id.scroll_card_detail)
    NestedScrollView scrollView;
    //    @BindView(R.id.videoView)
//    EasyVideoPlayer videoView;


    @BindView(R.id.time)
    TextView lblTotalTime;
    @BindView(R.id.time_current)
    TextView lblCurrentTime;

    @BindView(R.id.lbl_subtitle)
    TextView lblSubtitle;

    @BindView(R.id.btn_change_subtitle)
    TextView btnChangeSubTitle;

    @BindView(R.id.btn_like)
    MaterialFavoriteButton btnLike;
    @BindView(R.id.btn_setting)
    ImageView btnSetting;


    @BindView(R.id.layout_word_info)
    LinearLayout layoutWordInfo;

    @BindView(R.id.lbl_word_title)
    TextView lblWordTitle;
    @BindView(R.id.lbl_word_meaning)
    TextView lblWordMeaning;
    @BindView(R.id.lbl_word_n)
    TextView lblWordNoun;
    @BindView(R.id.lbl_word_v)
    TextView lblWordVerb;
    @BindView(R.id.lbl_word_adj)
    TextView lblWordAdj;
    @BindView(R.id.lbl_word_example1)
    TextView lblWordExample1;
    @BindView(R.id.lbl_word_example2)
    TextView lblWordExample2;
    @BindView(R.id.lbl_word_example3)
    TextView lblWordExample3;
    @BindView(R.id.lbl_word_example_mean1)
    TextView lblWordExampleMean1;
    @BindView(R.id.lbl_word_example_mean2)
    TextView lblWordExampleMean2;
    @BindView(R.id.lbl_word_example_mean3)
    TextView lblWordExampleMean3;

    @BindView(R.id.layout_synonym)
    LinearLayout layoutSynonym;
    @BindView(R.id.layout_word_noun)
    RelativeLayout layoutWordNoun;
    @BindView(R.id.layout_word_verb)
    RelativeLayout layoutWordVerb;
    @BindView(R.id.layout_word_adj)
    RelativeLayout layoutWordAdj;
    @BindView(R.id.layout_alert)
    RelativeLayout layoutAlert;

    @BindView(R.id.layout_example1)
    RelativeLayout layoutExample1;
    @BindView(R.id.layout_example2)
    RelativeLayout layoutExample2;
    @BindView(R.id.layout_example3)
    RelativeLayout layoutExample3;

    @BindView(R.id.btn_hidden_alert)
    ImageView btnAlert;
    @BindView(R.id.layout_movie_detail)
    RelativeLayout layoutMovieDetail;

    @BindView(R.id.img_clip_cover)
    ImageView imgClipCover;

    @BindView(R.id.layout_clip_cover)
    RelativeLayout layoutClipCover;
    @BindView(R.id.lbl_clip_cover_movie_title)
    TextView lblClipCoverMovieTitle;
    @BindView(R.id.lbl_clip_cover_word_title)
    TextView lblClipCoverWordTitle;
    @BindView(R.id.btn_clip_cover_play)
    ImageView btnClipCoverPlay;

    @BindView(R.id.videoView2)
    SimpleExoPlayerView simpleExoPlayerView;

    @BindView(R.id.layout_loading)
    FrameLayout layoutLoading;

    @BindView(R.id.example_header)
    TextView lblExampleHeader;

    ImageButton btnBack;
    TextView lblTitleWord, lblTitleMovie;

    View custom_action_bar;
    ColorDrawable colorDrawableActionBar;
    Clip clip;
    private int endTime, startTime;
    ArrayList<Subtitle> enSubtitles = new ArrayList<Subtitle>();
    ArrayList<Subtitle> faSubtitles = new ArrayList<Subtitle>();
    int seekbarDelay = 500;
    private ArrayList<Clip> otherClipArrayList = new ArrayList<>();

    boolean isStopPlayer = false;

    TTS textToSpeech;
    private int subtitlePosition = 0;
    boolean englishSubtitle = true;
    private SimpleExoPlayer player;
    private boolean playerState = false;
    //DebugTextViewHelper debugViewHelper;
    private int playerWindow;
    private long playerPosition;
    private Timeline.Window window = new Timeline.Window();
    private boolean dragging;

    Handler subTitleHandler = new Handler();
    private long subtitleDelay = 100;

    int likeCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialogue_detail);

        ButterKnife.bind(this);

        textToSpeech = new TTS(this);

        clip = (Clip) getIntent().getSerializableExtra("clip");

        likeCount = clip.getLikeCount();
        //movieId = getIntent().getIntExtra("movieId", 0);
        // movieName = getIntent().getStringExtra("movieName");
        if (clip.getLiked())
            btnLike.setFavorite(true);
        else
            btnLike.setFavorite(false);


        this.setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        custom_action_bar = getLayoutInflater().inflate(R.layout.actionbar_dialogue_detail, null);

        lblTitleWord = ((TextView) custom_action_bar.findViewById(R.id.lbl_title));
        lblTitleMovie = ((TextView) custom_action_bar.findViewById(R.id.lbl_title_movie));
        btnBack = ((ImageButton) custom_action_bar.findViewById(R.id.btn_back));

        toolbar.setBackgroundColor(Color.parseColor(MyApplication.getSharedPreferences().getString("colorMain", "#fafafa")));
        collapsingToolbar.setContentScrimColor(Color.parseColor(MyApplication.getSharedPreferences().getString("colorMain", "#fafafa")));
        ((ImageButton) custom_action_bar.findViewById(R.id.btn_back)).setColorFilter(Color.parseColor(MyApplication.getSharedPreferences().getString("colorSecond", "#212121")));
        //lblTitle.setTextColor(Color.parseColor(MyApplication.getSharedPreferences().getString("colorSecond","#212121")));

        getSupportActionBar().setCustomView(custom_action_bar);

        simpleExoPlayerView.requestFocus();

        btnClipCoverPlay.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnChangeSubTitle.setOnClickListener(this);
        btnLike.setOnClickListener(this);
        btnSetting.setOnClickListener(this);
        btnAlert.setOnClickListener(this);

        lblWordTitle.setOnClickListener(this);
        lblWordNoun.setOnClickListener(this);
        lblWordAdj.setOnClickListener(this);
        lblWordVerb.setOnClickListener(this);
        lblWordExample1.setOnClickListener(this);
        lblWordExample2.setOnClickListener(this);
        lblWordExample3.setOnClickListener(this);


        colorDrawableActionBar = new ColorDrawable(getResources().getColor(R.color.colorPrimary));
        getSupportActionBar().setBackgroundDrawable(colorDrawableActionBar);
        colorDrawableActionBar.setAlpha(0);

        //scrollView.setOnScrollViewListener(this);

        if (MyApplication.getSharedPreferences().getBoolean("alertHidden", false)) {
            layoutAlert.setVisibility(View.GONE);
        }

        layoutMovieDetail.getLayoutParams().height = (int) (((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getHeight() * (65.0f / 100.0f));

        lblTitleWord.setText(StringUtils.capitalizeFirstLetter(clip.getWordName()));
        lblTitleMovie.setText(StringUtils.capitalizeFirstLetter(clip.getMovieName()) + " Movie");

        lblClipCoverMovieTitle.setText(StringUtils.capitalizeFirstLetter(clip.getMovieName()) + " Movie");
        lblClipCoverWordTitle.setText(StringUtils.capitalizeFirstLetter(clip.getWordName()));

        Glide.with(getContext())
                .load(String.format(VideoCardApi.clipCover, clip.getId()))
                .into(imgClipCover);


        initVideo();

        getSamplesAndInfo();


    }

    void getSamplesAndInfo() {

        layoutLoading.setVisibility(View.VISIBLE);

        MyApplication.apiVideokart.getWordExample(clip.getWord().getId(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                final JsonObject result = new JsonParser().parse(new String(responseBody)).getAsJsonObject();
                JsonObject status = result.getAsJsonObject("status");

                if (status.get("code").getAsInt() == 200) {

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {


                                    JsonArray exampleArray = result.getAsJsonArray("data");
                                    lblExampleHeader.setText(clip.getWordName());
                                    if (exampleArray.size() >= 1) {
                                        lblWordExample1.setText(boldWord(exampleArray.get(0).getAsJsonObject().get("en").getAsString()));
                                        lblWordExampleMean1.setText(exampleArray.get(0).getAsJsonObject().get("fa").getAsString());
                                    } else {
                                        layoutExample1.setVisibility(View.GONE);
                                    }
                                    if (exampleArray.size() >= 2) {
                                        lblWordExample2.setText(boldWord(exampleArray.get(1).getAsJsonObject().get("en").getAsString()));
                                        lblWordExampleMean2.setText(exampleArray.get(1).getAsJsonObject().get("fa").getAsString());
                                    } else {
                                        layoutExample2.setVisibility(View.GONE);
                                    }
                                    if (exampleArray.size() >= 3) {
                                        lblWordExample3.setText(boldWord(exampleArray.get(2).getAsJsonObject().get("en").getAsString()));
                                        lblWordExampleMean3.setText(exampleArray.get(2).getAsJsonObject().get("fa").getAsString());
                                    } else {
                                        layoutExample3.setVisibility(View.GONE);
                                    }

                                }
                            });
                        }
                    }).start();

                } else {
                    layoutWordInfo.setVisibility(View.GONE);
                    btnLike.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                SnackbarManager.show(
                        Snackbar.with(getContext()) // context
                                .duration(Snackbar.SnackbarDuration.LENGTH_SHORT)
                                .swipeToDismiss(true)
                                .text("خطا در برقراری ارتباط") // text to display
                                .actionLabel("تلاش مجدد") // action button label
                                .actionListener(new ActionClickListener() {
                                    @Override
                                    public void onActionClicked(Snackbar snackbar) {
                                        getSamplesAndInfo();
                                    }
                                }) // action button's ActionClickListener
                        , DialogueDetailActivity.this); // activity where it is displayed

            }

            @Override
            public void onFinish() {
                super.onFinish();
                layoutLoading.setVisibility(View.GONE);

            }
        });


    }


    void initVideo() {

        getSubtitle();


        // 1. Create a default TrackSelector
        Handler mainHandler = new Handler();
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveVideoTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(mainHandler, videoTrackSelectionFactory);

// 2. Create a default LoadControl
        LoadControl loadControl = new DefaultLoadControl();

// 3. Create the player
        player = ExoPlayerFactory.newSimpleInstance(DialogueDetailActivity.this, trackSelector, loadControl);
        //simpleExoPlayerView.setUseController(false);
        // Bind the player to the view.
        simpleExoPlayerView.setPlayer(player);


        String userAgent = Util.getUserAgent(this, "ExoPlayerVideoKart");
        DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter();

        HttpDataSource.Factory httpDataSourceFactory = new DefaultHttpDataSourceFactory(userAgent, defaultBandwidthMeter);
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(DialogueDetailActivity.this, defaultBandwidthMeter, httpDataSourceFactory);

        if (clip.getVideoUrl().endsWith(".mp4")) {


            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            MediaSource videoSource = new ExtractorMediaSource(Uri.parse(clip.getVideoUrl()),
                    dataSourceFactory, extractorsFactory, null, null);

            player.prepare(videoSource);

        } else {

            Uri uri = Uri.parse(clip.getVideoUrl());

            DashMediaSource videoSource = new DashMediaSource(uri, dataSourceFactory, new DefaultDashChunkSource.Factory(dataSourceFactory), null, null);
            player.prepare(videoSource);

        }

        handlerStop = false;

        player.addListener(new ExoPlayer.EventListener() {
            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                switch (playbackState) {
                    case ExoPlayer.STATE_BUFFERING:
                        break;
                    case ExoPlayer.STATE_ENDED:
                        player.seekTo(0);
                        break;
                    case ExoPlayer.STATE_IDLE:
                        break;
                    case ExoPlayer.STATE_READY:
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
            }

            @Override
            public void onPositionDiscontinuity() {
            }
        });

        if (clip.getWord().getProun() != null)
            lblWordTitle.setText(String.format("%s (%s)", clip.getWord().getName(), clip.getWord().getProun()));
        else
            lblWordTitle.setText(clip.getWord().getName());

        lblWordMeaning.setText(clip.getWord().getMeaning());

        if (clip.getWord().getNoun() != null)
            lblWordNoun.setText(clip.getWord().getNoun());
        else
            layoutWordNoun.setVisibility(View.GONE);

        if (clip.getWord().getVerb() != null)
            lblWordVerb.setText(clip.getWord().getVerb());
        else
            layoutWordVerb.setVisibility(View.GONE);

        if (clip.getWord().getAdj() != null)
            lblWordAdj.setText(clip.getWord().getAdj());
        else
            layoutWordAdj.setVisibility(View.GONE);

        if (clip.getWord().getAdj() == null && clip.getWord().getVerb() == null && clip.getWord().getNoun() == null) {
            layoutSynonym.setVisibility(View.GONE);
        }

    }

    void getSubtitle() {

        MyApplication.apiVideokart.getMovieSubtitle(clip.getId(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                final JsonObject result = new JsonParser().parse(new String(responseBody)).getAsJsonObject();
                JsonObject status = result.getAsJsonObject("status");

                if (status.get("code").getAsInt() == 200) {



                                    JsonArray enArray = result.get("data").getAsJsonObject().get("en").getAsJsonArray();
                                    JsonArray faArray = result.get("data").getAsJsonObject().get("fa").getAsJsonArray();

                                    enSubtitles.clear();
                                    faSubtitles.clear();

                                    for (int i = 0; i < enArray.size(); i++) {
                                        enSubtitles.add(new Subtitle(enArray.get(i).getAsJsonObject()));
                                    }
                                    for (int i = 0; i < faArray.size(); i++) {
                                        faSubtitles.add(new Subtitle(faArray.get(i).getAsJsonObject()));
                                    }
                                    subTitleHandler.postDelayed(subtitleRunnable, subtitleDelay);



                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }


    private int videoCurrent;
    private boolean isBoldTargetSubtitle = false;
    private boolean handlerStop = false;
    private Runnable subtitleRunnable = new Runnable() {

        @Override
        public void run() {

            //Log.v("videoviwe","videoPosition"+ String.valueOf(videoView.getCurrentPosition()));
            if (player != null && player.getPlayWhenReady() && !handlerStop) {
                videoCurrent = (int) player.getCurrentPosition();


                if (englishSubtitle) {
                    for (Subtitle subtitle : enSubtitles) {

                        if (videoCurrent >= (subtitle.getDialogStart()) && videoCurrent <= (subtitle.getDialogEnd())) {
                            lblSubtitle.setText(subtitle.getDialogText());

                            subtitlePosition = enSubtitles.indexOf(subtitle) + 1;

                            break;
                        } else {
                            lblSubtitle.setText("...");
                        }
                    }
                } else {
                    for (Subtitle subtitle : faSubtitles) {
                        if (videoCurrent >= (subtitle.getDialogStart()) && videoCurrent <= (subtitle.getDialogEnd())) {
                            lblSubtitle.setText(subtitle.getDialogText());

                            break;
                        } else {
                            lblSubtitle.setText("...");
                        }
                    }
                }
            }
            subTitleHandler.postDelayed(subtitleRunnable, subtitleDelay);


        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_setting:

                Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/iran_sans.ttf");
                View custom = getLayoutInflater().inflate(R.layout.layout_speech_setting, null);

                final TextView lblValue = (TextView) custom.findViewById(R.id.lbl_value);
                SeekBar seekBar = (SeekBar) custom.findViewById(R.id.seekbar);

                lblValue.setText(PersianUtils.toFarsi(String.valueOf(MyApplication.getSharedPreferences().getInt("speech", 5))));
                seekBar.setProgress(MyApplication.getSharedPreferences().getInt("speech", 5));

                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                        MyApplication.saveLocalData("speech", seekBar.getProgress());
                        lblValue.setText(PersianUtils.toFarsi(String.valueOf(seekBar.getProgress())));


                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

                new MaterialDialog.Builder(DialogueDetailActivity.this)
                        .typeface(typeface, typeface)
                        .title("تنظیم سرعت تلفظ")
                        .contentGravity(GravityEnum.END)
                        .titleGravity(GravityEnum.END)
                        .contentColor(getResources().getColor(R.color.md_grey_700))
                        .positiveColor(getResources().getColor(R.color.md_blue_700))
                        .negativeColor(getResources().getColor(R.color.md_green_700))

                        .customView(custom, false)
                        .positiveText("تنظیم")
                        .negativeText("انصراف")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                textToSpeech.tts.setSpeechRate((float) MyApplication.getSharedPreferences().getInt("speech", 5) / 10);


                                dialog.dismiss();
                            }
                        })
                        .show();


                break;
            case R.id.btn_hidden_alert:
                layoutAlert.setVisibility(View.GONE);
                MyApplication.saveLocalData("alertHidden", true);
                break;
            case R.id.lbl_word_title:
                if (textToSpeech.isSpeaking()) {
                    textToSpeech.onPause();

                    player.setPlayWhenReady(true);

                } else {
                    textToSpeech.RunSpeech(clip.getWordName());

                    player.setPlayWhenReady(false);


                }

                break;
            case R.id.lbl_word_n:
                if (textToSpeech.isSpeaking()) {
                    textToSpeech.onPause();
                    player.setPlayWhenReady(false);

                } else {
                    textToSpeech.RunSpeech(lblWordNoun.getText().toString());
                    player.setPlayWhenReady(true);

                }
                break;
            case R.id.lbl_word_v:
                if (textToSpeech.isSpeaking()) {
                    textToSpeech.onPause();
                    player.setPlayWhenReady(false);

                } else {
                    textToSpeech.RunSpeech(lblWordVerb.getText().toString());
                    player.setPlayWhenReady(true);

                }
                break;
            case R.id.lbl_word_adj:
                if (textToSpeech.isSpeaking())
                    textToSpeech.onPause();
                else
                    textToSpeech.RunSpeech(lblWordAdj.getText().toString());
                break;
            case R.id.lbl_word_example1:
                if (textToSpeech.isSpeaking())
                    textToSpeech.onPause();
                else
                    textToSpeech.RunSpeech(lblWordExample1.getText().toString());

                break;
            case R.id.lbl_word_example2:
                if (textToSpeech.isSpeaking())
                    textToSpeech.onPause();
                else
                    textToSpeech.RunSpeech(lblWordExample2.getText().toString());

                break;
            case R.id.lbl_word_example3:
                if (textToSpeech.isSpeaking())
                    textToSpeech.onPause();
                else
                    textToSpeech.RunSpeech(lblWordExample3.getText().toString());

                break;

            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.btn_clip_cover_play:


                if (layoutClipCover.getVisibility() == View.VISIBLE) {
                    layoutClipCover.setVisibility(View.GONE);

                    lblTitleWord.setVisibility(View.VISIBLE);
                    lblTitleMovie.setVisibility(View.VISIBLE);

                    simpleExoPlayerView.setVisibility(View.VISIBLE);
                }

                player.setPlayWhenReady(true);
                playerState = true;

                MyApplication.apiVideokart.setView(clip.getId(), new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Log.v("like s", new String(responseBody));
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Log.v("like f", new String(responseBody));

                    }
                });

//                if (videoView.isPlaying()) {
//                    btnPlayPause.setImageResource(R.mipmap.ic_play_arrow_black_36dp);
//
//                    isStopPlayer = true;
//                    videoView.pause();
//                } else {
//
//                    if (layoutClipCover.getVisibility() == View.VISIBLE) {
//                        layoutClipCover.setVisibility(View.GONE);
//                        /// imgClipCover.setVisibility(View.GONE);
//
//                        layoutSeekbar.setVisibility(View.VISIBLE);
//
//                        lblTitleWord.setVisibility(View.VISIBLE);
//                        lblTitleMovie.setVisibility(View.VISIBLE);
//                        lblTitleTime.setVisibility(View.VISIBLE);
//
//                        videoView.setVisibility(View.VISIBLE);
//                    }
//
//                    btnPlayPause.setImageResource(R.mipmap.ic_pause_black_36dp);
//
////                    if (videoCurrent >= endTime) {
////                        videoView.seekTo(startTime * 1000);
////                    }
//                    subtitlePosition = 0;
//
//                    videoView.start();
//                    isStopPlayer = false;
//
//                    // seekBar.postDelayed(onEverySecond, seekbarDelay);
//                }
                break;
            case R.id.btn_change_subtitle:
                if (englishSubtitle) {
                    englishSubtitle = false;
                    btnChangeSubTitle.setText("انگلیسی");
                } else {
                    englishSubtitle = true;
                    btnChangeSubTitle.setText("فارسی");

                }
                break;
            case R.id.btn_like:

                MyApplication.apiVideokart.setLike(clip.getId(), new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        //Log.v("result", content);

                        JsonObject result = new JsonParser().parse(new String(responseBody)).getAsJsonObject();
                        JsonObject status = result.getAsJsonObject("status");

                        if (status.get("code").getAsInt() == 200) {

                            String action = result.get("data").getAsJsonObject().get("action").getAsString();
                            if (action.equals("liked")) {
                                btnLike.setFavorite(true);
                                ToastHandler.onShow(DialogueDetailActivity.this, "به علاقه مندی ها افزوده شد.", Toast.LENGTH_SHORT);
                            } else {
                                btnLike.setFavorite(false);
                                ToastHandler.onShow(DialogueDetailActivity.this, "از علاقه مندی ها حذف شد.", Toast.LENGTH_SHORT);

                            }
                            if (FavoriteClipsListActivity.clipArrayList != null) {
                                FavoriteClipsListActivity.clipArrayList.clear();
                            }

                            //ToastHandler.onShow(DialogueDetailActivity.this, action, Toast.LENGTH_SHORT);


                        } else {
                            SnackbarManager.show(
                                    Snackbar.with(getContext()) // context
                                            .textTypeface(MyApplication.getTypeFace())
                                            .duration(Snackbar.SnackbarDuration.LENGTH_SHORT)
                                            .swipeToDismiss(true)
                                            .text(status.get("message").getAsString()) // text to display

                                    , DialogueDetailActivity.this); // activity where it is dis
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        SnackbarManager.show(
                                Snackbar.with(getContext()) // context
                                        .duration(Snackbar.SnackbarDuration.LENGTH_SHORT)
                                        .swipeToDismiss(true)
                                        .text("خطا در برقراری ارتباط") // text to display

                                , DialogueDetailActivity.this); // activity where it is displayed
                    }
                });

                break;

        }

    }

    public SpannableStringBuilder boldWord(String text) {

        // String sourceString =text.replaceAll(clip.getWordName(),"<b>" + clip.getWordName() + "</b>");
        final SpannableStringBuilder sb = new SpannableStringBuilder(text);
        final StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);
        final ForegroundColorSpan fcs = new ForegroundColorSpan(getResources().getColor(R.color.md_red_500));


        String sr = text.toLowerCase();
        try {

            sb.setSpan(bss, sr.indexOf(clip.getWordName().toLowerCase()), sr.indexOf(clip.getWordName().toLowerCase()) + clip.getWordName().length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            sb.setSpan(fcs, sr.indexOf(clip.getWordName().toLowerCase()), sr.indexOf(clip.getWordName().toLowerCase()) + clip.getWordName().length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        } catch (Exception ex) {

        }

        return sb;
    }


    private void releasePlayer() {
        if (player != null) {
//            debugViewHelper.stop();
//            debugViewHelper = null;
            playerState = player.getPlayWhenReady();
            playerWindow = player.getCurrentWindowIndex();
            playerPosition = C.TIME_UNSET;
            Timeline timeline = player.getCurrentTimeline();
            if (timeline != null && timeline.getWindow(playerWindow, window).isSeekable) {
                playerPosition = player.getCurrentPosition();
            }
            player.release();
            player = null;
            handlerStop = true;


        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

            layoutMovieDetail.getLayoutParams().height = (int) (((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getHeight() * (95.0f / 100.0f));

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {

            layoutMovieDetail.getLayoutParams().height = (int) (((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getHeight() * (55.0f / 100.0f));

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                player.seekTo(data.getIntExtra("seek", 0));
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initVideo();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || player == null)) {
            initVideo();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
        try {
            textToSpeech.onPause();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }

    }


}
