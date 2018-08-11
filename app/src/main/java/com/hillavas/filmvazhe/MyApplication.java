package com.hillavas.filmvazhe;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;

import com.crashlytics.android.Crashlytics;
import com.hillavas.filmvazhe.api.HamrahApi;
import com.hillavas.filmvazhe.api.IrancellApi;
import com.hillavas.filmvazhe.api.VideoCardApi;


//import net.jhoobin.jhub.CharkhoneSdkApp;

import net.jhoobin.jhub.CharkhoneSdkApp;

import io.fabric.sdk.android.Fabric;

/**
 * Created by ArashJahani on 14/05/2015.
 */

public class MyApplication extends Application {

    public static VideoCardApi apiVideokart;
    public static IrancellApi irancellApi;
    public static HamrahApi hamrahApi;


    private static SharedPreferences sharedPreferences;
    private static Context _context;


    public static boolean hasNewVersion = false;


    @Override
    public void onCreate() {
        super.onCreate();

//        Colorful.defaults()
//                .primaryColor()
//                .translucent(false)
//                .dark(true);
//        Colorful.init(this);

        final Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics())
                .build();
        Fabric.with(fabric);

        sharedPreferences = getSharedPreferences("qwes11hfs", Context.MODE_PRIVATE);
        _context = getApplicationContext();

        apiVideokart = new VideoCardApi();
        irancellApi = new IrancellApi();
        hamrahApi=new HamrahApi();


        CharkhoneSdkApp.initSdk(this, getSecrets(), false);

    }

    public String[] getSecrets() {
        return getResources().getStringArray(R.array.secrets);
    }

    public static void saveLocalData(Object... items) {

        try {

            SharedPreferences.Editor editor = sharedPreferences.edit();

            for (int i = 0; i < items.length; i += 2) {

                if (items[i + 1] == null) {
                    continue;
                }
                String key = items[i].toString();
                Object value = items[i + 1];

                if (value instanceof Boolean) {
                    editor.putBoolean(key, Boolean.valueOf(value.toString()));
                } else if (value instanceof Integer) {
                    editor.putInt(key, ((Integer) value).intValue());

                } else {
                    editor.putString(key, value.toString());
                }


            }
            editor.commit();

        } catch (Exception ex) {
            ex.printStackTrace();

        }
    }

    public static SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public static Context getContext() {
        return _context;
    }

    public static Typeface getTypeFace() {
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/iran_sans.ttf");
        return typeface;
    }
}
