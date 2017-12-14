package com.yunfan.encoderdemo.ui.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by yunfan on 2017/3/21.
 */

public class MenuAdapter extends FragmentPagerAdapter {

    private final ArrayList<Fragment> mList;

    public MenuAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
        super(fm);
        mList = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mList.get(position);
    }

    @Override
    public int getCount() {
        return mList.size();
    }
}
