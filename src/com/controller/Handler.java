package com.controller;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import view.MainScreen;
import javax.swing.*;

import com.model.sentence.*;
import com.model.syntax.Grammars;
import com.model.syntax.StackItem;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.xml.XMLParser;
import com.xml.XMLResult;

public class Handler {
	private static final String sub_NP_noun = null;
	private JTextArea logTA;
	private JTextField dbPath;
	private JTextField scenarioPath;
	private JTextField finished ;
	public String scenario_Path;
	private Grammars gms;
	
	List<OurClass> classes=new ArrayList();
	OurClass log=new OurClass("log","Entity");
    OurClass withdrawalTransition=new OurClass("Transaction","Control");
    OurClass CustomerConsol=new OurClass("Consol","Boundary");
    OurClass CashDispenser=new OurClass("Dispenser","Boundary");
    public void setClasses()
    {
    	this.classes.add(log);
    	this.classes.add(withdrawalTransition);
    	this.classes.add(CustomerConsol);
    	this.classes.add(CashDispenser);
    }
	public JTextArea getLogTA() {
		return logTA;
	}
	public void setLogTA(JTextArea logTA) {
		this.logTA = logTA;
	}
	
	public void setDbPath(JTextField dbPath) {
		this.dbPath = dbPath;
	}
	public void setScenarioPath(JTextField scenarioPath) {
		this.scenarioPath = scenarioPath;
	}
	
	public void setFinished(JTextField finished) {
		this.finished = finished;
	}
	
	public JTextField getFinished() {
		return finished;
	}
	
