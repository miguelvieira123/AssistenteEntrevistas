package com.museupessoa.maf.assistenteentrevistas;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaRecorder;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.NetworkOnMainThreadException;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;
import com.museupessoa.maf.assistenteentrevistas.Fragments.Interview;
import com.museupessoa.maf.assistenteentrevistas.Fragments.Interviews;
import com.museupessoa.maf.assistenteentrevistas.auxiliary.UploadingFileToServer;
import com.museupessoa.maf.assistenteentrevistas.auxiliary.Zip;
import com.museupessoa.maf.assistenteentrevistas.dialogs.ChooseProjectDialogFragment;
import com.museupessoa.maf.assistenteentrevistas.dialogs.DeleteInterviewDialogFragment;
import com.museupessoa.maf.assistenteentrevistas.dialogs.FinishEnterviewDialogFragment;
import com.museupessoa.maf.assistenteentrevistas.dialogs.LinkChoiseForSendDialogFragment;
import com.museupessoa.maf.assistenteentrevistas.editInterviewPersonForm.EditPersonInfoPagerAdapter;
import com.museupessoa.maf.assistenteentrevistas.editInterviewPersonForm.PhotoForm;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private static String interview_path;
    private String audio_file_name;
    private String person_name;
    private final int CAMERA_RESULT = 89;
    private SimpleDateFormat sdf;
    private String fullFotoName;
    private String fotoName;
    DisplayMetrics metricsB;
    private static Integer REC_STATUS=0;
    private  static boolean SEND_STATUS=false;
    Handler seekHandler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interview);
        final Intent intent = getIntent();
        interview_path = intent.getStringExtra("path");
        String nome = getPersonNameFromXML();
        TextView name = (TextView)findViewById(R.id.person_name);
        name.setText(nome);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(General.CR, General.CG, General.CB)));
        metricsB = getResources().getDisplayMetrics();
        sdf = new SimpleDateFormat("ddMMyy_HHmmss");
        createAudioXML(interview_path);
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

        this.addListennerToButtons();

    }
    private void addListennerToButtons(){

        FloatingActionButton photo = (FloatingActionButton) findViewById(R.id.photo_button);
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fotoName = sdf.format(new Date()) + ".jpg";
                fullFotoName = interview_path + "/Fotos/" + fotoName;
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(fullFotoName)));
                startActivityForResult(cameraIntent, CAMERA_RESULT);
            }
        });
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

    private void createAudioXML(String path){
        File audio = new File(path + "/Audio/audio.xml" );
        if(!audio.exists()){
            Document doc = null;
            try {
                doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
                org.w3c.dom.Element root;
                root = doc.createElement("root");
                doc.appendChild(root);
                Transformer trans = TransformerFactory.newInstance().newTransformer();
                DOMSource xmlSource = new DOMSource(doc);
                StreamResult result = new StreamResult(path + "/Audio/audio.xml");
                trans.transform(xmlSource, result);
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (TransformerConfigurationException e) {
                e.printStackTrace();
            } catch (TransformerException e) {
                e.printStackTrace();
            }

        }
    }
    private boolean writeAudioToXML(String question, String path, String audioName){
        File audio = new File(path + "/Audio/audio.xml" );
        Document doc = null;
        if(!audio.exists()) {
            createAudioXML(path);
        }
            try {
                doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(audio);
                NodeList root =doc.getElementsByTagName("root");
                NodeList questionList = doc.getElementsByTagName("question");
                org.w3c.dom.Element n1;
                org.w3c.dom.Element n2;
                if(questionList.getLength()==0){
                    n1 = doc.createElement("question");
                    n1.setAttribute("name", question );
                    n2 = doc.createElement("audio");
                    n2.setAttribute("name", audioName);
                    n1.appendChild(n2);
                    root.item(0).appendChild(n1);
                }else{
                    for(int i =0; i<questionList.getLength();i++){
                        if(questionList.item(i).getAttributes().getNamedItem("name").getTextContent().equals(question)){
                            n2  = doc.createElement("audio");
                            n2.setAttribute("name", audioName);
                            questionList.item(i).appendChild(n2);
                            Transformer trans = TransformerFactory.newInstance().newTransformer();
                            DOMSource xmlSource = new DOMSource(doc);
                            StreamResult result = new StreamResult(path + "/Audio/audio.xml");
                            trans.transform(xmlSource, result);
                            return true;
                        }
                    }
                    n1 = doc.createElement("question");
                    n1.setAttribute("name", question );
                    n2 = doc.createElement("audio");
                    n2.setAttribute("name", audioName);
                    n1.appendChild(n2);
                    root.item(0).appendChild(n1);
                }
                Transformer trans = TransformerFactory.newInstance().newTransformer();
                DOMSource xmlSource = new DOMSource(doc);
                StreamResult result = new StreamResult(path + "/Audio/audio.xml");
                trans.transform(xmlSource, result);
                return true;
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

        return false;
    }


    public void newAudioStartRecord(String question){
        if(REC_STATUS==0) {
            rec_time.setBase(SystemClock.elapsedRealtime());
            rec_time.start();
            startRecording(interview_path);
            writeAudioToXML(question, interview_path, Integer.toString(conta_gravacoes) + ".mp4");
            startPlayProgressUpdater();
            REC_STATUS=1;
        }
    }
    public void newAudioStopRecord() {
        if(REC_STATUS==1) {
            REC_STATUS = 0;
            rec_time.stop();
            stopRecording();
            setContadorXML(interview_path, conta_gravacoes);
        }
    }

    private void startRecording(String path) {
        REC_STATUS=1;
        conta_gravacoes+=1;
        String mFileName = path +"/Audio/"+ conta_gravacoes +".mp4";
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
    public void stopRecording() {
        REC_STATUS=0;
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
                Pic(fullFotoName);
            }
            catch (Exception e ){
                Toast.makeText(this,"Tenta outra vez", Toast.LENGTH_LONG).show();
            }
        }else{
            List<Fragment> fragments =  getSupportFragmentManager().getFragments();
            if (fragments != null) {
                for (Fragment fragment : fragments) {
                    if(fragment instanceof PhotoForm)
                    {
                        fragment.onActivityResult(requestCode, resultCode, data);
                    }
                }
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
        File f  = new File(interview_path, "/manifesto.xml");
        if(f.exists()){
            Document doc = null;
            try {
                doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(f);
                NodeList photos = doc.getElementsByTagName("photos");
                org.w3c.dom.Element photo = doc.createElement("photo");
                photo.setAttribute("name", fotoName);
                photo.setAttribute("time", rec_time.getText().toString());
                photo.setAttribute("audio",conta_gravacoes+".mp3");
                if(photos.getLength()==0){
                    NodeList root = doc.getElementsByTagName("manifesto");
                    org.w3c.dom.Element photosN = doc.createElement("photos");
                    photosN.appendChild(photo);
                    root.item(0).appendChild(photosN);
                }else{
                    photos.item(0).appendChild(photo);
                }

                Transformer trans = TransformerFactory.newInstance().newTransformer();
                DOMSource xmlSource = new DOMSource(doc);
                StreamResult result = new StreamResult(interview_path + "/manifesto.xml");
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_interview, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.E_Editar:
                newAudioStopRecord();
                setContentView(R.layout.fragment_interview_metadata);

                CharSequence Titles[] = {"Texto", "Audio", "Foto"};
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
                return true;
            case R.id.E_Enviar:
                WifiManager wm = (WifiManager)getSystemService(Context.WIFI_SERVICE);
                if(wm.getWifiState()!=3){
                    Toast.makeText(this, "Se faz favor, liga o WiFi", Toast.LENGTH_LONG).show();
                    break;
                }
                newAudioStopRecord();
                FragmentManager fm = getFragmentManager();
                LinkChoiseForSendDialogFragment dialog = new LinkChoiseForSendDialogFragment();
                dialog.show(fm,"LinkChoiseForSendEnterview");
                return true;
            case R.id.E_Eliminar:
                FragmentManager addProject = getFragmentManager();
                DeleteInterviewDialogFragment dialogProjectName = new DeleteInterviewDialogFragment();
                dialogProjectName.show(addProject, "DeleteInterview");
                return true;
            case R.id.E_Listen:
                newAudioStopRecord();
                Intent intent = new Intent(this, ListenAudio.class);
                intent.putExtra("path",interview_path);
                startActivity(intent);
                return  true;
            case 16908332: //Aqui nós tratamos  android:parentActivityName(Este atributo está no Manifesto)
                FragmentManager finishE  = getFragmentManager();
                FinishEnterviewDialogFragment finE = new FinishEnterviewDialogFragment();
                finE.show(finishE,"FinishEntreview");
                return true;
            case R.id.E_Project:
                FragmentManager fmP = getFragmentManager();
                ChooseProjectDialogFragment project = new ChooseProjectDialogFragment();
                project.show(fmP,"ChooseProjectDialogFragment");
                return true;
            default:

                break;
    }
        return(super.onOptionsItemSelected(item));
    }

    public static List<String> getLinks(String PATH){
        File f  = new File(PATH, "/manifesto.xml");
        List <String> list = new ArrayList<String>();
        if(f.exists()){
            Document doc = null;
            try {
                doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(f);
                NodeList links = doc.getElementsByTagName("url");
                for (int i=0;i<links.getLength();i++){
                    list.add(links.item(i).getTextContent());
                }

            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }

        }
        return  list;
    }

    public void okClicked() {
        newAudioStopRecord();
        General.deleteDirectory(new File(interview_path));
        this.finish();
    }
    public void finishEntr() {
        newAudioStopRecord();
        this.finish();
    }

    public void cancelClicked() {
    }

    @Override
    public void onBackPressed() {

    }

    public void startPlayProgressUpdater() {
        if (REC_STATUS==1) {
           int ampl = mRecorder.getMaxAmplitude();
           int id=0;
           if(ampl>=0&&ampl<1000)id=R.drawable.amp1;
           else if(ampl>=1000&&ampl<2000)id=R.drawable.amp2;
           else if(ampl>=2000&&ampl<3000)id=R.drawable.amp3;
           else if(ampl>=3000&&ampl<4000)id=R.drawable.amp4;
           else if(ampl>=4000&&ampl<5000)id=R.drawable.amp5;
           else if(ampl>=5000&&ampl<6000)id=R.drawable.amp6;
           else if(ampl>=6000&&ampl<7000)id=R.drawable.amp7;
           else if(ampl>=7000&&ampl<8000)id=R.drawable.amp8;
           else if(ampl>=8000&&ampl<9000)id=R.drawable.amp9;
           else if(ampl>=9000&&ampl<10000)id=R.drawable.amp10;
           else if(ampl>=10000&&ampl<11000)id=R.drawable.amp11;
           else if(ampl>=11000&&ampl<12000)id=R.drawable.amp12;
           else if(ampl>=12000&&ampl<13000)id=R.drawable.amp13;
           else if(ampl>=13000&&ampl<14000)id=R.drawable.amp14;
           else if(ampl>=14000&&ampl<15000)id=R.drawable.amp15;
           else if(ampl>=15000&&ampl<16000)id=R.drawable.amp16;
           else if(ampl>=16000&&ampl<17000)id=R.drawable.amp17;
           else if(ampl>=17000&&ampl<18000)id=R.drawable.amp18;
           else if(ampl>=18000&&ampl<19000)id=R.drawable.amp19;
           else if(ampl>=19000&&ampl<20000)id=R.drawable.amp20;
           else if(ampl>=20000&&ampl<21000)id=R.drawable.amp21;
           else if(ampl>=21000&&ampl<22000)id=R.drawable.amp22;
           else if(ampl>=22000&&ampl<23000)id=R.drawable.amp23;
           else if(ampl>=23000&&ampl<24000)id=R.drawable.amp24;
           else if(ampl>=24000&&ampl<25000)id=R.drawable.amp25;
           else if(ampl>=25000&&ampl<26000)id=R.drawable.amp26;
           else if(ampl>=26000&&ampl<27000)id=R.drawable.amp27;
           else if(ampl>=27000&&ampl<28000)id=R.drawable.amp28;
           else if(ampl>=28000&&ampl<29000)id=R.drawable.amp29;
           else if(ampl>=29000&&ampl<30000)id=R.drawable.amp30;
           else if(ampl>=30000&&ampl<31000)id=R.drawable.amp31;
           else if(ampl>=31000)id=R.drawable.amp32;
            Interview.recAmpl.setBackgroundResource(id);

            Runnable notification = new Runnable() {
                public void run() {
                    startPlayProgressUpdater();
                }
            };
            seekHandler.postDelayed(notification,1000);
        }
    }

    public  static void sendEnterview(String link, int id){
        final List<String> links = new ArrayList<String>();
        switch (id){
            case R.id.LinksOfProject:
                send(getLinks(interview_path));
                break;
            case R.id.LinkNew:
               links.add(link);
                send(links);
                break;
        }

    }

    public static void send(final List<String> links){
        final String arq =  General.PATH + "/Zips/"+interview_path.substring(interview_path.lastIndexOf("/")+1,interview_path.length())+".zip";
        Zip.zip(interview_path, General.PATH + "/Zips",
                interview_path.substring(interview_path.lastIndexOf("/") + 1, interview_path.length()) + ".zip", true);

        Thread thrd = new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i=0;i< links.size();i++){
                    if(UploadingFileToServer.uploadingFileToTheServer(arq, links.get(i))) {
                        Interviews.setInterviewStatusSentByPath(interview_path);
                        SEND_STATUS=true;
                    }
                }
                new File(arq).delete();
            }
        });
        thrd.start();
        try {
            thrd.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(SEND_STATUS){
            General.setInterviewStatusSent(interview_path,true);
        }
    }

    public  static void changeProjectForInterview(String project){
        File f  = new File(interview_path+"/manifesto.xml");
        Document doc = null;
        if(f.exists()) {
            try {
                doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(f);
                NodeList nodeList = doc.getElementsByTagName("meta");
                nodeList.item(0).getAttributes().getNamedItem("project").setTextContent(project);
                Transformer trans = TransformerFactory.newInstance().newTransformer();
                DOMSource xmlSourse  =  new DOMSource(doc);
                StreamResult streamResult = new StreamResult(interview_path+"/manifesto.xml");
                trans.transform(xmlSourse,streamResult);
                
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
