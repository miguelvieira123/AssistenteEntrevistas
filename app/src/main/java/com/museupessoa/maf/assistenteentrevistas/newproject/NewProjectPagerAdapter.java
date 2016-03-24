package com.museupessoa.maf.assistenteentrevistas.newproject;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.museupessoa.maf.assistenteentrevistas.main.Configuration;
import com.museupessoa.maf.assistenteentrevistas.main.Interviews;
import com.museupessoa.maf.assistenteentrevistas.main.Main;
import com.museupessoa.maf.assistenteentrevistas.main.Projects;

/**
 * Created by smit on 24.03.2016.
 */
public class NewProjectPagerAdapter extends FragmentStatePagerAdapter {
    CharSequence Titles[];
    int NumbOfTabs;

    public NewProjectPagerAdapter(FragmentManager fm,CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);
        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new MetaInfo();
            case 1:
                return new Questions();
            case 2:
                return new Urls();
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
