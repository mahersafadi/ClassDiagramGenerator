package com.xml;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.*;

import com.controller.OurClass;
public class XMLResult {
	public List<OurClass> classes;
	
	public XMLResult(){
		
	}
	
	public List<OurClass> merge(List<OurClass> l){
		List<OurClass> newL = new ArrayList<OurClass>();
		for(int i=0; i<l.size(); i++){
			OurClass oc = l.get(i);
			int k = 0;
			while(k<newL.size() && !newL.get(k).getClassName().equalsIgnoreCase(oc.getClassName()))
				k++;
			
			if(k >= newL.size()){
				//it is not exist in newL
				newL.add(oc);
				
			}
			else{
				//it is exist previously
				//merge attributes and methods
				//merge attributes
				OurClass ocInNewL = newL.get(k);
				List<String> attr1 = oc.getAttributes();
				List<String> attr2 = ocInNewL.getAttributes();
				Map<String, String> mm = new HashMap<String, String>();
				for(int c = 0; c<attr1.size(); c++)
					mm.put(attr1.get(c).toLowerCase(), attr1.get(c).toLowerCase());
				for(int c = 0; c<attr2.size(); c++)
					mm.put(attr2.get(c).toLowerCase(), attr2.get(c).toLowerCase());
				attr1.clear();
				Iterator<String> it = mm.keySet().iterator();
				while(it.hasNext()){
					String key = it.next();
					attr1.add(key);
				}
				ocInNewL.setAttributes(attr1);

				//merge methods
				List<String> meth1 = oc.getMethod();
				List<String> meth2 = ocInNewL.getMethod();
				mm = new HashMap<String, String>();
				for(int c = 0; c<meth1.size(); c++)
					mm.put(meth1.get(c), meth1.get(c));
				for(int c = 0; c<meth2.size(); c++)
					mm.put(meth2.get(c), meth2.get(c));
				meth1.clear();
				it = mm.keySet().iterator();
				while(it.hasNext()){
					String key = it.next();
					if(!key.endsWith("()"))
							key += "()";
					meth1.add(key);
				}
				ocInNewL.setMethod(meth1);
			}
		}
		return newL;
	}
	
	public String generate(){
		String result = "";
		try{
			result = "<?xml  version=\"1.0\" encoding=\"utf-8\" ?>";
			result += "<class-diagram>";
			result += "<classes>\n";
			for(int i=0; i<classes.size(); i++){
				OurClass cls = classes.get(i);
				result += "	<class>\n";
				result += "		<name>\n";
				result += "			"+cls.getClassName()+"\n";
				result += "		</name>\n";
				result += "		<steriotype>";
				result += "			"+cls.getStereoType();
				result += "		</steriotype>";
				result += "		<CD>";
				switch (cls.getCD()) {
				case 1:
					result += "			low";
					break;
				case 2:
					result += "			medium";
					break;
				case 3:
					result += "			high";
					break;
				}
				
				result += "		</CD>";
				result += "		<attributes>\n";
				if(cls.getAttributes() != null){
					for(int k=0; k<cls.getAttributes().size(); k++){
						if(!cls.getClassName().equalsIgnoreCase(cls.getAttributes().get(k))){
							result += "			<attribute>";
							result += "			"+cls.getAttributes().get(k)+"\n";
							result += "			</attribute>";
						}
					}
				}
				result += "		</attributes>\n";
				result += "		<methods>";
				if(cls.getMethod() != null){
					for(int k=0; k<cls.getMethod().size(); k++){
						result += "			<method>";
						result += "				"+cls.getMethod().get(k);
						result += "			</method>";
					}
				}
				result += "		</methods>";
				result += "	</class>";
			}
			result += "</classes>";
			result += "<messages>";
			result += this.analyseMessages();
			result += "</messages>";
			result += "</class-diagram>";
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			
			String fileName = c.get(c.YEAR)+"-"+c.get(c.MONTH)+"-"+c.get(c.DATE)+"-"+c.get(c.HOUR)+"-"+c.get(c.MINUTE)+"-"+c.get(c.SECOND)+".xml";
			File f = new File("D:/GenerateResults");
			f.mkdirs();
			fileName = "D:/GenerateResults/"+fileName;
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(fileName)));
			bw.write(result);
			bw.close();
			result = "Done Successfully: Saved In File "+fileName;
		}
		catch(Exception e){
			e.printStackTrace();
			result = "Error: "+e.getMessage().substring(0, 100);
		}
		return result;
	}
	
	private String analyseMessages(){
		//For each class in classes check kind and generate the properate messages
		try{
			String str = "";
			for(int i=0; i<classes.size(); i++){
				OurClass cls = classes.get(i);
				try{
					Map<String, String> mm = cls.getSentence().getMessages(cls);
					if(mm.get("sender") != null && mm.get("receiver") != null){
						str += "				<msg>";
						str += "					<name>"+mm.get("msg")+"</name>";
						str += "					<sender>"+mm.get("sender")+"</sender>";
						str += "					<receiver>"+mm.get("receiver")+"</receiver>";
						str += "				</msg>";
					}
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
			return str;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return "";
	}
	
}
