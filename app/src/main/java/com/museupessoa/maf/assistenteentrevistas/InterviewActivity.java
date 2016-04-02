package com.museupessoa.maf.assistenteentrevistas;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.museupessoa.maf.assistenteentrevistas.Interview_main.Interview;

import java.util.Timer;

public class InterviewActivity extends AppCompatActivity {

    private Chronometer rec_time;

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

        rec_time = (Chronometer) findViewById(R.id.rec_chronometer);
        //add Listenners to Buttons
        this.addListennerToButtons();
    }


    private void addListennerToButtons(){
        //start recording method
        ImageButton rec = (ImageButton) findViewById(R.id.rec_button);
        rec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"start recording",Toast.LENGTH_LONG).show();
                rec_time.start();
            }
        });
        //take photo method
        ImageButton photo = (ImageButton) findViewById(R.id.photo_button);
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"taking photo",Toast.LENGTH_LONG).show();
            }
        });
    }


}
