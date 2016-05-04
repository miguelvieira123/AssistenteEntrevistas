package com.museupessoa.maf.assistenteentrevistas.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.museupessoa.maf.assistenteentrevistas.DownloadProjectsActivity;
import com.museupessoa.maf.assistenteentrevistas.R;


public class DowloadProjectDialogFragment  extends DialogFragment {
    private int position;

    public DowloadProjectDialogFragment(int pos){
        position = pos;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Queria guardar este projeto?")
                .setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Toast.makeText(getActivity(), "Guardado "+ Integer.toString(position), Toast.LENGTH_SHORT).show();
                            DownloadProjectsActivity callingActivity = (DownloadProjectsActivity) getActivity();
                            callingActivity.saveProject(position);
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
