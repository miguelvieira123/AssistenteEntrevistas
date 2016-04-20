package com.museupessoa.maf.assistenteentrevistas.newproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;
import com.museupessoa.maf.assistenteentrevistas.NewProjectActivity;
import com.museupessoa.maf.assistenteentrevistas.R;
import com.museupessoa.maf.assistenteentrevistas.adapters.RVNewProjectAdapter;

import com.museupessoa.maf.assistenteentrevistas.dialogs.NewProjectAddDialogFragment;
import com.museupessoa.maf.assistenteentrevistas.dialogs.NewProjectDialogFragmentEdit;
import com.museupessoa.maf.assistenteentrevistas.dialogs.NewProjectDialogFragmentNewItem;
import com.museupessoa.maf.assistenteentrevistas.dialogs.NewProjectItemActionDialogFragment;


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
        View metainfo = inflater.inflate(R.layout.fragment_newproject_metainfo,container,false);
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
        adapter = new RVNewProjectAdapter(NewProjectActivity.metaList);

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
            case NewProjectActivity.ADD:
                switch (requestCode) {
                    case 1:
                        NewProjectActivity.metaList.add(data.getStringExtra(NewProjectDialogFragmentNewItem.REQUEST));
                        Toast.makeText(this.getActivity(), "M Novo elemento foi adicionado", Toast.LENGTH_SHORT).show();
                        break;
                }
                break;
            case  NewProjectActivity.DELETE:
                NewProjectActivity.metaList.remove(requestCode);
                adapter.RVUpdateListAdapter(NewProjectActivity.metaList);
                recyclerView.setAdapter(adapter);
                Toast.makeText(this.getActivity(), "O elemento foi eliminado", Toast.LENGTH_LONG).show();
                break;
            case NewProjectActivity.EDIT:
                android.support.v4.app.FragmentManager fragmentEditManager = getFragmentManager();
                NewProjectDialogFragmentEdit dialogEdit = new NewProjectDialogFragmentEdit(NewProjectActivity.metaList.get(requestCode));
                dialogEdit.setTargetFragment(MetaInfo.this, requestCode);
                dialogEdit.show(fragmentEditManager, "NewProjectItemEditDialogFragment");
                break;
            case  NewProjectActivity.CHANGE:
                NewProjectActivity.metaList.set(requestCode,data.getStringExtra(NewProjectDialogFragmentEdit.REQUEST));
                adapter.RVUpdateListAdapter(NewProjectActivity.metaList);
                recyclerView.setAdapter(adapter);
                Toast.makeText(this.getActivity(), "O elemento foi alterado", Toast.LENGTH_LONG).show();
                break;

        }

    }
}
