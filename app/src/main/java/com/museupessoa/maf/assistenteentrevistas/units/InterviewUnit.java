package com.museupessoa.maf.assistenteentrevistas.units;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.util.Log;

import com.museupessoa.maf.assistenteentrevistas.Fragments.Interview;
import com.museupessoa.maf.assistenteentrevistas.General;
import com.museupessoa.maf.assistenteentrevistas.MainActivity;
import com.museupessoa.maf.assistenteentrevistas.R;

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
    public String project;
    public String time;
    public boolean sendStatus;
    public int pos;

    public InterviewUnit(String name, Bitmap foto, String path, String project,String time,boolean sendStatus, int pos) {
        this.name = name;
        this.foto = foto;
        this.path = path;
        this.project = project;
        this.time = time;
        this.sendStatus=sendStatus;
        this.pos = pos;
    }

    static public List<InterviewUnit> getInterviews(String PATH){
        String[] interview_list;
        List<InterviewUnit> interviews = new ArrayList<>();
        File folder = new File(PATH+"/Entrevistas");
        interview_list = folder.list();

        for(int i=0;i<interview_list.length;i++){

            // imagem ------------------------------------------------------------------------------
            Bitmap myBitmap = null;
            Bitmap imageRounded=null;
            myBitmap = BitmapFactory.decodeFile(PATH+"/Entrevistas/"+interview_list[i]+"/Fotos/foto_perfil_thumbnail.jpg");
            if(myBitmap!=null) {
                imageRounded = Bitmap.createBitmap(myBitmap.getWidth(), myBitmap.getHeight(), myBitmap.getConfig());
                Canvas canvas = new Canvas(imageRounded);
                Paint mpaint = new Paint();
                mpaint.setAntiAlias(true);
                mpaint.setShader(new BitmapShader(myBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
                canvas.drawRoundRect((new RectF(0, 0, myBitmap.getWidth(), myBitmap.getHeight())), 100, 100, mpaint);
            }
            // nome do entrevistado (Abrir XML) ----------------------------------------------------
            String interview_path = PATH + "/Entrevistas/"+interview_list[i] ;
            File manif_file = new File(interview_path, "manifesto.xml");
            String nome = "";
            String project= "";
            String time = "";
            String sendStatus="";
            Boolean sendS=false;
            try {
                InputStream is = new FileInputStream(manif_file.getPath());
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(new InputSource(is));
                doc.getDocumentElement().normalize();

                NodeList nodeList = doc.getElementsByTagName("meta");
                Node n = nodeList.item(0);
                if(n != null){
                    nome = n.getAttributes().getNamedItem("name").getNodeValue();
                    project = n.getAttributes().getNamedItem("project").getNodeValue();
                    time = n.getAttributes().getNamedItem("time").getNodeValue();
                    sendStatus = n.getAttributes().getNamedItem("send").getNodeValue();
                    if(sendStatus.equals("yes"))sendS=true;

                }


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(imageRounded==null){
                interviews.add(new InterviewUnit(nome, General.DEFAULT_ICON, interview_path,project,time,sendS,i));
            }else interviews.add(new InterviewUnit(nome, imageRounded, interview_path,project,time,sendS,i));
        }
        return  interviews;
    }


}
