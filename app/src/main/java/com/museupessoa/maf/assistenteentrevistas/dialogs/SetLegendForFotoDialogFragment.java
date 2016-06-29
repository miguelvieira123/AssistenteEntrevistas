package com.museupessoa.maf.assistenteentrevistas.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.museupessoa.maf.assistenteentrevistas.FotoActivity;
import com.museupessoa.maf.assistenteentrevistas.R;


public class SetLegendForFotoDialogFragment extends DialogFragment {
    int position;
    String legend;


    public SetLegendForFotoDialogFragment(int position, String legend){
       this.position = position;
       this.legend = legend;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_dialog_set_foto_legend, null);
        final EditText editText = (EditText)view.findViewById(R.id.EditTextChangeFotoName);
        editText.setText(legend);
        builder.setView(view);
        builder.setTitle("Escreve aqui legenda")
                .setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        FotoActivity.setLegendForFoto(position, editText.getText().toString());
                    }
                })
                .setNegativeButton("Canselar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        return builder.create();
    }
}
