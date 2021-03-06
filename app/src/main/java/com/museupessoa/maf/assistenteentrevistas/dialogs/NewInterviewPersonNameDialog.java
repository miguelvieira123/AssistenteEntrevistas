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


import com.museupessoa.maf.assistenteentrevistas.General;
import com.museupessoa.maf.assistenteentrevistas.NewInterviewActivity;
import com.museupessoa.maf.assistenteentrevistas.R;

public class NewInterviewPersonNameDialog  extends DialogFragment {

    EditText test;
    public static final String REQUEST = "new";
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_dialog_newinterview, null);
        test = (EditText) view.findViewById(R.id.PersonName);
        builder.setView(view);
        builder.setTitle("Criar Entrevista")
                .setPositiveButton("Aplicar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (test.getText().toString().isEmpty()) Toast.makeText(getActivity(),
                                "É obrigatório atribuir um nome à entrevista", Toast.LENGTH_SHORT).show();
                        else {
                            Intent intent = new Intent(getActivity(), NewInterviewActivity.class);
                            intent.putExtra("person_name",test.getText().toString());
                            //intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            startActivity(intent);

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
