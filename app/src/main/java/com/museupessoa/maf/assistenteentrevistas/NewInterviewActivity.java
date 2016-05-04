package com.museupessoa.maf.assistenteentrevistas;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;


import com.museupessoa.maf.assistenteentrevistas.Fragments.SelectProject;
import com.museupessoa.maf.assistenteentrevistas.editInterviewPersonForm.EditPersonInfoPagerAdapter;
import com.museupessoa.maf.assistenteentrevistas.tabs.SlidingTabLayout;


public class NewInterviewActivity extends AppCompatActivity {
    private String projects_path;
    private String selected_project;
    private String person_name;
    private String new_interview_path;


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("myProject", selected_project);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
            CharSequence Titles[]={"Escrita","Audio","Foto"};
            int Numboftabs = 3;
            EditPersonInfoPagerAdapter myPagerAdapter = new EditPersonInfoPagerAdapter(getSupportFragmentManager(), Titles, Numboftabs, new_interview_path);
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
            //Toast.makeText(this, "nome proj.: "+ selected_project, Toast.LENGTH_SHORT).show();

        }
    }



    public void editInterviewMetadata(String projectName){
        if(projectName != null){

            Toast.makeText(this, "Projeto selecionado: " + projectName, Toast.LENGTH_SHORT).show();
            this.selected_project = projectName;


            //CRIAR ENTREVISTA
            String new_interview_folder = General.createNewInterview(projects_path, projectName, person_name);
            new_interview_path = Environment.getExternalStoragePublicDirectory("/" + getResources().getString(R.string.APP_NAME)+"/Entrevistas/"+new_interview_folder).toString();



            //iniciar SlidingTabs para preencher metadata
            setContentView(R.layout.fragment_interview_metadata);


            CharSequence Titles[]={"Escrita","Audio","Foto"};
            int Numboftabs = 3;
            EditPersonInfoPagerAdapter myPagerAdapter = new EditPersonInfoPagerAdapter(getSupportFragmentManager(), Titles, Numboftabs, new_interview_path);
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

}
