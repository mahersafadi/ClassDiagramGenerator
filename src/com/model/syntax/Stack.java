package com.model.syntax;



import com.model.syntax.StackItem;

public class Stack {
	private StackItem top;
	public Stack() {
		top = null;
	}
	
	public boolean isEmpty(){
		if(top == null)
			return true;
		else
			return false;
	}
	
	public void push(String token, String realName){
		StackItem si = new StackItem();
		si.item = token;
		si.comeFrom = null;
		si.realItemValue = realName;
		si.next = null;
		
		push(si);
	}
	
	public void push(StackItem si){
		if(top == null)
			top = si;
		else
		{
			si.next = top;
			top = si;
		}
	}
	
	public StackItem pop(){
		if(isEmpty())
			return null;
		else{
			StackItem si = top;
			top = top.next;
			return si;
		}
	}

	public StackItem getTop() {
		return top;
	}

	public void setTop(StackItem top) {
		this.top = top;
	}
	

}


