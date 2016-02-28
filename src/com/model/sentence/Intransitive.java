package com.model.sentence;

import java.util.HashMap;
import java.util.Map;

import com.controller.OurClass;
import com.model.syntax.StackItem;

public class Intransitive extends Sentence{
	public Intransitive(StackItem subject, StackItem verp) {
		super(subject, verp);
	}
	@Override
	public OurClass analyse() {
		StackItem vgp = verp.comeFrom.get(0);
		if(vgp!= null && vgp.item.equalsIgnoreCase("vgp")){
			if(isSpecialVerp(vgp.realItemValue)){
				OurClass cls = new OurClass();
				String actorName = getActor();
				cls.setClassName(actorName);
				cls.setSubject(subject);
				cls.setVerp(vgp);
				cls.setSubject(subject);
				cls.setVerp(verp);
				cls.setStereoType(sterioTypeControl);
				cls.setSentence(this);
				cls.setCD(median);
				return cls;
			}
		}
		return null;
	}
	
	@Override
	public String toString() {
		return "Intransitive";
	}
	
	@Override
	public Map<String, String> getMessages(OurClass cls) {
		Map<String, String> mm = new HashMap<String, String>();
		StackItem vgp = verp.comeFrom.get(0);
		mm.put("msg", ""+vgp.realItemValue);
		String s = getFirstNoun(verp);
		String r = getLastNoun(verp);
		if(!s.equals(r)){
			s = getActor();
		}
		mm.put("sender", ""+s);
		mm.put("receiver", ""+r);
		return mm;
	}
}
