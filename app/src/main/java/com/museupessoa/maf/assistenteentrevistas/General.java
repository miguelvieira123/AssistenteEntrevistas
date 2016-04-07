package com.museupessoa.maf.assistenteentrevistas;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedWriter;
import java.io.File;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


public class General {

    public static final String TAG ="AssistenteEntrevistas";
    public static boolean createStructOfFolders(String path){
        try {
            File f = new File(path + "/Entrevistas");
            if (!f.exists())if(!f.mkdirs())return false;
            f.setWritable(true);

            f = new File(path + "/Projetos");
            if (!f.exists())if(!f.mkdirs())return false;
            f.setWritable(true);

            return true;
        } catch (Exception e1) {
            return false;
        }
    }

    public static boolean createInterview(String path, String personName, String interviewCode){
        try{
            File f = new File(path + "/Entrevistas/" + interviewCode );
            if (!f.exists())if(!f.mkdirs())return false;
            f.setWritable(true);
            f = new File(path + "/Entrevistas/" + interviewCode + "/Audio");
            if (!f.exists())if(!f.mkdirs())return false;
            f.setWritable(true);
            f = new File(path + "/Entrevistas/" + interviewCode + "/Fotos");
            if (!f.exists())if(!f.mkdirs())return false;
            f.setWritable(true);

            File outputFile = new File(path + "/Entrevistas/"+ interviewCode, "/manifesto.xml");
            if (!outputFile.exists()){
                Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
                org.w3c.dom.Element root;
                root = doc.createElement("manifesto");
                doc.appendChild(root);
                org.w3c.dom.Element n1;
                n1 = doc.createElement("meta");

                // FAKE -----
                org.w3c.dom.Element n2;
                n2 = doc.createElement("nome");
                n2.setTextContent(personName);
                n1.appendChild(n2);
                root.appendChild(n1);

                n1 = doc.createElement("perguntas");
                n2 = doc.createElement("pergunta");
                n2.setTextContent("Profissão");
                n1.appendChild(n2);
                n2 = doc.createElement("pergunta");
                n2.setTextContent("Estudos");
                n1.appendChild(n2);
                n2 = doc.createElement("pergunta");
                n2.setTextContent("Namoro");
                n1.appendChild(n2);
                n2 = doc.createElement("pergunta");
                n2.setTextContent("Literatura Favorita");
                n1.appendChild(n2);
                n2 = doc.createElement("pergunta");
                n2.setTextContent("Atividades Desportivas");
                n1.appendChild(n2);
                root.appendChild(n1);
                // FAKE -----


                n1 = doc.createElement("audio");
                n1.setAttribute("count", "0");
                root.appendChild(n1);

                n1 = doc.createElement("urls");
                n2 = doc.createElement("url");
                n1.appendChild(n2);
                root.appendChild(n1);

                Transformer trans = TransformerFactory.newInstance().newTransformer();
                DOMSource xmlSource = new DOMSource(doc);
                StreamResult result = new StreamResult(path + "/Entrevistas/" + interviewCode + "/manifesto.xml");
                trans.transform(xmlSource, result);

            }
            return true;
        } catch (Exception e) {
            //e.printStackTrace();
            return false;
        }
    }



    public static boolean createProject(String path,String projectName,List<String> info, List<String> questions, List<String> urls, int status){
        try {
            File f  = new File(path + "/Projetos/" + projectName + ".xml");
            if(!f.exists()||status==2){
                Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
                org.w3c.dom.Element root;
                root = doc.createElement("project");
                root.setAttribute("name", projectName);
                doc.appendChild(root);
                org.w3c.dom.Element n1;
                n1 = doc.createElement("meta");
                org.w3c.dom.Element child;
                for (int i=0; i<info.size() ;i++) {
                    child = doc.createElement("info");
                    child.setAttribute("id", Integer.toString(i));
                    child.setTextContent(info.get(i));
                    n1.appendChild(child);
                }
                root.appendChild(n1);
                n1 = doc.createElement("perguntas");
                for (int i=0; i<questions.size() ;i++) {
                    child = doc.createElement("p");
                    child.setTextContent(questions.get(i));
                    n1.appendChild(child);
                }
                root.appendChild(n1);
                n1 = doc.createElement("urls");
                for (int i=0; i<urls.size() ;i++) {
                    child = doc.createElement("url");
                    child.setTextContent(urls.get(i));
                    n1.appendChild(child);
                }
                root.appendChild(n1);
                Transformer trans = TransformerFactory.newInstance().newTransformer();
                DOMSource xmlSource = new DOMSource(doc);
                StreamResult result = new StreamResult(path + "/Projetos/" + projectName + ".xml");
                trans.transform(xmlSource, result);
            }
            return true;
        } catch (Exception e1) {
            return false;
        }

    }

    public static List<String> getProjectMeta(String nameProject, String PATH){
        List<String> meta = new ArrayList<String>();
        try {
            File f  = new File(PATH + "/Projetos/" + nameProject + ".xml");
            if(f.exists()) {
                DocumentBuilderFactory docBF = DocumentBuilderFactory.newInstance();
                DocumentBuilder docB = docBF.newDocumentBuilder();
                Document xmlRead = docB.parse(f);
                NodeList list = xmlRead.getElementsByTagName("info");
                for(int i=0; i<list.getLength();i++){
                    Node elem = list.item(i);
                    meta.add(elem.getTextContent());
                }
            }
            return meta;

        } catch (Exception e1) {
            return meta;
        }
    }
    public static List<String> getProjectQuestions(String nameProject, String PATH){
        List<String> questions = new ArrayList<String>();
        try {
            File f  = new File(PATH + "/Projetos/" + nameProject + ".xml");
            if(f.exists()) {
                DocumentBuilderFactory docBF = DocumentBuilderFactory.newInstance();
                DocumentBuilder docB = docBF.newDocumentBuilder();
                Document xmlRead = docB.parse(f);
                NodeList list = xmlRead.getElementsByTagName("p");
                for(int i=0; i<list.getLength();i++){
                    Node elem = list.item(i);
                    questions.add(elem.getTextContent());
                }
            }
            return questions;

        } catch (Exception e1) {
            return questions;
        }
    }
    public static List<String> getProjectUrls(String nameProject, String PATH){
        List<String> urls = new ArrayList<String>();
        try {
            File f  = new File(PATH + "/Projetos/" + nameProject + ".xml");
            if(f.exists()) {
                DocumentBuilderFactory docBF = DocumentBuilderFactory.newInstance();
                DocumentBuilder docB = docBF.newDocumentBuilder();
                Document xmlRead = docB.parse(f);
                NodeList list = xmlRead.getElementsByTagName("url");
                for(int i=0; i<list.getLength();i++){
                    Node elem = list.item(i);
                    urls.add(elem.getTextContent());
                }
            }
            return urls;

        } catch (Exception e1) {
            return urls;
        }
    }



    public static List<String> defaultMetaListInit() {

        List<String> local = new ArrayList<String>();
        local.add("Nome");
        local.add("Apelido");
        local.add("Idade");
        local.add("Profissão");
        local.add("Morada");
        return local;

    }
    public static List<String>  defaultQuestionsListInit(){
        List<String> local = new ArrayList<String>();
        local.add("Profissão");
        local.add("Estudos");
        local.add("Namoro");
        local.add("Literatura Favorita");
        local.add("Atividades Desportivas");
        return local;
    }
    public static List<String>  defaultLinksListInit(){
        List<String> local = new ArrayList<String>();
        local.add("http://www.museudapessoa.net/submicoes");
        return local;
    }

}
