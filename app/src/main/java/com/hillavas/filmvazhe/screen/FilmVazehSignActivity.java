package com.hillavas.filmvazhe.screen;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
//
//import com.android.billingclient.util.IabBroadcastReceiver;
//import com.android.billingclient.util.IabHelper;
//import com.android.billingclient.util.IabResult;
//import com.android.billingclient.util.Inventory;
//import com.android.billingclient.util.Purchase;
import com.android.billingclient.util.IabBroadcastReceiver;
import com.android.billingclient.util.IabHelper;
import com.android.billingclient.util.IabResult;
import com.android.billingclient.util.Inventory;
import com.android.billingclient.util.Purchase;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hillavas.filmvazhe.MyApplication;
import com.hillavas.filmvazhe.R;
import com.hillavas.filmvazhe.screen.core.BaseActivity;
import com.hillavas.filmvazhe.utils.AndroidUtils;
import com.hillavas.filmvazhe.utils.ToastHandler;
import com.hillavas.filmvazhe.utils.Validator;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.listeners.ActionClickListener;

import butterknife.ButterKnife;
import butterknife.BindView;
import cz.msebera.android.httpclient.Header;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.hillavas.filmvazhe.MyApplication.getContext;
import static com.hillavas.filmvazhe.MyApplication.irancellApi;

public class FilmVazehSignActivity extends BaseActivity implements View.OnClickListener, IabBroadcastReceiver.IabBroadcastListener {

    @BindView(R.id.layout_sign_up)
    LinearLayout layoutSignUp;
    @BindView(R.id.btn_sign_up)
    Button btnSignUp;
    @BindView(R.id.txt_phone)
    EditText txtPhone;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    //
    String code, phoneNumber = "";

    // Debug tag, for logging
    private static final String TAG = "Charkhone";
    // Does the user have the premium upgrade?
    private boolean mIsPremium = false;
    // SKUs for our products: the premium upgrade (non-consumable) and gas (consumable)
    private static final String SKU_PREMIUM = "FilmVazhePremium";
    // (arbitrary) request code for the purchase flow
    private static final int RC_REQUEST = 10001;
    // The helper object
    private IabHelper mHelper;
    // Provides purchase notification while this app is running
    private IabBroadcastReceiver mBroadcastReceiver;
    String payload = "";

    public String[] getSecrets() {
        return getResources().getStringArray(R.array.secrets);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filmvazeh_sign);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/iran_sans.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );


        ButterKnife.bind(this);

        btnSignUp.setOnClickListener(this);

        String base64EncodedPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCIHg4cW9avnZYkKOet/k/TSsXngpWXtVpuwvxXhfn3HdrWV47LA28aBrODL1n9+corT+F5nIVa5pd3p2xR99ob8rv3pssYkkBU9Z21d+JVx4tLxTXetazZCaL8Uux3sJTHNHC6Yuab/SXtZLK/2ArYRKbmbaNBo8CJgHXTNMRSBwIDAQAB";

        // Create the helper, passing it our context and the public key to verify signatures with
        Log.d(TAG, "Creating IAB helper.");
        mHelper = new IabHelper(this, base64EncodedPublicKey);

        // enable debug logging (for a production application, you should set this to false).
        mHelper.enableDebugLogging(false);

        // Start setup. This is asynchronous and the specified listener
        // will be called once setup completes.

        Log.d(TAG, "Starting setup.");

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

                        ToastHandler.onShow(FilmVazehSignActivity.this, " نسخه جدید برنامه رو نصب کنید.", Toast.LENGTH_LONG);
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(data.get("update").getAsJsonObject().get("download_url").getAsString())));
                        finish();
                        return;
                    }

                    MyApplication.saveLocalData(
                            "apiToken", data.get("device").getAsJsonObject().get("api_token").getAsString());


                    mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
                        public void onIabSetupFinished(IabResult result) {
                            Log.d(TAG, "Setup finished.");

                            if (!result.isSuccess()) {
                                // Oh noes, there was a problem.
                                complain("Problem setting up in-app billing: " + result);
                                return;
                            }

                            // Have we been disposed of in the meantime? If so, quit.
                            if (mHelper == null) return;

//                // IAB is fully set up. Now, let's get an inventory of stuff we own.
                            Log.d(TAG, "Setup successful. Querying inventory.");
                            try {
                                mHelper.queryInventoryAsync(mGotInventoryListener);
                            } catch (IabHelper.IabAsyncInProgressException e) {

                                complain("Error querying inventory. Another async operation in progress.");
                            }
                        }
                    });

