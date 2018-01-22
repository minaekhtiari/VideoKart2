package com.hillavas.filmvazhe.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hillavas.filmvazhe.screen.fragment.MoviesListFragment;
import com.hillavas.filmvazhe.R;

/**
 * Created by Arash on 16/05/18.
 */
public class MovieTabAdapter extends FragmentPagerAdapter {

    public MovieTabAdapter(FragmentManager supportFragmentManager) {
        super(supportFragmentManager);

    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new MoviesListFragment().newInstance(1);
            case 1:
                return new MoviesListFragment().newInstance(2);
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "سینمایی";
            case 1:
                return "انیمیشن";
        }
        return "";
    }

    public int getPageIcon(int position) {
        switch (position) {
            case 0:
                return R.mipmap.ic_menu_white_24dp;
            case 1:
                return R.mipmap.ic_search_white_24dp;
        }
        return 0;
    }

}
