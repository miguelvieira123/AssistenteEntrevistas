package com.museupessoa.maf.assistenteentrevistas.Fragments;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;


import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.melnykov.fab.FloatingActionButton;
import com.museupessoa.maf.assistenteentrevistas.DownloadProjectsActivity;
import com.museupessoa.maf.assistenteentrevistas.General;
import com.museupessoa.maf.assistenteentrevistas.InterviewActivity;
import com.museupessoa.maf.assistenteentrevistas.MainActivity;
import com.museupessoa.maf.assistenteentrevistas.NewProjectActivity;
import com.museupessoa.maf.assistenteentrevistas.R;
import com.museupessoa.maf.assistenteentrevistas.adapters.RVProjectAdapter;

import com.museupessoa.maf.assistenteentrevistas.auxiliary.UploadingFileToServer;
import com.museupessoa.maf.assistenteentrevistas.auxiliary.Zip;
import com.museupessoa.maf.assistenteentrevistas.dialogs.ConfirmFinishFormDialogFragment;
import com.museupessoa.maf.assistenteentrevistas.dialogs.DeleteInterviewDialogFragment;
import com.museupessoa.maf.assistenteentrevistas.dialogs.DeleteProjectDialogFragment;
import com.museupessoa.maf.assistenteentrevistas.dialogs.NewProjectDialogFragment;
import com.museupessoa.maf.assistenteentrevistas.dialogs.ProjectActionDialogFragment;
import com.museupessoa.maf.assistenteentrevistas.newproject.MetaInfo;
import com.museupessoa.maf.assistenteentrevistas.units.ProjectUnit;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Projects extends Fragment{
    private  static  RVProjectAdapter adapter;
    private static List<ProjectUnit> projectUnits;
    private static RecyclerView recyclerView;
    private FloatingActionButton fab;
    private FloatingActionButton fabD;
    private final  String TAG="AssistenteEntrevistas";
    public static final int NEW_PROJECT = 1;
    public static final int DELETE_PROJECT = 2;
    public static final int EDIT_PROJECT = 3;
    private static final int REQUEST = 1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View project = inflater.inflate(R.layout.fragment_projects,container,false);
        recyclerView = (RecyclerView) project.findViewById(R.id.recyclerView);
        fab = (FloatingActionButton) project.findViewById(R.id.fab);
        fabD = (FloatingActionButton) project.findViewById(R.id.fabDownload);
        return project;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);
        projectUnits = ProjectUnit.getProjects(Environment.getExternalStoragePublicDirectory("/"+getResources().getString(R.string.APP_NAME)).toString());
        adapter = new RVProjectAdapter(projectUnits);
        recyclerView.setAdapter(adapter);

       fab.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
               NewProjectDialogFragment dialogProjectName = new NewProjectDialogFragment();
               dialogProjectName.setTargetFragment(Projects.this, REQUEST);
               dialogProjectName.show(fragmentManager, "NewProjectDialogFragment");
           }
       });
        fabD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DownloadProjectsActivity.class);
                startActivity(intent);
            }
        });
        adapter.setOnItemClickListener((new RVProjectAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                android.support.v4.app.FragmentManager fragmentActionManager = getFragmentManager();
                ProjectActionDialogFragment dialogAction = new ProjectActionDialogFragment(projectUnits.get(position).name);
                dialogAction.setTargetFragment(Projects.this, position);
                dialogAction.show(fragmentActionManager, "ProjectItemActionDialogFragment");
            }
        }));

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case Projects.NEW_PROJECT:
                switch (requestCode) {
                    case 1:
                        if(ProjectUnit.ifExists(data.getStringExtra(NewProjectDialogFragment.REQUEST),projectUnits)){
                            Toast.makeText(getActivity(),"Este projeto já existe", Toast.LENGTH_LONG).show();
                        }
                        else{
                            Intent intent = new Intent(getActivity(), NewProjectActivity.class);
                            intent.putExtra("name",data.getStringExtra(NewProjectDialogFragment.REQUEST));
                            intent.putExtra("status",1);
                            startActivity(intent);
                        }
                        break;
                }
                break;
            case Projects.EDIT_PROJECT:
                Intent intent = new Intent(getActivity(), NewProjectActivity.class);
                intent.putExtra("name",projectUnits.get(requestCode).name);
                intent.putExtra("status",2);
                startActivity(intent);
                break;
            case  Projects.DELETE_PROJECT:
                android.support.v4.app.FragmentManager delProject = getFragmentManager();
                DeleteProjectDialogFragment dialogProjectName = new DeleteProjectDialogFragment();
                dialogProjectName.setTargetFragment(this, requestCode);
                dialogProjectName.show(delProject, "DeleteProject");
                break;
        }
    }

    public static void delProject(int pos){
       String deleted_project = projectUnits.get(pos).name;
       if(ProjectUnit.deleteProject(projectUnits.get(pos).name,
               Environment.getExternalStoragePublicDirectory("/" + General.APP_NAME).toString())) {
           projectUnits.remove(pos);
           for(int i =0;i<projectUnits.size();i++)projectUnits.get(i).pos=i;
           adapter.RVUpdateListAdapter(projectUnits);
           recyclerView.setAdapter(adapter);

       }


   }

    public  static void updateProjectsList(String pattern){
        int i;
        List<ProjectUnit> newProjectList = new ArrayList<ProjectUnit>();
        for(i=0;i<projectUnits.size();i++){
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(projectUnits.get(i).name);
            if(m.find()){
                newProjectList.add(new ProjectUnit(projectUnits.get(i).name, projectUnits.get(i).pos));
            }
        }
        adapter.RVUpdateListAdapter(newProjectList);
        recyclerView.setAdapter(adapter);
    }

    public  static void setCurrentProjectList(){
        adapter.RVUpdateListAdapter(projectUnits);
        recyclerView.setAdapter(adapter);
    }
}
