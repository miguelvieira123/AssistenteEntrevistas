package com.museupessoa.maf.assistenteentrevistas;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.museupessoa.maf.assistenteentrevistas.adapters.RVProjectAdapter;
import com.museupessoa.maf.assistenteentrevistas.adapters.MainActivityPagerAdapter;
import com.museupessoa.maf.assistenteentrevistas.tabs.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_PERMISSIONS = 112;
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
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(General.CR, General.CG, General.CB)));
        General.PATH = Environment.getExternalStoragePublicDirectory("/"+getResources().getString(R.string.APP_NAME)).toString();
        boolean hasStoragePermission = (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        boolean hasMicrophonePermission = (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED);
        if ( !hasMicrophonePermission && !hasStoragePermission ) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, REQUEST_PERMISSIONS);
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        boolean hasStoragePermission = (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        boolean hasMicrophonePermission = (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED);
        if (hasMicrophonePermission && hasStoragePermission) {
            createAppFolders();
        }
    }

    private void createAppFolders(){
        if (General.createStructOfFolders(Environment.getExternalStoragePublicDirectory("/" + APP_NAME).toString())) {
            General.createProject(Environment.getExternalStorageDirectory() + "/" + APP_NAME,
                    "Geral", General.defaultMetaListInit(), General.defaultQuestionsListInit(), General.defaultLinksListInit(), 1);

            //General.createInterview(Environment.getExternalStorageDirectory() + "/" + APP_NAME, "Dionísio Silva", "e000");
            //General.createInterview(Environment.getExternalStorageDirectory() + "/" + APP_NAME, "Ana Clara Pereira", "e001");

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


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSIONS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //createAppFolders();
                } else {
                    Toast.makeText(this, "Aplicação não consegue criar as Pastas", Toast.LENGTH_LONG).show();
                }
                if (grantResults.length > 1 && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(this, "Aplicação não consegue gravar áudio", Toast.LENGTH_LONG).show();
                }
            }
        }

    }

}
