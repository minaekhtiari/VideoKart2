package com.hillavas.filmvazhe.screen;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

public class UnsubscribeActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}


// implements View.OnClickListener, IabBroadcastReceiver.IabBroadcastListener {
//    //
////
//    TextView img_unsubscribe;
//
//
//    // Debug tag, for logging
//    private static final String TAG = "Charkhone";
//    // Does the user have the premium upgrade?
//    private boolean mIsPremium = false;
//    // SKUs for our products: the premium upgrade (non-consumable) and gas (consumable)
//    private static final String SKU_PREMIUM = "FilmVazhePremium";
//    // (arbitrary) request code for the purchase flow
//    private static final int RC_REQUEST = 10001;
//    // The helper object
//    private IabHelper mHelper;
//    // Provides purchase notification while this app is running
//    private IabBroadcastReceiver mBroadcastReceiver;
//    String payload = "";
//
//    Purchase subscribePurchase;
//
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_unsubscribe);
//
//
//        img_unsubscribe = (TextView) findViewById(R.id.img_unsubscribe);
//
//        img_unsubscribe.setOnClickListener(this);
//
//
//        String base64EncodedPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCIHg4cW9avnZYkKOet/k/TSsXngpWXtVpuwvxXhfn3HdrWV47LA28aBrODL1n9+corT+F5nIVa5pd3p2xR99ob8rv3pssYkkBU9Z21d+JVx4tLxTXetazZCaL8Uux3sJTHNHC6Yuab/SXtZLK/2ArYRKbmbaNBo8CJgHXTNMRSBwIDAQAB";
//
//        // Create the helper, passing it our context and the public key to verify signatures with
//        Log.d(TAG, "Creating IAB helper.");
//        mHelper = new IabHelper(this, base64EncodedPublicKey);
//
//        // enable debug logging (for a production application, you should set this to false).
//        mHelper.enableDebugLogging(true);
//
//        // Start setup. This is asynchronous and the specified listener
//        // will be called once setup completes.
//        Log.d(TAG, "Starting setup.");
//        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
//            public void onIabSetupFinished(IabResult result) {
//                Log.d(TAG, "Setup finished.");
//
//                if (!result.isSuccess()) {
//                    // Oh noes, there was a problem.
//                    complain("Problem setting up in-app billing: " + result);
//                    return;
//                }
//
//                // Have we been disposed of in the meantime? If so, quit.
//                if (mHelper == null) return;
//
////                // IAB is fully set up. Now, let's get an inventory of stuff we own.
//                Log.d(TAG, "Setup successful. Querying inventory.");
//                try {
//                    mHelper.queryInventoryAsync(mGotInventoryListener);
//                } catch (IabHelper.IabAsyncInProgressException e) {
//
//                    complain("Error querying inventory. Another async operation in progress.");
//                }
//            }
//        });
//
//
//    }
//
//
//    @Override
//    public void onClick(View view) {
//
//        switch (view.getId()) {
//            case R.id.img_unsubscribe:
//
//                Log.d(TAG, "Starting subscribe cancellation.");
//                try {
//                    mHelper.cancelSubscribeAsync(subscribePurchase,
//                            mCancelSubscribeFinishedListener);
//                } catch (IabHelper.IabAsyncInProgressException e) {
//                    complain("Error cancel subscribe. Another async operation in progress.");
//                    return;
//                }
//
//                break;
//
//
//        }
//    }
//
//    private IabHelper.OnCancelSubsFinishedListener mCancelSubscribeFinishedListener = new IabHelper.OnCancelSubsFinishedListener() {
//        public void onCancelSubscribeFinished(Purchase purchase, IabResult result) {
//            Log.d(TAG, "Cancelation finished. Purchase: " + purchase + ", result: " + result);
//            if (mHelper == null) return;
//            if (result.isSuccess()) {
//// successfully canceled, so we apply the effects of the item in our // game world's logic
//                Log.d(TAG, "Cancellation successful.");
//            } else {
//                complain("Error while cancelling: " + result);
//            }
//            Log.d(TAG, "End cancellation flow.");
//
//            MyApplication.getSharedPreferences().edit().clear().apply();
//
//            Intent intent = new Intent(UnsubscribeActivity.this, GuideActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//            finish();
//        }
//    };
//
//    // Listener that's called when we finish querying the items and subscriptions we own
//    private IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
//        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
//            Log.d(TAG, "Query inventory finished.");
//
//            // Have we been disposed of in the meantime? If so, quit.
//            if (mHelper == null) return;
//
//            // Is it a failure?
//            if (result.isFailure()) {
//                //complain("Failed to query inventory: " + result);
//                // return;
//            }
//            if (result.isSuccess()) {
//
//
//            }
//
//            Log.d(TAG, "Query inventory was successful.");
//
//            /*
//             * Check for items we own. Notice that for each purchase, we check
//             * the developer payload to see if it's correct! See
//             * verifyDeveloperPayload().
//             */
//
//            // Do we have the premium upgrade?
//            Purchase premiumPurchase = inventory.getPurchase(SKU_PREMIUM);
//            mIsPremium = (premiumPurchase != null && verifyDeveloperPayload(premiumPurchase));
//
//            subscribePurchase = premiumPurchase;
//
//
//        }
//    };
//
//    @Override
//    public void receivedBroadcast() {
//        // Received a broadcast notification that the inventory of items has changed
//        Log.d(TAG, "Received broadcast notification. Querying inventory.");
//        try {
//            mHelper.queryInventoryAsync(mGotInventoryListener);
//        } catch (IabHelper.IabAsyncInProgressException e) {
//            complain("Error querying inventory. Another async operation in progress.");
//        }
//    }
//
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);
//        if (mHelper == null) return;
//
//        // Pass on the activity result to the helper for handling
//        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
//            // not handled, so handle it ourselves (here's where you'd
//            // perform any handling of activity results not related to in-app
//            // billing...
//            super.onActivityResult(requestCode, resultCode, data);
//        } else {
//            Log.d(TAG, "onActivityResult handled by IABUtil.");
//        }
//    }
//
//    /**
//     * Verifies the developer payload of a purchase.
//     */
//    private boolean verifyDeveloperPayload(Purchase p) {
//        String verifyPayload = p.getDeveloperPayload();
//
//        if (verifyPayload.equals(payload)) {
//            return true;
//        }
//
//        return false;
//    }
//
//
//    // We're being destroyed. It's important to dispose of the helper here!
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//
//        // very important:
//        if (mBroadcastReceiver != null) {
//            unregisterReceiver(mBroadcastReceiver);
//        }
//
//        // very important:
//        Log.d(TAG, "Destroying helper.");
//        if (mHelper != null) {
//            mHelper.disposeWhenFinished();
//            mHelper = null;
//        }
//    }
//
//
//    private void complain(String message) {
//        Log.e(TAG, "**** TrivialDrive Error: " + message);
//        alert(message);
//    }
//
//    private void alert(String message) {
//
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
//
//    }
//
//
//}
