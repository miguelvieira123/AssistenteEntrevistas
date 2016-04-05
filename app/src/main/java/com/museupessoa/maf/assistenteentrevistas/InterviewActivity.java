package com.museupessoa.maf.assistenteentrevistas;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.museupessoa.maf.assistenteentrevistas.Interview_main.Interview;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Timer;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class InterviewActivity extends AppCompatActivity {

    private Chronometer rec_time;
    private MediaRecorder mRecorder = null;
    private int conta_gravacoes;
    private String interview_path;
    private String audio_file_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interview);
        final Intent intent = getIntent();
        interview_path = intent.getStringExtra("path");
        String n = intent.getStringExtra("name");
        TextView name = (TextView)findViewById(R.id.person_name);
        name.setText(n);
        //Log.d("InterviewActivity", ">>>>>InterviewActivity>>>> var path: " + interview_path);

        FragmentManager fragmentActionManager =  getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentActionManager.beginTransaction();
        Interview interview = new Interview();
        Bundle bundle = new Bundle();
        bundle.putString("path", interview_path);
        interview.setArguments(bundle);
        fragmentTransaction.add(R.id.interview_activity_questions_list, interview);
        fragmentTransaction.commit();

        conta_gravacoes = getContadorFromXML(interview_path);
        rec_time = (Chronometer) findViewById(R.id.rec_chronometer);



        //add Listenners to Buttons
        this.addListennerToButtons();
    }

    public boolean newAudioTag(String question){
        //FALTA: verificar se existe alguma gravação em curso.
        try{
            File outputFile = new File(interview_path + "/Audio/" + audio_file_name + ".xml" );
            if (outputFile.exists()){
                Document doc;
                doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(outputFile);
                org.w3c.dom.Element root;
                root = doc.getElementById(audio_file_name);
                org.w3c.dom.Element n1;
                n1 = doc.createElement("tag");
                n1.setAttribute("time", rec_time.getText().toString());
                n1.setTextContent(question);
                root.appendChild(n1);
                //doc.appendChild(root);
                Transformer trans = TransformerFactory.newInstance().newTransformer();
                DOMSource xmlSource = new DOMSource(doc);
                StreamResult result = new StreamResult(interview_path + "/Audio/" + audio_file_name + ".xml");
                trans.transform(xmlSource, result);
                Toast.makeText(this,"Tagged: ["+question+"] at "+rec_time.getText().toString(), Toast.LENGTH_SHORT).show();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean createAudioMetaInfo(String audioFileName){
        try{
            File outputFile = new File(interview_path + "/Audio/" + audioFileName + ".xml" );
            if (!outputFile.exists()){
                Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
                org.w3c.dom.Element root;
                root = doc.createElement("tags-log");
                root.setAttribute("id", audioFileName);
                doc.appendChild(root);
                Transformer trans = TransformerFactory.newInstance().newTransformer();
                DOMSource xmlSource = new DOMSource(doc);
                StreamResult result = new StreamResult(interview_path + "/Audio/" + audioFileName + ".xml");
                trans.transform(xmlSource, result);
            }
            return true;
        } catch (Exception e) {
            //e.printStackTrace();
            return false;
        }
    }
    private void addListennerToButtons(){
        ToggleButton rec = (ToggleButton) findViewById(R.id.toggleButton);
        rec.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    rec_time.setBase(SystemClock.elapsedRealtime());
                    rec_time.start();
                    startRecording(interview_path);
                    audio_file_name = "gravacao_"+conta_gravacoes;
                    createAudioMetaInfo("gravacao_"+conta_gravacoes);
                }else{
                    rec_time.stop();
                    stopRecording();
                    setContadorXML(interview_path, conta_gravacoes);
                }
            }
        });

        ImageButton photo = (ImageButton) findViewById(R.id.photo_button);
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "taking photo", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void startRecording(String path) {
        conta_gravacoes+=1;
        String mFileName = path +"/Audio/gravacao_"+ conta_gravacoes +".3gp";
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB);
        mRecorder.setAudioEncodingBitRate(16);
        mRecorder.setAudioSamplingRate(44100);
        mRecorder.setAudioSamplingRate(23);
        mRecorder.setAudioChannels(2);
        mRecorder.setOutputFile(mFileName);
        try {
            mRecorder.prepare();
        } catch (IOException e) {
            //System.out.println(e.getMessage());
           // Log.e(LOG_TAG, "prepare() failed");
        }
        mRecorder.start();
    }


    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }

    private static int getContadorFromXML(String path){
        int contador=0;
        File f  = new File(path, "manifesto.xml");
        if(f.exists()){
            Document doc = null;
            try {
                doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(f);
                NodeList audio = doc.getElementsByTagName("audio");
                NamedNodeMap attrs = audio.item(0).getAttributes();
                contador = Integer.parseInt(attrs.getNamedItem("count").getNodeValue());
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }
        }
        return contador;
    }

    private void setContadorXML(String path, int novaContagem){
        File f  = new File(path, "manifesto.xml");
        if(f.exists()){
            Document doc = null;
            try {
                doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(f);
                NodeList audio = doc.getElementsByTagName("audio");
                NamedNodeMap attrs = audio.item(0).getAttributes();
                attrs.getNamedItem("count").setNodeValue(""+novaContagem);

                Transformer trans = TransformerFactory.newInstance().newTransformer();
                DOMSource xmlSource = new DOMSource(doc);
                StreamResult result = new StreamResult(path + "/manifesto.xml");
                trans.transform(xmlSource, result);

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


}
