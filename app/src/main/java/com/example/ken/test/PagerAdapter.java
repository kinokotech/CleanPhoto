package com.example.ken.test;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.ArrayList;

public class PagerAdapter extends FragmentPagerAdapter {

    private ArrayList<String> image_path = new ArrayList<>( );
    private ArrayList<Integer> image_path_id = new ArrayList<>( );

    private CharSequence[] tabTitles = {"Garbage", "List"};

    public PagerAdapter(FragmentManager fm, ArrayList<Integer> image_path_id, ArrayList<String> image_path) {
        super(fm);
        this.image_path_id = image_path_id;
        this.image_path = image_path;
    }
    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Log.d("PagerAdapter", "getItem()");
                Bundle bundle = new Bundle();
                bundle.putIntegerArrayList("URI_ID",image_path_id);
                bundle.putStringArrayList("URI",image_path);
                Fragment1 fragment1 = new Fragment1();
                fragment1.setArguments(bundle);
                return fragment1;
            case 1:
                return new Fragment2();
            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return tabTitles.length;
    }
}