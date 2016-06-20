package com.museupessoa.maf.assistenteentrevistas;

import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedWriter;
import java.io.File;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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


public class General {

    public static final String TAG ="AssistenteEntrevistas";
    public  static String PATH = Environment.getExternalStoragePublicDirectory("/AssistenteEntrevistas").toString();
    public static  String APP_NAME = "AssistenteEntrevistas";
    public static int CR=227;
    public static int CG=110;
    public static int CB=20;

    public static boolean createStructOfFolders(String path){
        try {
            File f = new File(path + "/Entrevistas");
            if (!f.exists())if(!f.mkdirs())return false;
            f.setWritable(true);

            f = new File(path + "/Projetos");
            if (!f.exists())if(!f.mkdirs())return false;
            f.setWritable(true);

            f = new File(path + "/Zips");
            if (!f.exists())if(!f.mkdirs())return false;
            f.setWritable(true);

            f  = new File(path, "config.xml");
            if (!f.exists()){
                Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
                org.w3c.dom.Element root;
                root = doc.createElement("appConfig");
                doc.appendChild(root);
                org.w3c.dom.Element n1;
                n1 = doc.createElement("interviews");
                n1.setAttribute("counter", "2" );
                root.appendChild(n1);
                Transformer trans = TransformerFactory.newInstance().newTransformer();
                DOMSource xmlSource = new DOMSource(doc);
                StreamResult result = new StreamResult(path + "/config.xml");
                trans.transform(xmlSource, result);
            }

            return true;
        } catch (Exception e1) {
            return false;
        }
    }



    public static String createNewInterview(String app_path, String project_name, String person_name){

        List<String> meta = new ArrayList<>();
        List<String> perguntas= new ArrayList<>();
        List<String> urls = new ArrayList<>();

        // abrir project e sacar info
        try{
            File f  = new File(app_path+"/Projetos", project_name+".xml");
            if(f.exists()){
                Document doc = null;
                try {
                    doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(f);
                    NodeList n_meta = doc.getElementsByTagName("info");
                    NodeList n_perguntas = doc.getElementsByTagName("p");
                    NodeList n_urls = doc.getElementsByTagName("url");

                    for (int i=0; i<n_meta.getLength(); i++) {
                        meta.add(n_meta.item(i).getFirstChild().getNodeValue());
                    }
                    for (int i=0; i<n_perguntas.getLength(); i++) {
                        perguntas.add(n_perguntas.item(i).getFirstChild().getNodeValue());
                    }
                    for (int i=0; i<n_urls.getLength(); i++) {
                        urls.add(n_urls.item(i).getFirstChild().getNodeValue());
                    }

                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                }
            }
        }catch(Exception e){}

        int contador = getContadorFromXML(app_path);
        contador++;
        createInterview(app_path, person_name, "e00"+contador, meta, perguntas, urls);
        setContadorXML(app_path, contador);
        return "e00"+contador;
    }
    public static boolean createInterview(String path, String person_name, String interviewCode, List<String> meta, List<String> perguntas, List<String> urls){
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
                org.w3c.dom.Element n2;

                n1 = doc.createElement("meta");
                n1.setAttribute("name", person_name );
                for (String info: meta) {
                    //n2 = doc.createElement( info );

                    n2 = doc.createElement( "info" );
                    n2.setAttribute("name", info );
                    n1.appendChild(n2);
                }
                root.appendChild(n1);

                n1 = doc.createElement("perguntas");
                for (String perg: perguntas) {
                    n2 = doc.createElement("pergunta");
                    n2.setTextContent( perg );
                    n1.appendChild(n2);
                }
                root.appendChild(n1);

                n1 = doc.createElement("urls");
                for (String url : urls ) {
                    n2 = doc.createElement("url");
                    n2.setTextContent( url );
                    n1.appendChild(n2);

                }
                root.appendChild(n1);

                n1 = doc.createElement("audio");
                n1.setAttribute("count", "0");
                root.appendChild(n1);

                Transformer trans = TransformerFactory.newInstance().newTransformer();
                DOMSource xmlSource = new DOMSource(doc);
                StreamResult result = new StreamResult(path + "/Entrevistas/" + interviewCode + "/manifesto.xml");
                trans.transform(xmlSource, result);

            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
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
        local.add("http://www.museudapessoa.net/submissoes");
        return local;
    }


