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


public class Urls extends Fragment {
    private List<String> urlsList;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private final  String TAG="AssistenteEntrevistas";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View urls = inflater.inflate(R.layout.newproject_urls,container,false);
        recyclerView = (RecyclerView) urls.findViewById(R.id.NewProjectRV);
        fab = (FloatingActionButton) urls.findViewById(R.id.NewProjectfab);
        urlsList = new ArrayList<String>();
        return urls;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);
        RVNewProjectAdapter adapter = new RVNewProjectAdapter(urlsList);
        //recyclerView.setAdapter(adapter);
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
