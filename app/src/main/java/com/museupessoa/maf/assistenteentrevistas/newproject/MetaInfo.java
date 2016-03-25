package com.museupessoa.maf.assistenteentrevistas.newproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;
import com.museupessoa.maf.assistenteentrevistas.NewProject;
import com.museupessoa.maf.assistenteentrevistas.R;
import com.museupessoa.maf.assistenteentrevistas.adapters.RVNewProjectAdapter;
import com.museupessoa.maf.assistenteentrevistas.adapters.RVProjectAdapter;

import com.museupessoa.maf.assistenteentrevistas.dialogs.NewProjectAddDialogFragment;
import com.museupessoa.maf.assistenteentrevistas.dialogs.NewProjectDialogFragmentNewItem;
import com.museupessoa.maf.assistenteentrevistas.units.ProjectUnit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MetaInfo extends Fragment   {
    private RecyclerView recyclerView;
    RVNewProjectAdapter adapter;
    private FloatingActionButton fab;
    private FloatingActionButton add;
    private final  String TAG="AssistenteEntrevistas";
    private static final int REQUEST = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View metainfo = inflater.inflate(R.layout.newproject_metainfo,container,false);
        recyclerView = (RecyclerView) metainfo.findViewById(R.id.NewProjectRV);
        fab = (FloatingActionButton) metainfo.findViewById(R.id.NewProjectfab);
        add = (FloatingActionButton) metainfo.findViewById(R.id.NewProjectAdd);

        return metainfo;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);
        adapter = new RVNewProjectAdapter(NewProject.metaList);
        recyclerView.setAdapter(adapter);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                NewProjectDialogFragmentNewItem dialogProjectName = new NewProjectDialogFragmentNewItem();
                dialogProjectName.setTargetFragment(MetaInfo.this, REQUEST);
                dialogProjectName.show(fragmentManager, "NewProjectDialogFragmentMeta");

            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.support.v4.app.FragmentManager addProject = getFragmentManager();
                NewProjectAddDialogFragment dialogProjectName = new NewProjectAddDialogFragment();
                dialogProjectName.show(addProject, "NewProjectAddDialogFragment");
            }
        });
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode){
                case 1:
                    NewProject.metaList.add(data.getStringExtra(NewProjectDialogFragmentNewItem.REQUEST));
                        Toast.makeText(this.getActivity(), "Novo elemento foi adicionado", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}
