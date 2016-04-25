package com.museupessoa.maf.assistenteentrevistas.newinterview;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.museupessoa.maf.assistenteentrevistas.R;
import com.museupessoa.maf.assistenteentrevistas.newproject.MetaInfo;

/**
 * Created by Miguel on 24/04/2016.
 */
public class WrittenForm extends Fragment {

    public Class<? extends WrittenForm> getFragmentClass() {
        return this.getClass();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View metainfo = inflater.inflate(R.layout.fragment_new_interview_metainfo,container,false);
//        recyclerView = (RecyclerView) metainfo.findViewById(R.id.NewProjectRV);
        //      fab = (FloatingActionButton) metainfo.findViewById(R.id.NewProjectfab);
        //    add = (FloatingActionButton) metainfo.findViewById(R.id.NewProjectAdd);
        return metainfo;
    }

}
