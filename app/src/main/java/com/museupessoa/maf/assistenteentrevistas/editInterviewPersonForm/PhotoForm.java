package com.museupessoa.maf.assistenteentrevistas.editInterviewPersonForm;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class PhotoForm extends Fragment {
    private final int CAMERA_RESULT_F = 7;
    private final int CAMERA_RESULT_P = 11;
    private String interview_path;
    private String formPhoto_path;
    private String perfilPhoto_path;
    Button photoForm;
    Button photoPerfil;
    ImageView MetaPhoto;
    ImageView perfilPhoto;
    public PhotoForm(String interview_path) {
        this.interview_path = interview_path;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View metainfo = inflater.inflate(R.layout.fragment_new_interview_metainfo_photo,container,false);
        photoForm = (Button) metainfo.findViewById(R.id.BFrom);
        photoPerfil = (Button) metainfo.findViewById(R.id.BPrefil);
        MetaPhoto = (ImageView) metainfo.findViewById(R.id.photo_meta_view);
        perfilPhoto = (ImageView) metainfo.findViewById(R.id.perfil_photo_meta);
        formPhoto_path = interview_path + "/Fotos/form.jpg";
        perfilPhoto_path  = interview_path + "/Fotos/foto_perfil.jpg";

        File imgFile = new  File(interview_path + "/Fotos/form.jpg");
        Bitmap myBitmap = null;
        if(imgFile.exists()){
            myBitmap = decodeFile(imgFile);
            MetaPhoto.setImageBitmap(myBitmap);
        }else{
            MetaPhoto.setImageBitmap(null);
        }
        File perfilThumbnail = new  File(interview_path + "/Fotos/foto_perfil_thumbnail.jpg");
        if(perfilThumbnail.exists()){
            myBitmap = decodeFile(perfilThumbnail);
            perfilPhoto.setImageBitmap(myBitmap);
        }else{
            perfilPhoto.setImageBitmap(null);
        }

        return metainfo;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        photoForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(formPhoto_path)));
                getActivity().startActivityForResult(cameraIntent, CAMERA_RESULT_F);
            }
        });
        photoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(perfilPhoto_path)));
                getActivity().startActivityForResult(cameraIntent, CAMERA_RESULT_P);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_RESULT_F) {
            try{
                Pic(formPhoto_path);
            }
            catch (Exception e ){
                e.printStackTrace();
                Toast.makeText(getActivity(),"Tenta outra vez", Toast.LENGTH_LONG).show();
            }
        }
        else if (requestCode == CAMERA_RESULT_P){
            try{
                thumbnailPic(perfilPhoto_path);
            }
            catch (Exception e ){
                e.printStackTrace();
                Toast.makeText(getActivity(),"Tenta outra vez", Toast.LENGTH_LONG).show();
            }
        }

    }

    private void thumbnailPic(String PATH) throws IOException {
        File imgFile = new  File(PATH);
        File file = new  File(interview_path + "/Fotos/foto_perfil_thumbnail.jpg");
        Bitmap myBitmap = null;
        if(imgFile.exists()){
            myBitmap = decodeFile(imgFile);
            myBitmap = ThumbnailUtils.extractThumbnail(myBitmap, 64, 64);
            OutputStream fOut = null;
            fOut = new FileOutputStream(file);
            myBitmap.compress(Bitmap.CompressFormat.JPEG, 55, fOut);
            fOut.flush();
            fOut.close();
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

        //NewInterviewActivity.bitmapFormPhoto = BitmapFactory.decodeFile(PATH, bmOptions);
        //MetaPhoto.setImageBitmap(NewInterviewActivity.bitmapFormPhoto);
    }


    // Decodes image and scales it to reduce memory consumption
    private static Bitmap decodeFile(File f) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);
            // The new size we want to scale to
            final int REQUIRED_SIZE=256;
            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while(o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }
            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {}
        return null;
    }

}
