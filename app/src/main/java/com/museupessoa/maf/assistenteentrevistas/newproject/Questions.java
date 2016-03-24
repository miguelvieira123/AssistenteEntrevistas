package com.museupessoa.maf.assistenteentrevistas.newproject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.melnykov.fab.FloatingActionButton;
import com.museupessoa.maf.assistenteentrevistas.R;
import com.museupessoa.maf.assistenteentrevistas.adapters.RVNewProjectAdapter;
import com.museupessoa.maf.assistenteentrevistas.dialogs.NewProjectDialogFragmentNewItem;

import java.util.ArrayList;
import java.util.List;

public class Questions extends Fragment {
    private List<String> questionsList;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private final  String TAG="AssistenteEntrevistas";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View questions = inflater.inflate(R.layout.newproject_questions, container, false);
        recyclerView = (RecyclerView) questions.findViewById(R.id.NewProjectRV);
        fab = (FloatingActionButton) questions.findViewById(R.id.NewProjectfab);
        questionsList = new ArrayList<String>();
        return questions;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);
        RVNewProjectAdapter adapter = new RVNewProjectAdapter(questionsList);
        recyclerView.setAdapter(adapter);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                NewProjectDialogFragmentNewItem dialogProjectName = new NewProjectDialogFragmentNewItem();
                dialogProjectName.show(fragmentManager, "NewProjectDialogFragmentQuestions");


            }
        });
    }
}
