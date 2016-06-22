package com.museupessoa.maf.assistenteentrevistas.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.museupessoa.maf.assistenteentrevistas.Fragments.Configuration;
import com.museupessoa.maf.assistenteentrevistas.Fragments.Interviews;
import com.museupessoa.maf.assistenteentrevistas.Fragments.Main;
import com.museupessoa.maf.assistenteentrevistas.Fragments.Projects;
import com.museupessoa.maf.assistenteentrevistas.General;


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

    private void setCurrTab(int pos){

    }
}
