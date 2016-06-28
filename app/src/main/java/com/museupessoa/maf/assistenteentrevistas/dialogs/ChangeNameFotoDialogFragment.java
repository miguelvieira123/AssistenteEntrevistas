package com.museupessoa.maf.assistenteentrevistas.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.museupessoa.maf.assistenteentrevistas.FotoActivity;
import com.museupessoa.maf.assistenteentrevistas.NewInterviewActivity;
import com.museupessoa.maf.assistenteentrevistas.R;


public class ChangeNameFotoDialogFragment extends DialogFragment {
    int position;
    String name;


    public  ChangeNameFotoDialogFragment(int position,String name){
       this.position = position;
       this.name = name;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_dialog_change_foto_name, null);
        final EditText editText = (EditText)view.findViewById(R.id.EditTextChangeFotoName);
        editText.setText(name);
        builder.setView(view);
        builder.setTitle("Escreve aqui nome")
                .setPositiveButton("Mudar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        FotoActivity.changeName(position,editText.getText().toString());
                    }
                })
                .setNegativeButton("Canselar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        return builder.create();
    }
}
