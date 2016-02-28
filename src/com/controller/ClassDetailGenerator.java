package com.controller;

import java.util.*;

import com.model.syntax.StackItem;

public class ClassDetailGenerator {
	public static List<String> getAttributes(String clasName, StackItem subject, StackItem verp){
		List<StackItem> q = new ArrayList<StackItem>();
		Map<String,String> res = new HashMap<String, String>();
		q.add(verp);
		while(q.size() > 0){
			StackItem si = q.get(0);
			if(si != null && si.comeFrom != null){
				for(int k=0; k<si.comeFrom.size(); k++){
						q.add(si.comeFrom.get(k));
				}
			}
			if(		si.item.toLowerCase().equals("noun") || 
					si.item.toLowerCase().equals("adj")||
					si.item.toLowerCase().equals("ap")){
				if(!si.equals(clasName)){
					res.put(si.realItemValue, null);
				}
			}
			q.remove(0);
		}
		List<String> res1 = new ArrayList<String>();
		Iterator<String> it = res.keySet().iterator();
		while(it.hasNext()){
			String key = it.next();
			res1.add(key);
		}
		return res1;
	}
	
	public static List<String> getMethods(StackItem subject, StackItem verp){
		List<StackItem> q = new ArrayList<StackItem>();
		Map<String,String> res = new HashMap<String, String>();
		q.add(verp);
		while(q.size() > 0){
			StackItem si = q.get(0);
			if(si != null && si.comeFrom != null){
				for(int k=0; k<si.comeFrom.size(); k++){
					q.add(si.comeFrom.get(k));
				}
			}
			if(si.item.equals("verb")){
				res.put(si.realItemValue, null);
			}
			q.remove(0);
		}
		List<String> res1 = new ArrayList<String>();
		Iterator<String> it = res.keySet().iterator();
		while(it.hasNext()){
			String key = it.next();
			res1.add(key);
		}
		return res1;
	}
}
