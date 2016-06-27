package com.museupessoa.maf.assistenteentrevistas;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.museupessoa.maf.assistenteentrevistas.adapters.ListAudioAdapter;
import com.museupessoa.maf.assistenteentrevistas.units.AudioUnit;

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

public class ListenAudio extends AppCompatActivity {
    public static MediaPlayer mediaPlayer;
    public static ListAudioAdapter listAdapter;
    public static ExpandableListView expListView;
    public static List<AudioUnit> audioUnits;
    public static String interview_path;
    public static Integer lastPosGroup=-1;
    public static Integer lastPosChild=-1;
    static Integer ON =1;
    static Integer PAUSE = 2;
    static  Integer STOP =0;
    static Integer status=STOP;
    SeekBar seekBar;
    Handler seekHandler = new Handler();
    TextView time;
    ImageView icon;
    TextView audio;
    public static FragmentManager fm;
    public  static Integer groupPosToDel;
    public  static Integer childPosToDel;
    public  static  Integer DELETE=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen_audio);
        fm = getSupportFragmentManager();
        final Intent intent = getIntent();
        interview_path = intent.getStringExtra("path");
        mediaPlayer =  new MediaPlayer();
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(General.CR, General.CG, General.CB)));
        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        audioUnits = getListOfAudio(interview_path);
        listAdapter = new ListAudioAdapter(this, audioUnits);
        expListView.setAdapter(listAdapter);
        seekBar = (SeekBar) findViewById(R.id.seekBar);


        seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (status != STOP) {
                    SeekBar sb = (SeekBar) v;
                    mediaPlayer.seekTo(sb.getProgress());
                }
                return false;
            }
        });

      expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

          @Override
          public boolean onChildClick(ExpandableListView parent, View v,
                                      int groupPosition, int childPosition, long id) {
              View conv = listAdapter.childviewReferences.get(Integer.toString(groupPosition) + Integer.toString(childPosition));
              icon = (ImageView) conv.findViewById(R.id.ListenAudioIcon);
              audio = (TextView) conv.findViewById(R.id.ListenAudioName);
              time = (TextView) conv.findViewById(R.id.ListenAudioCurrentAudioTime);
              if (lastPosGroup == -1 && lastPosChild == -1) {
                  icon.setBackgroundResource(R.drawable.player_pause);
                  audio.setTextColor(Color.rgb(12, 147, 1));
                  startPlay(groupPosition, childPosition, status);
                  status = ON;
                  lastPosGroup = groupPosition;
                  lastPosChild = childPosition;
              } else if (lastPosGroup == groupPosition && lastPosChild == childPosition) {
                  if (status == PAUSE) {
                      mediaPlayer.start();
                      startPlayProgressUpdater(groupPosition, childPosition);
                      status = ON;
                      icon.setBackgroundResource(R.drawable.player_pause);
                      audio.setTextColor(Color.rgb(12, 147, 1));
                  } else if (status == ON) {
                      mediaPlayer.pause();
                      status = PAUSE;
                      audio.setTextColor(Color.rgb(120, 120, 120));
                      icon.setBackgroundResource(R.drawable.player_play);
                  }
                  //lastPosGroup=-1;
                  //lastPosChild=-1;
              } else {
                  icon.setBackgroundResource(R.drawable.player_pause);
                  audio.setTextColor(Color.rgb(12, 147, 1));
                  View vv = listAdapter.childviewReferences.get(Integer.toString(lastPosGroup) + Integer.toString(lastPosChild));
                  icon = (ImageView) vv.findViewById(R.id.ListenAudioIcon);
                  audio = (TextView) vv.findViewById(R.id.ListenAudioName);
                  time = (TextView) vv.findViewById(R.id.ListenAudioCurrentAudioTime);
                  audio.setTextColor(Color.rgb(120, 120, 120));
                  time.setText(General.getStrTime(audioUnits.get(lastPosGroup).time.get(lastPosChild)));
                  icon.setBackgroundResource(R.drawable.player_play);
                  conv = listAdapter.childviewReferences.get(Integer.toString(groupPosition) + Integer.toString(childPosition));
                  icon = (ImageView) conv.findViewById(R.id.ListenAudioIcon);
                  audio = (TextView) conv.findViewById(R.id.ListenAudioName);
                  time = (TextView) conv.findViewById(R.id.ListenAudioCurrentAudioTime);
                  startPlay(groupPosition, childPosition, status);
                  status = ON;
                  lastPosGroup = groupPosition;
                  lastPosChild = childPosition;

              }
              /*Toast.makeText(getApplicationContext(), audioUnits.get(groupPosition).audio.get(childPosition), Toast.LENGTH_SHORT)
                      .show();*/
              return false;
          }
      });


    }


    public static List<AudioUnit> getListOfAudio(String PATH){
        List<AudioUnit> audioList = new ArrayList<AudioUnit>();
        MediaMetadataRetriever mmr = (MediaMetadataRetriever) new MediaMetadataRetriever();
        File f  = new File(PATH+"/Audio/audio.xml");
        Document doc = null;
        if(f.exists()) {
            try {
                doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(f);
                NodeList nodeList = doc.getElementsByTagName("question");
                for(int i =0;i<nodeList.getLength();i++){
                    NodeList nodeList2 = nodeList.item(i).getChildNodes();
                    List<String> audios = new ArrayList<String>();
                    List<Integer> times = new ArrayList<Integer>();
                    for(int y=0;y<nodeList2.getLength();y++){
                       mmr.setDataSource(PATH + "/Audio/" + nodeList2.item(y).getAttributes().getNamedItem("name").getTextContent());
                       times.add(Integer.parseInt(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)));
                       audios.add(nodeList2.item(y).getAttributes().getNamedItem("name").getTextContent());
                    }
                   audioList.add(new AudioUnit(nodeList.item(i).getAttributes().getNamedItem("name").getTextContent(),audios,times));

                }

            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }

        }
        return audioList;
    }


    public void startPlayProgressUpdater(final Integer groupPosition, final Integer childPosition) {
        seekBar.setProgress(mediaPlayer.getCurrentPosition());
        time.setText(General.getStrTime(mediaPlayer.getCurrentPosition()));
        if (mediaPlayer.isPlaying()) {
            Log.e("Esto","startPlayProgressUpdater IF");
            Runnable notification = new Runnable() {
                public void run() {
                    startPlayProgressUpdater(groupPosition,childPosition);
                }
            };
            seekHandler.postDelayed(notification,1000);
        }else{
            Log.e("Esto", "startPlayProgressUpdater Else");
            if(status!=PAUSE) {
                status = STOP;
                mediaPlayer.reset();
                lastPosGroup = -1;
                lastPosChild = -1;
                //mediaPlayer.pause();
                icon.setBackgroundResource(R.drawable.player_play);
                audio.setTextColor(Color.rgb(120, 120, 120));
            }
        }
    }

    public void startPlay( int groupPosition, int childPosition, final Integer status){
        if(status==ON || status ==PAUSE){
            Log.e("Esto","startPlay IF");
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
        Log.e("Esto","startPlay");
        try {
            File f = new File(interview_path + "/Audio/" + audioUnits.get(groupPosition).audio.get(childPosition));
            mediaPlayer.setDataSource(interview_path + "/Audio/" + audioUnits.get(groupPosition).audio.get(childPosition));
            mediaPlayer.prepare();
            mediaPlayer.start();
            seekBar.setProgress(0);
            seekBar.setMax(mediaPlayer.getDuration());
            startPlayProgressUpdater(groupPosition,childPosition);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    public static   void deleteAudio(){
        if(groupPosToDel==lastPosGroup&&childPosToDel==lastPosChild){
            mediaPlayer.stop();
            mediaPlayer.reset();
            status=STOP;
        }
        File f  = new File(interview_path+"/Audio/audio.xml");
        Document doc = null;
        if(f.exists()) {
            try {
                doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(f);
                NodeList nodeList = doc.getElementsByTagName("question");
                for(int i =0;i<nodeList.getLength();i++){
                    if(nodeList.item(i).getAttributes().getNamedItem("name").getTextContent().
                            equals(audioUnits.get(groupPosToDel).question)){
                        NodeList nodeList2 = nodeList.item(i).getChildNodes();
                        for(int y=0;y<nodeList2.getLength();y++){
                            if(nodeList2.item(y).getAttributes().getNamedItem("name").getTextContent().
                                    equals(audioUnits.get(groupPosToDel).audio.get(childPosToDel))){
                                nodeList.item(i).removeChild(nodeList2.item(y));//Apagamos audio do XML
                                listAdapter.deleteAudioFromList(groupPosToDel, childPosToDel);//Apagamos audio da Lista
                                expListView.collapseGroup(groupPosToDel);//para renovar os elementos de um grupo
                                expListView.expandGroup(groupPosToDel);//para renovar os elementos de um grupo
                                break;//é muito importante ter aqui este break; se não vai apagar todos elementos até ao fim dum grupo
                            }
                        }
                    }


                }
                Transformer trans = TransformerFactory.newInstance().newTransformer();
                DOMSource xmlSource = new DOMSource(doc);
                StreamResult result = new StreamResult(interview_path+"/Audio/audio.xml");
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
