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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.museupessoa.maf.assistenteentrevistas.General;
import com.museupessoa.maf.assistenteentrevistas.InterviewActivity;
import com.museupessoa.maf.assistenteentrevistas.R;
import com.museupessoa.maf.assistenteentrevistas.adapters.RVQuestionAdapter;
import com.museupessoa.maf.assistenteentrevistas.units.QuestionUnit;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


public class Interview extends Fragment {

    private String path;
    private RecyclerView recyclerView;
    private RVQuestionAdapter adapter;
    private List<QuestionUnit> questionUnits;
    public Interview() {
        this.path = null;
    }
    ImageView recStatus;
    TextView question;
    private  Integer lastPos=-1;
    android.support.v7.widget.CardView cardView;
    int width;
    int height;
    LinearLayout.LayoutParams paramsLayout;
    public  static  ProgressBar vProgressBar;
    //public  static  ProgressBar vProgressBarOld;

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
        questionUnits = getQuestionsFromProject(path);
        if(questionUnits.size()==0){
            TextView tv = (TextView)getActivity().findViewById(R.id.IfProjectDontExists);
            tv.setText("O projeto associado não tem nenhuma pergunta");
        }

        adapter = new RVQuestionAdapter(questionUnits);
        recyclerView.setAdapter(adapter);
        final Activity activity = getActivity();
        adapter.setOnItemClickListener(new RVQuestionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
               // Log.e("LAST-POS",Integer.toString(lastPos));
               // Log.e("CURR-POS",Integer.toString(position));
                //Log.e("--------","-------------------------");
                recStatus=(ImageView)v.findViewById(R.id.RecStatus);
                question = (TextView)v.findViewById(R.id.InterviewQuestion);
                cardView = ( android.support.v7.widget.CardView)v.findViewById(R.id.InterviewQuestionsCV);
                if(lastPos==-1){
                    vProgressBar = (ProgressBar)v.findViewById(R.id.vprogressbar);
                    lastPos=position;
                    cardView.setCardBackgroundColor(Color.rgb(209, 248, 255));
                    question.setTextColor(Color.rgb(151, 35, 26));
                    recStatus.setBackgroundResource(R.drawable.microphonepressed);
                    if (activity instanceof InterviewActivity){
                        ((InterviewActivity) activity).newAudioStartRecord(questionUnits.get(position).question);
                    }
                }
                else if(lastPos==position){
                    vProgressBar = (ProgressBar)v.findViewById(R.id.vprogressbar);
                    cardView.setCardBackgroundColor(Color.WHITE);
                    question.setTextColor(Color.rgb(143, 142, 141));
                    recStatus.setBackgroundResource(R.drawable.microphonedisabled);
                    vProgressBar.setProgress(0);
                    lastPos=-1;
                    if (activity instanceof InterviewActivity){
                        ((InterviewActivity) activity).newAudioStopRecord();
                    }
                }
                else {
                   /* if (activity instanceof InterviewActivity){
                        ((InterviewActivity) activity).newAudioStopRecord();
                    }
                    if (activity instanceof InterviewActivity){
                        ((InterviewActivity) activity).newAudioStartRecord(questionUnits.get(position).question);
                    }
                    cardView.setCardBackgroundColor(Color.rgb(209, 248, 255));
                    question.setTextSize(18);
                    question.setTextColor(Color.rgb(151, 35, 26));
                    recStatus.setBackgroundResource(R.drawable.microphonepressed);

                    ViewGroup v1 = (ViewGroup)recyclerView.getChildAt(lastPos);
                    recStatus=(ImageView)v1.findViewById(R.id.RecStatus);
                    question = (TextView)v1.findViewById(R.id.InterviewQuestion);
                    cardView = ( android.support.v7.widget.CardView)v1.findViewById(R.id.InterviewQuestionsCV);
                    cardView.setCardBackgroundColor(Color.WHITE);
                    question.setTextSize(16);
                    vProgressBarOld = (ProgressBar)v1.findViewById(R.id.vprogressbar);
                    question.setTextColor(Color.rgb(143, 142, 141));
                    vProgressBarOld.setProgress(0);
                    recStatus.setBackgroundResource(R.drawable.microphonedisabled);

                    lastPos=position;*/
                }

            }
        });
    }

    public List<QuestionUnit> getQuestionsFromProject(String PATH){
        List<QuestionUnit> questionUnits = new ArrayList<QuestionUnit>();
        File manifest  = new File(PATH, "/manifesto.xml");
        if(manifest.exists()){
            Document doc = null;
            try {
                doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(manifest);
                NodeList meta = doc.getElementsByTagName("meta");
                String projectName = meta.item(0).getAttributes().getNamedItem("project").getTextContent();
                File project =  new File(PATH+"/"+projectName+".xml");
                if(!project.exists())return questionUnits;
                doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(project);
                NodeList questions = doc.getElementsByTagName("p");
                for(int i=0;i<questions.getLength();i++){
                    if(questions.item(i).hasAttributes()){
                        String metafieldData = getMetaInfoByName(questions.item(i).getAttributes().
                                getNamedItem("metafield").getTextContent(),PATH);
                        int op = Integer.parseInt(questions.item(i).getAttributes().
                                getNamedItem("op").getTextContent());
                        switch (op){
                            case 1:
                                if(Integer.parseInt(metafieldData)==Integer.parseInt(
                                        questions.item(i).getAttributes().
                                                getNamedItem("value").getTextContent())){
                                    questionUnits.add(new QuestionUnit(questions.item(i).getTextContent()));
                                }
                                break;
                            case 2:
                                if(Integer.parseInt(metafieldData)!=Integer.parseInt(
                                        questions.item(i).getAttributes().
                                                getNamedItem("value").getTextContent())){
                                    questionUnits.add(new QuestionUnit(questions.item(i).getTextContent()));
                                }
                                break;
                            case 3:
                                if(Integer.parseInt(metafieldData)<Integer.parseInt(
                                        questions.item(i).getAttributes().
                                                getNamedItem("value").getTextContent())){
                                    questionUnits.add(new QuestionUnit(questions.item(i).getTextContent()));
                                }
                                break;
                            case 4:
                                if(Integer.parseInt(metafieldData)>Integer.parseInt(
                                        questions.item(i).getAttributes().
                                                getNamedItem("value").getTextContent())){
                                    questionUnits.add(new QuestionUnit(questions.item(i).getTextContent()));
                                }
                                break;
                            case 5:
                                if(Integer.parseInt(metafieldData)<=Integer.parseInt(
                                        questions.item(i).getAttributes().
                                                getNamedItem("value").getTextContent())){
                                    questionUnits.add(new QuestionUnit(questions.item(i).getTextContent()));
                                }
                                break;
                            case 6:
                                if(Integer.parseInt(metafieldData)>=Integer.parseInt(
                                        questions.item(i).getAttributes().
                                                getNamedItem("value").getTextContent())){
                                    questionUnits.add(new QuestionUnit(questions.item(i).getTextContent()));
                                }
                                break;
                            case 7:
                                Pattern p = Pattern.compile(questions.item(i).getAttributes().
                                        getNamedItem("value").getTextContent());
                                Matcher m = p.matcher(metafieldData);
                                if(m.find()){
                                    questionUnits.add(new QuestionUnit(questions.item(i).getTextContent()));
                                }

                                break;

                        }
                    }else questionUnits.add(new QuestionUnit(questions.item(i).getTextContent()));
                }
                File projectN =  new File(General.PATH+"/Projetos/"+projectName+".xml");
                if(projectN.exists()){
                    if(!General.getTimeOfProject(PATH, projectName).equals(General.getTimeOfProject(General.PATH+"/Projetos",projectName))){
                        doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(projectN);
                        NodeList questions2 = doc.getElementsByTagName("p");
                        for(int i=0;i<questions2.getLength();i++){
                            if(!existsString(questions2.item(i).getTextContent(),questionUnits)){
                                questionUnits.add(new QuestionUnit(questions2.item(i).getTextContent()));
                            }
                        }
                    }
                }

            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }

        }
        return questionUnits;
    }

    public String getMetaInfoByName(String name, String PATH){
        String out = new String();
        File manifest  = new File(PATH, "/manifesto.xml");
        if(manifest.exists()){
            Document doc = null;
            try {
                doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(manifest);
                NodeList meta = doc.getElementsByTagName("info");
                for(int i=0;i<meta.getLength();i++){
                    if(meta.item(i).getAttributes().getNamedItem("name").getTextContent().equals(name)){
                        return meta.item(i).getTextContent();
                    }
                }

            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }

        }
        return out;
    }


    public boolean existsString(String str, List<QuestionUnit> questionUnits){
        for (int i=0;i<questionUnits.size();i++){
            if(questionUnits.get(i).question.equals(str))return true;
        }
        return  false;
    }

    public void createNewQuestion(){
        if(writeQuestionToXML("Pergunta Rapida" + Integer.toString(questionUnits.size()))){
            questionUnits.add(new QuestionUnit("Pergunta Rapida" + Integer.toString(questionUnits.size())));
            adapter.updateRVQuestionAdapter(questionUnits);
            recyclerView.setAdapter(adapter);
        }
    }

    private boolean writeQuestionToXML(String question){
        String projectName = General.getProjectNameOfInterview(path);
        File f = new File(path+"/"+projectName+".xml");
        if(f.exists()){
            try {
                Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(f);
                NodeList perguntas = doc.getElementsByTagName("perguntas");
                org.w3c.dom.Element n1;
                n1 = doc.createElement("p");
                n1.setTextContent(question);
                perguntas.item(0).appendChild(n1);
                Transformer trans = TransformerFactory.newInstance().newTransformer();
                DOMSource xmlSource = new DOMSource(doc);
                StreamResult result = new StreamResult(path+"/"+projectName+".xml");
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
        }
        return  false;
    }
}
