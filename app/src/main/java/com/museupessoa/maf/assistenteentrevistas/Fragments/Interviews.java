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
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;
import com.museupessoa.maf.assistenteentrevistas.General;
import com.museupessoa.maf.assistenteentrevistas.InterviewActivity;
import com.museupessoa.maf.assistenteentrevistas.NewInterviewActivity;
import com.museupessoa.maf.assistenteentrevistas.R;
import com.museupessoa.maf.assistenteentrevistas.adapters.RVInterviewAdapter;
import com.museupessoa.maf.assistenteentrevistas.dialogs.NewInterviewPersonNameDialog;
import com.museupessoa.maf.assistenteentrevistas.units.InterviewUnit;

import java.util.ArrayList;
import java.util.List;


public class Interviews extends Fragment {
    private List<InterviewUnit> interviewUnits;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private TextView info;
    private RVInterviewAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View interview  = inflater.inflate(R.layout.fragment_interviews,container,false);
        recyclerView = (RecyclerView) interview.findViewById(R.id.recyclerView);
        fab = (FloatingActionButton) interview.findViewById(R.id.fab);
        info = (TextView) interview.findViewById(R.id.textView);
        return interview;
    }


    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);

        interviewUnits = InterviewUnit.getInterviews(Environment.getExternalStoragePublicDirectory("/" + getResources().getString(R.string.APP_NAME)).toString());
        if (interviewUnits.size()>0){
            info.setText("");
        }else{
            info.setText("A lista de Entrevistas está vazia");
        }

        adapter = new RVInterviewAdapter(interviewUnits);
        recyclerView.setAdapter(adapter);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                NewInterviewPersonNameDialog new_interview_dialog = new NewInterviewPersonNameDialog();
                new_interview_dialog.setTargetFragment(Interviews.this, 0);
                new_interview_dialog.show(fragmentManager, "NewInterviewPersonNameDialog");
            }
        });

        adapter.setOnItemClickListener(new RVInterviewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent intent = new Intent(getActivity(), InterviewActivity.class);
                intent.putExtra("path",interviewUnits.get(position).path);
                startActivity(intent);
            }
        });

    }

}
