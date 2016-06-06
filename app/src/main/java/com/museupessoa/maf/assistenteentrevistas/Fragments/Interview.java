package com.museupessoa.maf.assistenteentrevistas.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.museupessoa.maf.assistenteentrevistas.InterviewActivity;
import com.museupessoa.maf.assistenteentrevistas.R;
import com.museupessoa.maf.assistenteentrevistas.adapters.RVQuestionAdapter;
import com.museupessoa.maf.assistenteentrevistas.units.QuestionUnit;

import java.util.List;


public class Interview extends Fragment {

    private String path;
    private RecyclerView recyclerView;
    private RVQuestionAdapter adapter;
    private List<QuestionUnit> questionUnits;
    public Interview() {
        this.path = null;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.path = this.getArguments().getString("path");
        View questions = inflater.inflate(R.layout.fragment_interview_questions, container, false);
        recyclerView = (RecyclerView) questions.findViewById(R.id.recyclerView);
        return questions;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.path = this.getArguments().getString("path");
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);
        questionUnits = QuestionUnit.getQuestions(path);
        adapter = new RVQuestionAdapter(questionUnits);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new RVQuestionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {

                Activity activity = getActivity();
                if (activity instanceof InterviewActivity){
                    ((InterviewActivity) activity).newAudioTag(questionUnits.get(position).question);
                }
            }
        });
    }


}
