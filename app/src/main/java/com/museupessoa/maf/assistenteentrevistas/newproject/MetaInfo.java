package com.museupessoa.maf.assistenteentrevistas.newproject;

import android.app.Activity;
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
import com.museupessoa.maf.assistenteentrevistas.R;
import com.museupessoa.maf.assistenteentrevistas.adapters.RVNewProjectAdapter;
import com.museupessoa.maf.assistenteentrevistas.adapters.RVProjectAdapter;

import com.museupessoa.maf.assistenteentrevistas.dialogs.NewProjectDialogFragmentNewItem;
import com.museupessoa.maf.assistenteentrevistas.units.ProjectUnit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MetaInfo extends Fragment  {
    private List<String> metaList;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private final  String TAG="AssistenteEntrevistas";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View metainfo = inflater.inflate(R.layout.newproject_metainfo,container,false);
        recyclerView = (RecyclerView) metainfo.findViewById(R.id.NewProjectRV);
        fab = (FloatingActionButton) metainfo.findViewById(R.id.NewProjectfab);
        metaList = defaultMetaListInit();
        return metainfo;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);
        RVNewProjectAdapter adapter = new RVNewProjectAdapter(metaList);
        recyclerView.setAdapter(adapter);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                NewProjectDialogFragmentNewItem dialogProjectName = new NewProjectDialogFragmentNewItem();
                dialogProjectName.show(fragmentManager, "NewProjectDialogFragmentMeta");

            }
        });
    }

    public List<String> defaultMetaListInit() {

            List<String> local = new ArrayList<String>();
            local.add("Nome");
            local.add("Apelido");
            local.add("Idade");
            local.add("Profissão");
            local.add("Morada");
            return local;

    }

}
