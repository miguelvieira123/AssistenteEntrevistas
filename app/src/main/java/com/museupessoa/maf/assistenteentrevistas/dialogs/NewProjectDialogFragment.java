package com.museupessoa.maf.assistenteentrevistas.dialogs;


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

import com.museupessoa.maf.assistenteentrevistas.R;
import com.museupessoa.maf.assistenteentrevistas.Fragments.Projects;

public class NewProjectDialogFragment extends DialogFragment  {
    EditText test;
    public static final String REQUEST = "new";
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_dialog_newproject, null);
        test = (EditText) view.findViewById(R.id.NewProjectNameEdit);
        builder.setView(view);
        builder.setTitle("Novo Projeto")
                .setPositiveButton("Aplicar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(test.getText().toString().isEmpty())Toast.makeText(getActivity(),
                                "É obrigatório atribuir um nome ao projeto", Toast.LENGTH_SHORT).show();
                        else {
                            Intent intent = new Intent();
                            intent.putExtra(REQUEST, test.getText().toString());
                            getTargetFragment().onActivityResult(getTargetRequestCode(), Projects.NEW_PROJECT, intent);

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

     /*
        form= getActivity().getLayoutInflater()
                .inflate(R.layout.fragment_dialog_newproject, null);
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        return(builder.setTitle("Novo Projeto").setView(form)
                .setPositiveButton("Aplicar", this)
                .setNegativeButton("Cancelar", null).create());
    }
    @Override
    public void onClick(DialogInterface dialog, int which) {

        EditText name=(EditText)form.findViewById(R.id.NewProjectNameEdit);
        String projectName = name.getText().toString();
        if(!projectName.isEmpty()) {
            Intent intent = new Intent(getActivity(), NewProjectActivity.class);
            intent.putExtra("name",projectName);
            startActivity(intent);
        }
        else Toast.makeText(getActivity(), "O nome do projeto não pode ser vasio", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onDismiss(DialogInterface unused) {
        super.onDismiss(unused);
    }
    @Override
    public void onCancel(DialogInterface unused) {
        super.onCancel(unused);
    }
    */
}
