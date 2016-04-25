package com.museupessoa.maf.assistenteentrevistas.newinterview;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.museupessoa.maf.assistenteentrevistas.R;
import com.museupessoa.maf.assistenteentrevistas.newproject.MetaInfo;


public class WrittenForm extends Fragment {

    public Class<? extends WrittenForm> getFragmentClass() {
        return this.getClass();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View metainfo = inflater.inflate(R.layout.fragment_new_interview_metainfo,container,false);
        return metainfo;
    }

}
