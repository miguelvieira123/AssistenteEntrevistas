package com.museupessoa.maf.assistenteentrevistas.editInterviewPersonForm;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

public class EditPersonInfoPagerAdapter extends FragmentStatePagerAdapter {

    private CharSequence Titles[];
    private int NumbOfTabs;
    private String interview_path;


    public EditPersonInfoPagerAdapter(FragmentManager fm, CharSequence mTitles[], int numbOfTabs, String interview_path) {
        super(fm);
        this.Titles = mTitles;
        this.NumbOfTabs = numbOfTabs;
        this.interview_path = interview_path;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new WrittenForm(interview_path);
            case 1:
                return new AudioForm(interview_path);
            case 2:
                return new PhotoForm(interview_path);
        }
        return null;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    @Override
    public int getCount() {
        return NumbOfTabs;
    }




}
