package com.controller;


import java.util.ArrayList;
import java.util.List;

import com.model.syntax.StackItem;

public class OurClass {

	private String className="";
	private String stereoType="";
	List<String> method = new ArrayList<String>();
	List<String> attributes = new ArrayList<String>();
	
	private StackItem subject;
	private StackItem verp;
	private String executer;
	private com.model.sentence.Sentence sentence;
	private int CD=0;
	public String getClassName() {
		return className;
	}
	public OurClass(String name,String stereo) {
		this.setClassName(name);
		this.setStereoType(stereo);
		}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getStereoType() {
		return stereoType;
	}
	public void setStereoType(String stereoType) {
		this.stereoType = stereoType;
	}
	public List<String> getMethod() {
		return method;
	}
	public void setAttributes(List<String> attributes) {
		this.attributes = attributes;
	}
	public List<String> getAttributes() {
		return attributes;
	}
	public void setMethod(List<String> method) {
		this.method = method;
	}
	public int getCD() {
		return CD;
	}
	public void setCD(int cD) {
		CD = cD;
	}
	public OurClass() {
		super();
		method = new ArrayList<String>();
		attributes = new ArrayList<String>();
	}
	public StackItem getSubject() {
		return subject;
	}
	public void setSubject(StackItem subject) {
		this.subject = subject;
	}
	public StackItem getVerp() {
		return verp;
	}
	public void setVerp(StackItem verp) {
		this.verp = verp;
	}
	public String getExecuter() {
		return executer;
	}
	public void setExecuter(String executer) {
		this.executer = executer;
	}
	public com.model.sentence.Sentence getSentence() {
		return sentence;
	}
	public void setSentence(com.model.sentence.Sentence sentence) {
		this.sentence = sentence;
	}
	
	
}
