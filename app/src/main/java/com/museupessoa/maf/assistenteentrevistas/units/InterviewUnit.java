package com.museupessoa.maf.assistenteentrevistas.units;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.util.Log;

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
        String[] interview_list;
        List<InterviewUnit> interviews = new ArrayList<>();
        File folder = new File(PATH+"/Entrevistas");
        interview_list = folder.list();

        for(int i=0;i<interview_list.length;i++){

            // imagem ------------------------------------------------------------------------------
            Bitmap myBitmap = null;
            myBitmap = BitmapFactory.decodeFile(PATH+"/Entrevistas/"+interview_list[i]+"/Fotos/foto_perfil_thumbnail.jpg");

            // nome do entrevistado (Abrir XML) ----------------------------------------------------
            String interview_path = PATH + "/Entrevistas/"+interview_list[i] ;
            File manif_file = new File(interview_path, "manifesto.xml");
            String nome = "";
            try {
                InputStream is = new FileInputStream(manif_file.getPath());
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(new InputSource(is));
                doc.getDocumentElement().normalize();

                NodeList nodeList = doc.getElementsByTagName("meta");
                Node n = nodeList.item(0);
                if(n != null){nome = n.getAttributes().getNamedItem("name").getNodeValue();}

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


}
