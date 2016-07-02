package com.museupessoa.maf.assistenteentrevistas.Fragments;

import android.app.FragmentManager;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.museupessoa.maf.assistenteentrevistas.General;
import com.museupessoa.maf.assistenteentrevistas.R;
import com.museupessoa.maf.assistenteentrevistas.dialogs.FinishEnterviewDialogFragment;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class Configuration extends Fragment {
    RadioGroup radioGroup;
    public static int photoQuality;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View configuration = inflater.inflate(R.layout.fragment_configuration,container,false);
        radioGroup = (RadioGroup)configuration.findViewById(R.id.radioGroupPhotoQuality);
        switch (photoQuality){
            case 70:
                radioGroup.check(R.id.PhotoQLow);
            break;
            case 95:
                radioGroup.check(R.id.PhotoQMedium);
            break;
            case 100:
                radioGroup.check(R.id.PhotoQHigh);
                break;
        }
        return configuration;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.PhotoQLow:
                        photoQuality=70;
                        General.setPhotoQualityFromXML(General.PATH,70);
                        break;
                    case R.id.PhotoQMedium:
                        photoQuality=95;
                        General.setPhotoQualityFromXML(General.PATH,95);
                        break;
                    case R.id.PhotoQHigh:
                        photoQuality=100;
                        General.setPhotoQualityFromXML(General.PATH,100);
                        break;
                }
            }
        });

    }


}
