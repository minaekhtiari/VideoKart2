package com.hillavas.filmvazhe.api;


import com.hillavas.filmvazhe.MyApplication;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by ArashJahani on 15/05/2015.
 */

public class IrancellApi {


    private String apiPath = "http://79.175.138.91:1035/%s";


    String url;


    public IrancellApi() {

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
        client.addHeader("ApiKey", "35de8d82-acf6-424c-87c7-27342628043d");
        client.setMaxRetriesAndTimeout(2, 1000);

        if (type.equals("post")) {
            client.post(url, requestParams, callback);
        } else {
            client.get(url, requestParams, callback);
        }
    }


    public void disabelCharging(String phoneNumber, AsyncHttpResponseHandler callback) {
        execute("post", "Subscription/DisabelCharging", callback, "mobileNumber", phoneNumber);
    }

    public void isSubscribed(String phoneNumber, AsyncHttpResponseHandler callback) {
        execute("get", "Subscription/IsSubscribed", callback, "mobileNumber", phoneNumber);
    }

}
