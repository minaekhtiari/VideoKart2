package com.hillavas.filmvazhe.screen;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hillavas.filmvazhe.MyApplication;
import com.hillavas.filmvazhe.R;
import com.hillavas.filmvazhe.screen.control.TimerView;
import com.hillavas.filmvazhe.screen.core.BaseActivity;
import com.hillavas.filmvazhe.utils.AndroidUtils;
import com.hillavas.filmvazhe.utils.ToastHandler;
import com.hillavas.filmvazhe.utils.Validator;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.listeners.ActionClickListener;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

import static com.hillavas.filmvazhe.MyApplication.getContext;
import static com.hillavas.filmvazhe.MyApplication.hamrahApi;

//
//import com.android.billingclient.util.IabBroadcastReceiver;
//import com.android.billingclient.util.IabHelper;
//import com.android.billingclient.util.IabResult;
//import com.android.billingclient.util.Inventory;
//import com.android.billingclient.util.Purchase;

public class VideoKartSignActivity extends BaseActivity implements View.OnClickListener, TextWatcher {


    @BindView(R.id.layout_sign_up)
    LinearLayout layoutSign;

    @BindView(R.id.txt_phone)
    EditText txtPhone;
    @BindView(R.id.btn_sign_up)
    AppCompatButton btnSignUp;


