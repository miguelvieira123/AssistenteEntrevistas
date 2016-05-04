package com.museupessoa.maf.assistenteentrevistas.dialogs;


import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.museupessoa.maf.assistenteentrevistas.DownloadProjectsActivity;

public class DeleteSrcLinkDialogFragment  extends DialogFragment{
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Queria eleminar este link?")
                .setPositiveButton("Eleminar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DownloadProjectsActivity callingActivity = (DownloadProjectsActivity) getActivity();
                        callingActivity.deleteLink();
                        dismiss();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getActivity(), "Cancelado", Toast.LENGTH_SHORT).show();
                    }
                });
        return builder.create();
    }
}
