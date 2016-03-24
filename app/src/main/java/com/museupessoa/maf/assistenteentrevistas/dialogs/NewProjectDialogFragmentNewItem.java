package com.museupessoa.maf.assistenteentrevistas.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.museupessoa.maf.assistenteentrevistas.NewProject;
import com.museupessoa.maf.assistenteentrevistas.R;


public class NewProjectDialogFragmentNewItem extends DialogFragment implements
        DialogInterface.OnClickListener {
    private View form=null;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        form= getActivity().getLayoutInflater()
                .inflate(R.layout.newproject_dialog_fragment, null);
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        return(builder.setTitle("Novo Elemento").setView(form)
                .setPositiveButton("Adicionar", this)
                .setNegativeButton("Cancelar", null).create());
    }
    @Override
    public void onClick(DialogInterface dialog, int which) {

        EditText name=(EditText)form.findViewById(R.id.NewProjectNameEdit);
        String projectName = name.getText().toString();
        if(!projectName.isEmpty()) {

        }
        else Toast.makeText(getActivity(), "Não pode ser vasio", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onDismiss(DialogInterface unused) {
        super.onDismiss(unused);
    }
    @Override
    public void onCancel(DialogInterface unused) {
        super.onCancel(unused);
    }
}

