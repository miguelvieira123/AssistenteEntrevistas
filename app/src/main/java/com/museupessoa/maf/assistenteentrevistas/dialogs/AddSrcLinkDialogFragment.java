package com.museupessoa.maf.assistenteentrevistas.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.museupessoa.maf.assistenteentrevistas.DownloadProjectsActivity;
import com.museupessoa.maf.assistenteentrevistas.R;

public class AddSrcLinkDialogFragment extends DialogFragment  {
    EditText link;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_dialog_add_src_link, null);
        link = (EditText) view.findViewById(R.id.AddSrcLinkDF);
        link.setText("http://");
        builder.setView(view);
        builder.setTitle("Criar nova referencia")
                .setPositiveButton("Aplicar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (link.getText().toString().isEmpty()) Toast.makeText(getActivity(),
                                "A referencia tem que ser", Toast.LENGTH_SHORT).show();
                        else {

                            DownloadProjectsActivity callingActivity = (DownloadProjectsActivity) getActivity();
                            callingActivity.addSourceLink(link.getText().toString());
                            dismiss();
                        }

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
