package com.museupessoa.maf.assistenteentrevistas;

import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.museupessoa.maf.assistenteentrevistas.auxiliary.UploadingFileToServer;
import com.museupessoa.maf.assistenteentrevistas.auxiliary.Zip;
import com.museupessoa.maf.assistenteentrevistas.dialogs.NewProjectAddDialogFragment;
import com.museupessoa.maf.assistenteentrevistas.dialogs.NewProjectDialogFragmentEdit;
import com.museupessoa.maf.assistenteentrevistas.dialogs.NewProjectDialogFragmentNewItem;
import com.museupessoa.maf.assistenteentrevistas.newproject.MetaInfo;
import com.museupessoa.maf.assistenteentrevistas.newproject.NewProjectPagerAdapter;
import com.museupessoa.maf.assistenteentrevistas.tabs.SlidingTabLayout;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    CharSequence Titles[]={"MetaInfo","Perguntas","Referências"};
    int Numboftabs =3;
    public final  String APP_NAME = "AssistenteEntrevistas";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_project);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(General.CR, General.CG, General.CB)));
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
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy HH:mm:ss");
        if(General.createProject(Environment.getExternalStorageDirectory()+"/"+APP_NAME,
                name,metaList,questionsList,urlsList,status,sdf.format(new Date()))){
            if(status==1)
        Toast.makeText(getApplicationContext(), "Novo projeto foi criado",
                Toast.LENGTH_LONG).show();
            else if(status==2)
                Toast.makeText(getApplicationContext(), "O projeto foi alterado",
                        Toast.LENGTH_LONG).show();
        }
        else {
            if(status==1)
            Toast.makeText(getApplicationContext(), "Novo projeto não foi criado",
                    Toast.LENGTH_LONG).show();
            else if(status==2)
                Toast.makeText(getApplicationContext(), "O projeto não foi alterado",
                        Toast.LENGTH_LONG).show();
        }
        this.finish();
    }

    public void cancelClicked() {
        this.finish();
        Toast.makeText(getApplicationContext(), "Cancelado",
                Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_new_project, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.P_Accept:
                FragmentManager addProject = getSupportFragmentManager();
                NewProjectAddDialogFragment dialogProjectName = new NewProjectAddDialogFragment();
                dialogProjectName.show(addProject, "NewProjectAddDialogFragment");
                break;
        }
        return(super.onOptionsItemSelected(item));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        switch (resultCode){
            case NewProjectActivity.ADD:
                switch (requestCode) {
                    case 1:
                        NewProjectActivity.metaList.add(data.getStringExtra(NewProjectDialogFragmentNewItem.REQUEST));
                        Toast.makeText(this, "Campo foi adicionado", Toast.LENGTH_SHORT).show();
                        break;
                }
        }

    }
    @Override
    public void onBackPressed() {
        FragmentManager addProject = getSupportFragmentManager();
        NewProjectAddDialogFragment dialogProjectName = new NewProjectAddDialogFragment();
        dialogProjectName.show(addProject, "NewProjectAddDialogFragment");
    }

}
