package com.model.sentence;

import java.util.HashMap;
import java.util.Map;

import com.controller.OurClass;
import com.model.syntax.StackItem;

public class Transitive extends Sentence{
	public Transitive(StackItem subject, StackItem verp){
		super(subject, verp);
		this.subject = subject;
		this.verp = verp;
	}
	
	@Override
	public OurClass analyse() {
		String actor = this.getActor();
		StackItem np = verp.comeFrom.get(1);
		String noun = getLastNoun(np);
		OurClass cls = new OurClass();
		cls.setClassName(noun);
		cls.setSubject(subject);
		cls.setSentence(this);
		cls.setVerp(verp);
		StackItem vgp = verp.comeFrom.get(0);
		if("system".equalsIgnoreCase(actor)){
			if(vgp != null && vgp.item.equalsIgnoreCase("vgp")){
				String verbName = vgp.realItemValue;
				if(isSpecialVerp(verbName)){
					cls.setStereoType(sterioTypeControl);
				}
				else{
					cls.setStereoType(sterioTypeEntity);
				}
			}
		}
		else{
			boolean hasPP = false;
			for(int i=0; i<np.comeFrom.size(); i++)
				if(np.comeFrom.get(i).item.equalsIgnoreCase("pp"))
					hasPP = true;
			
			if(hasPP){
				cls.setCD(hight);
				cls.setStereoType(sterioTypeBoundry);
			}
			else{
				cls.setCD(median);
				cls.setStereoType(sterioTypeEntity);
			}
		}
		//Now set the vgp+np->noun
		String nn = getFirstNoun(np);
		if(nn != null){
			String firstChar = ""+nn.charAt(0);
			firstChar = firstChar.toUpperCase();
			try{
				nn = nn.substring(1, nn.length());
			}
			catch(Exception e){
				
			}
			nn = firstChar+nn;
		}
		else
			nn = "";
		String methodName = vgp.realItemValue+nn;
		if(cls != null){
			cls.getMethod().add(methodName);
		}
		return cls;
	}
	
	@Override
	public String toString() {
		return "Transitive";
	}
	
	@Override
	public Map<String, String> getMessages(OurClass cls) {
		Map<String, String> mm = new HashMap<String, String>();
		StackItem vgp = verp.comeFrom.get(0);
		mm.put("msg", ""+vgp.realItemValue);
		String actor = getActor();
		boolean ok = false;
		if("system".equalsIgnoreCase(actor)){
			ok = true;
			if(sterioTypeEntity.equals(cls.getStereoType())){
				String s = getFirstNoun(verp);
				String r = getLastNoun(verp);
				if(s == null)
					s = "Corrsponding Control";
				if(r == null)
					r = "Corrsponding Control";
				if(!s.equals(r)){
					mm.put("sender", s);
					mm.put("receiver", r);
				}
			}
			else if(sterioTypeControl.equals(cls.getStereoType())){
				String s = getFirstNoun(verp);
				String r = getLastNoun(verp);
				if(s == null)
					s = "Entity Class, No Sender";
				if(r == null)
					r = "Entity Class, No Receiver";
				if(s.equals(r)){
					if(sterioTypeControl.equals(cls.getStereoType())){
						s = getActor();
					}
					else
						s = "From Corrsponding Control";
				}
				mm.put("sender", s);
				mm.put("receiver", r);
				
			}
		}
		
		if(!ok){
			if(isSpecialVerp(verp.realItemValue)){
				String s = getFirstNoun(verp);
				if(s == null)
					s = "Corrsponding Control";
				mm.put("sender", actor);
				mm.put("receiver", s);
			}
			else{
				String s = getFirstNoun(verp);
				String r = getLastNoun(verp);
				if(s == null)
					s = "Corrsponding Entity";
				if(r == null)
					r = "Corrsponding Entity";
				if(s.equals(r)){
					if(sterioTypeControl.equals(cls.getStereoType())){
						s = getActor();
					}
					else
						s = "From Corrsponding Control";
				}
				mm.put("sender", s);
				mm.put("receiver", r);
			}
		}
		return mm;
	}
}
