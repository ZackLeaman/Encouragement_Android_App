package com.wordpress.zackleaman.materialtablayout;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Zack on 7/20/2016.
 * This Class is used to extend the FragmentPagerAdapter in order to make a material tab layout
 * with 4 fragment tabs
 */
class ViewPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private ArrayList<String> mTabTitles = new ArrayList<>();

    void addFragments(Fragment fragments, String titles){
        this.mFragments.add(fragments);
        this.mTabTitles.add(titles);
    }


    ViewPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabTitles.get(position);
    }


}
