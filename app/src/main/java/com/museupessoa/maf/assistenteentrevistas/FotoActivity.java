package com.museupessoa.maf.assistenteentrevistas;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.app.FragmentManager;
import android.graphics.Point;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;

import com.museupessoa.maf.assistenteentrevistas.adapters.SwipeFotoAdapter;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class FotoActivity extends AppCompatActivity {
    static String interview_path;
    public static ViewPager viewPager;
    public static SwipeFotoAdapter adapter;
    public static FragmentManager fm;
    static List<String> fotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foto);
        getSupportActionBar().hide();
        final Intent intent = getIntent();
        interview_path = intent.getStringExtra("path");
        fm = getFragmentManager();
        fotos = getlistOfFoto(interview_path);
        viewPager = (ViewPager)findViewById(R.id.ViewPager_foto);
        adapter = new SwipeFotoAdapter(this,fotos,interview_path);
        viewPager.setAdapter(adapter);

    }

    private List<String> getlistOfFoto(String path){
        List<String> out = new ArrayList<String>();
        File f = new File(path+"/Fotos/");
        String[] list =f.list();
        for(int i=0;i<list.length;i++)out.add(list[i]);
        return  out;
    }
    public static void deleteFoto(int position){
        File f  = new File( interview_path + "/Manifesto.xml");
        Document doc = null;
        if(f.exists()) {
            try {

                doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(f);
                NodeList root = doc.getElementsByTagName("photos");
                NodeList photos = doc.getElementsByTagName("photo");
                for(int i =0;i<photos.getLength();i++){
                    if(photos.item(i).getAttributes().getNamedItem("name").getTextContent().equals(fotos.get(position))){
                        root.item(0).removeChild(photos.item(i));
                        Transformer trans = TransformerFactory.newInstance().newTransformer();
                        DOMSource xmlSource = new DOMSource(doc);
                        StreamResult result = new StreamResult(interview_path + "/Manifesto.xml");
                        trans.transform(xmlSource, result);
                        break;
                    }
                }
                File img = new File(interview_path+"/Fotos/"+fotos.get(position));
                if(img.exists()){img.delete();}
                fotos.remove(position);
                adapter.updateData(fotos);
                viewPager.setAdapter(FotoActivity.adapter);
                if(position!=0)viewPager.setCurrentItem(position-1);
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (TransformerConfigurationException e) {
                e.printStackTrace();
            } catch (TransformerException e) {
                e.printStackTrace();
            }

        }

    }
    public static void changeName( int position,String newName ){
        File f  = new File( interview_path + "/Manifesto.xml");
        Document doc = null;
        if(f.exists()) {
            try {
                doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(f);
                NodeList photos = doc.getElementsByTagName("photo");
                for(int i =0;i<photos.getLength();i++){
                    if(photos.item(i).getAttributes().getNamedItem("name").getTextContent().equals(fotos.get(position))){
                        photos.item(i).getAttributes().getNamedItem("name").setTextContent(newName);
                        Transformer trans = TransformerFactory.newInstance().newTransformer();
                        DOMSource xmlSource = new DOMSource(doc);
                        StreamResult result = new StreamResult(interview_path + "/Manifesto.xml");
                        trans.transform(xmlSource, result);
                        break;
                    }
                }
                File img = new File(interview_path+"/Fotos/"+fotos.get(position));
                img.renameTo(new File(interview_path+"/Fotos/"+newName));
                fotos.set(position, newName);
                adapter.updateData(fotos);
                viewPager.setAdapter(FotoActivity.adapter);
             } catch (SAXException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (TransformerConfigurationException e) {
                e.printStackTrace();
            } catch (TransformerException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
