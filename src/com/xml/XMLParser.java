package com.xml;

import java.io.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import java.nio.charset.Charset;
import java.util.Date;
import javax.xml.parsers.*;

public class XMLParser {
	public XMLParser() {
    }
	public Document parseXML(String xmlAsString, String charset) throws Exception{
        BufferedReader br = null;
        try{
            Charset inputCharset = Charset.forName(charset);
            Reader r = new InputStreamReader(new ByteArrayInputStream(xmlAsString.getBytes(inputCharset)));
            br = new BufferedReader(r);
            StringBuilder sb = new StringBuilder();
            while(br.ready()){
              sb.append(br.readLine());
            }
            br.close();
            InputStream is = new ByteArrayInputStream(sb.toString().getBytes());
            return parserXML(is);
        }
        catch(UnsupportedEncodingException exxx){            
            InputStream is = new ByteArrayInputStream(xmlAsString.getBytes());
            return parserXML(is);
        }
    }
    
    public Document parserXML(InputStream is)throws ParserConfigurationException, SAXException, IOException
    {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      try{
        return builder.parse(is);
      }
      catch(Exception ex){
        System.out.println("Error is done during execution "+ new Date().toString());
        return builder.parse(new ByteArrayInputStream("<error>Error is done; Execution is faild</error>".getBytes()));
      }
    }
    
    public Document parseXML(String xmlAsString)throws ParserConfigurationException, SAXException, IOException
    {
      InputStream is = new ByteArrayInputStream(xmlAsString.getBytes());
      /*BufferedReader br = new BufferedReader(new InputStreamReader(is));
      StringBuilder sb = new StringBuilder();
      String line = null;
      while (br.ready()) {
        line = br.readLine();
        sb.append(line + "\n");
      }

      br.close();
      sb.toString();*/
      return parserXML(is);
    }
    
    public String getXmlFileContent(String xmlPath){
        try{
          FileReader f = new FileReader(xmlPath);
          BufferedReader br= new BufferedReader(f);
          String result = "";
          while(br.ready()){
            result += br.readLine();
          }
          return result;
        }
        catch(Exception ex){
          ex.printStackTrace();
        }
        return "";
    }
    
    public Document getXmlFileAsDocument(String xmlPath, String charset){
        BufferedReader br = null;
        try{
            Reader r = new InputStreamReader(new FileInputStream(xmlPath), charset);
            br = new BufferedReader(r);
            StringBuilder sb = new StringBuilder();
            while(br.ready()){
              sb.append(br.readLine());
            }
            br.close();
            
            InputStream is = new ByteArrayInputStream(sb.toString().getBytes());
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            try{
              return builder.parse(new File(xmlPath));
            }
            catch(Exception ex){
              System.out.println("Error is done during execution "+ new Date().toString());
              return builder.parse(new ByteArrayInputStream("<error>Error is done; Execution is faild</error>".getBytes()));
            }
        }
        catch(Exception e){
            e.printStackTrace();
            if(br != null){
              try{
                br.close();
              }
              catch(Exception ex){
                ex.printStackTrace();
              }
            }
        }
        return null;
    }
    
    public Document getXmlFileAsDocument(String xmlPath){
      BufferedReader br = null;
      try{
        br = new BufferedReader(new FileReader(new File(xmlPath)));
        StringBuilder sb = new StringBuilder();
        while(br.ready()){
          sb.append(br.readLine());
        }
        br.close();
        return parseXML(sb.toString());
      }
      catch(Exception e){
        e.printStackTrace();
        if(br != null){
          try{
            br.close();
          }
          catch(Exception ex){
            ex.printStackTrace();
          }
        }
      }
      return null;
    }
}
