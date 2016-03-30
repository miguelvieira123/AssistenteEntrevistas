package com.museupessoa.maf.assistenteentrevistas.Interview_main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.museupessoa.maf.assistenteentrevistas.R;
import com.museupessoa.maf.assistenteentrevistas.adapters.RVQuestionAdapter;
import com.museupessoa.maf.assistenteentrevistas.units.InterviewUnit;
import com.museupessoa.maf.assistenteentrevistas.units.QuestionUnit;

import java.util.List;

/**
 * Created by Miguel on 29/03/2016.
 */
public class Interview extends Fragment {

    private String path;
    private RecyclerView recyclerView;
    private RVQuestionAdapter adapter;
    private List<QuestionUnit> questionUnits;

    public Interview(String path){
        this.path = path;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View question  = inflater.inflate(R.layout.interview_main,container,false);
        recyclerView = (RecyclerView) question.findViewById(R.id.recyclerView);
        return question;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);
        questionUnits = QuestionUnit.getQuestions(path);
        adapter = new RVQuestionAdapter(questionUnits);
        recyclerView.setAdapter(adapter);
    }
}
