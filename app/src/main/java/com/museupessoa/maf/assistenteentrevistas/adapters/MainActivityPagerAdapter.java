package com.museupessoa.maf.assistenteentrevistas.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.museupessoa.maf.assistenteentrevistas.main.Configuration;
import com.museupessoa.maf.assistenteentrevistas.main.Interviews;
import com.museupessoa.maf.assistenteentrevistas.main.Main;
import com.museupessoa.maf.assistenteentrevistas.main.Projects;


public  class MainActivityPagerAdapter extends FragmentStatePagerAdapter{
    CharSequence Titles[];
    int NumbOfTabs;

    public MainActivityPagerAdapter(FragmentManager fm,CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);
        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new Main();
            case 1:
                return new Projects();
            case 2:
                return new Interviews();
            case 3:
                return new Configuration();
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
