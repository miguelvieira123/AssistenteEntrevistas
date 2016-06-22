package com.museupessoa.maf.assistenteentrevistas.dialogs;

import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.museupessoa.maf.assistenteentrevistas.Fragments.Projects;
import com.museupessoa.maf.assistenteentrevistas.InterviewActivity;


public class DeleteProjectDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Tem a certeza absoluta?")
                .setPositiveButton("Sem Dúvida", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Projects.delProject(getTargetRequestCode());
                        Toast.makeText(getActivity(), "Projeto  eliminado", Toast.LENGTH_LONG).show();

                    }
                })
                .setNegativeButton("Estou Na Dúvida", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getActivity(), "Cancelado", Toast.LENGTH_LONG).show();
                    }
                });
        return builder.create();
    }
}
