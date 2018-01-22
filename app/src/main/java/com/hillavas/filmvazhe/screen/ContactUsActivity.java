package com.hillavas.filmvazhe.screen;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hillavas.filmvazhe.screen.core.BaseActivity;
import com.hillavas.filmvazhe.MyApplication;
import com.hillavas.filmvazhe.R;
import com.loopj.android.http.AsyncHttpResponseHandler;

import butterknife.ButterKnife;
import butterknife.BindView;
import cz.msebera.android.httpclient.Header;


/**
 * Created by Arash on 2/24/2015.
 */
public class ContactUsActivity extends BaseActivity {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.lbl_about)
    TextView lblAbout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        ButterKnife.bind(this);

        this.setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        View custom_action_bar = getLayoutInflater().inflate(R.layout.actionbar_back, null);
        TextView lblTitle = (TextView) custom_action_bar.findViewById(R.id.lbl_title);
        ((ImageButton) custom_action_bar.findViewById(R.id.btn_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        lblTitle.setText(getString(R.string.contact_us));


        toolbar.setBackgroundColor(Color.parseColor(MyApplication.getSharedPreferences().getString("colorMain","#fafafa")));
        ((ImageButton) custom_action_bar.findViewById(R.id.btn_back)).setColorFilter(Color.parseColor(MyApplication.getSharedPreferences().getString("colorSecond","#212121")));
        lblTitle.setTextColor(Color.parseColor(MyApplication.getSharedPreferences().getString("colorSecond","#212121")));

        getSupportActionBar().setCustomView(custom_action_bar);



        

    }

}
