package com.museupessoa.maf.assistenteentrevistas.dialogs;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;
import com.museupessoa.maf.assistenteentrevistas.ListenAudio;

public class DeleteAudioDialogFragment   extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Queria eliminar gravação "+ListenAudio.audioUnits.get(ListenAudio.groupPosToDel).audio.get(ListenAudio.childPosToDel))
                .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                       ListenAudio.deleteAudio();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getActivity(),"Cancelado",Toast.LENGTH_LONG).show();
                    }
                });
        return builder.create();
    }
}
