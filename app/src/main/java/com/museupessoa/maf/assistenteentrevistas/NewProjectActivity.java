package com.museupessoa.maf.assistenteentrevistas;

import android.content.Intent;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
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

public class NewProjectActivity extends AppCompatActivity  {
    public static List<String> metaList;
    public static List<String> questionsList;
    public static List<String> urlsList;
    public static final int DELETE = 1;
    public static final int EDIT = 2;
    public static final int ADD= 3;
    public static final int CHANGE = 4;
    public  String name;
    public  String PATH;
    public int status;
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
        PATH = Environment.getExternalStoragePublicDirectory("/"+getResources().getString(R.string.APP_NAME)).toString();
        Intent intent = getIntent();
        status = intent.getIntExtra("status",0);
        if(status==1) {
            metaList = General.defaultMetaListInit();
            questionsList = new ArrayList<String>();
            urlsList = new ArrayList<String>();
            name = getIntent().getStringExtra("name");
            setTitle("Novo Projeto: " + name.subSequence(0, name.length()));
        }else if(status==2){
            name = getIntent().getStringExtra("name");
            metaList = General.getProjectMeta(name,PATH);
            questionsList = General.getProjectQuestions(name,PATH);
            urlsList = General.getProjectUrls(name,PATH);
            setTitle("Alteração: " + name.subSequence(0, name.length()));
        }
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
                name,metaList,questionsList,urlsList,status)){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
            if(status==1)
        Toast.makeText(getApplicationContext(), "Novo projecto foi criado",
                Toast.LENGTH_LONG).show();
            else if(status==2)
                Toast.makeText(getApplicationContext(), "O projecto foi alterado",
                        Toast.LENGTH_LONG).show();
        }
        else {
            if(status==1)
            Toast.makeText(getApplicationContext(), "Novo projecto não foi criado",
                    Toast.LENGTH_LONG).show();
            else if(status==2)
                Toast.makeText(getApplicationContext(), "O projecto não foi alterado",
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
