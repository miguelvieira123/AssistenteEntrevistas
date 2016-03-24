package com.museupessoa.maf.assistenteentrevistas;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.museupessoa.maf.assistenteentrevistas.newproject.NewProjectPagerAdapter;
import com.museupessoa.maf.assistenteentrevistas.tabs.SlidingTabLayout;

public class NewProject extends AppCompatActivity {

    ViewPager pager;
    NewProjectPagerAdapter newProjectPagerAdapter;
    SlidingTabLayout tabs;
    CharSequence Titles[]={"Metainfo","Perguntas","Referencias"};
    int Numboftabs =3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_project);
        String name = getIntent().getStringExtra("name");
        setTitle("Novo Projeto: " + name.subSequence(0,name.length()));
        newProjectPagerAdapter = new NewProjectPagerAdapter(getSupportFragmentManager(), Titles, Numboftabs);
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(newProjectPagerAdapter);
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true);

        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsCor);
            }

        });
        tabs.setViewPager(pager);
    }
}
