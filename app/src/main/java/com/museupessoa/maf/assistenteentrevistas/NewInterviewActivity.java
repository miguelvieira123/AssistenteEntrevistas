package com.museupessoa.maf.assistenteentrevistas;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;


import com.museupessoa.maf.assistenteentrevistas.Fragments.SelectProject;
import com.museupessoa.maf.assistenteentrevistas.auxiliary.UploadingFileToServer;
import com.museupessoa.maf.assistenteentrevistas.auxiliary.Zip;
import com.museupessoa.maf.assistenteentrevistas.dialogs.ConfirmFinishFormDialogFragment;
import com.museupessoa.maf.assistenteentrevistas.dialogs.NewProjectAddDialogFragment;
import com.museupessoa.maf.assistenteentrevistas.editInterviewPersonForm.AudioForm;
import com.museupessoa.maf.assistenteentrevistas.editInterviewPersonForm.EditPersonInfoPagerAdapter;
import com.museupessoa.maf.assistenteentrevistas.editInterviewPersonForm.PhotoForm;
import com.museupessoa.maf.assistenteentrevistas.editInterviewPersonForm.WrittenForm;
import com.museupessoa.maf.assistenteentrevistas.tabs.SlidingTabLayout;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


public class NewInterviewActivity extends AppCompatActivity {
    private String projects_path;
    private String selected_project;
    private String person_name;
    private String new_interview_path;
    public static Bitmap bitmapFormPhoto;
    public int status=0;
    private HashMap<String,EditText> allViews = new HashMap<String,EditText>();

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("myProject", selected_project);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(General.CR, General.CG, General.CB)));
        getSupportActionBar().setTitle("Nova Entrevista");
        if (savedInstanceState == null){
            Intent intent = getIntent();
            person_name = intent.getStringExtra("person_name");
            setContentView(R.layout.activity_new_interview);
            projects_path = Environment.getExternalStoragePublicDirectory("/" + getResources().getString(R.string.APP_NAME)).toString();
            FragmentManager fragmentActionManager =  getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentActionManager.beginTransaction();
            SelectProject selectProject = new SelectProject();
            Bundle bundle = new Bundle();
            bundle.putString("path", projects_path);
            selectProject.setArguments(bundle);
            fragmentTransaction.add(R.id.new_interview_frameLayout, selectProject);
            fragmentTransaction.commit();
        }else{
            setContentView(R.layout.fragment_interview_metadata);
            CharSequence Titles[]={"Texto","Audio","Foto"};
            int Numboftabs = 3;
            EditPersonInfoPagerAdapter myPagerAdapter = new EditPersonInfoPagerAdapter(getSupportFragmentManager(), Titles, Numboftabs, new_interview_path, selected_project);
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

            selected_project = savedInstanceState.getString("myProject");

        }
    }


    public void editInterviewMetadata(String projectName){
        if(projectName != null){
            status=1;
            Toast.makeText(this, "Projeto selecionado: " + projectName, Toast.LENGTH_SHORT).show();
            this.selected_project = projectName;


            //CRIAR ENTREVISTA
            String new_interview_folder = General.createNewInterview(projects_path, projectName, person_name);
            new_interview_path = Environment.getExternalStoragePublicDirectory("/" + getResources().getString(R.string.APP_NAME)+"/Entrevistas/"+new_interview_folder).toString();

            //Copiar Projeto para a pasta da entervista
            copyProject(projectName,new_interview_path);

            //iniciar SlidingTabs para preencher metadata
            setContentView(R.layout.fragment_interview_metadata);
            CharSequence Titles[]={"Texto","Audio","Foto"};
            int Numboftabs = 3;
            EditPersonInfoPagerAdapter myPagerAdapter = new EditPersonInfoPagerAdapter(getSupportFragmentManager(), Titles, Numboftabs, new_interview_path, selected_project);
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
        }else{
            Toast.makeText(this, "Não tem nenhum projeto selecionado.", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_new_interview, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.E_Accept:
                if(status==1) {
                    if(AudioForm.REC==1) AudioForm.stopRecording();
                    FragmentManager addInterview = getFragmentManager();
                    ConfirmFinishFormDialogFragment dialogProjectName = new ConfirmFinishFormDialogFragment();
                    dialogProjectName.show(addInterview, "AcceptNewInterview");
                }
        }
        return(super.onOptionsItemSelected(item));
    }



    @Override
    public void onBackPressed() {
    }


    public void okClicked() {
        NewInterviewActivity.bitmapFormPhoto=null;
        this.finish();
        Intent intent = new Intent(this, InterviewActivity.class);
        intent.putExtra("path",new_interview_path);
        getFragmentManager().popBackStack();
        startActivity(intent);
    }

    public void cancelClicked() {
        this.finish();
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
            List<Fragment> fragments =  getSupportFragmentManager().getFragments();
            if (fragments != null) {
                for (Fragment fragment : fragments) {
                    if(fragment instanceof PhotoForm) {
                        fragment.onActivityResult(requestCode, resultCode, data);
                    }
                }
            }
    }

    public boolean copyProject(String projectName, String newPath){
        File f  = new File(General.PATH+"/Projetos/"+projectName+".xml");
        Log.e("S1",General.PATH+"/Projetos/"+projectName+".xml");
        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(f);
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            DOMSource domSource = new DOMSource(doc);
            StreamResult streamResult = new StreamResult(newPath+"/"+projectName+".xml");
            Log.e("S2", newPath + "/" + projectName+".xml");
            transformer.transform(domSource,streamResult);
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
        return  false;
    }
}
