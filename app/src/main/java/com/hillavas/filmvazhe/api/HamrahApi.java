package com.hillavas.filmvazhe.api;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by ArashJahani on 15/05/2015.
 */

public class HamrahApi {


    private String apiPath = "http://79.175.138.118:8080/%s";


    String url;


    public HamrahApi() {

    }

    private void execute(String type, String method, AsyncHttpResponseHandler callback, Object... params) {


        url = String.format(apiPath, method);

        RequestParams requestParams = new RequestParams();
        for (int i = 1; i < params.length; i += 2) {

            if (params[i] == null) {
                continue;
            }
            String key = String.valueOf(params[i - 1]);
            String value = String.valueOf(params[i]);
            requestParams.put(key, value);
        }

        AsyncHttpClient client = new AsyncHttpClient();
        client.setMaxRetriesAndTimeout(2, 1000);

        if (type.equals("post")) {
            client.post(url, requestParams, callback);
        } else {
            client.get(url, requestParams, callback);
        }
    }

    public void subscribeRequest(String number,String serviceId,String channel, AsyncHttpResponseHandler callback) {
        execute("post", "Otp/SubscribeRequest", callback, "MobileNumber",number,"ServiceId",serviceId,"Channel",channel);
    }


    public void subscribeConfirm(String ServiceId, Integer TransactionId,String pin, AsyncHttpResponseHandler callback) {
        execute("post", "Otp/SubscribeConfirm", callback, "ServiceId",ServiceId,"TransactionId",TransactionId,"pin",pin);
    }

    public void getSubscribeState(String MobileNumber, String ServiceId, AsyncHttpResponseHandler callback) {
        execute("get", "Subscription/GetSubscriptionState", callback, "MobileNumber",MobileNumber,"ServiceId",ServiceId);
    }


}
