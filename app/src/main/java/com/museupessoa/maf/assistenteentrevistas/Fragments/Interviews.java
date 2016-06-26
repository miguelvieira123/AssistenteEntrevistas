package com.museupessoa.maf.assistenteentrevistas.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.museupessoa.maf.assistenteentrevistas.units.ProjectUnit;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Interviews extends Fragment {
    private static List<InterviewUnit> interviewUnits;
    private static RecyclerView recyclerView;
    private FloatingActionButton fab;
    private TextView info;
    private static RVInterviewAdapter adapter;


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

    public  static void updateInterviewList(String pattern){
        int i;
        List<InterviewUnit> newInterviewList = new ArrayList<InterviewUnit>();
        for(i=0;i<interviewUnits.size();i++){
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(interviewUnits.get(i).name);
            if(m.find()){
                newInterviewList.add(new InterviewUnit(interviewUnits.get(i).name,interviewUnits.get(i).foto,
                                                interviewUnits.get(i).path,interviewUnits.get(i).project,
                                                interviewUnits.get(i).time,interviewUnits.get(i).sendStatus,
                                                interviewUnits.get(i).pos));
            }
        }
        adapter.RVUpdateListAdapter(newInterviewList);
        recyclerView.setAdapter(adapter);
    }
    public  static void setCurrentInterviewList(){
        adapter.RVUpdateListAdapter(interviewUnits);
        recyclerView.setAdapter(adapter);
    }
    public static boolean setInterviewStatusSentByPath(String Path){
        int i=0;
        for(i=0;i<interviewUnits.size();i++){
            if(interviewUnits.get(i).path.equals(Path)){
                interviewUnits.get(i).sendStatus=true;
                return true;
            }
        }
        return false;
    }
    public static boolean getInterviewStatusSentByPath(String Path){
        int i=0;
        for(i=0;i<interviewUnits.size();i++){
            if(interviewUnits.get(i).path.equals(Path)){
                return interviewUnits.get(i).sendStatus;
            }
        }
        return false;
    }

}
