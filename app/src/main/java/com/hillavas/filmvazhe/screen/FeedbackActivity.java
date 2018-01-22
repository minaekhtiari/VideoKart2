package com.hillavas.filmvazhe.screen;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;


import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hillavas.filmvazhe.screen.dialog.LoadingDialog;
import com.hillavas.filmvazhe.MyApplication;
import com.hillavas.filmvazhe.R;
import com.hillavas.filmvazhe.screen.core.BaseActivity;
import com.hillavas.filmvazhe.utils.StringUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;

import butterknife.ButterKnife;
import butterknife.BindView;
import cz.msebera.android.httpclient.Header;

import static com.hillavas.filmvazhe.MyApplication.getContext;

/**
 * Created by arash on 09/20/2014.
 */
public class FeedbackActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.lbl_title)
    TextView lblTitle;
    @BindView(R.id.txt_feedback)
    EditText txtMsg;
    @BindView(R.id.send_button)
    Button btnSend;

    AlertDialog dialog;
    LoadingDialog loadingDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        ButterKnife.bind(this);

        this.setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View custom_action_bar = getLayoutInflater().inflate(R.layout.actionbar_back, null);

        ((TextView) custom_action_bar.findViewById(R.id.lbl_title)).setText(getString(R.string.feedback));
        custom_action_bar.findViewById(R.id.btn_back).setOnClickListener(this);

        getSupportActionBar().setCustomView(custom_action_bar);

        toolbar.setBackgroundColor(Color.parseColor(MyApplication.getSharedPreferences().getString("colorMain","#fafafa")));
        ((ImageButton) custom_action_bar.findViewById(R.id.btn_back)).setColorFilter(Color.parseColor(MyApplication.getSharedPreferences().getString("colorSecond","#212121")));
        ((TextView) custom_action_bar.findViewById(R.id.lbl_title)).setTextColor(Color.parseColor(MyApplication.getSharedPreferences().getString("colorSecond","#212121")));

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSendTicket(view);
            }
        });

        loadingDialog = new LoadingDialog(this);


    }

    public void onSendTicket(View v) {

        String msg = txtMsg.getText().toString();


        if (StringUtils.isNullOrEmpty(msg)) {
            SnackbarManager.show(
                    Snackbar.with(getContext()) // context
                            .textTypeface(MyApplication.getTypeFace())
                            .duration(Snackbar.SnackbarDuration.LENGTH_SHORT)
                            .swipeToDismiss(true)
                            .text(getString(R.string.invalid_message)) // text to display

                    , FeedbackActivity.this); // activity where it is dis
            return;
        }
        loadingDialog.show();
        MyApplication.apiVideokart.sendTicket(msg, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                btnSend.setEnabled(false);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                JsonObject json = null;

                json = new JsonParser().parse(new String(responseBody)).getAsJsonObject();

                JsonObject status = json.get("status").getAsJsonObject();
                if (status.get("code").getAsInt() == 200) {

                    new MaterialDialog.Builder(FeedbackActivity.this)
                            .typeface(MyApplication.getTypeFace(),MyApplication.getTypeFace())
                            .title(getString(R.string.feedback))
                            .contentGravity(GravityEnum.END)
                            .titleGravity(GravityEnum.END)
                            .contentColor(getResources().getColor(R.color.md_grey_700))
                            .positiveColor(getResources().getColor(R.color.md_blue_700))
                            .negativeColor(getResources().getColor(R.color.md_green_700))

                            .content(getString(R.string.create_ticket_done))
                            .positiveText("باشه")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                    dialog.dismiss();
                                }
                            })
                            .show();


                } else {
                    SnackbarManager.show(
                            Snackbar.with(getContext()) // context
                                    .textTypeface(MyApplication.getTypeFace())
                                    .duration(Snackbar.SnackbarDuration.LENGTH_SHORT)
                                    .swipeToDismiss(true)
                                    .text(status.get("message").getAsString()) // text to display

                            , FeedbackActivity.this); // activity where it is dis
                }
                txtMsg.setText("");

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                SnackbarManager.show(
                        Snackbar.with(getContext()) // context
                                .duration(Snackbar.SnackbarDuration.LENGTH_SHORT)
                                .swipeToDismiss(true)
                                .text("خطا در برقراری ارتباط") // text to display

                        , FeedbackActivity.this); // activity where it is displayed
                 }

            @Override
            public void onFinish() {
                super.onFinish();
                btnSend.setEnabled(true);
                loadingDialog.dismiss();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

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

    }


}
