package com.museupessoa.maf.assistenteentrevistas.editInterviewPersonForm;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.museupessoa.maf.assistenteentrevistas.General;
import com.museupessoa.maf.assistenteentrevistas.NewInterviewActivity;
import com.museupessoa.maf.assistenteentrevistas.NewProjectActivity;
import com.museupessoa.maf.assistenteentrevistas.R;
import com.museupessoa.maf.assistenteentrevistas.adapters.RVNewProjectAdapter;
import com.museupessoa.maf.assistenteentrevistas.dialogs.NewProjectDialogFragmentNewItem;
import com.museupessoa.maf.assistenteentrevistas.dialogs.NewProjectItemActionDialogFragment;

import java.io.File;

public class PhotoForm extends Fragment {
    private final int CAMERA_RESULT_F = 0;
    private final int CAMERA_RESULT_P = 1;
    private String new_interview_path;
    private String formPhoto;
    private String prefilPhoto;
    Button photoForm;
    Button photoPrefil;
    ImageView MetaPhoto;
    public PhotoForm(String new_interview_path) {
        this.new_interview_path = new_interview_path;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View metainfo = inflater.inflate(R.layout.fragment_new_interview_metainfo_photo,container,false);
        photoForm = (Button) metainfo.findViewById(R.id.BFrom);
        photoPrefil = (Button) metainfo.findViewById(R.id.BPrefil);
        MetaPhoto = (ImageView) metainfo.findViewById(R.id.photo_meta_view);
        formPhoto = new_interview_path + "/Fotos/form.jpg";
        prefilPhoto  = new_interview_path + "/Fotos/foto_perfil.jpg";
        if(NewInterviewActivity.bitmapFormPhoto!=null)MetaPhoto.setImageBitmap(NewInterviewActivity.bitmapFormPhoto);
        return metainfo;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        photoForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(formPhoto)));
                startActivityForResult(cameraIntent, CAMERA_RESULT_F);

            }
        });
        photoPrefil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(prefilPhoto)));
                startActivityForResult(cameraIntent,CAMERA_RESULT_P);

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_RESULT_F) {
            try{
                Pic(formPhoto);
            }
            catch (Exception e ){
                Toast.makeText(getActivity(),"Tenta outra vez", Toast.LENGTH_LONG).show();
            }

        }
        else if (requestCode == CAMERA_RESULT_P){
            try{
                Pic(prefilPhoto);
            }
            catch (Exception e ){
                Toast.makeText(getActivity(),"Tenta outra vez", Toast.LENGTH_LONG).show();
            }

        }

    }

    private void Pic(String PATH) {
        int targetW = MetaPhoto.getWidth();
        int targetH = MetaPhoto.getHeight();

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(PATH, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        NewInterviewActivity.bitmapFormPhoto = BitmapFactory.decodeFile(PATH, bmOptions);
        MetaPhoto.setImageBitmap(NewInterviewActivity.bitmapFormPhoto);
    }

}
