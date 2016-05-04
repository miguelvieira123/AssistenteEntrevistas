package com.museupessoa.maf.assistenteentrevistas.editInterviewPersonForm;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class EditPersonInfoPagerAdapter extends FragmentStatePagerAdapter {

    private CharSequence Titles[];
    private int NumbOfTabs;
    private String new_interview_path;


    public EditPersonInfoPagerAdapter(FragmentManager fm, CharSequence mTitles[], int numbOfTabs, String new_interview_path) {
        super(fm);
        this.Titles = mTitles;
        this.NumbOfTabs = numbOfTabs;
        this.new_interview_path = new_interview_path;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new WrittenForm(new_interview_path);
            case 1:
                return new AudioForm(new_interview_path);
            case 2:
                return new PhotoForm(new_interview_path);
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
