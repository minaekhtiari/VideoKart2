package com.hillavas.filmvazhe.utils;

import android.os.Build;
import android.provider.Settings;

import com.hillavas.filmvazhe.MyApplication;

/**
 * Created by ArashJahani on 15/06/2015.
 */
public class AndroidUtils {
    public static Integer getAppVersionCode() {
        try {
            Integer versionCode = MyApplication.getContext().getPackageManager().getPackageInfo(MyApplication.getContext().getPackageName(), 0).versionCode;
            return versionCode;
            //return 2f;
        } catch (Exception ex) {
            ex.printStackTrace();
            return 1;
        }
    }

    public static String getAppVersionName() {
        try {
            String versionName = MyApplication.getContext().getPackageManager().getPackageInfo(MyApplication.getContext().getPackageName(), 0).versionName;
            return versionName;
            //return 2f;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "1";
        }
    }

    public static String getDeviceID() {
        String android_id = Settings.Secure.getString(MyApplication.getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        return android_id;
    }

    public static String getDeviceName() {
        return Build.BRAND + "_" + Build.MODEL;
    }

    public static Integer getAndroidVersion() {
        return Build.VERSION.SDK_INT;
    }
}
