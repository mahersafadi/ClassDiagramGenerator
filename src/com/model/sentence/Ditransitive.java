package com.model.sentence;

import com.controller.OurClass;
import com.model.syntax.StackItem;

public class Ditransitive extends Sentence{
	public Ditransitive(StackItem subject, StackItem verp) {
		super(subject, verp);
	}
	
	@Override
	public OurClass analyse() {
		
		return super.analyse();
	}
	
	@Override
	public String toString() {
		return "Ditransitive";
	}
}