    private static int getContadorFromXML(String path){
        int contador=0;
        File f  = new File(path, "config.xml");
        if(f.exists()){
            Document doc = null;
            try {
                doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(f);
                NodeList audio = doc.getElementsByTagName("interviews");
                NamedNodeMap attrs = audio.item(0).getAttributes();
                contador = Integer.parseInt(attrs.getNamedItem("counter").getNodeValue());
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
    private static void setContadorXML(String path, int novaContagem){
        File f  = new File(path, "config.xml");
        if(f.exists()){
            Document doc = null;
            try {
                doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(f);
                NodeList audio = doc.getElementsByTagName("interviews");
                NamedNodeMap attrs = audio.item(0).getAttributes();
                attrs.getNamedItem("counter").setNodeValue(""+novaContagem);

                Transformer trans = TransformerFactory.newInstance().newTransformer();
                DOMSource xmlSource = new DOMSource(doc);
                StreamResult result = new StreamResult(path + "/config.xml");
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



    public static void addSourceLink(String path, String link){
        File f  = new File(path, "/links.xml");
        if(f.exists()){
            try {

                DocumentBuilderFactory docBF = DocumentBuilderFactory.newInstance();
                DocumentBuilder docB = docBF.newDocumentBuilder();
                Document xmlWrite = docB.parse(f);
                NodeList root = xmlWrite.getElementsByTagName("links");
                org.w3c.dom.Element child;
                child = xmlWrite.createElement("link");
                child.setTextContent(link);
                root.item(0).appendChild(child);
                Transformer trans = TransformerFactory.newInstance().newTransformer();
                DOMSource xmlSource = new DOMSource(xmlWrite);
                StreamResult result = new StreamResult(path + "/links.xml");
                trans.transform(xmlSource, result);


            } catch (Exception e1) {

            }
        }else{

            General.createSourceLinkFile(path);

        }

    }
    public static void deleteLink(String path, String link){
        File f  = new File(path, "/links.xml");
        if(f.exists()){
            try {
                DocumentBuilderFactory docBF = DocumentBuilderFactory.newInstance();
                DocumentBuilder docB = docBF.newDocumentBuilder();
                Document xmlWrite = docB.parse(f);
                NodeList links = xmlWrite.getElementsByTagName("link");
                for(int i=0; i<links.getLength();i++){
                    Node elem = links.item(i);
                    if(elem.getTextContent().equals(link)){
                        Node root = elem.getParentNode();
                        root.removeChild(elem);
                    }

                }
                Transformer trans = TransformerFactory.newInstance().newTransformer();
                DOMSource xmlSource = new DOMSource(xmlWrite);
                StreamResult result = new StreamResult(path + "/links.xml");
                trans.transform(xmlSource, result);

            } catch (Exception e1) {

            }
        }else{

            General.createSourceLinkFile(path);

        }
    }


    public static List<String> getSourceLinks(String path){
        List<String> links = new ArrayList<String>();
        File f  = new File(path, "/links.xml");
        if(f.exists()){
            try {
                DocumentBuilderFactory docBF = DocumentBuilderFactory.newInstance();
                DocumentBuilder docB = docBF.newDocumentBuilder();
                Document xmlRead = docB.parse(f);
                NodeList link = xmlRead.getElementsByTagName("link");
                for(int i=0; i<link.getLength();i++){
                    Node elem = link.item(i);
                    links.add(elem.getTextContent());
                 }
            } catch (Exception e1) {

            }
        }else{
            General.createSourceLinkFile(path);
            return links;
        }
        return links;
    }
    public static void createSourceLinkFile(String path){
        try {
                File f  = new File(path + "/links.xml");
                Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
                org.w3c.dom.Element root;
                root = doc.createElement("links");
                doc.appendChild(root);
                Transformer trans = TransformerFactory.newInstance().newTransformer();
                DOMSource xmlSource = new DOMSource(doc);
                StreamResult result = new StreamResult(path + "/links.xml");
                trans.transform(xmlSource, result);
        } catch (Exception e1) {

        }

    }

    public static boolean deleteDirectory(File path) {
        if( path.exists() ) {
            File[] files = path.listFiles();
            for(int i=0; i<files.length; i++) {
                if(files[i].isDirectory()) {
                    deleteDirectory(files[i]);
                }
                else {
                    files[i].delete();
                }
            }
        }
        return(path.delete());
    }

}
