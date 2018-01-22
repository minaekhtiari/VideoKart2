package com.hillavas.filmvazhe.screen;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationViewPager;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hillavas.filmvazhe.MyApplication;
import com.hillavas.filmvazhe.R;
import com.hillavas.filmvazhe.adapters.SearchSeggestionAdapter;
import com.hillavas.filmvazhe.adapters.MainNavigationAdapter;
import com.hillavas.filmvazhe.screen.core.BaseActivity;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.mikepenz.materialdrawer.Drawer;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.BindView;
import cz.msebera.android.httpclient.Header;

public class HomeActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.menutabs)
    AHBottomNavigation bottomNavigation;
    @BindView(R.id.viewpager)
    AHBottomNavigationViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);


        setupViews();




    }

    private void setupViews() {

        MainNavigationAdapter adapter = new MainNavigationAdapter(getSupportFragmentManager());
        viewPager.setPagingEnabled(false);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(4);
        viewPager.setCurrentItem(3);

        for (int i = 0; i < adapter.getCount(); i++) {
            bottomNavigation.addItem(new AHBottomNavigationItem(adapter.getPageTitle(i).toString(), adapter.getPageIcon(i), R.color.title));
        }
        bottomNavigation.setCurrentItem(3);

        //bottomNavigation.setBehaviorTranslationEnabled(true);

        bottomNavigation.setDefaultBackgroundColor(Color.parseColor(MyApplication.getSharedPreferences().getString("colorMain","#fafafa")));//Colorful.getThemeDelegate().getPrimaryColor().getColorRes()
        bottomNavigation.setAccentColor(Color.parseColor(MyApplication.getSharedPreferences().getString("colorSecond","#212121")));
        bottomNavigation.setInactiveColor(Color.parseColor(MyApplication.getSharedPreferences().getString("colorThird","#666666")));

        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);

        bottomNavigation.setNotificationBackgroundColor(Color.parseColor("#FFB300"));

       // bottomNavigation.setNotification("1", 2);

        try {
            Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/iran_sans.ttf");
            bottomNavigation.setTitleTypeface(typeface);
            bottomNavigation.setNotificationTypeface(typeface);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                viewPager.setCurrentItem(position,false);

                View view = HomeActivity.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                return true;
            }
        });
        bottomNavigation.setOnNavigationPositionListener(new AHBottomNavigation.OnNavigationPositionListener() {
            @Override
            public void onPositionChange(int y) {
                //viewPager.setCurrentItem(y);


            }
        });

    }


    @Override
    public void onClick(View view) {

    }

    @Override
    public void onBackPressed() {


        super.onBackPressed();

    }

}
