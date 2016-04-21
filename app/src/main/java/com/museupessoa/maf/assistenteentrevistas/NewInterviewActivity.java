package com.museupessoa.maf.assistenteentrevistas;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;


import com.museupessoa.maf.assistenteentrevistas.Fragments.SelectProject;


public class NewInterviewActivity extends AppCompatActivity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_interview);
        final Intent intent = getIntent();
        String projects_path = Environment.getExternalStoragePublicDirectory("/" + getResources().getString(R.string.APP_NAME)).toString();


        FragmentManager fragmentActionManager =  getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentActionManager.beginTransaction();
        SelectProject selectProject = new SelectProject();
        Bundle bundle = new Bundle();
        bundle.putString("path", projects_path);
        selectProject.setArguments(bundle);
        fragmentTransaction.add(R.id.selectable_projects_list, selectProject);
        fragmentTransaction.commit();
    }

    public void editInterviewMetadata(String projectName){
        if(projectName != null){
            Toast.makeText(this, "Projeto selecionado: "+ projectName, Toast.LENGTH_SHORT).show();
            //arrancar Activity Editar Interview
        }else{
            Toast.makeText(this, "Nenhum projeto foi selecionado!", Toast.LENGTH_SHORT).show();
        }
    }

}
