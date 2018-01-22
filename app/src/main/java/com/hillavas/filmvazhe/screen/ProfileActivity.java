package com.hillavas.filmvazhe.screen;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hillavas.filmvazhe.screen.core.BaseActivity;
import com.hillavas.filmvazhe.MyApplication;
import com.hillavas.filmvazhe.R;
import com.hillavas.filmvazhe.model.Transaction;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

import static com.hillavas.filmvazhe.MyApplication.getContext;

public class ProfileActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.lbl_payment_text)
    TextView lblPaymentText;
    @BindView(R.id.lbl_phone_number)
    TextView lblPhoneNumber;
    @BindView(R.id.btn_subscribe_service)
    Button btnSubScribe;
    @BindView(R.id.btn_off_service)
    Button btnOffService;

    @BindView(R.id.transaction_list)
    ListView lvTransactionList;
    @BindView(R.id.btn_edit)
    Button btnEdit;
    @BindView(R.id.txt_name)
    EditText txtName;
    @BindView(R.id.transaction_progress)
    ProgressBar transactionProgress;

    String shortCode = null;
    String activationCode = null;

    String phoneNumber;
    private ArrayList<Transaction> transactionList = new ArrayList<>();
    private static final int PERMISSION_SEND_SMS = 123;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ButterKnife.bind(this);

        this.setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View custom_action_bar = getLayoutInflater().inflate(R.layout.actionbar_profile, null);

        ((TextView) custom_action_bar.findViewById(R.id.lbl_title)).setText(getString(R.string.profile));
        custom_action_bar.findViewById(R.id.btn_back).setOnClickListener(this);
        custom_action_bar.findViewById(R.id.lbl_profile_exit).setOnClickListener(this);

        toolbar.setBackgroundColor(Color.parseColor(MyApplication.getSharedPreferences().getString("colorMain","#fafafa")));
        ((ImageButton) custom_action_bar.findViewById(R.id.btn_back)).setColorFilter(Color.parseColor(MyApplication.getSharedPreferences().getString("colorSecond","#212121")));
        ((TextView) custom_action_bar.findViewById(R.id.lbl_title)).setTextColor(Color.parseColor(MyApplication.getSharedPreferences().getString("colorSecond","#212121")));

        getSupportActionBar().setCustomView(custom_action_bar);

        btnSubScribe.setOnClickListener(this);
        btnOffService.setOnClickListener(this);
        btnEdit.setOnClickListener(this);

        phoneNumber = MyApplication.getSharedPreferences().getString("phoneNumber", "");
        lblPhoneNumber.setText(phoneNumber);
        txtName.setText(MyApplication.getSharedPreferences().getString("fullName", ""));

        getBasicInfo();

    }


    void getBasicInfo() {



    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.btn_edit:

                MyApplication.apiVideokart.editAccount(String.valueOf(txtName.getText()), new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        MyApplication.saveLocalData("fullName", String.valueOf(txtName.getText()));

                        SnackbarManager.show(
                                Snackbar.with(getContext()) // context
                                        .textTypeface(MyApplication.getTypeFace())
                                        .duration(Snackbar.SnackbarDuration.LENGTH_SHORT)
                                        .swipeToDismiss(true)
                                        .text("ذخیره شد") // text to display

                                , ProfileActivity.this); // activity where it is dis
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                        SnackbarManager.show(
                                Snackbar.with(getContext()) // context
                                        .textTypeface(MyApplication.getTypeFace())
                                        .duration(Snackbar.SnackbarDuration.LENGTH_SHORT)
                                        .swipeToDismiss(true)
                                        .text("خطایی رخ داده") // text to display

                                , ProfileActivity.this); // activity where it is dis
                    }
                });

                break;

            case R.id.btn_subscribe_service:

                if (shortCode != null && activationCode != null) {

                    if (Build.VERSION.SDK_INT >= 23) {
                        if (ContextCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                            // request permission (see result in onRequestPermissionsResult() method)
                            ActivityCompat.requestPermissions(ProfileActivity.this,
                                    new String[]{Manifest.permission.SEND_SMS},
                                    PERMISSION_SEND_SMS);
                        } else {
                            // permission already granted run sms send
                            sendSMS();
                        }
                    } else {
                        sendSMS();
                    }


                } else {
                    SnackbarManager.show(
                            Snackbar.with(getContext()) // context
                                    .textTypeface(MyApplication.getTypeFace())
                                    .duration(Snackbar.SnackbarDuration.LENGTH_SHORT)
                                    .swipeToDismiss(true)
                                    .text("خطا در برقراری ارتباط") // text to display

                            , ProfileActivity.this); // activity where it is dis                    getBasicInfo();
                }

                break;

            case R.id.btn_off_service:
                btnOffService.setEnabled(false);




                break;
            case R.id.lbl_profile_exit:
                MyApplication.getSharedPreferences().edit().clear().commit();
                startActivity(new Intent(ProfileActivity.this, FilmVazehSignActivity.class));
                finish();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_SEND_SMS: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    sendSMS();
                } else {
                    Toast.makeText(ProfileActivity.this, "SEND_SMS Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                return;
            }
        }
    }


    void sendSMS() {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(shortCode, null, activationCode, null, null);
            Toast.makeText(ProfileActivity.this, "پیام شما با موفقیت ارسال شد. منتظر پیامک تایید باشید", Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