	public static void main(String[] args){
		Handler h = new Handler();
		MainScreen mainScreen = new MainScreen(h);
		
		
		com.model.syntax.Grammars gms1 = new com.model.syntax.Grammars();
		h.gms=gms1;
		try{
			String path = h.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
			path = path.substring(0, path.length()-"bin\\".length());
			path = path.substring(0)+"sampleGrammars.y";
			
			h.gms.loadSyntaxFile(path);
			h.gms.print();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		if(h.getFinished()== null){
			
		}
	}
	
	public void generateIsClicked(){
		this.logTA.setText(this.logTA.getText()+"\n");
	}
	
	////fff
	public void writeText(){
	String sfs = this.logTA.getText();
//	File f = new File("audits.txt");				
	//FileWriter fw = new FileWriter(f);
//	fw.write(sfs);
	try {
	    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("scenario.txt", true)));
	    out.println(sfs);
	    out.close();
	} catch (IOException e) {
	    //oh noes!
	}
	System.out.println(sfs);
	System.out.println("ok write");
	}
	
	public void readTextlines(File file){
		try{
			FileInputStream fstream = new FileInputStream(file.getPath());
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String line;
			readDictionaryXml rdx = new readDictionaryXml();
			List<String> outOfLang = new ArrayList<String>();
			//classes=new ArrayList();
			while (br.ready()) {
				//remove any extra spaces or dot at the end of line
				if( !"".equalsIgnoreCase(line = br.readLine())){
					List<String> pp = new ArrayList<String>();
					line = line.trim();
					if(line.endsWith("."))
						line = line.substring(0, line.length()-1);
					line = line.trim();				  
					//=======================================
					//Split line to words
					int wStart = 0;
					for(int k=0; k<line.length(); k++){
						if(line.charAt(k) == ' ' || line.charAt(k) == '\t' || line.charAt(k) == '\r'){
							String w = line.substring(wStart, k);
							pp.add(w);
							wStart = k+1;
						}
					}
					String w = line.substring(wStart, line.length());
					if(w != null && !"".equals(w)){
						pp.add(w);
					}
					String [] sentence = new String[pp.size()];
					String [] kind = new String [pp.size()]; 
					for(int k=0;k<pp.size();k++){
						sentence[k] = pp.get(k);
						kind[k]=rdx.readxml(sentence[k]);
						System.out.println (sentence[k]+".........."+kind[k]);
					}
					this.gms.setPredicates(kind);
					this.gms.setRealWords(sentence);
					java.util.Map<String, Object> m = this.gms.applySample();
					if(m.get("error") != null){
						JOptionPane.showMessageDialog(null,"Error:"+m.get("error"));
						JOptionPane.showMessageDialog(null,"Sentence is not from Language");
					}
					else{
						if("true".equals(m.get("from_lang"))){
							System.out.println("Sentence is from Language");
							StackItem subject = (StackItem)m.get("subject");
							StackItem verp	= (StackItem)m.get("verp");
							String executer = (String)m.get("executer");
							System.out.println("executer:"+executer);
							System.out.println("Subject:"+subject);
							System.out.println("execute the function :");
							
							
							choose_func (executer ,subject,verp );
							FindMessage( subject,verp,executer);
						  }
						  else{
							  System.out.println("Sentence is not from Language");
							  outOfLang.add("The Line ["+line+"] is not from language");
						  }
					}
				}
			}
			in.close();
			XMLResult xmlResult = new XMLResult();
			xmlResult.classes = xmlResult.merge(classes);
			String generationResult = xmlResult.generate();
			if(outOfLang.size() > 0){
				String LinesOutOfLangs = "The file has sentendes out of language\n";
				for(int i=0; i<outOfLang.size(); i++){
					LinesOutOfLangs += outOfLang.get(i)+"\n";
				}
				JOptionPane.showMessageDialog(null, LinesOutOfLangs);
			}
			{
				JOptionPane.showMessageDialog(null, "Generation Result:\n"+generationResult);
			}
		}
		catch (Exception e){//Catch exception if any
			  System.err.println("Error: " + e.getMessage());
		}
	}
	
	//split sentence
	public String[] splitString (String sentence ){
		String [] array;
		array = sentence.split(" ");
		System.out.println(array);
		return array;

	}
	//get message
	public void FindMessage(StackItem sub,StackItem predicate,String executer)
	 {
		if(executer.equalsIgnoreCase("Transitive")){
			TransitiveMessage(sub,predicate,executer);
		}
		if(executer.equalsIgnoreCase("Intransitive")){
			IntransitiveMessage(sub,predicate,executer);
		}
		if(executer.equalsIgnoreCase("Ditransitive")){
			DitransitiveMessage(sub,predicate,executer);
		}
		if(executer.equalsIgnoreCase("Intensive")){
			
		}
		if(executer.equalsIgnoreCase("Complex transitive")){
			Complex_transitiveMessage(sub,predicate,executer);
		}
		if(executer.equalsIgnoreCase("Prepositional")){
		
		}
		if(executer.equalsIgnoreCase("Non-finite")){
			Non_finiteMessage(sub,predicate,executer);
		}
	}
		 
	public void Complex_transitiveMessage(StackItem sub,StackItem predicate,String executer)
	{
		String sub_noun=sub.comeFrom.get(0).realItemValue;
		String k=predicate.comeFrom.get(2).item;
		if(k.equalsIgnoreCase("NP"))
		{
			String pre_noun=getPrepNoun2(predicate.comeFrom.get(1));
			for(int i=0;i<classes.size();i++)
			{
				if(classes.get(i).getClassName().equalsIgnoreCase(pre_noun)
						&&
						classes.get(i).getStereoType().equalsIgnoreCase("Entity"))
				{
					String message=predicate.comeFrom.get(0).comeFrom.get(0).realItemValue;
					for(int j=0;j<classes.size();j++)
					{
						if(classes.get(j).getStereoType().equalsIgnoreCase("control"))
						{
							classes.get(j).getMethod().add(message);
						}
					}
				}
					
			}
		}
		
		if(k.equalsIgnoreCase("AP"))
		{
			
			if(sub_noun.equalsIgnoreCase("system"))
			{
				String pre_noun=getPrepNoun2(predicate.comeFrom.get(1));
				for(int i=0;i<classes.size();i++)
				{
					if(classes.get(i).getClassName().equalsIgnoreCase(pre_noun)
							&&
							classes.get(i).getStereoType().equalsIgnoreCase("Entity"))
					{
						String message=predicate.comeFrom.get(0).comeFrom.get(0).realItemValue;
						for(int j=0;j<classes.size();j++)
						{
							if(classes.get(j).getStereoType().equalsIgnoreCase("control"))
							{
								classes.get(j).getMethod().add(message);
							}
						}
					}
						
				}	
			}
		}
		if(k.equalsIgnoreCase("PP"))
		{
			if(sub_noun.equalsIgnoreCase("system"))
			{
				String pre_noun=getPrepNoun2(predicate.comeFrom.get(1));
				for(int j=0;j<classes.size();j++)
				{
					if(classes.get(j).getClassName().equalsIgnoreCase(pre_noun))
					{
						if(classes.get(j).getStereoType().equalsIgnoreCase("Entity"))
						{
							String message=predicate.comeFrom.get(0).comeFrom.get(0).realItemValue;
						    for(int i=0;i<classes.size();i++)
						    {
						    	if(classes.get(i).getStereoType().equalsIgnoreCase("control"))
						    	{
						    		classes.get(i).getMethod().add(message);
						    	}
						    }
						}
						if(classes.get(j).getStereoType().equalsIgnoreCase("boundary"))
						{
							String message=predicate.comeFrom.get(0).comeFrom.get(0).realItemValue;
						    for(int i=0;i<classes.size();i++)
						    {
						    	if(classes.get(i).getClassName().equalsIgnoreCase(sub_noun))
						    	{
						    		classes.get(i).getMethod().add(message);
						    	}
						    }
							
						}	
											}
					}
			}
			else
			{
				String pre_noun=getPrepNoun2(predicate.comeFrom.get(1));
				for(int j=0;j<classes.size();j++)
				{
					
				if(classes.get(j).getClassName().equalsIgnoreCase(pre_noun)
						&&
						classes.get(j).getStereoType().equalsIgnoreCase("boundary"))
				{
				String message=predicate.comeFrom.get(0).comeFrom.get(0).realItemValue;
			    for(int i=0;i<classes.size();i++)
			    {
			    	if(classes.get(i).getClassName().equalsIgnoreCase(sub_noun))
			    	{
			    		classes.get(i).getMethod().add(message);
			    	}
			    }
				
			}
				}
		   }
	}
	}
	 public void TransitiveMessage(StackItem sub,StackItem predicate,String executer)
	 {   
		 String value="";
	 	 boolean cpp=false;String prep="";
		 String sub_noun=sub.comeFrom.get(0).realItemValue;
		 cpp=ContainPP(predicate.comeFrom.get(1));
		 if(sub_noun.equalsIgnoreCase("system")||!sub_noun.equalsIgnoreCase("system"))
			 
		 {
		 if(cpp)
		 {
			 prep=getPrep(predicate.comeFrom.get(1));
			 if(prep.equalsIgnoreCase("from"))
			 {
				 String verb_noun=getPrepNoun(predicate.comeFrom.get(1));
				 for(int i=0;i<classes.size()-1;i++)
				 {
					 if(classes.get(i).getClassName().equalsIgnoreCase(verb_noun)
							 &&
							 classes.get(i).getStereoType().equalsIgnoreCase("boundary"))
					 {
						 String message=predicate.comeFrom.get(0).comeFrom.get(0).realItemValue;
						 classes.get(i).getMethod().add(message);
					 }
				 }
			 }
			 else
			 {
				 String verb_noun=getPrepNoun(predicate.comeFrom.get(1));
				 for(int i=0;i<classes.size()-1;i++)
				 {
					 if(classes.get(i).getClassName().equalsIgnoreCase(verb_noun)
							 &&
							 classes.get(i).getStereoType().equalsIgnoreCase("boundary"))
					 {
						 String message=predicate.comeFrom.get(0).comeFrom.get(0).realItemValue;
						 for(int j=0;j<classes.size()-1;j++)
						 { 
							 if(classes.get(i).getClassName().equalsIgnoreCase(sub_noun))
							 {
								 classes.get(i).getMethod().add(message); 
							 }
							 
						 }
					 }
					 
					 if(classes.get(i).getClassName().equalsIgnoreCase(verb_noun)
							 &&
							 classes.get(i).getStereoType().equalsIgnoreCase("Entity"))
					 {
						 String message=predicate.comeFrom.get(0).comeFrom.get(0).realItemValue;
						 for(int j=0;j<classes.size()-1;j++)
						 {
							 if(classes.get(j).getStereoType().equalsIgnoreCase("control"))
								 classes.get(j).getMethod().add(message);
						 }
					 }
				 }
			 }
		 }
		 if(!cpp && sub_noun.equalsIgnoreCase("system"))
		 		 {
			 String verb_noun= getPrepNoun2(predicate.comeFrom.get(1));
			 //String message=predicate.comeFrom.get(0).comeFrom.get(0).realItemValue;
			 for(int i=0;i<classes.size()-1;i++)
			 {
				 if(classes.get(i).getClassName().equalsIgnoreCase(verb_noun)
						 &&
						 classes.get(i).getStereoType().equalsIgnoreCase("control"))
				 {
					 String message=predicate.comeFrom.get(0).comeFrom.get(0).realItemValue;
					 classes.get(i).getMethod().add(message);
				 }
				 
			 }
		 }
		 }
		 }
		 
	 
		public void Non_finiteMessage(StackItem sub,StackItem predicate,String executer)
		{
			String sub_noun=sub.comeFrom.get(0).realItemValue;
			String verb_noun=getPrepNoun2(predicate.comeFrom.get(1));
			for(int i=0;i<classes.size();i++)
			 {
				 if(classes.get(i).getClassName().equalsIgnoreCase(verb_noun)
						 &&
						 classes.get(i).getStereoType().equalsIgnoreCase("Entity"))
				 {
					 StackItem non_f=predicate.comeFrom.get(2);
					 if(non_f.comeFrom.size()==2)
					 {
						 String message=non_f.comeFrom.get(1).comeFrom.get(0).comeFrom.get(0).realItemValue;
						  for(int j=0;j<classes.size();j++)
						  {
							  if(classes.get(j).getStereoType().equalsIgnoreCase("control"))
							  {
								  classes.get(j).getMethod().add(message);
							  }
						  }
					 }
					 if(non_f.comeFrom.size()==1)
					 {
						 String message=non_f.comeFrom.get(0).realItemValue;
						  for(int j=0;j<classes.size();j++)
						  {
							  if(classes.get(j).getStereoType().equalsIgnoreCase("control"))
							  {
								  classes.get(j).getMethod().add(message);
							  }
						  }
					 }
					 
				 }
				 
			 }
			
		}
	
		
		public void IntransitiveMessage(StackItem sub,StackItem predicate,String executer)
		{
			String sub_noun=sub.comeFrom.get(0).realItemValue;
			for(int i=0;i<classes.size()-1;i++)
			 {
				 if(classes.get(i).getClassName().equalsIgnoreCase(sub_noun)
						 &&
						 classes.get(i).getStereoType().equalsIgnoreCase("control"))
				 {
					 String message=predicate.comeFrom.get(0).comeFrom.get(0).realItemValue;
					 classes.get(i).getMethod().add(message);
				 }
				 
			 }
			
		}
	
		public void DitransitiveMessage(StackItem sub,StackItem predicate,String executer)
		{
			String sub_noun=sub.comeFrom.get(0).realItemValue;
			String verb_noun=getPrepNoun2(predicate.comeFrom.get(3));
			for(int i=0;i<classes.size();i++)
			 {
				 if(classes.get(i).getClassName().equalsIgnoreCase(verb_noun)
						 &&
						 classes.get(i).getStereoType().equalsIgnoreCase("boundary"))
				 {
					 String message=predicate.comeFrom.get(0).comeFrom.get(0).realItemValue;
					 for(int j=0;j<classes.size();j++)
					 {
						 if(classes.get(j).getStereoType().equalsIgnoreCase("control"))
						 {
							 classes.get(j).getMethod().add(message);		 
						 }
					 }
					 
				 }
				 
			 }
			
			
		}
		
		public String getRealValue(StackItem p)
		{
			
			if(p.comeFrom==null)
			return p.realItemValue;	
			
			
			if(p.comeFrom.size()==2)
			return 		getRealValue(p.comeFrom.get(0)).concat(" "+getRealValue(p.comeFrom.get(1)));
			else
				return getRealValue(p.comeFrom.get(0));
				
			
			
			
		}
		
		public boolean ContainPP(StackItem p)
		{
			
			if(p.item.equalsIgnoreCase("PP"))
			return true;	
			
			
			
				if(p.comeFrom!=null)
				{
			if(p.comeFrom.size()==2)
			return 		ContainPP(p.comeFrom.get(0))||ContainPP(p.comeFrom.get(1));
			else
				return ContainPP(p.comeFrom.get(0));
				
	     		}
		
				else
					return false;
		}
		
		public String getPrepNoun(StackItem p)
		{
			
			if(p.item.equalsIgnoreCase("PP"))
			return getPrepNoun2(p.comeFrom.get(1));	
			
			
			
				if(p.comeFrom!=null)
				{
			if(p.comeFrom.size()==2)
			return 		getPrepNoun(p.comeFrom.get(0)).concat(getPrepNoun(p.comeFrom.get(1)));
			else
				return getPrepNoun(p.comeFrom.get(0));
				
	     		}
		
				else
					return "";
		}

		public String getPrepNoun2(StackItem p)
		{
			
			if(p.item.equalsIgnoreCase("noun"))
			return p.realItemValue;	
			
			
			
				if(p.comeFrom!=null)
				{
			if(p.comeFrom.size()==2)
				{
				if(p.comeFrom.get(0).item=="NP")
					return getPrepNoun2(p.comeFrom.get(0));
				else
					return getPrepNoun2(p.comeFrom.get(1));
				}
			
			else
				return getPrepNoun2(p.comeFrom.get(0));
				
	     		}
		
				else
					return "";
		}
		
		public String getPrep(StackItem p)
		{
			
			if(p.item.equalsIgnoreCase("Prep"))
			return p.realItemValue;	
			
			
			
				if(p.comeFrom!=null)
				{
			if(p.comeFrom.size()==2)
			return 		getPrep(p.comeFrom.get(0)).concat(getPrep(p.comeFrom.get(1)));
			else
				return getPrep(p.comeFrom.get(0));
				
	     		}
		
				else
					return "";
		}
		
		public void realValue(StackItem p)
		{String s="";
			if(p.comeFrom!=null)
			{
				if(p.comeFrom.size()==2)
				{realValue(p.comeFrom.get(0));
				realValue(p.comeFrom.get(1));}
				else
				{realValue(p.comeFrom.get(0));}
			}
			else
			{
				s.concat(p.realItemValue);
			}
		}

	
	/////
	
	//choose func
	void choose_func (String exectur ,StackItem subject,StackItem verp ){
		if(exectur.equals("Intrasitive")){
			Intransitive t = new Intransitive(subject, verp);
			OurClass cls = t.analyse();
			if(cls != null)
				classes.add(cls);
		}
		if(exectur.equals("Transitive")){
			Transitive t = new Transitive(subject, verp);
			OurClass cls = t.analyse();
			if(cls != null)
				classes.add(cls);
		}
		if(exectur.equals("Complex_transitive")){
			
		}
		if(exectur.equals("Ditransitive")){
			Ditransitive t = new Ditransitive(subject, verp);
			OurClass cls = t.analyse();
			if(cls != null)
				classes.add(cls);
		}
		if(exectur.equals("Intensive")){
			
		}
		if(exectur.equals("Prepositional")){
			
		}
		if(exectur.equals("Non_finite")){
		}
		/*
		if(exectur.equals("Intrasitive")){ 
			intransitive(subject , verp);
		}
		if(exectur.equals("Transitive")){
			transitive(subject , verp);
		}
		if(exectur.equals("Complex_transitive")){
			complex_transitive(subject , verp);
		}
		if(exectur.equals("Ditransitive")){
			Ditransitive(subject , verp);
		}
		if(exectur.equals("Intensive")){
			Intensive(subject , verp);
		}
		if(exectur.equals("Prepositional")){
			Prepositional(subject , verp);
		}
		if(exectur.equals("Non_finite")){
			Non_finite(subject , verp);
		}*/
	}
	
	
	
	
	//table1
	String findActor1(StackItem subject ,List<StackItem> comefrom){
		String sysActor="";
		for(int i =0 ;i<subject.comeFrom.size();i++){
			if(subject.comeFrom.get(i).item.equals("NP")){
				if((subject.comeFrom.get(i).realItemValue.equals("system"))||
						     (subject.comeFrom.get(i).realItemValue.equals("systems"))){
					sysActor="system";}
				else{
					sysActor="actor";}
				i=subject.comeFrom.size()+1;
			}
		}
		System.out.println("system"+sysActor);
		return sysActor;
	}
	
	String find_sub_NP_noun(StackItem subject){
		String sub_NP_noun="";
		for(int i=0;i<subject.comeFrom.size();i++){
			if(subject.comeFrom.get(i).item.equals("noun")){
				sub_NP_noun=subject.comeFrom.get(i).realItemValue;
			}
			
			if(subject.comeFrom.get(i).item.equals("NP")){
				StackItem gg = subject.comeFrom.get(i);
				sub_NP_noun=find_sub_NP_noun(gg);
			}
			
		}
		
		return sub_NP_noun;
	}
	
	String find_PP_prep (StackItem verp){
		String PP_prep ="";
		for(int i = 0 ; i< verp.comeFrom.size();i++){
			if(verp.comeFrom.get(i).item.equals("PP")){
			 List<StackItem> mm = (verp.comeFrom.get(i)).comeFrom;
			 for(int y=0;y<mm.size();y++){
				 if(mm.get(y).item.equals("prep")){
					 PP_prep = mm.get(y).realItemValue;
				 }}}
				 }
		
		return PP_prep;
		
	}
	
	void intransitive (StackItem subject,StackItem verp){
		for(int i =0 ;i<verp.comeFrom.size();i++){
			if(verp.comeFrom.get(i).item.toLowerCase().equals("vgp")){
				List<StackItem> aa = (verp.comeFrom.get(i)).comeFrom;
				for(int k =0 ;k<aa.size();k++){
					if(aa.get(k).item.equals("verb")){
						if((aa.get(k).realItemValue.equals("starts"))||(aa.get(k).realItemValue.equals("ends"))){
							//subject/Np/noun
							for(int z =0 ;z<subject.comeFrom.size();z++){
								if(subject.comeFrom.get(z).item.equals("NP")){
								 String sub_Np_noun= subject.comeFrom.get(z).realItemValue;
								 String object = sub_Np_noun;
								 String streotype = "control";
								 int CD = 2;//median
								 OurClass ourclass = new OurClass ();
								 ourclass.setClassName(object);
								 ourclass.setSubject(subject);
								 ourclass.setVerp(verp);
								 ourclass.setStereoType(streotype);
								 ourclass.setCD(CD);
								 boolean exist = false;
								 for(int m=0; m<classes.size()&&!exist; m++)
									 if(classes.get(m).equals(ourclass.getClassName()))
										 exist = true;
								 if(!exist){
									 classes.add(ourclass);
								 }
								 ourclass.getAttributes().addAll(ClassDetailGenerator.getAttributes(ourclass.getClassName(), subject, verp));
								 ourclass.getMethod().addAll(ClassDetailGenerator.getMethods(subject, verp));
								 
								 System.out.println(object +" "+streotype+" "+CD);
								}
							}
						}
					}
				}
			}
		}
	}
	
	void transitive (StackItem subject,StackItem verp){
		 
		String sysActor = findActor1(subject ,subject.comeFrom);
		for(int i = 0 ; i< verp.comeFrom.size();i++){
			if(verp.comeFrom.get(i).item.equals("PP")){
			 List<StackItem> mm = (verp.comeFrom.get(i)).comeFrom;
			 for(int y=0;y<mm.size();y++){
				 if(mm.get(y).item.equals("prep")){
					 if((sysActor.equals("actor"))&&((mm.get(y).realItemValue.equals("on"))
						||(mm.get(y).realItemValue.equals("into"))
						||(mm.get(y).realItemValue.equals("of"))
						||(mm.get(y).realItemValue.equals("from")))){
						 
						 List<StackItem> tt = (mm.get(y+1)).comeFrom; 
						 for(int y1=0;y1<tt.size();y1++){
							 if(tt.get(y1).item.equals("NP")){
								 List<StackItem> kk = (tt.get(y1)).comeFrom; 
								 for(int y2=0;y2<kk.size();y2++){
									 if(kk.get(y2).item.equals("noun")){
										System.out.println(kk.get(y2).item);
										String object = kk.get(y2).realItemValue;
										String streotype = "boundary";
										int CD = 1;
										OurClass our = new OurClass();
										our.setCD(CD);
										our.setClassName(object);
										our.setSubject(subject);
										our.setVerp(verp);
										our.setStereoType(streotype);
										boolean exist = false;
										for(int m=0; m<classes.size()&&!exist; m++)
											if(classes.get(m).equals(our.getClassName()))
												exist = true;
										if(!exist){
											classes.add(our);
										}
										our.getAttributes().addAll(ClassDetailGenerator.getAttributes(our.getClassName(), subject, verp));
										our.getMethod().addAll(ClassDetailGenerator.getMethods(subject, verp));
										System.out.println("mmmmmmmmmmmm");
										y2=kk.size()+1;
									 }//if
								 }//for
							 }//if
							 System.out.println(tt.get(y1).item);
							 System.out.println("hhdddddddddh");
						 }
						 y=mm.size()+1;
					 }//if
					 else if(sysActor.equals("actor")){
						 for(int b=0;b<verp.comeFrom.size();b++){
						   if(verp.comeFrom.get(b).equals("NP")){
							   List<StackItem> aa=(verp.comeFrom.get(b)).comeFrom;
							   for(int b1=0;b1<aa.size();b1++){
								   if(aa.get(b1).equals("noun")){
									   String object1 = aa.get(b1).realItemValue;
										 String streotype1 = "entity";
										 int CD1 = 2;
										 OurClass our1 = new OurClass();
										 our1.setCD(CD1);
										 our1.setClassName(object1);
										 our1.setSubject(subject);
										 our1.setVerp(verp);
										 our1.setStereoType(streotype1);
										 boolean exist = false;
										 for(int m=0; m<classes.size()&&!exist; m++)
											if(classes.get(m).equals(our1.getClassName()))
												exist = true;
										 if(!exist){
											classes.add(our1);
										 }
										 our1.getAttributes().addAll(ClassDetailGenerator.getAttributes(our1.getClassName(),subject, verp));
										 our1.getMethod().addAll(ClassDetailGenerator.getMethods(subject, verp));
										 System.out.println("wwwwwwwwww");
									   
									   b1=aa.size()+1;
									   
								   }}
							   b=verp.comeFrom.size()+1;
						   } 
						 }
					 }
				 }
			 }
			}
		}
		
		if(sysActor.equals("system")){
			for(int i = 0 ; i< verp.comeFrom.size();i++){
				if(verp.comeFrom.get(i).item.equals("PP")){
				 List<StackItem> mm = (verp.comeFrom.get(i)).comeFrom;
				 for(int y=0;y<mm.size();y++){
					 System.out.println(mm.get(y).item);
					 System.out.println("h1h1h1h1h1h111111111h1111h1hhh111h");
					 if(mm.get(y).item.equals("prep")){
						 if((mm.get(y).realItemValue.equals("on"))||(mm.get(y).realItemValue.equals("into"))
								 ||(mm.get(y).realItemValue.equals("of"))||(mm.get(y).realItemValue.equals("from"))){
							 
							 List<StackItem> tt = (mm.get(y+1)).comeFrom; 
							 for(int y1=0;y1<tt.size();y1++){
								 if(tt.get(y1).item.equals("NP")){
									 List<StackItem> kk = (tt.get(y1)).comeFrom; 
									 for(int y2=0;y2<kk.size();y2++){
										 if(kk.get(y2).item.equals("noun")){
									 System.out.println(kk.get(y2).item);
									 String object = kk.get(y2).realItemValue;
									 System.out.println("object ="+object);
									 String streotype = "boundary";
									 int CD = 2;
									 OurClass our = new OurClass();
									 our.setCD(CD);
									 our.setClassName(object);
									 our.setSubject(subject);
									 our.setVerp(verp);
									 our.setStereoType(streotype);
									 boolean exist = false;
									 for(int m=0; m<classes.size()&&!exist; m++)
										if(classes.get(m).equals(our.getClassName()))
											exist = true;
									 if(!exist){
										classes.add(our);
									 }
									 our.getAttributes().addAll(ClassDetailGenerator.getAttributes(our.getClassName(), subject, verp));
									 our.getMethod().addAll(ClassDetailGenerator.getMethods(subject, verp));
									 System.out.println("mmmmmjjjjjjjjmmmmmmm");
									 y2=kk.size()+1;
									}
								 }
								 y1=tt.size()+1;}
								// System.out.println(tt.get(y1).item);
								 
								 System.out.println("hhdddddddddh");}
							
						 } y=mm.size()+1;
						 
						 }
				 }
				i=verp.comeFrom.size()+1; 
				}}
		
			//vgp/verb=special verb
			//vgp/verb
			for(int h=0;h<verp.comeFrom.size();h++){
				if(verp.comeFrom.get(h).item.toLowerCase().equals("vgp")){
					System.out.println("sys/vgp");
					List<StackItem> ff=(verp.comeFrom.get(h)).comeFrom;
					 for(int f=0;f<ff.size();f++){
						 if(ff.get(f).item.equals("verb")){
							 System.out.println("sys/verb");
							 if((ff.get(f).realItemValue.equals("start"))||(ff.get(f).realItemValue.equals("end"))
									 ||(ff.get(f).realItemValue.equals("starts"))||(ff.get(f).realItemValue.equals("ends"))){
								 System.out.println("sys/ends");
								 for(int j=0;j<verp.comeFrom.size();j++){
									 if(verp.comeFrom.get(j).item.equals("NP")){
										 System.out.println("sys/NP");
										 List<StackItem> bn = (verp.comeFrom.get(j)).comeFrom;
										    for(int q=0;q<bn.size();q++){
										    	 System.out.println("sys/Np/.."+bn.get(q).item);
										    	 if(bn.get(q).item.equals("noun")){
										    		 System.out.println("sys/noun");
										    		
										   		 String object2 = bn.get(q).realItemValue;
												 System.out.println("object ="+object2);
												 String streotype2 = "control";
												 int CD2 = 1;
												 OurClass our8 = new OurClass();
												 our8.setCD(CD2);
												 our8.setClassName(object2);
												 our8.setSubject(subject);
												 our8.setVerp(verp);
												 our8.setSubject(subject);
												 our8.setVerp(verp);
												 our8.setStereoType(streotype2);
												 
												 
												 our8.getAttributes().addAll(ClassDetailGenerator.getAttributes(our8.getClassName(), subject, verp));
												 our8.getMethod().addAll(ClassDetailGenerator.getMethods(subject, verp));
												 boolean exist = false;
												 for(int m=0; m<classes.size()&&!exist; m++)
													if(classes.get(m).equals(our8.getClassName()))
														exist = true;
												 if(!exist){
													classes.add(our8);
												 }
												 System.out.println("mmmmmjjjjjjjjmmmmmmm");
										    		
										    	//	q=bn.size()+1;
										    	}
										    	 else{System.out.println("not noun");
										    	 if(bn.get(q).item.equals("NP")){
										    		 List<StackItem> vv = (bn.get(q)).comeFrom;
										    		 for(int r=0;r<vv.size();r++){
												    	 System.out.println("sys/Np/.."+vv.get(r).item);
												    	 if(vv.get(r).item.equals("noun")){
												    		 System.out.println("sys/noun");
												    		
													   		 String object4 = vv.get(r).realItemValue;
															 System.out.println("object ="+object4);
															 String streotype4 = "control";
															 int CD4 = 1;
															 OurClass our11 = new OurClass();
															 our11.setCD(CD4);
															 our11.setClassName(object4);
															 our11.setSubject(subject);
															 our11.setVerp(verp);
															 our11.setSubject(subject);
															 our11.setVerp(verp);
															 our11.setStereoType(streotype4);
															 
															 boolean exist = false;
															 for(int m=0; m<classes.size()&&!exist; m++)
																if(classes.get(m).equals(our11.getClassName()))
																	exist = true;
															 if(!exist){
																classes.add(our11);
															 }
															 our11.getAttributes().addAll(ClassDetailGenerator.getAttributes(our11.getClassName(), subject, verp));
															 our11.getMethod().addAll(ClassDetailGenerator.getMethods(subject, verp));
															 System.out.println("rrrrrrrrrr");
												    		
												    	 }
										    	 }
										    }
									 }
								 }
								 f=ff.size()+1;
								}
							}//for
						}//if
							 
						else{
								System.out.println("hereeeeee");
								 ////
								 for(int j1=0;j1<verp.comeFrom.size();j1++){
									 System.out.println("not specverb/Np/.."+verp.comeFrom.get(j1).item);
									 if(verp.comeFrom.get(j1).item.equals("NP")){
										 System.out.println("yes Np");
										 List<StackItem> bn1 = (verp.comeFrom.get(j1)).comeFrom;
										    for(int q1=0;q1<bn1.size();q1++){
										    	System.out.println("NP/NP  "+bn1.get(q1).item);
										    	if(bn1.get(q1).item.equals("noun")){
										    		
										    		String object3 = bn1.get(q1).realItemValue;
													 System.out.println("object3 ="+object3);
													 String streotype3 = "entity";
													 int CD3 = 2;
													 OurClass our9 = new OurClass();
													 our9.setCD(CD3);
													 our9.setClassName(object3);
													 our9.setSubject(subject);
													 our9.setVerp(verp);
													 our9.setSubject(subject);
													 our9.setVerp(verp);
													 our9.setStereoType(streotype3);
													 
													 boolean exist = false;
													 for(int m=0; m<classes.size()&&!exist; m++)
														if(classes.get(m).equals(our9.getClassName()))
															exist = true;
													 if(!exist){
														classes.add(our9);
													 }
													 our9.getAttributes().addAll(ClassDetailGenerator.getAttributes(our9.getClassName(), subject, verp));
													 our9.getMethod().addAll(ClassDetailGenerator.getMethods(subject, verp));
													 q1=bn1.size()+1;
										    	}
										    	reapetNp(bn1);
										    }
										 j1=	 verp.comeFrom.size()+1;
									 }
								 }
							 }
							 f=ff.size()+1; 
						 }
					 }
					h=verp.comeFrom.size()+1;
				}
			}
		}
	}
	
	void reapetNp(List<StackItem> dd){
		int h=0;
		List<StackItem>  gg=null;
		System.out.println("in reapetNp");
		for(int b=0;b<dd.size();b++){
			System.out.println("dd.get(b) "+dd.get(b).item);
			if(dd.get(b).item.equals("NP")){
				 gg =(dd.get(b)).comeFrom;
				for(h=0;h<gg.size();h++){
					if(gg.get(h).item.equals("noun")){
						String object3 = dd.get(b).realItemValue;
						 System.out.println("object3 ="+object3);
						 String streotype3 = "entity";
						 int CD3 = 2;
						 OurClass our9 = new OurClass();
					     our9.setCD(CD3);
						 our9.setClassName(object3);
						 our9.setStereoType(streotype3);
						 
						 /*our9.getAttributes().addAll(ClassDetailGenerator.getAttributes(subject, verp));
						 our9.getMethod().addAll(ClassDetailGenerator.getMethods(subject, verp));*/
						 boolean exist = false;
						 for(int m=0; m<classes.size()&&!exist; m++)
							if(classes.get(m).equals(our9.getClassName()))
								exist = true;
						 if(!exist){
							classes.add(our9);
						 }
					}
				}
					    						
			}
			//now
			if(dd.get(b).item.equals("noun")){
				String object3 = dd.get(b).realItemValue;
				 System.out.println("object3 ="+object3);
				 String streotype3 = "entity";
				 int CD3 = 2;
				 OurClass our9 = new OurClass();
				 our9.setCD(CD3);
				 our9.setClassName(object3);
				 our9.setStereoType(streotype3);
				 classes.add(our9);
			}
			
		}
		//if(h==gg.size()+1){
			for(int o=0;o<gg.size();o++){
				if((gg!=null)&& (gg.get(o).item.equals("NP"))){
					System.out.println("jjjjjjjjjjkkkkkk");
					List<StackItem> oo =(gg.get(o)).comeFrom;
			System.out.println("in reapet22222");
			reapetNp(oo);}
			}
		//}
		
	}

	String reapetNpl(List<StackItem> dd){
		int h=0;
		String object3="";
		//List<StackItem>  gg=null;
		System.out.println("in reapetNp");
		for(int b=0;b<dd.size();b++){
			System.out.println("dd.get(b) "+dd.get(b).item);
			
					if(dd.get(b).item.equals("noun")){
						object3 = dd.get(b).realItemValue;
						 System.out.println("NP/noun=== ="+object3);
						
					}
				}
					    						
					
		//if(h==gg.size()+1){
			for(int o=0;o<dd.size();o++){
				if(dd.get(o).item.equals("NP")){
					List<StackItem> oo =(dd.get(o)).comeFrom;
			System.out.println("in reapet22222");
			reapetNp(oo);}
			}
		//}
		return object3;
	}
	
	//find predicate/NP1//noun
	String find_NP_noun(StackItem verp){
		String NP_noun ="";
		for(int i=0;i<verp.comeFrom.size();i++){
			if(verp.comeFrom.get(i).item.equals("NP")){
				List<StackItem> xx=(verp.comeFrom.get(i)).comeFrom;
				for(int y=0;y<xx.size();y++){
					if(xx.get(y).item.equals("noun")){
						NP_noun = xx.get(y).realItemValue;
					}
				}
				for(int y=0;y<xx.size();y++){
					if(xx.get(y).item.equals("NP")){
						List<StackItem> xx1=(xx.get(y)).comeFrom;
						NP_noun= reapetNpl(xx1);
					}}
				
			}
		}
		return NP_noun;
	}
	
	//find predicate/NP2/noun
	String find_NP2_noun(StackItem verp){
		String Np2_noun="";int j=0;
		List<StackItem> xx=null;
		for(int i=0;i<verp.comeFrom.size();i++){
			if(verp.comeFrom.get(i).item.equals("NP")){
				 j=i;
				 System.out.println("j="+j);
			}}
			for(int k=j;k<verp.comeFrom.size();k++){
				 
					if(verp.comeFrom.get(k).item.equals("NP")){
						/////
						 System.out.println("k="+k);
						 xx=(verp.comeFrom.get(k)).comeFrom;
						 for(int y=0;y<xx.size();y++){
							if(xx.get(y).equals("noun")){
								Np2_noun = xx.get(y).realItemValue;
							}
						}
					}
			}
						for(int y=0;y<xx.size();y++){
							if(xx.get(y).equals("NP")){
								List<StackItem> xx1=(xx.get(y)).comeFrom;
								Np2_noun= reapetNpl(xx1);
							}
						}
		return Np2_noun;
						}
	
	//find PP/NP/noun
	String find_pp_NP_noun (StackItem verp){
		String PP_NP_noun="";
		for(int i=0;i<verp.comeFrom.size();i++){
			System.out.println(verp.comeFrom.get(i).item+"ffffffff");
			if(verp.comeFrom.get(i).item.equals("PP")){
				List<StackItem> ff=(verp.comeFrom.get(i)).comeFrom;
				  for(int j=0;j<ff.size();j++){
					  System.out.println(ff.get(j).item+"fhffhfhffhhf");
					  if(ff.get(j).item.equals("NP")){
						  System.out.println(ff.get(j).item+"hohohhohohho");
							List<StackItem> ff1=(ff.get(j)).comeFrom;
							 for(int h=0;h<ff1.size();h++){
								 if(ff1.get(h).item.equals("noun")){
									 PP_NP_noun=ff1.get(h).realItemValue; 
								 }
							 }
								 for(int h1=0;h1<ff1.size();h1++){
									 System.out.println(ff1.get(h1).item+"llllll");
									 if(ff1.get(h1).item.equals("NP")){
											List<StackItem> ff2=(ff1.get(h1)).comeFrom;
										 /////gg
										 for(int u=0;u<ff2.size();u++){
											 System.out.println("to repeat"+ff2.get(u).item);
										 }
											 ////gg
										 PP_NP_noun=reapetNpl(ff2);
										
									 }
							 }
				  }
			}
		}
		}
		
		return PP_NP_noun;
	}
	
	void complex_transitive(StackItem subject,StackItem verp){
		String sysActor = findActor1(subject ,subject.comeFrom);
		String PP_NP_noun = find_pp_NP_noun (verp);
		String NP2_noun = find_NP2_noun(verp);
		String NP1_noun = find_NP_noun(verp);
		for(int i=0;i<verp.comeFrom.size();i++){
			System.out.println(verp.comeFrom.get(i).item);
			if(verp.comeFrom.get(i).item.equals("AP")){//ss5b
				if(sysActor.equals("system")){
					OurClass our =new OurClass();
					our.setClassName(NP1_noun);
					our.setSubject(subject);
					our.setVerp(verp);
					our.setStereoType("entity");
					our.setCD(2);
					classes.add(our);
				}
				
				
			}//////ss5b
			if(verp.comeFrom.get(i).item.equals("PP")){//ss5c
				if(sysActor.equals("actor")){
					OurClass our1 =new OurClass();
					our1.setClassName(PP_NP_noun);
					our1.setSubject(subject);
					our1.setVerp(verp);
					our1.setStereoType("boundary");
					our1.setCD(1);
					classes.add(our1);
					
				/*	OurClass our2 =new OurClass();
					our2.setClassName(NP1_noun);
					our2.setStereoType("entity");
					our2.setCD(2);
					classes.add(our2);*/
					
				}
			 if(sysActor.equals("system")){
				 OurClass our1 =new OurClass();
					our1.setClassName(PP_NP_noun);
					our1.setSubject(subject);
					our1.setVerp(verp);
					System.out.println(PP_NP_noun+"yyyyyyyy");
					our1.setStereoType("boundary");
					our1.setCD(2);
					classes.add(our1); 
					
				/*	OurClass our2 =new OurClass();
					our2.setClassName(NP1_noun);
					System.out.println(NP1_noun+"yyyyyyyy");
					our2.setStereoType("entity");
					our2.setCD(2);
					classes.add(our2);*/
					
				 
			 }
				
			}//////ss5c
			
		}
		
		
	}

	void Ditransitive(StackItem subject,StackItem verp){
		String sysActor = findActor1(subject ,subject.comeFrom);
		String NP2_noun = find_NP2_noun(verp);
		String NP1_noun = find_NP_noun(verp);
		if((sysActor.equals("system"))||(sysActor.equals("system"))){
			OurClass our =new OurClass();	
			OurClass our1 =new OurClass();
			our1.setClassName(NP1_noun);
			our1.setSubject(subject);
			our1.setVerp(verp);
			our1.setStereoType("entity");
			our1.setCD(2);
			classes.add(our1);
			
			
		}
	}
	
    void Intensive(StackItem subject,StackItem verp){
    	String sub_NP_noun = find_sub_NP_noun( subject);
    	String sysActor = findActor1(subject ,subject.comeFrom);
		String PP_NP_noun = find_pp_NP_noun (verp);
		String NP2_noun = find_NP2_noun(verp);
		String PP_prep = find_PP_prep( verp);
		String NP1_noun = find_NP_noun(verp);
    	for(int i=0;i<verp.comeFrom.size();i++){
    	  if(verp.comeFrom.get(i).item.equals("AP")){
    		  String AP_attrib="";
    		  for(int k=0;k<verp.comeFrom.size();k++){
	    		AP_attrib= AP_attrib.concat(verp.comeFrom.get(k).item+" ");
    		  }
    		  //identified previosly
    		  for(int h=0;h<classes.size();h++){
    			  if(classes.get(h).getClassName().equals(sub_NP_noun)){
    				  classes.get(h).getAttributes().add(AP_attrib);
    				  classes.get(h).setCD(2);}
    		  }
    	  }
    	  if(verp.comeFrom.get(i).item.equals("PP")){
    		  List<StackItem> nn=(verp.comeFrom.get(i)).comeFrom;
    		  if((PP_prep.equals("of"))||(PP_prep.equals("from"))||(PP_prep.equals("on"))||(PP_prep.equals("into"))){
    			  for(int h=0;h<classes.size();h++){
    				 if(classes.get(h).getClassName().equals(PP_NP_noun)){
    					  classes.get(h).getAttributes().add(sub_NP_noun); 
    					  classes.get(h).setCD(2);
    				 }
    			  }
    		  }
    	  }
    	}
    }
   
    void Prepositional(StackItem subject,StackItem verp){
    	
    }
    
	void Non_finite(StackItem subject,StackItem verp){
		String pp_NP_noun = find_pp_NP_noun ( verp);
		String sub_NP_noun=find_sub_NP_noun( subject);
		String sysActor =findActor1( subject ,subject.comeFrom);
		if(sub_NP_noun.equals("system")){
			OurClass our =new OurClass();
			our.setClassName(pp_NP_noun);
			our.setSubject(subject);
			our.setVerp(verp);
			our.setStereoType("entity");
			our.setCD(2);
			classes.add(our);
			
		}
		
	}
	
	public String getDBPathFromConfigFile(){
		XMLParser xmlParser = new XMLParser();
		String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		path = path.substring(0)+"Config.xml";
		Document doc = xmlParser.getXmlFileAsDocument(path, "cp1256");
		NodeList nl = doc.getElementsByTagName("db-path");
		Node e = nl.item(0);
		String s = e.getTextContent();
		return s;
	}
	
	public String getScenarioPathFromConfigFile(){
		XMLParser xmlParser = new XMLParser();
		String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		path = path.substring(0)+"Config.xml";
		Document doc = xmlParser.getXmlFileAsDocument(path, "cp1256");
		NodeList nl = doc.getElementsByTagName("scenario-path");
		Node e = nl.item(0);
		String s = e.getTextContent();
		return s;
	}
	
	
	
	public boolean saveConfigFile(){
		try{
			
			XMLParser xmlParser = new XMLParser();
			String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
			path = path.substring(0)+"Config.xml";
			Document doc = xmlParser.getXmlFileAsDocument(path, "cp1256");
			NodeList nl = doc.getElementsByTagName("scenario-path");
			String v1 = nl.item(0).getTextContent();
			BufferedWriter bw = new BufferedWriter(new FileWriter(new java.io.File(path)));
	        bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
			bw.write("<config>\n");
			bw.write("		<db-path>"+dbPath.getText()+"</db-path>");
			bw.write("		<scenario-path>"+scenarioPath.getText()+"</scenario-path>");			
			bw.write("</config>");
			bw.close();

			return true;
		}
		catch(Exception e){return false;}
	}	
}