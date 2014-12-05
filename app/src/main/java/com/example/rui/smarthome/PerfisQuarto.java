package com.example.rui.smarthome;

/**
 * Created by ricar_000 on 5/26/2014.
 */
import android.content.Context;

import com.example.rui.server.Perfil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class PerfisQuarto {

    /*public static ArrayList<Perfil> ReadXML(Context context){
        ArrayList<Perfil> profiles = new ArrayList<Perfil>();
        Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        dbf.setCoalescing(true);
        try {

            DocumentBuilder db = dbf.newDocumentBuilder();

            // InputSource is = new InputSource();
            FileInputStream _stream = context.getApplicationContext().openFileInput("profiles.xml");
            // is.setCharacterStream(new StringReader(_stream));
            doc = db.parse(_stream);

            NodeList nl = doc.getElementsByTagName("Profile");

            for(int i=0; i < nl.getLength(); i++) {
                Element e = (Element) nl.item(i);

                String nome = e.getAttribute("Nome");

                String light = e.getAttribute("Light");
                String value = e.getAttribute("Value");

                profiles.add(new Perfil(nome, Boolean.valueOf(light), Integer.parseInt(value)));
            }

            return profiles;

        } catch (ParserConfigurationException e) {
            return profiles;
        } catch (SAXException e) {
            return profiles;
        } catch (IOException e) {
            return profiles;
        }

    }


    private static Perfil exists(String name, ArrayList<Perfil> profiles ){
        for(int i=0;i < profiles.size();i++){
            Perfil pr = profiles.get(i);
            if(pr.getName_perfil().compareTo(name)==0){
                return pr;
            }
        }
        return null;
    }

    public static void WriteXML(Context context, Perfil prof){

        //First we will read existent profiles and than we will add the new one and parse them all
        ArrayList<Perfil> profiles = ReadXML(context);
        boolean updated = false;
        for(int i=0;i<profiles.size();i++) {
            Perfil p = profiles.get(i);
            if(p.getName_perfil().compareTo(prof.getName_perfil())==0){
                profiles.set(i,prof);
                updated = true;
            }
        }

        if(!updated)
            profiles.add(prof);


        try
        {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            //root elements
            Document doc = docBuilder.newDocument();

            Element rootElement = doc.createElement("Profiles");
            for(int i=0;i<profiles.size();i++){
                Perfil profile = profiles.get(i);

                Element profileItem = doc.createElement("Profile");
                profileItem.setAttribute("Nome",profile.getName_perfil());

                //set attribute to staff element
                Attr t1 = doc.createAttribute("Light");
                t1.setValue(String.valueOf(profile.getLight_Perfil()));
                profileItem.setAttributeNode(t1);
                //set attribute to staff element
                Attr t2 = doc.createAttribute("Value");
                t2.setValue(String.valueOf(profile.getValue()));
                profileItem.setAttributeNode(t2);
                //set attribute to staff element

                rootElement.appendChild(profileItem);

            }
            doc.appendChild(rootElement);
            //write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);

            FileOutputStream _stream = context.getApplicationContext().openFileOutput("profiles.xml", context.getApplicationContext().MODE_WORLD_WRITEABLE);

            StreamResult result = new StreamResult(_stream);
            transformer.transform(source, result);
            System.out.println("Done");

        }catch(FileNotFoundException ex){
            ex.printStackTrace();
        }
        catch(ParserConfigurationException pce)
        {
            pce.printStackTrace();
        }
        catch(
                TransformerException tfe
                )

        {
            tfe.printStackTrace();
        }

    }*/


}
