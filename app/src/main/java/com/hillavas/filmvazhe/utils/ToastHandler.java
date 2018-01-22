package com.hillavas.filmvazhe.utils;

import android.content.Context;
import android.widget.Toast;

import com.hillavas.filmvazhe.MyApplication;


/**
 * Created by Arash
 */
public class ToastHandler {

    public static void onShow(Context context,String desc,int length){
        Toast.makeText(MyApplication.getContext(),desc,length).show();
    }
}