//                    String number = MyApplication.getSharedPreferences().getString("phoneNumber", null);
//                    if (number != null) {
//                        txtPhone.setText(number);
//                        checkAccount(number);
//
//
//                    }


                } else {
                    SnackbarManager.show(
                            Snackbar.with(getContext()) // context
                                    .textTypeface(MyApplication.getTypeFace())
                                    .duration(Snackbar.SnackbarDuration.LENGTH_SHORT)
                                    .swipeToDismiss(true)
                                    .text(status.get("message").getAsString()) // text to display
                                    .actionListener(new ActionClickListener() {
                                        @Override
                                        public void onActionClicked(Snackbar snackbar) {

                                            loginAnonymous();

                                        }
                                    })
                            , FilmVazehSignActivity.this); // activity where it is dis


                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {


                SnackbarManager.show(
                        Snackbar.with(getContext()) // context
                                .textTypeface(MyApplication.getTypeFace())
                                .duration(Snackbar.SnackbarDuration.LENGTH_LONG)
                                .swipeToDismiss(false)
                                .actionLabel("تلاش مجدد")
                                .text("خطا در برقراری ارتباط") // text to display
                                .actionListener(new ActionClickListener() {
                                    @Override
                                    public void onActionClicked(Snackbar snackbar) {


                                        loginAnonymous();

                                    }
                                })
                        , FilmVazehSignActivity.this); // activity where it is dis

            }

            @Override
            public void onFinish() {
                super.onFinish();


            }
        });


    }

//

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_sign_up:

                phoneNumber = String.valueOf(txtPhone.getText());


                Intent fillInIntent = new Intent();
                if (!Validator.isMobileNumber(phoneNumber)) {
                    alert("شماره ایرانسلی معتبر نیست.");
                    return;
                }

