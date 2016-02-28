package com.controller;

import java.io.File;
import org.w3c.dom.Document;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException; 

public class readDictionaryXml{

    public String readxml (String inputword){
    	  String kindword = null;
    try {
           
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse (new File("dictionaryf.xml"));

            // normalize text representation
            doc.getDocumentElement ().normalize ();
         //   System.out.println ("Root element of the doc is " + 
            //     doc.getDocumentElement().getNodeName());


            NodeList listOfPersons = doc.getElementsByTagName("word");
            int totalPersons = listOfPersons.getLength();
          //  System.out.println("Total no of people : " + totalPersons);

            for(int s=0; s<listOfPersons.getLength() ; s++){


                Node firstPersonNode = listOfPersons.item(s);
                if(firstPersonNode.getNodeType() == Node.ELEMENT_NODE){


                    Element firstPersonElement = (Element)firstPersonNode;

                    //-------
                    NodeList firstNameList = firstPersonElement.getElementsByTagName("name");
                    Element firstNameElement = (Element)firstNameList.item(0);

                    NodeList textFNList = firstNameElement.getChildNodes();
                  //  System.out.println(((Node)textFNList.item(0)).getNodeValue().trim()+"fofo");
                    if((((Node)textFNList.item(0)).getNodeValue().trim()).equals(inputword)){
               //     System.out.println("word Name : " + 
                 //          ((Node)textFNList.item(0)).getNodeValue().trim());

                    //-------
                    NodeList lastNameList = firstPersonElement.getElementsByTagName("kind");
                    Element lastNameElement = (Element)lastNameList.item(0);

                    NodeList textLNList = lastNameElement.getChildNodes();
                //    System.out.println("word kind : " + 
                 //          ((Node)textLNList.item(0)).getNodeValue().trim());

                   kindword =  ((Node)textLNList.item(0)).getNodeValue().trim();
                    }
                    //------


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
        return kindword;

    }//end of function


}