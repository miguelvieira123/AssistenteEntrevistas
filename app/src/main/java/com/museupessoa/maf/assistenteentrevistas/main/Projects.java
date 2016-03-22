package com.museupessoa.maf.assistenteentrevistas.main;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.museupessoa.maf.assistenteentrevistas.R;
import com.museupessoa.maf.assistenteentrevistas.adapters.RVProjectAdapter;
import com.museupessoa.maf.assistenteentrevistas.units.ProjectUnit;

import java.util.ArrayList;
import java.util.List;


public class Projects extends Fragment {

    private List<ProjectUnit> projectUnits;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View project = inflater.inflate(R.layout.projects,container,false);
        recyclerView = (RecyclerView) project.findViewById(R.id.recyclerView);
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
    }


    private void initializeAdapter(){
        RVProjectAdapter adapter = new RVProjectAdapter(projectUnits);
        recyclerView.setAdapter(adapter);
    }

}
