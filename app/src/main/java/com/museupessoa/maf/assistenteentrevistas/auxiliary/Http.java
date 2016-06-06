package com.museupessoa.maf.assistenteentrevistas.auxiliary;


import android.util.Log;
import android.widget.Toast;

import com.museupessoa.maf.assistenteentrevistas.DownloadProjectsActivity;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
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

public class Http {
    public static final  String TAG = "AssistenteEntrevistas";

    public static List<String> getListOfProjects2(String url) throws IOException {
        try {
            List<String> remoteProjects;
            remoteProjects = new ArrayList<String>();
            DocumentBuilderFactory docBF = DocumentBuilderFactory.newInstance();
            DocumentBuilder docB = docBF.newDocumentBuilder();
            Document xmlRead = docB.parse(url + "/list.xml");

            NodeList list = xmlRead.getElementsByTagName("p");
            for(int i=0; i<list.getLength();i++){
                Node elem = list.item(i);
                remoteProjects.add(elem.getTextContent());
            }
            return remoteProjects;
        } catch (ParserConfigurationException e) {

        } catch (SAXException e) {

        }
        return new ArrayList<String>();
    }
    public static void saveProject(String url,String path, String name) throws IOException {
        try {
            DocumentBuilderFactory docBF = DocumentBuilderFactory.newInstance();
            DocumentBuilder docB = docBF.newDocumentBuilder();
            Document xmlRead = docB.parse(url + "/"+name+".xml");
            Transformer trans = TransformerFactory.newInstance().newTransformer();
            DOMSource xmlSource = new DOMSource(xmlRead);
            StreamResult result = new StreamResult(path + "/Projetos/"+name+".xml");
            trans.transform(xmlSource, result);

        } catch (ParserConfigurationException e) {
            Log.d(TAG,e.toString());
        } catch (SAXException e) {
            Log.d(TAG, e.toString());
        } catch (TransformerConfigurationException e) {
            Log.d(TAG, e.toString());
        } catch (TransformerException e) {
            Log.d(TAG, e.toString());
        }

    }


}