//                MyApplication.saveLocalData("phoneNumber", phoneNumber);
//
//                checkAccount(phoneNumber);

                fillInIntent.putExtra("msisdn", phoneNumber);
                mHelper.setFillInIntent(fillInIntent);


                payload = "";
                try {

                    mHelper.launchPurchaseFlow(FilmVazehSignActivity.this, SKU_PREMIUM, RC_REQUEST,
                            mPurchaseFinishedListener, payload);

                    layoutSignUp.setVisibility(View.INVISIBLE);

                } catch (IabHelper.IabAsyncInProgressException e) {
                    alert("Error launching purchase flow. Another async operation in progress.");
                }

                break;

        }
    }

    void checkAccount(final String phoneNumber) {

        if (phoneNumber == null) {
            return;
        }

        layoutSignUp.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);


        irancellApi.isSubscribed(phoneNumber, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                JsonObject result = new JsonParser().parse(new String(responseBody)).getAsJsonObject();

                if (result.get("IsSuccessfull").getAsBoolean()) { // if is false

                    if (result.get("Result").getAsBoolean()) {
                        startActivity(new Intent(FilmVazehSignActivity.this, HomeActivity.class));
                        finish();
                    } else {
                        sendMessageDialog();
                    }

                } else {

                    Toast.makeText(FilmVazehSignActivity.this, result.get("Message").getAsString(), Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(FilmVazehSignActivity.this, "خطا در برقراری ارتباط", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFinish() {
                super.onFinish();

                layoutSignUp.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

    }

    void sendMessageDialog() {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(new ContextThemeWrapper(FilmVazehSignActivity.this, R.style.myDialog));
        builder1.setMessage(
                "جهت عضویت در الفچین عدد ۱ را به ۷۳۸۷۵۲ ارسال کنید."
        );
        builder1.setTitle("اشتراک");
        //builder1.setCancelable(false);

        builder1.setPositiveButton(
                "ارسال",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Uri sms_uri = Uri.parse("smsto:738752");
                        Intent sms_intent = new Intent(Intent.ACTION_SENDTO, sms_uri);
                        sms_intent.putExtra("sms_body", "1");
                        startActivity(sms_intent);

                        dialog.cancel();
                        FilmVazehSignActivity.this.finish();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    //
    // Listener that's called when we finish querying the items and subscriptions we own
    private IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            Log.d(TAG, "Query inventory finished.");

            // Have we been disposed of in the meantime? If so, quit.
            if (mHelper == null) return;

            // Is it a failure?
            if (result.isFailure()) {
                //complain("Failed to query inventory: " + result);
                // return;
            }
            if (result.isSuccess()) {


            }

            Log.d(TAG, "Query inventory was successful.");

            /*
             * Check for items we own. Notice that for each purchase, we check
             * the developer payload to see if it's correct! See
             * verifyDeveloperPayload().
             */

            // Do we have the premium upgrade?
            Purchase premiumPurchase = inventory.getPurchase(SKU_PREMIUM);
            mIsPremium = (premiumPurchase != null && verifyDeveloperPayload(premiumPurchase));

            if (mIsPremium) {

                if (!MyApplication.getSharedPreferences().getBoolean("disabelCharging", false)) {

                    DisableAccount(MyApplication.getSharedPreferences().getString("phoneNumber", ""));

                } else {

                    startActivity(new Intent(FilmVazehSignActivity.this, HomeActivity.class));
finish();
                }
            } else {
                layoutSignUp.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);

            }

        }
    };

    @Override
    public void receivedBroadcast() {
        // Received a broadcast notification that the inventory of items has changed
        Log.d(TAG, "Received broadcast notification. Querying inventory.");
        try {
            mHelper.queryInventoryAsync(mGotInventoryListener);
        } catch (IabHelper.IabAsyncInProgressException e) {
            complain("Error querying inventory. Another async operation in progress.");
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);
        if (mHelper == null) return;

        // Pass on the activity result to the helper for handling
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            // not handled, so handle it ourselves (here's where you'd
            // perform any handling of activity results not related to in-app
            // billing...
            super.onActivityResult(requestCode, resultCode, data);
        } else {
            Log.d(TAG, "onActivityResult handled by IABUtil.");
        }
    }

    /**
     * Verifies the developer payload of a purchase.
     */
    private boolean verifyDeveloperPayload(Purchase p) {
        String verifyPayload = p.getDeveloperPayload();

        if (verifyPayload.equals(payload)) {
            return true;
        }

        return false;
    }

    // Callback for when a purchase is finished
    private IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            Log.d(TAG, "Purchase finished: " + result + ", purchase: " + purchase);
            layoutSignUp.setVisibility(View.VISIBLE);

            // if we were disposed of in the meantime, quit.
            if (mHelper == null) return;

            if (result.isFailure()) {
                complain("خطا در پرداخت.");
                return;
            }
            if (!verifyDeveloperPayload(purchase)) {
                complain("خطا در تایید پرداخت");
                return;
            }

            Log.d(TAG, "Purchase successful.");

            if (purchase.getSku().equals(SKU_PREMIUM)) {
                // bought the premium upgrade!
                Log.d(TAG, "Purchase is premium upgrade. Congratulating user.");
                //alert("Thank you for upgrading to premium!");
                mIsPremium = true;

                DisableAccount(phoneNumber);
            }
        }
    };

    void DisableAccount(final String phoneNumber) {

        MyApplication.saveLocalData("phoneNumber", phoneNumber);

        irancellApi.disabelCharging(phoneNumber, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                JsonObject result = new JsonParser().parse(new String(responseBody)).getAsJsonObject();

                if (result.get("IsSuccessfull").getAsBoolean()) { // if is false

                    if (result.get("Result").getAsBoolean()) {

                        MyApplication.saveLocalData("disabelCharging", true);
                    }
                } else {
                    // Toast.makeText(FilmVazehSignActivity.this, result.get("Message").getAsString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                //Toast.makeText(IntroActivity.this, "خطا در برقراری ارتباط", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFinish() {

                startActivity(new Intent(FilmVazehSignActivity.this, HomeActivity.class));
                finish();
            }
        });
    }


    // We're being destroyed. It's important to dispose of the helper here!
    @Override
    public void onDestroy() {
        super.onDestroy();

        // very important:
        if (mBroadcastReceiver != null) {
            unregisterReceiver(mBroadcastReceiver);
        }

        // very important:
        Log.d(TAG, "Destroying helper.");
        if (mHelper != null) {
            mHelper.disposeWhenFinished();
            mHelper = null;
        }
    }

    private void complain(String message) {
        Log.e(TAG, "**** TrivialDrive Error: " + message);
        alert(message);
    }

    private void alert(String message) {

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
