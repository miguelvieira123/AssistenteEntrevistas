package com.museupessoa.maf.assistenteentrevistas.main;

import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;


import com.melnykov.fab.FloatingActionButton;
import com.museupessoa.maf.assistenteentrevistas.NewProject;
import com.museupessoa.maf.assistenteentrevistas.R;
import com.museupessoa.maf.assistenteentrevistas.adapters.RVProjectAdapter;

import com.museupessoa.maf.assistenteentrevistas.dialogs.NewProjectDialogFragment;
import com.museupessoa.maf.assistenteentrevistas.units.ProjectUnit;

import java.util.List;


public class Projects extends Fragment{

    private List<ProjectUnit> projectUnits;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private final  String TAG="AssistenteEntrevistas";



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View project = inflater.inflate(R.layout.projects,container,false);
        recyclerView = (RecyclerView) project.findViewById(R.id.recyclerView);
        fab = (FloatingActionButton) project.findViewById(R.id.fab);
        return project;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);
        projectUnits = ProjectUnit.getProjects(Environment.getExternalStoragePublicDirectory("/"+getResources().getString(R.string.APP_NAME)).toString());
        initializeAdapter();

       fab.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
               NewProjectDialogFragment dialogProjectName = new NewProjectDialogFragment();
               dialogProjectName.show(fragmentManager, "NewProjectDialogFragment");
               //dialogProjectName.getArguments();
               // getFragmentManager().beginTransaction().replace(R.id.fragmentParentViewGroup, new NewProject()).commit();
               //Intent intent = new Intent(getActivity(), NewProject.class);
               //startActivity(intent);

           }
       });
       //fab.attachToRecyclerView(recyclerView);


    }


    private void initializeAdapter(){
        RVProjectAdapter adapter = new RVProjectAdapter(projectUnits);
        recyclerView.setAdapter(adapter);
    }


}
