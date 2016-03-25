package com.museupessoa.maf.assistenteentrevistas;

import android.content.Intent;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.museupessoa.maf.assistenteentrevistas.dialogs.NewProjectDialogFragmentNewItem;
import com.museupessoa.maf.assistenteentrevistas.newproject.MetaInfo;
import com.museupessoa.maf.assistenteentrevistas.newproject.NewProjectPagerAdapter;
import com.museupessoa.maf.assistenteentrevistas.tabs.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

public class NewProject extends AppCompatActivity  {
    public static List<String> metaList;
    public static List<String> questionsList;
    public static List<String> urlsList;
    public  String name;
    ViewPager pager;
    NewProjectPagerAdapter newProjectPagerAdapter;
    SlidingTabLayout tabs;
    CharSequence Titles[]={"Metainfo","Perguntas","Referencias"};
    int Numboftabs =3;
    public final  String APP_NAME = "AssistenteEntrevistas";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_project);
        metaList = General.defaultMetaListInit();
        questionsList = new ArrayList<String>();
        urlsList = new ArrayList<String>();
        name = getIntent().getStringExtra("name");
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

    public void okClicked() {
        if(General.createProject(Environment.getExternalStorageDirectory()+"/"+APP_NAME,
                name,metaList,questionsList,urlsList)){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        Toast.makeText(getApplicationContext(), "Novo projecto foi criado",
                Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(getApplicationContext(), "Novo projecto não foi criado",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void cancelClicked() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        Toast.makeText(getApplicationContext(), "Cancelado",
                Toast.LENGTH_LONG).show();
    }
}
