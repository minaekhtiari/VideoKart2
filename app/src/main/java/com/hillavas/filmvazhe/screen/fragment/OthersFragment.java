package com.hillavas.filmvazhe.screen.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.billingclient.util.IabHelper;
import com.android.billingclient.util.IabResult;
import com.android.billingclient.util.Inventory;
import com.android.billingclient.util.Purchase;
import com.hillavas.filmvazhe.screen.FeedbackActivity;
import com.hillavas.filmvazhe.screen.FilmVazehSignActivity;
import com.hillavas.filmvazhe.screen.UnsubscribeActivity;
import com.hillavas.filmvazhe.screen.WebActivity;
import com.hillavas.filmvazhe.MyApplication;
import com.hillavas.filmvazhe.R;
import com.thebluealliance.spectrum.SpectrumDialog;

import butterknife.ButterKnife;
import butterknife.BindView;

public class OthersFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "Charkhone";
    private IabHelper mHelper;

    public OthersFragment() {
        // Required empty public constructor
    }


    @BindView(R.id.layout_toolbar)
    RelativeLayout toolbar;
    @BindView(R.id.lbl_title)
    TextView lblTitle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_others, container, false);
        ButterKnife.bind(this, v);


        toolbar.setBackgroundColor(Color.parseColor(MyApplication.getSharedPreferences().getString("colorMain", "#fafafa")));
        lblTitle.setTextColor(Color.parseColor(MyApplication.getSharedPreferences().getString("colorSecond", "#212121")));


        ((RelativeLayout) v.findViewById(R.id.btn_raw)).setOnClickListener(this);
        ((RelativeLayout) v.findViewById(R.id.btn_about)).setOnClickListener(this);
        ((RelativeLayout) v.findViewById(R.id.btn_support)).setOnClickListener(this);
        ((RelativeLayout) v.findViewById(R.id.btn_exit)).setOnClickListener(this);
        ((RelativeLayout) v.findViewById(R.id.btn_unsubscribe)).setOnClickListener(this);
        ((RelativeLayout) v.findViewById(R.id.btn_change_theme)).setOnClickListener(this);


        return v;


    }


    @Override
    public void onClick(View view) {
        Intent intent = null;

        switch (view.getId()) {
            case R.id.btn_unsubscribe:

                startActivity(new Intent(getActivity(), UnsubscribeActivity.class));

                break;
            case R.id.btn_change_theme:


                new SpectrumDialog.Builder(getContext())
                        .setColors(R.array.demo_colors)
                        .setSelectedColorRes(R.color.white)
                        .setDismissOnColorSelected(true)
                        .setOutlineWidth(1)
                        .setOnColorSelectedListener(new SpectrumDialog.OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(boolean positiveResult, @ColorInt int color) {
                                if (positiveResult) {

                                    MyApplication.saveLocalData("colorMain", String.format("#%06X", (0xFFFFFF & color)));
                                    if (color == getResources().getColor(R.color.md_grey_500) || color == getResources().getColor(R.color.white)) {
                                        MyApplication.saveLocalData("colorSecond", "#212121");
                                        MyApplication.saveLocalData("colorThird", "#666666");


                                    } else {
                                        MyApplication.saveLocalData("colorSecond", "#ffffff");
                                        MyApplication.saveLocalData("colorThird", "#ffffff");


                                    }
                                    if (color == getResources().getColor(R.color.md_red_500))

                                        MyApplication.saveLocalData("colorDark", String.format("#%06X", (0xFFFFFF & getResources().getColor(R.color.md_red_700))));

                                    else if (color == getResources().getColor(R.color.md_pink_500))

                                        MyApplication.saveLocalData("colorDark", String.format("#%06X", (0xFFFFFF & getResources().getColor(R.color.md_pink_700))));

                                    else if (color == getResources().getColor(R.color.md_purple_500))

                                        MyApplication.saveLocalData("colorDark", String.format("#%06X", (0xFFFFFF & getResources().getColor(R.color.md_purple_700))));

                                    else if (color == getResources().getColor(R.color.md_deep_purple_500))

                                        MyApplication.saveLocalData("colorDark", String.format("#%06X", (0xFFFFFF & getResources().getColor(R.color.md_deep_purple_700))));


                                    else if (color == getResources().getColor(R.color.md_indigo_500))

                                        MyApplication.saveLocalData("colorDark", String.format("#%06X", (0xFFFFFF & getResources().getColor(R.color.md_indigo_700))));


                                    else if (color == getResources().getColor(R.color.md_blue_500))

                                        MyApplication.saveLocalData("colorDark", String.format("#%06X", (0xFFFFFF & getResources().getColor(R.color.md_blue_700))));


                                    else if (color == getResources().getColor(R.color.md_light_blue_500))

                                        MyApplication.saveLocalData("colorDark", String.format("#%06X", (0xFFFFFF & getResources().getColor(R.color.md_light_blue_700))));

                                    else if (color == getResources().getColor(R.color.md_cyan_500))

                                        MyApplication.saveLocalData("colorDark", String.format("#%06X", (0xFFFFFF & getResources().getColor(R.color.md_cyan_700))));

                                    else if (color == getResources().getColor(R.color.md_teal_500))

                                        MyApplication.saveLocalData("colorDark", String.format("#%06X", (0xFFFFFF & getResources().getColor(R.color.md_teal_700))));

                                    else if (color == getResources().getColor(R.color.md_green_500))

                                        MyApplication.saveLocalData("colorDark", String.format("#%06X", (0xFFFFFF & getResources().getColor(R.color.md_green_700))));


                                    else if (color == getResources().getColor(R.color.md_light_green_500))

                                        MyApplication.saveLocalData("colorDark", String.format("#%06X", (0xFFFFFF & getResources().getColor(R.color.md_light_green_700))));

                                    else if (color == getResources().getColor(R.color.md_lime_500))

                                        MyApplication.saveLocalData("colorDark", String.format("#%06X", (0xFFFFFF & getResources().getColor(R.color.md_lime_700))));

                                    else if (color == getResources().getColor(R.color.md_yellow_500))

                                        MyApplication.saveLocalData("colorDark", String.format("#%06X", (0xFFFFFF & getResources().getColor(R.color.md_yellow_700))));

                                    else if (color == getResources().getColor(R.color.md_amber_500))

                                        MyApplication.saveLocalData("colorDark", String.format("#%06X", (0xFFFFFF & getResources().getColor(R.color.md_amber_700))));

                                    else if (color == getResources().getColor(R.color.md_orange_500))

                                        MyApplication.saveLocalData("colorDark", String.format("#%06X", (0xFFFFFF & getResources().getColor(R.color.md_orange_700))));

                                    else if (color == getResources().getColor(R.color.md_deep_orange_500))

                                        MyApplication.saveLocalData("colorDark", String.format("#%06X", (0xFFFFFF & getResources().getColor(R.color.md_deep_orange_700))));

                                    else if (color == getResources().getColor(R.color.md_brown_500))

                                        MyApplication.saveLocalData("colorDark", String.format("#%06X", (0xFFFFFF & getResources().getColor(R.color.md_brown_700))));

                                    else if (color == getResources().getColor(R.color.md_blue_grey_500))

                                        MyApplication.saveLocalData("colorDark", String.format("#%06X", (0xFFFFFF & getResources().getColor(R.color.md_blue_grey_700))));

                                    else if (color == getResources().getColor(R.color.md_grey_500))

                                        MyApplication.saveLocalData("colorDark", String.format("#%06X", (0xFFFFFF & getResources().getColor(R.color.md_grey_700))));

                                    else if (color == getResources().getColor(R.color.white))

                                        MyApplication.saveLocalData("colorDark", "#616161");


                                    new MaterialDialog.Builder(getActivity())
                                            .typeface(MyApplication.getTypeFace(), MyApplication.getTypeFace())
                                            .title("توجه")
                                            .contentGravity(GravityEnum.END)
                                            .titleGravity(GravityEnum.END)
                                            .contentColor(getResources().getColor(R.color.md_grey_700))
                                            .positiveColor(getResources().getColor(R.color.md_blue_700))
                                            .negativeColor(getResources().getColor(R.color.md_green_700))

                                            .content("برای تنظیم شدن رنگ جدید کافیست برنامه را بسته و دوباره وارد شوید")
                                            .positiveText("باشه")
                                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                                @Override
                                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                                    dialog.dismiss();
                                                    Intent intent1 = new Intent(getActivity(), FilmVazehSignActivity.class);
                                                    intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    getActivity().startActivity(intent1);

                                                }
                                            })
                                            .show();

                                }
                            }
                        }).build().show(getFragmentManager(), "dialog_demo_1");

                break;
            case R.id.btn_raw:

                try {


                    intent = new Intent(getContext(), WebActivity.class);
                    intent.putExtra("title", "قوانین");
                    if (getContext().getPackageName().equals("com.hillavas.videokart")) {
                        intent.putExtra("url", "file:///android_asset/terms.html");
                    } else {
                        intent.putExtra("url", "file:///android_asset/terms_irancell.html");
                    }
                } catch (Exception ex) {
                }
                break;
            case R.id.btn_about:
                try {

                    intent = new Intent(getContext(), WebActivity.class);
                    intent.putExtra("title", "درباره ما");
                    if (getContext().getPackageName().equals("com.hillavas.videokart")) {
                        intent.putExtra("url", "file:///android_asset/about.html");
                    } else {
                        intent.putExtra("url", "file:///android_asset/about_irancell.html");
                    }
                } catch (Exception ex) {
                }

                break;
            case R.id.btn_support:
                intent = new Intent(getContext(), FeedbackActivity.class);
                break;

            case R.id.btn_exit:
                getActivity().finish();
                break;
        }

        if (intent != null) {
            startActivity(intent);
        }
    }
}
