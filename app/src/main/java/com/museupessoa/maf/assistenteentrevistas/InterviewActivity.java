package com.museupessoa.maf.assistenteentrevistas;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.museupessoa.maf.assistenteentrevistas.Fragments.Interview;
import com.museupessoa.maf.assistenteentrevistas.auxiliary.Zip;
import com.museupessoa.maf.assistenteentrevistas.dialogs.DeleteSrcLinkDialogFragment;
import com.museupessoa.maf.assistenteentrevistas.editInterviewPersonForm.EditPersonInfoPagerAdapter;
import com.museupessoa.maf.assistenteentrevistas.tabs.SlidingTabLayout;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
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
    private String person_name;
    private final int CAMERA_RESULT = 0;
    SimpleDateFormat sdf;
    private String fotoName;
    DisplayMetrics metricsB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interview);
        final Intent intent = getIntent();
        interview_path = intent.getStringExtra("path");
        String nome = getPersonNameFromXML();
        Button name = (Button)findViewById(R.id.person_name);
        name.setText(nome);

        metricsB = getResources().getDisplayMetrics();
        sdf = new SimpleDateFormat("ddMMyy_HHmmss");

        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //iniciar SlidingTabs para preencher metadata
                setContentView(R.layout.fragment_interview_metadata);


                CharSequence Titles[]={"Aplicação","Audio","Foto"};
                int Numboftabs = 3;
                EditPersonInfoPagerAdapter myPagerAdapter = new EditPersonInfoPagerAdapter(getSupportFragmentManager(), Titles, Numboftabs, interview_path);
                ViewPager pager = (ViewPager) findViewById(R.id.new_interview_pager);
                pager.setAdapter(myPagerAdapter);
                SlidingTabLayout tabs = (SlidingTabLayout) findViewById(R.id.new_interview_tabs);
                tabs.setDistributeEvenly(true);

                tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
                    @Override
                    public int getIndicatorColor(int position) {
                        return getResources().getColor(R.color.tabsCor);
                    }

                });
                tabs.setViewPager(pager);
            }
        });


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



    private String getPersonNameFromXML(){
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
        return nome;
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
                if (isChecked) {
                    rec_time.setBase(SystemClock.elapsedRealtime());
                    rec_time.start();
                    startRecording(interview_path);
                    audio_file_name = "gravacao_" + conta_gravacoes;
                    createAudioMetaInfo("gravacao_" + conta_gravacoes);
                } else {
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
                fotoName = interview_path + "/Fotos/"+sdf.format(new Date())+".jpg";
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(fotoName)));
                startActivityForResult(cameraIntent, CAMERA_RESULT);
            }
        });
    }

    private void startRecording(String path) {
        conta_gravacoes+=1;
        String mFileName = path +"/Audio/gravacao_"+ conta_gravacoes +".mp4";
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mRecorder.setAudioEncodingBitRate(32);
        mRecorder.setAudioSamplingRate(44100);
        mRecorder.setAudioChannels(2);
        mRecorder.setOutputFile(mFileName);
        try {
            mRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_RESULT) {
            try{
                Pic(fotoName);
            }
            catch (Exception e ){
                Toast.makeText(this,"Tenta outra vez", Toast.LENGTH_LONG).show();
            }

        }

    }

    private void Pic(String PATH) {
        int targetW = (int)metricsB.widthPixels;
        int targetH =(int) metricsB.heightPixels;
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(PATH, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;
        BitmapFactory.decodeFile(PATH, bmOptions);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_interview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.Zip:
                    Zip.zip(interview_path, General.PATH + "/Zips",
                            interview_path.substring(interview_path.lastIndexOf("/")+1,interview_path.length())+".zip",true);
                    Toast.makeText(this,"O arquivo foi criado",Toast.LENGTH_LONG).show();
                return true;
    }
        return(super.onOptionsItemSelected(item));
    }
}
