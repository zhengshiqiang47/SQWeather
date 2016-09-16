package com.example.zsq.sqweather;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by zsq on 16-9-11.
 */

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    ArrayList<WeatherFragment> fragments;
    public MyFragmentPagerAdapter(FragmentManager fm,ArrayList<WeatherFragment> list) {
        super(fm);
        fragments = list;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        fragments.get(position).onDestroy();
        Log.e("Adapter", position + "");
    }
}
