package com.hillavas.filmvazhe.screen.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

import com.hillavas.filmvazhe.R;


/**
 * Created by Arash on 15/10/02.
 */
public class LoadingDialog extends Dialog {

    public LoadingDialog(Context context) {
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_loading);
        setCancelable(false);

    }
}
