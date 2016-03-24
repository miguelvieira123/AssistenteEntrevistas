package com.museupessoa.maf.assistenteentrevistas;

import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.museupessoa.maf.assistenteentrevistas.adapters.RVProjectAdapter;
import com.museupessoa.maf.assistenteentrevistas.adapters.MainActivityPagerAdapter;
import com.museupessoa.maf.assistenteentrevistas.tabs.SlidingTabLayout;


public class MainActivity extends AppCompatActivity {
    public final  String APP_NAME = "AssistenteEntrevistas";
    ViewPager pager;
    MainActivityPagerAdapter myPagerAdapter;
    SlidingTabLayout tabs;
    CharSequence Titles[]={"Home","Projetos","Entrevistas","Definições"};
    int Numboftabs =4;
    String[] info={"Name","Apelido","Idade","Profissão"};
    String[] questions = {"Pergunta 1","Pergunta 2","Pergunta 3","Pergunta 4","Pergunta 5"};
    String[] urls = {"URL 1"};

    final String TAG ="AssistenteEntrevistas";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

<<<<<<< HEAD
           if(General.createStructOfFolders(Environment.getExternalStoragePublicDirectory("/"+APP_NAME).toString())){
=======

           if(General.createStructOfForders(Environment.getExternalStoragePublicDirectory("/"+APP_NAME).toString())){
>>>>>>> alex
                General.createProject(Environment.getExternalStorageDirectory()+"/"+APP_NAME,
                      "Geral",info,questions,urls);

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
