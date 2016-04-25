package com.museupessoa.maf.assistenteentrevistas;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.Toast;


import com.museupessoa.maf.assistenteentrevistas.Fragments.SelectProject;
import com.museupessoa.maf.assistenteentrevistas.adapters.MainActivityPagerAdapter;
import com.museupessoa.maf.assistenteentrevistas.newinterview.NewInterviewPagerAdapter;
import com.museupessoa.maf.assistenteentrevistas.tabs.SlidingTabLayout;


public class NewInterviewActivity extends AppCompatActivity {
    private String projects_path;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_new_interview_select_project);
        //final Intent intent = getIntent();
        projects_path = Environment.getExternalStoragePublicDirectory("/" + getResources().getString(R.string.APP_NAME)).toString();


        FragmentManager fragmentActionManager =  getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentActionManager.beginTransaction();
        SelectProject selectProject = new SelectProject();
        Bundle bundle = new Bundle();
        bundle.putString("path", projects_path);
        selectProject.setArguments(bundle);
        fragmentTransaction.add(R.id.selectable_projects_list, selectProject);
        fragmentTransaction.commit();
    }

    public void editInterviewMetadata(String projectName){
        if(projectName != null){
            Toast.makeText(this, "Projeto selecionado: "+ projectName, Toast.LENGTH_SHORT).show();

            //iniciar SlidingTabs para preencher metadata
            setContentView(R.layout.activity_new_interview);


            CharSequence Titles[]={"Escrita","Audio","Foto"};
            int Numboftabs = 3;
            NewInterviewPagerAdapter myPagerAdapter = new NewInterviewPagerAdapter(getSupportFragmentManager(), Titles, Numboftabs);
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
