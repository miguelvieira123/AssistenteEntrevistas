package com.museupessoa.maf.assistenteentrevistas.newproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;
import com.museupessoa.maf.assistenteentrevistas.NewProject;
import com.museupessoa.maf.assistenteentrevistas.R;
import com.museupessoa.maf.assistenteentrevistas.adapters.RVNewProjectAdapter;
import com.museupessoa.maf.assistenteentrevistas.dialogs.NewProjectDialogFragmentNewItem;

import java.util.ArrayList;
import java.util.List;


public class Urls extends Fragment {
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private final  String TAG="AssistenteEntrevistas";
    private static final int REQUEST = 1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View urls = inflater.inflate(R.layout.newproject_urls,container,false);
        recyclerView = (RecyclerView) urls.findViewById(R.id.NewProjectRV);
        fab = (FloatingActionButton) urls.findViewById(R.id.NewProjectfab);
        return urls;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);
        RVNewProjectAdapter adapter = new RVNewProjectAdapter(NewProject.urlsList);
        recyclerView.setAdapter(adapter);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
               NewProjectDialogFragmentNewItem dialogProjectName = new NewProjectDialogFragmentNewItem();
                dialogProjectName.setTargetFragment(Urls.this, REQUEST);
               dialogProjectName.show(fragmentManager, "NewProjectDialogFragmentQuestions");
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode){
                case 1:
                    NewProject.urlsList.add(data.getStringExtra(NewProjectDialogFragmentNewItem.REQUEST));
                    Toast.makeText(this.getActivity(),"Novo elemento foi adicionado", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}
