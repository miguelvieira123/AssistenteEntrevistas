package com.museupessoa.maf.assistenteentrevistas.dialogs;

import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.museupessoa.maf.assistenteentrevistas.NewProject;
import com.museupessoa.maf.assistenteentrevistas.main.Projects;

public class ProjectActionDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());;
        builder.setTitle("O que você quer fazer")
                .setPositiveButton("Editar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent();
                        getTargetFragment().onActivityResult(getTargetRequestCode(), Projects.EDIT_PROJECT, intent);
                    }
                })
                .setNegativeButton("Eliminar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent();
                        getTargetFragment().onActivityResult(getTargetRequestCode(), Projects.DELETE_PROJECT, intent);
                    }
                });
        return builder.create();
    }
}