    @BindView(R.id.layout_verify)
    LinearLayout layoutVerify;
    @BindView(R.id.txt_code)
    EditText txtCode;
    @BindView(R.id.lbl_timer)
    TimerView timerView;
    @BindView(R.id.btn_send_again)
    TextView btnSendAgain;
    @BindView(R.id.btn_back)
    TextView btnBack;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.layout_header)
    LinearLayout layoutHeader;

    String code, phoneNumber = "";
    private Integer transactionId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/iran_sans.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );


        setContentView(R.layout.activity_videokart_sign);

        ButterKnife.bind(this);


        btnSignUp.setOnClickListener(this);
        btnSendAgain.setOnClickListener(this);
        btnBack.setOnClickListener(this);

        int width = ((WindowManager) MyApplication.getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getHeight();

        layoutHeader.getLayoutParams().height = (int) (width * (45.0f / 100.0f));


        txtCode.addTextChangedListener(this);

        loginAnonymous();

    }

    void loginAnonymous() {


        MyApplication.apiVideokart.hello(new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                //Log.v("result", content);


                JsonObject result = new JsonParser().parse(new String(responseBody)).getAsJsonObject();
                JsonObject status = result.getAsJsonObject("status");

                if (status.get("code").getAsInt() == 200) {

                    JsonObject data = result.get("data").getAsJsonObject();

                    if (data.get("update").getAsJsonObject().get("last_version").getAsInt() > AndroidUtils.getAppVersionCode()) {

                        ToastHandler.onShow(VideoKartSignActivity.this, " نسخه جدید برنامه رو نصب کنید.", Toast.LENGTH_LONG);
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(data.get("update").getAsJsonObject().get("download_url").getAsString())));
                        finish();
                        return;
                    }

                    MyApplication.saveLocalData(
                            "apiToken", data.get("device").getAsJsonObject().get("api_token").getAsString());

                    if (MyApplication.getSharedPreferences().getBoolean("vasRegister", false)) {

                        String phoneNumber = MyApplication.getSharedPreferences().getString("phoneNumber", "");

                        MyApplication.hamrahApi.getSubscribeState(phoneNumber, "8", new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                                JsonObject response = new JsonParser().parse(new String(responseBody)).getAsJsonObject();

                                if (response.get("IsSuccessful").getAsBoolean() && response.get("Result").getAsBoolean()) {

                                    startActivity(new Intent(VideoKartSignActivity.this, HomeActivity.class));
                                    finish();

                                } else {
                                    layoutSign.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);

                                }

                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                                SnackbarManager.show(
                                        Snackbar.with(getContext()) // context
                                                .textTypeface(MyApplication.getTypeFace())
                                                .duration(Snackbar.SnackbarDuration.LENGTH_INDEFINITE)
                                                .swipeToDismiss(false)
                                                .actionLabel("تلاش مجدد")
                                                .text("خطا در برقراری ارتباط") // text to display
                                                .actionListener(new ActionClickListener() {
                                                    @Override
                                                    public void onActionClicked(Snackbar snackbar) {


                                                        loginAnonymous();

                                                    }
                                                })
                                        , VideoKartSignActivity.this); // activity where it is dis

                            }
                        });


                    } else {
                        layoutSign.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }


                } else {
                    SnackbarManager.show(
                            Snackbar.with(getContext()) // context
                                    .textTypeface(MyApplication.getTypeFace())
                                    .duration(Snackbar.SnackbarDuration.LENGTH_INDEFINITE)
                                    .swipeToDismiss(false)
                                    .actionLabel("تلاش مجدد")
                                    .text(status.get("message").getAsString()) // text to display
                                    .actionListener(new ActionClickListener() {
                                        @Override
                                        public void onActionClicked(Snackbar snackbar) {

                                            loginAnonymous();

                                        }
                                    })
                            , VideoKartSignActivity.this); // activity where it is dis


                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                progressBar.setVisibility(View.GONE);


                SnackbarManager.show(
                        Snackbar.with(getContext()) // context
                                .textTypeface(MyApplication.getTypeFace())
                                .duration(Snackbar.SnackbarDuration.LENGTH_INDEFINITE)
                                .swipeToDismiss(false)
                                .actionLabel("تلاش مجدد")
                                .text("خطا در برقراری ارتباط") // text to display
                                .actionListener(new ActionClickListener() {
                                    @Override
                                    public void onActionClicked(Snackbar snackbar) {


                                        loginAnonymous();

                                    }
                                })
                        , VideoKartSignActivity.this); // activity where it is dis

            }
        });

    }

    void vasRegister() {

        phoneNumber = String.valueOf(txtPhone.getText());
//        code = String.valueOf(new Random().nextInt(9000 - 1000) + 1000);
        //Log.v("code", String.valueOf(code));
        if (Validator.isMobileNumber(phoneNumber)) {

            progressBar.setVisibility(View.VISIBLE);
            btnSignUp.setVisibility(View.INVISIBLE);

            MyApplication.hamrahApi.subscribeRequest(phoneNumber, "8", "A-Akh100", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {


                    JsonObject response = new JsonParser().parse(new String(responseBody)).getAsJsonObject();

                    if (response.get("IsSuccessful").getAsBoolean()) {

                        transactionId = response.get("Result").getAsInt();

                        MyApplication.saveLocalData("phoneNumber", phoneNumber);


                        if (transactionId == -1) {

                            MyApplication.saveLocalData("vasRegister", true);

                            startActivity(new Intent(VideoKartSignActivity.this, HomeActivity.class));
                            finish();

                        } else {

                            layoutSign.setVisibility(View.GONE);
                            layoutVerify.setVisibility(View.VISIBLE);


                            timerView.setTime(120000);
                            timerView.setOnTimerListener(new TimerView.TimerListener() {
                                @Override
                                public void onTick(long millisUntilFinished) {

                                }

                                @Override
                                public void onFinish() {

                                    layoutSign.setVisibility(View.VISIBLE);
                                    btnSignUp.setVisibility(View.VISIBLE);

                                    layoutVerify.setVisibility(View.GONE);

                                }
                            });
                            timerView.startCountDown();
                        }
                    } else {

                        SnackbarManager.show(
                                Snackbar.with(getContext()) // context
                                        .textTypeface(MyApplication.getTypeFace())
                                        .duration(Snackbar.SnackbarDuration.LENGTH_SHORT)
                                        .swipeToDismiss(true)
                                        .text(response.get("Message").getAsString()) // text to display

                                , VideoKartSignActivity.this); // activity where it is dis

                        btnSignUp.setVisibility(View.VISIBLE);
                        layoutSign.setVisibility(View.VISIBLE);
                        layoutVerify.setVisibility(View.GONE);

                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable
                        error) {

                    btnSignUp.setVisibility(View.VISIBLE);
                    layoutSign.setVisibility(View.VISIBLE);


                    SnackbarManager.show(
                            Snackbar.with(getContext()) // context
                                    .duration(Snackbar.SnackbarDuration.LENGTH_SHORT)
                                    .swipeToDismiss(true)
                                    .text("خطا در برقراری ارتباط") // text to display

                            , VideoKartSignActivity.this); // activity where it is displayed                    layoutsign.setVisibility(View.VISIBLE);


                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    btnSignUp.setEnabled(true);
                    progressBar.setVisibility(View.GONE);



                }
            });

        } else

        {
            SnackbarManager.show(
                    Snackbar.with(getContext()) // context
                            .textTypeface(MyApplication.getTypeFace())
                            .duration(Snackbar.SnackbarDuration.LENGTH_SHORT)
                            .swipeToDismiss(true)
                            .text(R.string.phone_number_is_not_valid) // text to display

                    , VideoKartSignActivity.this); // activity where it is dis
        }

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_back:

                timerView.stopCountDown();

                btnSignUp.setVisibility(View.VISIBLE);
                layoutSign.setVisibility(View.VISIBLE);
                layoutVerify.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);

                break;
            case R.id.btn_sign_up:

                vasRegister();

                break;

            case R.id.btn_send_again:

                timerView.stopCountDown();
                vasRegister();

                break;
        }
    }

    void verificationSubmit() {

        progressBar.setVisibility(View.VISIBLE);
        layoutVerify.setClickable(false);

        // layoutVerify.setVisibility(View.GONE);
        String pin = String.valueOf(txtCode.getText());

        MyApplication.hamrahApi.subscribeConfirm("8", transactionId, pin, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {


                JsonObject response = new JsonParser().parse(new String(responseBody)).getAsJsonObject();

                if (response.get("IsSuccessful").getAsBoolean()) {

                    MyApplication.saveLocalData("vasRegister", true);

                    startActivity(new Intent(VideoKartSignActivity.this, HomeActivity.class));
                    finish();

                    //Toast.makeText(SignActivity.this, "done", Toast.LENGTH_SHORT).show();
                } else {

                    //Show error
                    ToastHandler.onShow(VideoKartSignActivity.this, "کد فعال سازی معتبر نیست.", Toast.LENGTH_SHORT);

                    progressBar.setVisibility(View.GONE);
                    layoutVerify.setClickable(true);
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {


                SnackbarManager.show(
                        Snackbar.with(getContext()) // context
                                .duration(Snackbar.SnackbarDuration.LENGTH_SHORT)
                                .swipeToDismiss(true)
                                .text("خطا در برقراری ارتباط") // text to display

                        , VideoKartSignActivity.this); // activity where it is displayed
                progressBar.setVisibility(View.GONE);
                layoutVerify.setClickable(true);

            }

            @Override
            public void onFinish() {
                super.onFinish();

            }
        });
    }


    void DisableAccount(final String phoneNumber) {

        MyApplication.saveLocalData("phoneNumber", phoneNumber);


        MyApplication.irancellApi.disabelCharging(phoneNumber, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                JsonObject result = new JsonParser().parse(new String(responseBody)).getAsJsonObject();

                if (result.get("IsSuccessfull").getAsBoolean()) {
                    MyApplication.saveLocalData("disabelCharging", true);
                    startActivity(new Intent(VideoKartSignActivity.this, HomeActivity.class));
                    finish();
                } else {
                    Toast.makeText(VideoKartSignActivity.this, result.get("Message").getAsString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(VideoKartSignActivity.this, "خطا در برقراری ارتباط", Toast.LENGTH_SHORT).show();

            }
        });
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (charSequence.length() == 4) {
            verificationSubmit();

        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
