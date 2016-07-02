package com.museupessoa.maf.assistenteentrevistas.editInterviewPersonForm;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;
import com.museupessoa.maf.assistenteentrevistas.InterviewActivity;
import com.museupessoa.maf.assistenteentrevistas.NewInterviewActivity;
import com.museupessoa.maf.assistenteentrevistas.R;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


public class WrittenForm extends Fragment {

    private String new_interview_path;
    private String project_name;
    private FloatingActionButton add;

    LinearLayout linearLayout;
    LinearLayout.LayoutParams layout_params;
    private static int viewsCount = 0;
    private static HashMap<String,EditText> allViews = new HashMap<String,EditText>();

    public Class<? extends WrittenForm> getFragmentClass() {
        return this.getClass();
    }

    public WrittenForm(String new_interview_path, String project_name) {
        this.new_interview_path = new_interview_path;
        this.project_name = project_name;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View metainfo = inflater.inflate(R.layout.fragment_new_interview_metainfo_text,container,false);
        layout_params = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT,
                                                LinearLayout.LayoutParams.WRAP_CONTENT);
        layout_params.setMargins(5, 5, 5, 5);
        linearLayout = (LinearLayout) metainfo.findViewById(R.id.edit_interview_metadata);
        this.createEditTextForm();

        return metainfo;
    }


    @Override
    public void onStop() {
        super.onStop();
        savePersonMetainfo();
    }

    @Override
    public void onPause(){
        super.onPause();
        savePersonMetainfo();
    }


    private void savePersonMetainfo( ){
        HashMap<String, String> info = new HashMap<>();
        for (String key: allViews.keySet()) {
            info.put(key, allViews.get(key).getText().toString());
        }
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy HH:mm:ss");
            File f  = new File(new_interview_path, "manifesto.xml");
            if(f.exists()){
                Document doc = null;
                try {
                    doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(f);
                    NodeList nodeList_meta = doc.getElementsByTagName("meta");
                    String person_name = nodeList_meta.item(0).getAttributes().getNamedItem("name").getNodeValue();
                    doc.getFirstChild().removeChild(nodeList_meta.item(0));
                    Node root = doc.getElementsByTagName("manifesto").item(0);

                    org.w3c.dom.Element n1;
                    org.w3c.dom.Element n2;

                    n1 = doc.createElement("meta");
                    n1.setAttribute("name", person_name );
                    n1.setAttribute("project", project_name ); ///ATENÇÃO A ISTO!!!!!!!
                    n1.setAttribute("time", sdf.format(new Date()));
                    n1.setAttribute("send","no");
                    for (String key: info.keySet()) {
                        n2 = doc.createElement( "info" );
                        n2.setAttribute("name", key );
                        n2.setTextContent(info.get(key));
                        n1.appendChild(n2);
                    }
                    root.appendChild(n1);

                    Transformer trans = TransformerFactory.newInstance().newTransformer();
                    DOMSource xmlSource = new DOMSource(doc);
                    StreamResult result = new StreamResult(new_interview_path + "/manifesto.xml");
                    trans.transform(xmlSource, result);

                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                }
            }
        }catch(Exception e){e.printStackTrace();}
    }




    private void createEditText(String hint, String value) {
        EditText editText = new EditText(this.getContext());
        TextView label = new TextView(this.getContext());
        label.setText(hint+":");
        editText.setId(viewsCount++);
        editText.setHint(hint);
        editText.setSingleLine();
        editText.setText(value);
        Log.d("VALUE", " " + value);
        allViews.put(hint, editText);//para mais tarde recolher o input
        linearLayout.addView(label, layout_params);
        linearLayout.addView(editText, layout_params);
    }



    private void createEditTextForm(){
        try{
            File f  = new File(new_interview_path, "manifesto.xml");
            if(f.exists()){
                Document doc = null;
                try {
                    doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(f);
                    NodeList nodeList_meta = doc.getElementsByTagName("meta");
                    NodeList node_meta = nodeList_meta.item(0).getChildNodes();

                    for (int i=0; i< node_meta.getLength(); i++) {
                        if (node_meta.item(i).hasChildNodes()){
                            createEditText( node_meta.item(i).getAttributes().getNamedItem("name").getNodeValue(),
                                            node_meta.item(i).getFirstChild().getNodeValue());
                        }else{
                            createEditText( node_meta.item(i).getAttributes().getNamedItem("name").getNodeValue(),
                                            "");
                        }
                    }
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                }
            }
        }catch(Exception e){}

    }




}
