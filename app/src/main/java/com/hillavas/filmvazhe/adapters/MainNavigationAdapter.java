package com.hillavas.filmvazhe.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hillavas.filmvazhe.screen.fragment.MoviesFragment;
import com.hillavas.filmvazhe.screen.fragment.OthersFragment;
import com.hillavas.filmvazhe.R;
import com.hillavas.filmvazhe.screen.fragment.MainFragment;
import com.hillavas.filmvazhe.screen.fragment.SearchFragment;

/**
 * Created by Arash on 16/05/18.
 */
public class MainNavigationAdapter extends FragmentPagerAdapter {

    public MainNavigationAdapter(FragmentManager supportFragmentManager) {
        super(supportFragmentManager);

    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new OthersFragment();
            case 1:
                return new SearchFragment();
//            case 2:
//                return new StoreFragment();
            case 2:
                return new MoviesFragment();
            case 3:
                return new MainFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "سایر";
            case 1:
                return "جستجو";
//            case 2:
//                return "فروشگاه";
            case 2:
                return "فیلم\u200Cها";
            case 3:
                return "کتاب\u200Cها";
        }
        return "";
    }

    public int getPageIcon(int position) {
        switch (position) {
            case 0:
                return R.mipmap.ic_more_horiz_black_24dp;
            case 1:
                return R.mipmap.ic_search_white_24dp;
//            case 2:
//                return R.mipmap.ic_local_grocery_store_black_24dp;
            case 2:
                return R.mipmap.ic_ondemand_video_black_24dp;
            case 3:
                return R.mipmap.ic_home_black_24dp;
        }
        return 0;
    }

}
