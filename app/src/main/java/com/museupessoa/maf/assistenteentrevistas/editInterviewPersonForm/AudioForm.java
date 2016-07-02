package com.museupessoa.maf.assistenteentrevistas.editInterviewPersonForm;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.museupessoa.maf.assistenteentrevistas.DownloadProjectsActivity;
import com.museupessoa.maf.assistenteentrevistas.R;
import com.museupessoa.maf.assistenteentrevistas.adapters.RVFormAudioAdapter;
import com.museupessoa.maf.assistenteentrevistas.adapters.RVProjectAdapter;
import com.museupessoa.maf.assistenteentrevistas.dialogs.NewProjectDialogFragment;
import com.museupessoa.maf.assistenteentrevistas.dialogs.ProjectActionDialogFragment;
import com.museupessoa.maf.assistenteentrevistas.units.ProjectUnit;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


public class AudioForm extends Fragment {

    private static String new_interview_path;
    static RVFormAudioAdapter adapter;
    private List<String> formNames;
    private static RecyclerView recyclerView;
    static Integer posStatus=-1;
    public static int REC=0;
    private static MediaRecorder mRecorder = null;
    static android.support.v7.widget.CardView cardView;
    static TextView audioName;

    public AudioForm(String new_interview_path) {
        this.new_interview_path = new_interview_path;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View metainfo = inflater.inflate(R.layout.fragment_new_interview_metainfo_audio,container,false);
        recyclerView = (RecyclerView) metainfo.findViewById(R.id.recyclerView);
        formNames = getFormsNames();
        File f = new File(new_interview_path+"/Audio/Form");
        if(!f.exists())f.mkdirs();
        return metainfo;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);
        adapter = new RVFormAudioAdapter(formNames,new_interview_path);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener((new RVFormAudioAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position,CardView cv) {
                cardView = (android.support.v7.widget.CardView )v.findViewById(R.id.CV_audioForm);
                audioName = (TextView)v.findViewById(R.id.audioFormName);
                if(posStatus==-1){
                    startRecording(new_interview_path, formNames.get(position));
                    audioName.setTextColor(Color.rgb(151, 35, 26));
                    cardView.setCardBackgroundColor(Color.rgb(209, 248, 255));
                    posStatus=position;
                }
                else if(posStatus==position) {
                    stopRecording();
                    recyclerView.setAdapter(adapter);
                    cardView.setCardBackgroundColor(Color.WHITE);
                    audioName.setTextColor(Color.rgb(120, 120, 120));
                    posStatus=-1;
                }
                else {
                   /* audioName.setTextColor(Color.rgb(151, 35, 26));
                    cardView.setCardBackgroundColor(Color.rgb(209, 248, 255));
                    ViewGroup  vv = (ViewGroup)recyclerView.getChildAt(posStatus);
                    cardView = (android.support.v7.widget.CardView )vv.findViewById(R.id.CV_audioForm);
                    audioName = (TextView)vv.findViewById(R.id.audioFormName);
                    cardView.setBackgroundColor(Color.WHITE);
                    audioName.setTextColor(Color.rgb(120, 120, 120));
                    stopRecording();
                    startRecording(new_interview_path, formNames.get(position));
                    posStatus=position;*/
                }

            }
        }));

    }

    private void startRecording(String path, String name) {
        REC=1;
        mRecorder = new MediaRecorder();
        String mFileName = path +"/Audio/Form/"+name+".mp4";
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        mRecorder.setAudioEncodingBitRate(16);
        mRecorder.setAudioSamplingRate(44100);
        mRecorder.setAudioChannels(1);
        mRecorder.setOutputFile(mFileName);
        try {
            mRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mRecorder.start();
    }

    public static void stopRecording() {
        REC=0;
        if(mRecorder!=null) {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;

        }
    }

    private List<String> getFormsNames(){
        List<String> forms = new ArrayList<>();
        try{
            File f  = new File(new_interview_path, "manifesto.xml");
            if(f.exists()){
                Document doc = null;
                try {
                    doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(f);
                    NodeList nodeList_meta = doc.getElementsByTagName("meta");
                    NodeList node_meta = nodeList_meta.item(0).getChildNodes();

                    for (int i=0; i< node_meta.getLength(); i++) {
                        forms.add(node_meta.item(i).getAttributes().getNamedItem("name").getNodeValue());
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
        return forms;
    }

    public static void startPlay(String name){
        if(REC==1){
        }else {
            MediaPlayer mediaPlayer = new MediaPlayer();
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.reset();
            }
            try {
                if(new File(new_interview_path + "/Audio/Form/" + name + ".mp4").exists()) {
                    mediaPlayer.setDataSource(new_interview_path + "/Audio/Form/" + name + ".mp4");
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
