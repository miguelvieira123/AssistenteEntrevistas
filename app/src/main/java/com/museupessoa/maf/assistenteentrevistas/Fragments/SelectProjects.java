package com.museupessoa.maf.assistenteentrevistas.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.museupessoa.maf.assistenteentrevistas.R;
import com.museupessoa.maf.assistenteentrevistas.adapters.RVProjectAdapter;
import com.museupessoa.maf.assistenteentrevistas.adapters.RVQuestionAdapter;
import com.museupessoa.maf.assistenteentrevistas.units.ProjectUnit;

import java.util.List;

public class SelectProjects extends Fragment {

    private String path;
    private RecyclerView recyclerView;
    private List<ProjectUnit> projectUnits;
    private RVProjectAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.path = this.getArguments().getString("path");
        View projects = inflater.inflate(R.layout.fragment_new_interview_projects, container, false);
        recyclerView = (RecyclerView) projects.findViewById(R.id.recyclerView);
        return projects;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.path = this.getArguments().getString("path");
        super.onActivityCreated(savedInstanceState);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());



        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);
        projectUnits = ProjectUnit.getProjects(path);
        adapter = new RVProjectAdapter(projectUnits);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new RVProjectAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                //v.setSelected(true);
                //v.setActivated(true);
                //v.setBackgroundColor(ContextCompat.getColor(v.getContext(), R.color.cardview_dark_background));
                CardView c = (CardView) v.findViewById(R.id.CV_Project);
                c.setCardBackgroundColor(ContextCompat.getColor(v.getContext(), R.color.cardview_dark_background));
            }
        });

    }
}
