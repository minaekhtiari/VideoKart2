package com.hillavas.filmvazhe.screen;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hillavas.filmvazhe.MyApplication;
import com.hillavas.filmvazhe.R;
import com.hillavas.filmvazhe.screen.core.BaseActivity;

import butterknife.ButterKnife;
import butterknife.BindView;


/**
 * Created by Arash on 2/24/2015.
 */
public class WebActivity extends BaseActivity {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.webview)
    WebView webView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);

        this.setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        View custom_action_bar = getLayoutInflater().inflate(R.layout.actionbar_back, null);
        TextView lblTitle = (TextView) custom_action_bar.findViewById(R.id.lbl_title);
        ImageButton btnBack=(ImageButton) custom_action_bar.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        lblTitle.setText(getIntent().getStringExtra("title"));

        getSupportActionBar().setCustomView(custom_action_bar);

        toolbar.setBackgroundColor(Color.parseColor(MyApplication.getSharedPreferences().getString("colorMain","#fafafa")));
        btnBack.setColorFilter(Color.parseColor(MyApplication.getSharedPreferences().getString("colorSecond","#212121")));
        lblTitle.setTextColor(Color.parseColor(MyApplication.getSharedPreferences().getString("colorSecond","#212121")));

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(getIntent().getStringExtra("url"));
    }

}
