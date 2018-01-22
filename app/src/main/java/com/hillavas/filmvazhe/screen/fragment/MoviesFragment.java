package com.hillavas.filmvazhe.screen.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationViewPager;
import com.hillavas.filmvazhe.MyApplication;
import com.hillavas.filmvazhe.R;
import com.hillavas.filmvazhe.adapters.MovieTabAdapter;
import com.hillavas.filmvazhe.adapters.MoviesAdapter;
import com.hillavas.filmvazhe.model.Movie;
import com.nispok.snackbar.Snackbar;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.BindView;

public class MoviesFragment extends Fragment implements View.OnClickListener {


    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.pager)
    AHBottomNavigationViewPager viewPager;
    @BindView(R.id.fab_store)
    FloatingActionButton floatingActionButton;

    AHBottomNavigation ahBottomNavigation;

    MoviesAdapter moviesAdapter;
    ArrayList<Movie> movieArrayList = new ArrayList<>();
    Snackbar snackbar;
    private Integer startPosition = 0;
    private boolean notMoreAnyMovie = false;
    private boolean startState = false;


    public MoviesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MoviesFragment newInstance() {
        MoviesFragment fragment = new MoviesFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_movies, container, false);
        ButterKnife.bind(this, v);

//        ahBottomNavigation = (AHBottomNavigation) getActivity().findViewById(R.id.menutabs);
//        ahBottomNavigation.manageFloatingActionButtonBehavior(floatingActionButton);
//        ahBottomNavigation.setTranslucentNavigationEnabled(true);

        floatingActionButton.setVisibility(View.INVISIBLE);


        tabLayout.setBackgroundColor(Color.parseColor(MyApplication.getSharedPreferences().getString("colorMain", "#fafafa")));


        floatingActionButton.setOnClickListener(this);

        tabLayout.setSelectedTabIndicatorColor(Color.parseColor(MyApplication.getSharedPreferences().getString("colorSecond", "#212121")));


        MovieTabAdapter movieTabAdapter = new MovieTabAdapter(getFragmentManager());
        viewPager.setAdapter(movieTabAdapter);
        tabLayout.setupWithViewPager(viewPager);


        for (int i = tabLayout.getTabCount() - 1; i >= 0; i--) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                View view = getActivity().getLayoutInflater().inflate(R.layout.layout_tab_item, null);
                ((TextView) view.findViewById(android.R.id.text1)).setTextColor(Color.parseColor(MyApplication.getSharedPreferences().getString("colorSecond", "#212121")));

                tab.setCustomView(view);

                tab.setText(movieTabAdapter.getPageTitle(i));

                // tab.setIcon(movieTabAdapter.getPageIcon(i));

            }
        }

        return v;

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_store:

                //Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/iran_sans.ttf");

                new MaterialDialog.Builder(getActivity())
                        .typeface(MyApplication.getTypeFace(), MyApplication.getTypeFace())
                        .title("فعال سازی")
                        .contentGravity(GravityEnum.END)
                        .titleGravity(GravityEnum.END)
                        .contentColor(getResources().getColor(R.color.md_grey_700))
                        .positiveColor(getResources().getColor(R.color.md_blue_700))
                        .negativeColor(getResources().getColor(R.color.md_green_700))
                        .content("مایل به فعال سازی بسته هستید؟")
                        .positiveText("بله")
                        .negativeText("بعدا")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                Uri uri = Uri.parse("smsto:307566");
                                Intent it = new Intent(Intent.ACTION_SENDTO, uri);
                                it.putExtra("sms_body", "1");
                                startActivity(it);

                                dialog.dismiss();
                            }
                        })
                        .show();


                break;
        }
    }
}
