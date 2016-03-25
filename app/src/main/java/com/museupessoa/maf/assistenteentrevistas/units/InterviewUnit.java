package com.museupessoa.maf.assistenteentrevistas.units;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Miguel on 24/03/2016.
 */
public class InterviewUnit {
    public String name;
    public Bitmap foto;

    public InterviewUnit(String name, Bitmap foto) {
        this.name = name;
        this.foto = foto;
    }

    static public List<InterviewUnit> getInterviews(String PATH){
        String[] list;
        List<InterviewUnit> interviews = new ArrayList<>();
        File f = new File(PATH+"/Entrevistas");
        list = f.list();



        for(int i=0;i<list.length;i++){
            File imgFile = new  File(PATH+"/Entrevistas/e00"+i+"/Fotos/foto_perfil.jpg");
            Bitmap myBitmap = null;
            if(imgFile.exists()){
                myBitmap = decodeFile(imgFile);
                int dimension = getSquareCropDimensionForBitmap(myBitmap);
                myBitmap = ThumbnailUtils.extractThumbnail(myBitmap, dimension, dimension);
            }
            interviews.add(new InterviewUnit(list[i], myBitmap));
        }
        return  interviews;
    }


    public static int getSquareCropDimensionForBitmap(Bitmap bitmap)
    {
        //use the smallest dimension of the image to crop to
        return Math.min(bitmap.getWidth(), bitmap.getHeight());
    }

    // Decodes image and scales it to reduce memory consumption
    private static Bitmap decodeFile(File f) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);
            // The new size we want to scale to
            final int REQUIRED_SIZE=30;
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
