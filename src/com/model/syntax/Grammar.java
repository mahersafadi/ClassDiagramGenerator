package com.model.syntax;
import java.util.*;
public class Grammar {
	private String grammarName;
	private List<Item> grammarItems;
	private String grammarExecuter;
	public Grammar oldGrammar;
	public Grammar(){
		grammarItems = new ArrayList<Item>();
	}
	public Grammar(String n){
		grammarItems = new ArrayList<Item>();
		this.grammarName = n;
	}
	public String getGrammarExecuter() {
		return grammarExecuter;
	}
	public void setGrammarExecuter(String grammarExecuter) {
		this.grammarExecuter = grammarExecuter;
	}
	public String getGrammarName() {
		return grammarName;
	}
	public void setGrammarName(String grammarName) {
		this.grammarName = grammarName;
	}
	public List<Item> getGrammarItems() {
		return grammarItems;
	}
	public void setGrammarItems(List<Item> grammarItems) {
		this.grammarItems = grammarItems;
	}
}
