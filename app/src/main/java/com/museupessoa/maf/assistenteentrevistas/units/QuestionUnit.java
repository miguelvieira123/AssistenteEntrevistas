package com.museupessoa.maf.assistenteentrevistas.units;

import android.app.Activity;
import android.widget.Toast;

import com.museupessoa.maf.assistenteentrevistas.R;
import com.museupessoa.maf.assistenteentrevistas.adapters.RVQuestionAdapter;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


public class QuestionUnit {
    public String question;

    public QuestionUnit(String question) {
        this.question = question;
    }

    public static List<QuestionUnit> getQuestions(String path){
        String[] list;
        List<QuestionUnit> questions = new ArrayList<>();
        list = getQuestionsFromXML(path);
        for (int i=0; i<list.length; i++){
            questions.add(new QuestionUnit(list[i]));
        }
        return  questions;
    }


    private static String[] getQuestionsFromXML(String path){
        ArrayList<String> res = new ArrayList<String>();
        File f  = new File(path, "manifesto.xml");
        if(f.exists()){
            Document doc = null;
            try {
                doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(f);
                NodeList perguntas = doc.getElementsByTagName("pergunta");
                for (int i=0; i < perguntas.getLength(); i++){
                    //res.add(perguntas.item(i).getNodeValue());
                    //perguntas.item(i).getFirstChild().getNodeValue();
                    res.add(perguntas.item(i).getFirstChild().getNodeValue());
                }

            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }

        }
        String[] stockArr = new String[res.size()];
        stockArr = res.toArray(stockArr);

        return stockArr;
    }

}
