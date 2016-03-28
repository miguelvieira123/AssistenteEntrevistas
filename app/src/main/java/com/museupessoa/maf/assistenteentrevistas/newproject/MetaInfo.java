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
import android.widget.Button;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;
import com.museupessoa.maf.assistenteentrevistas.NewProject;
import com.museupessoa.maf.assistenteentrevistas.R;
import com.museupessoa.maf.assistenteentrevistas.adapters.RVNewProjectAdapter;
import com.museupessoa.maf.assistenteentrevistas.adapters.RVProjectAdapter;

import com.museupessoa.maf.assistenteentrevistas.dialogs.NewProjectAddDialogFragment;
import com.museupessoa.maf.assistenteentrevistas.dialogs.NewProjectDialogFragmentEdit;
import com.museupessoa.maf.assistenteentrevistas.dialogs.NewProjectDialogFragmentNewItem;
import com.museupessoa.maf.assistenteentrevistas.dialogs.NewProjectItemActionDialogFragment;
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

    public Class<? extends MetaInfo> getFragmentClass() {
        return this.getClass();
    }

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

        adapter.setOnItemClickListener(new RVNewProjectAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Log.d(TAG,"M2 "+"Pos = "+Integer.toString(position));
                android.support.v4.app.FragmentManager fragmentActionManager = getFragmentManager();
                NewProjectItemActionDialogFragment dialogAction = new NewProjectItemActionDialogFragment();
                dialogAction.setTargetFragment(MetaInfo.this, position);
                dialogAction.show(fragmentActionManager, "NewProjectItemActionDialogFragment");
            }
        });
        recyclerView.setAdapter(adapter);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"M3");
                android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                NewProjectDialogFragmentNewItem dialogProjectName = new NewProjectDialogFragmentNewItem();
                dialogProjectName.setTargetFragment(MetaInfo.this, REQUEST);
                dialogProjectName.show(fragmentManager, "NewProjectDialogFragmentItem");

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

        switch (resultCode){
            case NewProject.ADD:
                switch (requestCode) {
                    case 1:
                        NewProject.metaList.add(data.getStringExtra(NewProjectDialogFragmentNewItem.REQUEST));
                        Toast.makeText(this.getActivity(), "M Novo elemento foi adicionado", Toast.LENGTH_SHORT).show();
                        break;
                }
                break;
            case  NewProject.DELETE:
                NewProject.metaList.remove(requestCode);
                adapter.RVUpdateListAdapter(NewProject.metaList);
                recyclerView.setAdapter(adapter);
                Toast.makeText(this.getActivity(), "O elemento foi eliminado", Toast.LENGTH_LONG).show();
                break;
            case NewProject.EDIT:
                android.support.v4.app.FragmentManager fragmentEditManager = getFragmentManager();
                NewProjectDialogFragmentEdit dialogEdit = new NewProjectDialogFragmentEdit(NewProject.metaList.get(requestCode));
                dialogEdit.setTargetFragment(MetaInfo.this, requestCode);
                dialogEdit.show(fragmentEditManager, "NewProjectItemEditDialogFragment");
                break;
            case  NewProject.CHANGE:
                NewProject.metaList.set(requestCode,data.getStringExtra(NewProjectDialogFragmentEdit.REQUEST));
                adapter.RVUpdateListAdapter(NewProject.metaList);
                recyclerView.setAdapter(adapter);
                Toast.makeText(this.getActivity(), "O elemento foi alterado", Toast.LENGTH_LONG).show();
                break;

        }

    }
}
