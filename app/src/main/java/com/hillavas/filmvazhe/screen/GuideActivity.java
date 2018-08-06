package com.hillavas.filmvazhe.screen;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.codemybrainsout.onboarder.AhoyOnboarderActivity;
import com.codemybrainsout.onboarder.AhoyOnboarderCard;
import com.hillavas.filmvazhe.MyApplication;
import com.hillavas.filmvazhe.R;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class GuideActivity extends AhoyOnboarderActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (MyApplication.getSharedPreferences().getBoolean("guide", false)) {

            if (getPackageName().equals("com.hillavas.videokart")) {
                startActivity(new Intent(GuideActivity.this, VideoKartSignActivity.class));

            } else {
                startActivity(new Intent(GuideActivity.this, FilmVazehSignActivity.class));

            }

            finish();


        }


        AhoyOnboarderCard ahoyOnboarderCard1 = new AhoyOnboarderCard("", "هم فیلم ببین هم لغت یاد بگیر", R.drawable.g1);
        AhoyOnboarderCard ahoyOnboarderCard2 = new AhoyOnboarderCard("", "یک سکانس یک لغت ... یادگیری انگلیسی به همین راحتی", R.drawable.g2);
        AhoyOnboarderCard ahoyOnboarderCard3 = new AhoyOnboarderCard("", "زیرنویس سکانس هارو ببین،لغات جدید رو بهتر یاد بگیر و از دوستات جلو بزن", R.drawable.g3);
        AhoyOnboarderCard ahoyOnboarderCard4 = new AhoyOnboarderCard("", String.format("تلفظ درست لغات رو با %s تمرین کن و یاد بگیر", getString(R.string.app_name)), R.drawable.g4);
        AhoyOnboarderCard ahoyOnboarderCard5 = new AhoyOnboarderCard("", String.format("با %s،دیگه دیدن فیلم ها با زبان اصلی برات یک آرزو نیست", getString(R.string.app_name)), R.drawable.g5);


        ahoyOnboarderCard1.setBackgroundColor(R.color.black_transparent);
        ahoyOnboarderCard2.setBackgroundColor(R.color.black_transparent);
        ahoyOnboarderCard3.setBackgroundColor(R.color.black_transparent);
        ahoyOnboarderCard4.setBackgroundColor(R.color.black_transparent);
        ahoyOnboarderCard5.setBackgroundColor(R.color.black_transparent);


        List<AhoyOnboarderCard> pages = new ArrayList<>();

        pages.add(ahoyOnboarderCard1);
        pages.add(ahoyOnboarderCard2);
        pages.add(ahoyOnboarderCard3);
        pages.add(ahoyOnboarderCard4);
        pages.add(ahoyOnboarderCard5);


        for (AhoyOnboarderCard page : pages) {
            page.setTitleColor(R.color.white);
            page.setDescriptionColor(R.color.white);
            page.setDescriptionTextSize(18);
            page.setIconLayoutParams(550, 550, 32, 32, 32, 32);
        }

        setFinishButtonTitle("بزن بریم");
        showNavigationControls(true);
        setGradientBackground();

        //set the button style you created
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setFinishButtonDrawableStyle(ContextCompat.getDrawable(this, R.drawable.rounded_button));
        }
        setFont(MyApplication.getTypeFace());

        setOnboardPages(pages);
    }

    @Override
    public void onFinishButtonPressed() {

        Class cls = FilmVazehSignActivity.class;

        if (getPackageName().equals("com.hillavas.videokart")) {
            cls = VideoKartSignActivity.class;
        }

        startActivity(new Intent(GuideActivity.this, cls));
        MyApplication.saveLocalData("guide", true);

        finish();
    }


    @Override
    public void onBackPressed() {
        return;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
