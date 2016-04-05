package com.museupessoa.maf.assistenteentrevistas;

import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.museupessoa.maf.assistenteentrevistas.adapters.RVProjectAdapter;
import com.museupessoa.maf.assistenteentrevistas.adapters.MainActivityPagerAdapter;
import com.museupessoa.maf.assistenteentrevistas.tabs.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    public final  String APP_NAME = "AssistenteEntrevistas";
    ViewPager pager;
    MainActivityPagerAdapter myPagerAdapter;
    SlidingTabLayout tabs;
    CharSequence Titles[]={"Home","Projetos","Entrevistas","Definições"};

    int Numboftabs =4;
    final String TAG ="AssistenteEntrevistas";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

           if(General.createStructOfFolders(Environment.getExternalStoragePublicDirectory("/"+APP_NAME).toString())){
                General.createProject(Environment.getExternalStorageDirectory()+"/"+APP_NAME,
                      "Geral",General.defaultMetaListInit(),General.defaultQuestionsListInit(),General.defaultLinksListInit(),1);

               General.createInterview(Environment.getExternalStorageDirectory() + "/" + APP_NAME, "Dionísio Silva", "e000");
               General.createInterview(Environment.getExternalStorageDirectory() + "/" + APP_NAME, "Ana Clara Pereira", "e001");

                myPagerAdapter = new MainActivityPagerAdapter(getSupportFragmentManager(), Titles, Numboftabs);
                pager = (ViewPager) findViewById(R.id.pager);
                pager.setAdapter(myPagerAdapter);
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

}
