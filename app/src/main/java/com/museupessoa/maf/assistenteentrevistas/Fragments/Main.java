package com.museupessoa.maf.assistenteentrevistas.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.melnykov.fab.FloatingActionButton;
import com.museupessoa.maf.assistenteentrevistas.NewInterviewActivity;
import com.museupessoa.maf.assistenteentrevistas.R;
import com.museupessoa.maf.assistenteentrevistas.dialogs.NewInterviewPersonNameDialog;


public class Main extends Fragment {

    FloatingActionButton fab;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View main = inflater.inflate(R.layout.fragment_main,container,false);
        fab = (FloatingActionButton) main.findViewById(R.id.fab);
        return main;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                NewInterviewPersonNameDialog new_interview_dialog = new NewInterviewPersonNameDialog();
                new_interview_dialog.setTargetFragment(Main.this, 0);
                new_interview_dialog.show(fragmentManager, "NewInterviewPersonNameDialog");
            }
        });
    }
}
