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
import android.widget.Button;

import com.museupessoa.maf.assistenteentrevistas.NewInterviewActivity;
import com.museupessoa.maf.assistenteentrevistas.R;
import com.museupessoa.maf.assistenteentrevistas.adapters.RVProjectAdapter;
import com.museupessoa.maf.assistenteentrevistas.adapters.RVQuestionAdapter;
import com.museupessoa.maf.assistenteentrevistas.units.ProjectUnit;

import java.util.List;

public class SelectProject extends Fragment {

    private String path;
    private RecyclerView recyclerView;
    private List<ProjectUnit> projectUnits;
    private RVProjectAdapter adapter;
    private String projectName;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.path = this.getArguments().getString("path");
        View projects = inflater.inflate(R.layout.fragment_new_interview_projects, container, false);
        Button ok_button = (Button) projects.findViewById(R.id.confirm_project_selection);
        ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = getActivity();
                if (activity instanceof NewInterviewActivity){
                    ((NewInterviewActivity)activity).editInterviewMetadata(projectName);
                }
            }
        });

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
                v.setSelected(true);
                projectName = projectUnits.get(position).name;
            }
        });

    }
}
