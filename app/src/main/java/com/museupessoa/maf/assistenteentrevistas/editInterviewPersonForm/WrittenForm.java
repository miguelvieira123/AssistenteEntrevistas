package com.museupessoa.maf.assistenteentrevistas.editInterviewPersonForm;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;
import com.museupessoa.maf.assistenteentrevistas.InterviewActivity;
import com.museupessoa.maf.assistenteentrevistas.R;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


public class WrittenForm extends Fragment {

    private String new_interview_path;
    private FloatingActionButton add;

    LinearLayout linearLayout;
    LinearLayout.LayoutParams layout_params;
    private static int viewsCount = 0;
    private List<View> allViews = new ArrayList<View>();

    public Class<? extends WrittenForm> getFragmentClass() {
        return this.getClass();
    }

    public WrittenForm(String new_interview_path) {
        this.new_interview_path = new_interview_path;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View metainfo = inflater.inflate(R.layout.fragment_new_interview_metainfo_text,container,false);
        add = (FloatingActionButton) metainfo.findViewById(R.id.acceptInterviewChanges );
        layout_params = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT,
                                                LinearLayout.LayoutParams.WRAP_CONTENT);
        layout_params.setMargins(5, 5, 5, 5);

        linearLayout = (LinearLayout) metainfo.findViewById(R.id.edit_interview_metadata);

        //createEditText("Nome");
        createEditTextForm();


        return metainfo;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), InterviewActivity.class);
                intent.putExtra("path",new_interview_path);
                startActivity(intent);
            }
        });
    }


    private void createEditText(String hint) {
        EditText editText = new EditText(this.getContext());
        editText.setId(viewsCount++);
        editText.setHint(hint);
        editText.setSingleLine();
        allViews.add(editText);
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
                        createEditText(node_meta.item(i).getNodeName());
                    }

                    //NamedNodeMap attrs = audio.item(0).getAttributes();
                    //contador = Integer.parseInt(attrs.getNamedItem("count").getNodeValue());
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
