package com.museupessoa.maf.assistenteentrevistas.editInterviewPersonForm;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.museupessoa.maf.assistenteentrevistas.R;

/**
 * Created by Miguel on 24/04/2016.
 */
public class AudioForm extends Fragment {

    private String new_interview_path;


    public AudioForm(String new_interview_path) {
        this.new_interview_path = new_interview_path;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View metainfo = inflater.inflate(R.layout.fragment_new_interview_metainfo_audio,container,false);
//        recyclerView = (RecyclerView) metainfo.findViewById(R.id.NewProjectRV);
  //      fab = (FloatingActionButton) metainfo.findViewById(R.id.NewProjectfab);
    //    add = (FloatingActionButton) metainfo.findViewById(R.id.NewProjectAdd);
        return metainfo;
    }



}
