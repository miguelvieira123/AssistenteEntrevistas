package com.museupessoa.maf.assistenteentrevistas.dialogs;


import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.museupessoa.maf.assistenteentrevistas.NewProjectActivity;
import com.museupessoa.maf.assistenteentrevistas.R;

public class NewProjectDialogFragmentEdit extends DialogFragment {
    EditText test;
    public static final String REQUEST = "edit";
    private String element;

    public NewProjectDialogFragmentEdit(String element) {
        this.element = element;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_dialog_newproject, null);
        test = (EditText) view.findViewById(R.id.NewProjectNameEdit);
        test.setText(element.subSequence(0, element.length()));
        builder.setView(view);
        builder.setTitle("Alterar")
                .setPositiveButton("Aplicar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (test.getText().toString().isEmpty()) Toast.makeText(getActivity(),
                                "O campo é de preenchimento obrigatório!", Toast.LENGTH_SHORT).show();
                        else {
                            Intent intent = new Intent();
                            intent.putExtra(REQUEST, test.getText().toString());
                            getTargetFragment().onActivityResult(getTargetRequestCode(), NewProjectActivity.CHANGE, intent);

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


