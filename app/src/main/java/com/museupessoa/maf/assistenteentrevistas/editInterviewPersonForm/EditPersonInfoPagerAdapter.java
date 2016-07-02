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
    private String project_name;


    public EditPersonInfoPagerAdapter(FragmentManager fm, CharSequence mTitles[], int numbOfTabs, String interview_path, String selected_project) {
        super(fm);
        this.Titles = mTitles;
        this.NumbOfTabs = numbOfTabs;
        this.interview_path = interview_path;
        this.project_name = selected_project;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new WrittenForm(interview_path, project_name);
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
