package com.museupessoa.maf.assistenteentrevistas;

import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.museupessoa.maf.assistenteentrevistas.tabs.SlidingTabLayout;

import java.io.File;


public class MainActivity extends AppCompatActivity {
    public final  String APP_NAME = "AssistenteEntrevistas";
    ViewPager pager;
    MyPagerAdapter myPagerAdapter;
    SlidingTabLayout tabs;
    CharSequence Titles[]={"Home","Projectos","Entervistas","Demenições"};
    int Numboftabs =4;
    String[] info={"Name","Apelido","Idade","Profissão"};
    String[] questions = {"Pergunta 1","Pergunta 2","Pergunta 3","Pergunta 4","Pergunta 5"};
    String[] urls = {"URL 1"};

    final String TAG ="AssistenteEntrevistas";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

           if(General.createStructOfForders(Environment.getExternalStoragePublicDirectory("/"+APP_NAME).toString())){
                General.createProject(Environment.getExternalStorageDirectory()+"/"+APP_NAME,
                      "Geral",info,questions,urls);

                myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), Titles, Numboftabs);
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
