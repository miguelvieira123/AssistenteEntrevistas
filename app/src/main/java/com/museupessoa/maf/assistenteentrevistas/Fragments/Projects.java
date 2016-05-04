package com.museupessoa.maf.assistenteentrevistas.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;


import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.melnykov.fab.FloatingActionButton;
<<<<<<< HEAD:app/src/main/java/com/museupessoa/maf/assistenteentrevistas/Fragments/Projects.java
=======
import com.museupessoa.maf.assistenteentrevistas.DownloadProjectsActivity;
>>>>>>> origin/ImportProjects:app/src/main/java/com/museupessoa/maf/assistenteentrevistas/Fragments/Projects.java
import com.museupessoa.maf.assistenteentrevistas.NewProjectActivity;
import com.museupessoa.maf.assistenteentrevistas.R;
import com.museupessoa.maf.assistenteentrevistas.adapters.RVProjectAdapter;

import com.museupessoa.maf.assistenteentrevistas.dialogs.NewProjectDialogFragment;
import com.museupessoa.maf.assistenteentrevistas.dialogs.ProjectActionDialogFragment;
import com.museupessoa.maf.assistenteentrevistas.units.ProjectUnit;

import java.util.List;


public class Projects extends Fragment{
    RVProjectAdapter adapter;
    private List<ProjectUnit> projectUnits;
    private RecyclerView recyclerView;
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
                ProjectActionDialogFragment dialogAction = new ProjectActionDialogFragment();
                dialogAction.setTargetFragment(Projects.this, position);
                dialogAction.show(fragmentActionManager, "ProjectItemActionDialogFragment");
            }
        }));

       //fab.attachToRecyclerView(recyclerView);
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
                Toast.makeText(getActivity(),"EDIT",Toast.LENGTH_LONG).show();
                break;
            case  Projects.DELETE_PROJECT:
                String deleted_project = projectUnits.get(requestCode).name;
                if(ProjectUnit.deleteProject(projectUnits.get(requestCode).name,
                        Environment.getExternalStoragePublicDirectory("/"+getResources().getString(R.string.APP_NAME)).toString())) {
                    projectUnits.remove(requestCode);
                    adapter.RVUpdateListAdapter(projectUnits);
                    recyclerView.setAdapter(adapter);
                    Toast.makeText(getActivity(), "Projeto "+deleted_project+" eliminado", Toast.LENGTH_LONG).show();
                }
                else Toast.makeText(getActivity(), "Erro", Toast.LENGTH_LONG).show();
                break;
        }
    }
}
