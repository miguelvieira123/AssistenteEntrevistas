package com.museupessoa.maf.assistenteentrevistas;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaRecorder;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ActionMenuView;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.melnykov.fab.FloatingActionButton;
import com.museupessoa.maf.assistenteentrevistas.Fragments.Interview;
import com.museupessoa.maf.assistenteentrevistas.auxiliary.UploadingFileToServer;
import com.museupessoa.maf.assistenteentrevistas.auxiliary.Zip;
import com.museupessoa.maf.assistenteentrevistas.dialogs.ConfirmFinishFormDialogFragment;
import com.museupessoa.maf.assistenteentrevistas.dialogs.DeleteInterviewDialogFragment;
import com.museupessoa.maf.assistenteentrevistas.dialogs.DeleteSrcLinkDialogFragment;
import com.museupessoa.maf.assistenteentrevistas.editInterviewPersonForm.EditPersonInfoPagerAdapter;
import com.museupessoa.maf.assistenteentrevistas.editInterviewPersonForm.PhotoForm;
import com.museupessoa.maf.assistenteentrevistas.tabs.SlidingTabLayout;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Time;
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
    private String interview_path;
    private String audio_file_name;
    private String person_name;
    private final int CAMERA_RESULT = 89;
    private SimpleDateFormat sdf;
    private String fullFotoName;
    private String fotoName;
    DisplayMetrics metricsB;


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


        /*
        LinearLayout LQList = (LinearLayout) findViewById(R.id.interview_activity_questions_list);

        FrameLayout.LayoutParams paramsLayout = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                (metricsB.heightPixels-(int)(metricsB.heightPixels*0.47))
        );

        LQList.setLayoutParams(paramsLayout);
        */
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
                if(audio_file_name==null) photo.setAttribute("audio","");
                else photo.setAttribute("audio",audio_file_name+".mp3");
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
                    final String arq =  General.PATH + "/Zips/"+interview_path.substring(interview_path.lastIndexOf("/")+1,interview_path.length())+".zip";
                    Zip.zip(interview_path, General.PATH + "/Zips",
                            interview_path.substring(interview_path.lastIndexOf("/")+1,interview_path.length())+".zip",true);
                    Thread thrd = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        List <String> links = getLinks(interview_path);
                        for(int i=0;i<links.size();i++){
                            UploadingFileToServer.uploadingFileToTheServer(arq, links.get(i));
                        }
                        new File(arq).delete();
                    }
                    });
                    thrd.start();
                    Toast.makeText(this,"O arquivo foi criado",Toast.LENGTH_LONG).show();
                return true;
            case R.id.E_Eliminar:
                FragmentManager addProject = getFragmentManager();
                DeleteInterviewDialogFragment dialogProjectName = new DeleteInterviewDialogFragment();
                dialogProjectName.show(addProject, "DeleteInterview");
                return true;
            case R.id.E_Accept:
                //Toast.makeText(this,"accepted",Toast.LENGTH_LONG).show();
                break;
    }
        return(super.onOptionsItemSelected(item));
    }

    public List<String> getLinks(String PATH){
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
        General.deleteDirectory(new File(interview_path));
        this.finish();
    }

    public void cancelClicked() {

    }

    @Override
    public void onBackPressed() {
        this.finish();
    }

}
