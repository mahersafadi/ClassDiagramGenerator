package com.model.syntax;

public class Item {
	public static final int LITERAL = 1;
	public static final int GRAMMAR = 2;
	private String name;
	private int type;
	public Item(){
		;
	}
	public Item(String name, int type){
		this.name = name;
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
}
