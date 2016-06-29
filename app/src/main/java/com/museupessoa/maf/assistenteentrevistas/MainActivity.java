package com.museupessoa.maf.assistenteentrevistas;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.museupessoa.maf.assistenteentrevistas.Fragments.Interviews;
import com.museupessoa.maf.assistenteentrevistas.Fragments.Projects;
import com.museupessoa.maf.assistenteentrevistas.adapters.RVProjectAdapter;
import com.museupessoa.maf.assistenteentrevistas.adapters.MainActivityPagerAdapter;
import com.museupessoa.maf.assistenteentrevistas.dialogs.NewProjectDialogFragmentNewItem;
import com.museupessoa.maf.assistenteentrevistas.tabs.SlidingTabLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

         General.DEFAULT_ICON = BitmapFactory.decodeResource(getResources(),
             R.drawable.default_icon_for_m);
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
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy HH:mm:ss");
        if (General.createStructOfFolders(Environment.getExternalStoragePublicDirectory("/" + APP_NAME).toString())) {
            General.createProject(Environment.getExternalStorageDirectory() + "/" + APP_NAME,
                    "Geral", General.defaultMetaListInit(), General.defaultQuestionsListInit(), General.defaultLinksListInit(), 1,sdf.format(new Date()));

            myPagerAdapter = new MainActivityPagerAdapter(getSupportFragmentManager(), Titles, Numboftabs);
            pager = (ViewPager) findViewById(R.id.pager);
            pager.setAdapter(myPagerAdapter);
            pager.setCurrentItem(General.CURR_TAB);

            tabs = (SlidingTabLayout) findViewById(R.id.tabs);
            tabs.setDistributeEvenly(true);

            tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
                @Override
                public int getIndicatorColor(int position) {
                    return getResources().getColor(R.color.tabsCor);
                }

            });
            tabs.setViewPager(pager);
            tabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                   General.CURR_TAB=position;
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        switch (resultCode){
            default:

                break;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_search,menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final  android.support.v7.widget.SearchView searchView = ( android.support.v7.widget.SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new  android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return  false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                switch (General.CURR_TAB){
                    case 1:
                        if(!newText.isEmpty())Projects.updateProjectsList(newText);
                        else  Projects.setCurrentProjectList();
                        break;
                    case 2:
                        if(!newText.isEmpty()) Interviews.updateInterviewList(newText);
                        else Interviews.setCurrentInterviewList();
                        break;
                }
                return false;
            }
        });
       searchView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
           @Override
           public void onViewAttachedToWindow(View v) {
           }

           @Override
           public void onViewDetachedFromWindow(View v) {
               switch (General.CURR_TAB){
                   case 1:
                       Projects.setCurrentProjectList();
                       break;
                   case 2:
                       Interviews.setCurrentInterviewList();
                       break;
               }

           }
       });



        return super.onCreateOptionsMenu(menu);
    }



}
