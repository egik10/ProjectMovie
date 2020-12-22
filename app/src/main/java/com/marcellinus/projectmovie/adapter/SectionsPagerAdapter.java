package com.marcellinus.projectmovie.adapter;

import android.content.Context;

import com.marcellinus.projectmovie.fragment.FavoriteFragment;
import com.marcellinus.projectmovie.fragment.MovieFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class SectionsPagerAdapter extends FragmentPagerAdapter {
    private final Context context;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.context = context;
    }

//    @StringRes
//    private final int[] TAB_TITLES = new int[]{
//            R.string.movies,
//            R.string.favorites
//    };

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new MovieFragment();
                break;
            case 1:
                fragment = new FavoriteFragment();
                break;
        }
        return fragment;
    }

//    @Nullable
//    @Override
//    public CharSequence getPageTitle(int position) {
//        return context.getResources().getString(TAB_TITLES[position]);
//    }

    @Override
    public int getCount() {
        return 2;
    }
}
