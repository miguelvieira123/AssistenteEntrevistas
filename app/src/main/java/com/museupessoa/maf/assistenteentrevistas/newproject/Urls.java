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
import com.museupessoa.maf.assistenteentrevistas.dialogs.NewProjectDialogFragmentEdit;
import com.museupessoa.maf.assistenteentrevistas.dialogs.NewProjectDialogFragmentNewItem;
import com.museupessoa.maf.assistenteentrevistas.dialogs.NewProjectItemActionDialogFragment;


public class Urls extends Fragment {
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private final  String TAG="AssistenteEntrevistas";
    private static final int REQUEST = 1;
    private static  final int ACTION = 2;
    RVNewProjectAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View urls = inflater.inflate(R.layout.fragment_newproject_urls,container,false);
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
        adapter = new RVNewProjectAdapter(NewProjectActivity.urlsList);
        recyclerView.setAdapter(adapter);
        Log.d(TAG, "U1");

       /* adapter.setOnItemClickListener(new RVNewProjectAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                //Log.d(TAG, "onItemClick position: ");
            }

            @Override
            public void onItemLongClick(int position, View v) {
                Log.d(TAG, "U2");
               *//* android.support.v4.app.FragmentManager fragmentActionManager = getFragmentManager();
                NewProjectItemActionDialogFragment dialogAction = new NewProjectItemActionDialogFragment(position);
                dialogAction.setTargetFragment(Urls.this, REQUEST);
                dialogAction.show(fragmentActionManager, "NewProjectItemActionDialogFragment");*//*
            }
        });*/
        adapter.setOnItemClickListener(new RVNewProjectAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Log.d(TAG,"U2 "+"Pos = "+Integer.toString(position));
                android.support.v4.app.FragmentManager fragmentActionManager = getFragmentManager();
                NewProjectItemActionDialogFragment dialogAction = new NewProjectItemActionDialogFragment();
                dialogAction.setTargetFragment(Urls.this, position);
                dialogAction.show(fragmentActionManager, "NewProjectItemActionDialogFragment");
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "U3");
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
        switch (resultCode){
            case NewProjectActivity.ADD:
                switch (requestCode) {
                    case 1:
                        NewProjectActivity.urlsList.add(data.getStringExtra(NewProjectDialogFragmentNewItem.REQUEST));
                        Toast.makeText(this.getActivity(), "Novo elemento foi adicionado", Toast.LENGTH_SHORT).show();
                        break;
                }
                break;
            case  NewProjectActivity.DELETE:
                NewProjectActivity.urlsList.remove(requestCode);
                adapter.RVUpdateListAdapter(NewProjectActivity.urlsList);
                recyclerView.setAdapter(adapter);
                Toast.makeText(this.getActivity(), "O elemento foi eliminado", Toast.LENGTH_LONG).show();
                break;
            case NewProjectActivity.EDIT:
                android.support.v4.app.FragmentManager fragmentEditManager = getFragmentManager();
                NewProjectDialogFragmentEdit dialogEdit = new NewProjectDialogFragmentEdit(NewProjectActivity.urlsList.get(requestCode));
                dialogEdit.setTargetFragment(Urls.this, requestCode);
                dialogEdit.show(fragmentEditManager, "NewProjectItemEditDialogFragment");
                break;
            case  NewProjectActivity.CHANGE:
                NewProjectActivity.urlsList.set(requestCode,data.getStringExtra(NewProjectDialogFragmentEdit.REQUEST));
                adapter.RVUpdateListAdapter(NewProjectActivity.urlsList);
                recyclerView.setAdapter(adapter);
                Toast.makeText(this.getActivity(), "O elemento foi alterado", Toast.LENGTH_LONG).show();
                break;
        }
    }
}
