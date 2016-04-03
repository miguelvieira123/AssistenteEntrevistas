package com.museupessoa.maf.assistenteentrevistas;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class InterviewActivity extends AppCompatActivity {

    private Chronometer rec_time;
    private MediaRecorder mRecorder = null;
    private int conta_gravacoes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interview);
        final Intent intent = getIntent();
        String path = intent.getStringExtra("path");
        String n = intent.getStringExtra("name");
        TextView name = (TextView)findViewById(R.id.person_name);
        name.setText(n);
        Log.d("InterviewActivity", ">>>>>InterviewActivity>>>> var path: " + path);

        FragmentManager fragmentActionManager =  getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentActionManager.beginTransaction();
        Interview interview = new Interview();
        Bundle bundle = new Bundle();
        bundle.putString("path", path);
        interview.setArguments(bundle);
        fragmentTransaction.add(R.id.interview_activity_questions_list, interview);
        fragmentTransaction.commit();

        conta_gravacoes = getContadorFromXML(path);
        rec_time = (Chronometer) findViewById(R.id.rec_chronometer);
        //add Listenners to Buttons
        this.addListennerToButtons();
    }


    private void addListennerToButtons(){
        ToggleButton rec = (ToggleButton) findViewById(R.id.toggleButton);
        rec.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    rec_time.setBase(SystemClock.elapsedRealtime());
                    rec_time.start();
                }else{
                    rec_time.stop();
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
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mRecorder.setAudioChannels(2);
        try {
            mRecorder.prepare();
        } catch (IOException e) {
            //System.out.println(e.getMessage());
           // Log.e(LOG_TAG, "prepare() failed");
        }
        mRecorder.start();
    }

    private static int getContadorFromXML(String path){
        //ArrayList<String> res = new ArrayList<String>();
        int contador=0;
        File f  = new File(path, "manifesto.xml");
        if(f.exists()){
            Document doc = null;
            try {
                doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(f);
                NodeList audio = doc.getElementsByTagName("audio");
                /*
                for (int i=0; i < perguntas.getLength(); i++){
                    res.add(perguntas.item(i).getFirstChild().getNodeValue());
                }*/
                NamedNodeMap attrs = audio.item(0).getAttributes();
                contador = Integer.parseInt(attrs.getNamedItem("contador").getNodeValue());

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



}
