package com.museupessoa.maf.assistenteentrevistas.dialogs;

import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.museupessoa.maf.assistenteentrevistas.NewProjectActivity;


public class NewProjectAddDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Deseja criar o projeto?")
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ( (NewProjectActivity)getActivity()).okClicked();
                    }
                })
                .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ((NewProjectActivity)getActivity()).cancelClicked();
                    }
                });
        return builder.create();
    }

}
