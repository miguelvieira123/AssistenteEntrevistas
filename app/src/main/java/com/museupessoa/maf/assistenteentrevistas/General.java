package com.museupessoa.maf.assistenteentrevistas;


import org.w3c.dom.Document;

import java.io.BufferedWriter;
import java.io.File;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


public class General {
    public static boolean createStructOfFolders(String path){
        try {
            File f = new File(path + "/Entrevistas");
            if (!f.exists())if(!f.mkdirs())return false;
            f.setWritable(true);



            f = new File(path + "/Entrevistas/e000/Audio");
            if (!f.exists())if(!f.mkdirs())return false;
            f.setWritable(true);
            f = new File(path + "/Entrevistas/e000/Fotos");
            if (!f.exists())if(!f.mkdirs())return false;
            f.setWritable(true);
            f = new File(path + "/Entrevistas/e000/manifesto.xml");
            if (!f.exists())if(!f.mkdirs())return false;
            f.setWritable(true);

            File outputFile = new File(path + "/Entrevistas/e000", "manifesto2.xml");
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
            writer.write("cenas\n");
            writer.flush();
            writer.close();


            // END - Entrevista Fake!


            f = new File(path + "/Projetos");
            if (!f.exists())if(!f.mkdirs())return false;
            f.setWritable(true);
            return true;
        } catch (Exception e1) {
            return false;
        }
    }




    public static boolean createProject(String path,String projectName,List<String> info, List<String> questions, List<String> urls){
        try {
            File f  = new File(path + "/Projetos/" + projectName + ".xml");
            if(!f.exists()){
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

    public static List<String> defaultMetaListInit() {

        List<String> local = new ArrayList<String>();
        local.add("Nome");
        local.add("Apelido");
        local.add("Idade");
        local.add("Profissão");
        local.add("Morada");
        return local;

    }
    public static List<String> defaultQuestionsListInit() {

        List<String> local = new ArrayList<String>();
        local.add("Pergunta 1");
        local.add("Pergunta 2");
        local.add("Pergunta 3");
        local.add("Pergunta 4");
        local.add("Pergunta 5");
        return local;

    }
    public static List<String> defaultLinksListInit() {

        List<String> local = new ArrayList<String>();
        local.add("link 1");
        local.add("link 2");

        return local;

    }
}
