package com.example.coursework;
// Zarko Ivanov S1431661

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class FragmentAdapter extends FragmentStatePagerAdapter {
    private final List<Fragment> fragList = new ArrayList<>();
    private final List<String> fragTitleList = new ArrayList<>();

    public FragmentAdapter(FragmentManager fm){
        super(fm);
    }

    void addFragment(Fragment frag, String title){
        fragList.add(frag);
        fragTitleList.add(title);
    }

    @Override
    public Fragment getItem(int position){
        return fragList.get(position);
    }

    @Override
    public int getCount(){
        return fragList.size();
    }

}
