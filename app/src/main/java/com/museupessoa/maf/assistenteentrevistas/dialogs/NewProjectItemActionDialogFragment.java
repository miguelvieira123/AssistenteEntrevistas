package com.museupessoa.maf.assistenteentrevistas.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.museupessoa.maf.assistenteentrevistas.NewProject;
import com.museupessoa.maf.assistenteentrevistas.R;

public class NewProjectItemActionDialogFragment extends DialogFragment {
    public static final String ACTION = "action";


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());;
        builder.setTitle("O que você quer fazer")
                .setPositiveButton("Editar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent();
                        //intent.putExtra(ACTION, element);
                        getTargetFragment().onActivityResult(getTargetRequestCode(), NewProject.EDIT, intent);
                    }
                })
                .setNegativeButton("Eliminar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent();
                        //intent.putExtra(ACTION, element);
                        getTargetFragment().onActivityResult(getTargetRequestCode(), NewProject.DELETE, intent);
                    }
                });
        return builder.create();
    }
}