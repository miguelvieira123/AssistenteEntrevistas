package com.museupessoa.maf.assistenteentrevistas;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;


import com.museupessoa.maf.assistenteentrevistas.Fragments.Interview;
import com.museupessoa.maf.assistenteentrevistas.Fragments.SelectProjects;

import java.util.ArrayList;


public class NewInterviewActivity extends AppCompatActivity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_interview);
        final Intent intent = getIntent();
        String projects_path = Environment.getExternalStoragePublicDirectory("/" + getResources().getString(R.string.APP_NAME)).toString();


        FragmentManager fragmentActionManager =  getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentActionManager.beginTransaction();
        SelectProjects selectProjects = new SelectProjects();
        Bundle bundle = new Bundle();
        bundle.putString("path", projects_path);
        selectProjects.setArguments(bundle);
        fragmentTransaction.add(R.id.selectable_projects_list, selectProjects);
        fragmentTransaction.commit();
    }


}
