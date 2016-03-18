package com.museupessoa.maf.assistenteentrevistas;

import android.app.ActionBar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TableLayout;

import adapters.MyFragmentPagerAdapter;

public class MainActivity extends AppCompatActivity {

    private TableLayout mTabLayout;
    private ViewPager mViewPager;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // novo branch development para controlar as releases

        //mTabLayout = (TableLayout) findViewById(R.id.tab_layout);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);

        mViewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(),getResources().getStringArray(R.array.titles_tab)));
        //mTabLayout.setupWithViewPager(mViewPager);


    }
}
