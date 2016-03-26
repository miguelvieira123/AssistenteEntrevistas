package com.museupessoa.maf.assistenteentrevistas.main;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.museupessoa.maf.assistenteentrevistas.R;
import com.museupessoa.maf.assistenteentrevistas.adapters.RVInterviewAdapter;
import com.museupessoa.maf.assistenteentrevistas.adapters.RVProjectAdapter;
import com.museupessoa.maf.assistenteentrevistas.units.InterviewUnit;
import com.museupessoa.maf.assistenteentrevistas.units.ProjectUnit;

import java.util.List;


public class Interviews extends Fragment {
    private List<InterviewUnit> interviewUnits;
    private RecyclerView recyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View interview  = inflater.inflate(R.layout.interviews,container,false);
        recyclerView = (RecyclerView) interview.findViewById(R.id.recyclerView);
        return interview;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);
        interviewUnits = InterviewUnit.getInterviews(Environment.getExternalStoragePublicDirectory("/" + getResources().getString(R.string.APP_NAME)).toString());
        initializeAdapter();




    }


    private void initializeAdapter(){
        RVInterviewAdapter adapter = new RVInterviewAdapter(interviewUnits);
        recyclerView.setAdapter(adapter);
    }










}
