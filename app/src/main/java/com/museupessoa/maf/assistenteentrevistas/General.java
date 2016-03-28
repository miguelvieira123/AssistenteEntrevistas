package com.museupessoa.maf.assistenteentrevistas;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedWriter;
import java.io.File;

import java.io.FileWriter;
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

            // BEGIN - Entrevista Fake!
            f = new File(path + "/Entrevistas/e000");
            if (!f.exists())if(!f.mkdirs())return false;
            f.setWritable(true);
            f = new File(path + "/Entrevistas/e000/Audio");
            if (!f.exists())if(!f.mkdirs())return false;
            f.setWritable(true);
            f = new File(path + "/Entrevistas/e000/Fotos");
            if (!f.exists())if(!f.mkdirs())return false;
            f.setWritable(true);

            File outputFile = new File(path + "/Entrevistas/e000", "manifesto.xml");
            if (!outputFile.exists()){
                BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
                writer.write("<manifesto>\n\t<meta>\n\t\t<nome>Dionísio Mbanze</nome>\n\t</meta>\n</manifesto>\n");
                writer.flush();
                writer.close();
            }
            // END - Entrevista Fake!
            // BEGIN - Entrevista Fake!
            f = new File(path + "/Entrevistas/e001");
            if (!f.exists())if(!f.mkdirs())return false;
            f.setWritable(true);
            f = new File(path + "/Entrevistas/e001/Audio");
            if (!f.exists())if(!f.mkdirs())return false;
            f.setWritable(true);
            f = new File(path + "/Entrevistas/e001/Fotos");
            if (!f.exists())if(!f.mkdirs())return false;
            f.setWritable(true);

            outputFile = new File(path + "/Entrevistas/e001", "manifesto.xml");
            if (!outputFile.exists()){
                BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
                writer.write("<manifesto>\n\t<meta>\n\t\t<nome>Graça Sucá</nome>\n\t</meta>\n</manifesto>\n");
                writer.flush();
                writer.close();
            }
            // END - Entrevista Fake!


            f = new File(path + "/Projetos");
            if (!f.exists())if(!f.mkdirs())return false;
            f.setWritable(true);
            return true;
        } catch (Exception e1) {
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
        local.add("Pergunta 1");
        local.add("Pergunta 2");
        local.add("Pergunta 3");
        local.add("Pergunta 4");
        local.add("Pergunta 5");
        return local;
    }
    public static List<String>  defaultLinksListInit(){
        List<String> local = new ArrayList<String>();
        local.add("Link 1");
        local.add("Link 2");
        return local;
    }

}
