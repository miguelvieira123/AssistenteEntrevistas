package com.museupessoa.maf.assistenteentrevistas.newinterview;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class NewInterviewPagerAdapter  extends FragmentStatePagerAdapter {

    CharSequence Titles[];
    int NumbOfTabs;

    public NewInterviewPagerAdapter(FragmentManager fm, CharSequence mTitles[], int numbOfTabs) {
        super(fm);
        this.Titles = mTitles;
        this.NumbOfTabs = numbOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new WrittenForm();
            case 1:
                return new AudioForm();
            case 2:
                return new PhotoForm();
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
