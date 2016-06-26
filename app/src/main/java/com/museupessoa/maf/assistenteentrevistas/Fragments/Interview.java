package com.museupessoa.maf.assistenteentrevistas.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.museupessoa.maf.assistenteentrevistas.General;
import com.museupessoa.maf.assistenteentrevistas.InterviewActivity;
import com.museupessoa.maf.assistenteentrevistas.R;
import com.museupessoa.maf.assistenteentrevistas.adapters.RVQuestionAdapter;
import com.museupessoa.maf.assistenteentrevistas.units.QuestionUnit;

import java.util.List;


public class Interview extends Fragment {

    private String path;
    private RecyclerView recyclerView;
    private RVQuestionAdapter adapter;
    private List<QuestionUnit> questionUnits;
    public Interview() {
        this.path = null;
    }
    ImageView recStatus;
    public  static  ImageView recAmpl;
    ImageView recAmplLast;
    TextView question;
    private  Integer lastPos=-1;
    android.support.v7.widget.CardView cardView;
    int width;
    int height;
    LinearLayout.LayoutParams paramsLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.path = this.getArguments().getString("path");
        View questions = inflater.inflate(R.layout.fragment_interview_questions, container, false);
        recyclerView = (RecyclerView) questions.findViewById(R.id.recyclerView);
        return questions;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.path = this.getArguments().getString("path");
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);
        questionUnits = QuestionUnit.getQuestions(path);
        adapter = new RVQuestionAdapter(questionUnits);
        recyclerView.setAdapter(adapter);
        final Activity activity = getActivity();
        adapter.setOnItemClickListener(new RVQuestionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                recStatus=(ImageView)v.findViewById(R.id.RecStatus);
                question = (TextView)v.findViewById(R.id.InterviewQuestion);
                recAmpl = (ImageView)v.findViewById(R.id.RecAmplitude);
                cardView = ( android.support.v7.widget.CardView)v.findViewById(R.id.InterviewQuestionsCV);
                if(lastPos==-1){
                    height=cardView.getHeight();
                    width=cardView.getWidth();
                    lastPos=position;
                    cardView.setCardBackgroundColor(Color.rgb(209, 248, 255));
                    question.setTextSize(18);
                    question.setTextColor(Color.rgb(151, 35, 26));
                    recStatus.setBackgroundResource(R.drawable.microphonepressed);
                    paramsLayout = new  LinearLayout.LayoutParams(
                            width+10,
                            height+10
                    );
                    cardView.setLayoutParams(paramsLayout);
                    if (activity instanceof InterviewActivity){
                        ((InterviewActivity) activity).newAudioStartRecord(questionUnits.get(position).question);
                    }
                }
                else if(lastPos==position){
                    cardView.setCardBackgroundColor(Color.WHITE);
                    question.setTextSize(16);
                    question.setTextColor(Color.rgb(143, 142, 141));
                    recStatus.setBackgroundResource(R.drawable.microphonedisabled);
                    lastPos=-1;
                    paramsLayout = new  LinearLayout.LayoutParams(
                            width,
                            height
                    );
                    cardView.setLayoutParams(paramsLayout);
                    if (activity instanceof InterviewActivity){
                        ((InterviewActivity) activity).newAudioStopRecord();
                        recAmpl.setBackgroundResource(R.drawable.amp1);
                    }
                }
                else {
                    if (activity instanceof InterviewActivity){
                        ((InterviewActivity) activity).newAudioStopRecord();
                    }
                    if (activity instanceof InterviewActivity){
                        ((InterviewActivity) activity).newAudioStartRecord(questionUnits.get(position).question);
                    }
                    cardView.setCardBackgroundColor(Color.rgb(209, 248, 255));
                    question.setTextSize(18);
                    question.setTextColor(Color.rgb(151, 35, 26));
                    recStatus.setBackgroundResource(R.drawable.microphonepressed);
                    paramsLayout = new  LinearLayout.LayoutParams(
                            width+10,
                            height+10
                    );
                    cardView.setLayoutParams(paramsLayout);
                    ViewGroup v1 = (ViewGroup)recyclerView.getChildAt(lastPos);
                    recStatus=(ImageView)v1.findViewById(R.id.RecStatus);
                    question = (TextView)v1.findViewById(R.id.InterviewQuestion);
                    cardView = ( android.support.v7.widget.CardView)v1.findViewById(R.id.InterviewQuestionsCV);
                    cardView.setCardBackgroundColor(Color.WHITE);
                    question.setTextSize(16);
                    recAmplLast = (ImageView)v1.findViewById(R.id.RecAmplitude);
                    question.setTextColor(Color.rgb(143, 142, 141));
                    recAmplLast.setBackgroundResource(R.drawable.amp1);
                    recStatus.setBackgroundResource(R.drawable.microphonedisabled);
                    paramsLayout = new  LinearLayout.LayoutParams(
                            width,
                            height
                    );
                    cardView.setLayoutParams(paramsLayout);
                    lastPos=position;
                }

            }
        });
    }


}
