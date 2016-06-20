package com.museupessoa.maf.assistenteentrevistas;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;
import com.museupessoa.maf.assistenteentrevistas.adapters.RVProjectAdapter;
import com.museupessoa.maf.assistenteentrevistas.auxiliary.Http;
import com.museupessoa.maf.assistenteentrevistas.dialogs.AddSrcLinkDialogFragment;
import com.museupessoa.maf.assistenteentrevistas.dialogs.DeleteSrcLinkDialogFragment;
import com.museupessoa.maf.assistenteentrevistas.dialogs.DowloadProjectDialogFragment;
import com.museupessoa.maf.assistenteentrevistas.dialogs.NewProjectDialogFragmentNewItem;
import com.museupessoa.maf.assistenteentrevistas.dialogs.NewProjectItemActionDialogFragment;
import com.museupessoa.maf.assistenteentrevistas.dialogs.ProjectActionDialogFragment;
import com.museupessoa.maf.assistenteentrevistas.units.ProjectUnit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DownloadProjectsActivity extends AppCompatActivity {
    private Spinner src;
    public  String PATH;
    private static final int REQUEST = 1;
    List<String> links;
    public ArrayAdapter<String> dataAdapter;
    public Integer lastPos;
    public List<String> remoteProjects;
    public List<ProjectUnit> projects;
    public List<ProjectUnit> localProjects;
    public static final  String TAG = "AssistenteEntrevistas";
    private RecyclerView recyclerView;
    RVProjectAdapter adapter;
    private FloatingActionButton add;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        lastPos=0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_projects);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(General.CR, General.CG, General.CB)));
        setTitle("Download de Projetos");
        recyclerView = (RecyclerView) findViewById(R.id.remoteProjectsRV);
        LinearLayoutManager llm = new LinearLayoutManager(DownloadProjectsActivity.this);
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);
        add = (FloatingActionButton) findViewById(R.id.fabDownloadProjects);



        src = (Spinner) findViewById(R.id.Sources);
        PATH = Environment.getExternalStoragePublicDirectory("/" + getResources().getString(R.string.APP_NAME)).toString();
        links = General.getSourceLinks(PATH);
        if(links.size()==0) Toast.makeText(DownloadProjectsActivity.this,"Ainda não existem links",Toast.LENGTH_LONG).show();
        dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, links);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        src.setAdapter(dataAdapter);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                AddSrcLinkDialogFragment newSrcLink = new AddSrcLinkDialogFragment();
                newSrcLink.show(fm, "Adicionar novo link");
            }
        });

        src.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                lastPos = position;
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                           remoteProjects = new ArrayList<String>();
                           remoteProjects =  Http.getListOfProjects2(dataAdapter.getItem(position));
                        } catch (IOException e) {

                        }
                    }
                });
                thread.start();
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                projects = new ArrayList<>();
                localProjects = ProjectUnit.getProjects(PATH);
                for (int i=0; i<remoteProjects.size();i++){
                    if(!ProjectUnit.ifExists(remoteProjects.get(i),localProjects))
                        projects.add(new ProjectUnit(remoteProjects.get(i)));
                }

                adapter = new RVProjectAdapter(projects);
                recyclerView.setAdapter(adapter);
                adapter.setOnItemClickListener((new RVProjectAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {
                        FragmentManager fm = getSupportFragmentManager();
                        DowloadProjectDialogFragment dialogAction = new DowloadProjectDialogFragment(position);
                        dialogAction.show(fm, "Download do Projeto");
                    }
                }));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                lastPos = -1;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_download_projects, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { switch(item.getItemId()) {
        case R.id.Delete:
            FragmentManager fm = getSupportFragmentManager();
            DeleteSrcLinkDialogFragment deleteAction = new DeleteSrcLinkDialogFragment();
            deleteAction.show(fm, "Eliminar Link");
            return(true);
    }
        return(super.onOptionsItemSelected(item));
    }

    public void addSourceLink(String link) {
        links.add(link);
        src.setAdapter(dataAdapter);
        General.addSourceLink(PATH, link);
    }
    public  void saveProject(final int position){
        if(lastPos>=0){
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Http.saveProject(dataAdapter.getItem(src.getSelectedItemPosition()), PATH, projects.get(position).name);
                    } catch (IOException e) {

                    }
                }
            });
            thread.start();
        }

    }
    public  void deleteLink(){
        General.deleteLink(PATH,links.get(src.getSelectedItemPosition()));
        links.remove(src.getSelectedItemPosition());
        src.setAdapter(dataAdapter);
    }


}
