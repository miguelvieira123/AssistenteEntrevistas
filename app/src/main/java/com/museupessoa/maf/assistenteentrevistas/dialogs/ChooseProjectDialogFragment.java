package com.museupessoa.maf.assistenteentrevistas.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.museupessoa.maf.assistenteentrevistas.General;
import com.museupessoa.maf.assistenteentrevistas.InterviewActivity;
import com.museupessoa.maf.assistenteentrevistas.R;
import com.museupessoa.maf.assistenteentrevistas.units.ProjectUnit;

import java.util.ArrayList;
import java.util.List;


public class ChooseProjectDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final List<ProjectUnit> projectUnits = ProjectUnit.getProjects(General.PATH);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragmet_dialog_choose_project, null);
        final RadioGroup radioGroup = (RadioGroup)view.findViewById(R.id.ChooseProjectRadioGroup);
        for(int i=0;i<projectUnits.size();i++){
            RadioButton radioButton = new RadioButton(getActivity());
            radioButton.setText(projectUnits.get(i).name);
            if(projectUnits.get(i).name.equals("Geral"))radioButton.setChecked(true);
            radioButton.setId(i + 1);
            radioGroup.addView(radioButton);
        }
        builder.setView(view);
        builder.setTitle("Escolha o novo projeto").setPositiveButton("Alterar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                InterviewActivity.changeProjectForInterview(projectUnits.get(radioGroup.getCheckedRadioButtonId()-1).name);
            }
        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return builder.create();
    }


}
