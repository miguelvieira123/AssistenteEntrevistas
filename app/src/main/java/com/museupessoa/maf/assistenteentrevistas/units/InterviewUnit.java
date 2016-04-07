package com.museupessoa.maf.assistenteentrevistas.units;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


public class InterviewUnit {
    public String name;
    public Bitmap foto;
    public String path;

    public InterviewUnit(String name, Bitmap foto, String path) {
        this.name = name;
        this.foto = foto;
        this.path = path;
    }

    static public List<InterviewUnit> getInterviews(String PATH){
        String[] list;
        List<InterviewUnit> interviews = new ArrayList<>();
        File f = new File(PATH+"/Entrevistas");
        list = f.list();

        for(int i=0;i<list.length;i++){

            // imagem ------------------------------------------------------------------------------
            File imgFile = new  File(PATH+"/Entrevistas/e00"+i+"/Fotos/foto_perfil.jpg");
            Bitmap myBitmap = null;
            if(imgFile.exists()){
                myBitmap = decodeFile(imgFile);
                int dimension = getSquareCropDimensionForBitmap(myBitmap);
                myBitmap = ThumbnailUtils.extractThumbnail(myBitmap, dimension, dimension);
            }
            // nome do entrevistado (Abrir XML) ----------------------------------------------------
            String interview_path = PATH + "/Entrevistas/e00" + i;
            File manif_file = new File(interview_path, "manifesto.xml");
            String nome = "";
            try {
                InputStream is = new FileInputStream(manif_file.getPath());
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(new InputSource(is));
                doc.getDocumentElement().normalize();
                NodeList nodeList = doc.getElementsByTagName("nome");
                Node n = nodeList.item(0);
                nome = n.getFirstChild().getNodeValue();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            interviews.add(new InterviewUnit(nome, myBitmap, interview_path));
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
