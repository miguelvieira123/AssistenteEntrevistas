package com.museupessoa.maf.assistenteentrevistas.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.museupessoa.maf.assistenteentrevistas.R;



public class Configuration extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View configuration = inflater.inflate(R.layout.configuration,container,false);
        return configuration;
    }
}