package com.model.sentence;

import com.controller.OurClass;
import com.model.syntax.StackItem;
import java.util.*;

public class Sentence {
	public static int low = 0;
	public static int median = 1;
	public static int hight = 2;
	public static String [] CDNames = {"low", "median", "hight"};
	public static String sterioTypeEntity = "entity";
	public static String sterioTypeControl = "control";
	public static String sterioTypeBoundry = "boundry";
	
	StackItem subject;
	StackItem verp;
	private String [] specialPerp = new String[] {"on", "to", "into", "from"};
	private String [] specialVerp = new String[] {"start", "end", "starts", "ends"};
	public Sentence(StackItem subject, StackItem verp) {
		this.subject = subject;
		this.verp = verp;
	}
	
	public String getActor(){
		if(subject != null){
			List<StackItem> queue = new ArrayList<StackItem>();
			if("subject".equalsIgnoreCase(subject.item)){
				queue.add(subject);
				while(queue.size() > 0 && queue.get(0).item.equalsIgnoreCase("noun")){
					for(int i=0; i<queue.get(0).comeFrom.size(); i++){
						queue.add(queue.get(0).comeFrom.get(i));
					}
					queue.remove(0);
				}
			}
			if(queue.size() > 0)
				return queue.get(0).realItemValue;
			else
				return "";
		}
		return "";
	}
	
	public OurClass analyse(){
		return null;
	}
	
	public boolean isSpecialPrep(String prep){
		boolean found = false;
		for(int i =0; i<specialPerp.length && !found; i++){
			if(specialPerp[i].equalsIgnoreCase(prep))
				found = true;
		}
		return found;
	}
	
	public boolean isSpecialVerp(String verp){
		boolean found = false;
		for(int i=0; i<specialVerp.length && !found; i++){
			if(specialVerp[i].equalsIgnoreCase(verp))
				found = true;
		}
		return found;
	}
	
	public String getLastNoun(StackItem si){
		if(si != null){
			String res = null;
			if(si.item.equalsIgnoreCase("noun"))
				res = si.realItemValue;
			if(si.comeFrom != null){
				for(int i=0; i<si.comeFrom.size(); i++){
					String res1 = getLastNoun(si.comeFrom.get(i));
					if(res1 != null)
						res = res1;
				}
			}
			return res;
		}
		else
			return null;
	}
	
	public String getFirstNoun(StackItem si){
		if(si != null){
			String res = null;
			if(si.item.equalsIgnoreCase("noun"))
				res = si.realItemValue;
			else{
				if(si.comeFrom != null){
					for(int i=0; i<si.comeFrom.size(); i++){
						String res1 = getLastNoun(si.comeFrom.get(i));
						if(res1 != null)
							res = res1;
					}
				}
			}
			return res;
		}
		else
			return null;
	}
	
	@Override
	public String toString() {
		return "transitive";
	}
	
	public Map<String, String> getMessages(OurClass cls){
		return null;
	}
}
