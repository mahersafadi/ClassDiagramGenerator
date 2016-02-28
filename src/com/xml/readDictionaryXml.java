package com.xml;
import java.io.File;
import org.w3c.dom.Document;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException; 

public class readDictionaryXml{
	String kindWord;
	String attribKind;

    public String readXmlDictionary (String inputWord){
    try {

            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse (new File("dictionaryf.xml"));

            // normalize text representation
            doc.getDocumentElement ().normalize ();
            System.out.println ("Root element of the doc is " + 
                 doc.getDocumentElement().getNodeName());


            NodeList listOfwords = doc.getElementsByTagName("word");
            int totalwords = listOfwords.getLength();
            System.out.println("Total no of word : " + totalwords);

            for(int s=0; s<listOfwords.getLength() ; s++){


                Node nameNode = listOfwords.item(s);
                if(nameNode.getNodeType() == Node.ELEMENT_NODE){


                    Element nameWordDicElement = (Element)nameNode;

                    //-------
                    NodeList nameWordList = nameWordDicElement.getElementsByTagName("name");
                    Element nameWordElement = (Element)nameWordList.item(0);

                    NodeList textFNList = nameWordElement.getChildNodes();
                    System.out.println("Name Word : " + 
                           ((Node)textFNList.item(0)).getNodeValue().trim());

                    //-------
                    NodeList kindWordList = nameWordDicElement.getElementsByTagName("kind");
                    Element kindWordElement = (Element)kindWordList.item(0);

                    NodeList textLNList = kindWordElement.getChildNodes();
                    System.out.println("Kind Word : " + 
                           ((Node)textLNList.item(0)).getNodeValue().trim());

                    //----
                    if(((Node)textFNList.item(0)).getNodeValue().trim()==inputWord){
                    kindWord=((Node)textLNList.item(0)).getNodeValue().trim();
                    
                    //to read attribute of kind
                    Node nvalue=(Node)kindWordList.item(0);
                    if(nvalue.hasAttributes())
                   attribKind= kindWordElement.getAttribute("attrib");
                    
                    s=listOfwords.getLength();	
                    	
                    }


                }//end of if clause


            }//end of for loop with s var
       

        }catch (SAXParseException err) {
        System.out.println ("** Parsing error" + ", line " 
             + err.getLineNumber () + ", uri " + err.getSystemId ());
        System.out.println(" " + err.getMessage ());

        }catch (SAXException e) {
        Exception x = e.getException ();
        ((x == null) ? e : x).printStackTrace ();

        }catch (Throwable t) {
        t.printStackTrace ();
        }
        //System.exit (0);
        return kindWord;
    }//end of func


}

