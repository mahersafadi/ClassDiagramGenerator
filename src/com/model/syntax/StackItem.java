package com.model.syntax;

import java.util.*;
public class StackItem {

	public	String item;
	public	String realItemValue;
	public 	List<StackItem> comeFrom;
	public 	String executer;
	public	StackItem next;
	
	public StackItem(){
		comeFrom = new ArrayList<StackItem>();
	}
	public StackItem(List<StackItem> comeFrom1){
		if(comeFrom1 != null )
			comeFrom = comeFrom1;
		else
			comeFrom = new ArrayList<StackItem>();
	}
}
