package com.model.syntax;

import java.io.*;
import java.util.*;
public class Grammars {
	private List<Grammar> syntaxGrammars;
	private List<String> syntaxTokens;
	private Stack stack;
	private String [] predicates;
	private String[] realWords;
	private int counter;
	private boolean vgpAppears;
	public Grammars(){
		stack = new Stack();		
		syntaxTokens = new ArrayList<String>();
		syntaxGrammars = new ArrayList<Grammar>();
	}
	
	public void loadSyntaxFile(String path)throws Exception{
		/*
		 * Maher
		 * Input: Grammars File path
		 * It must contain %token to get tokens
		 * It must contain tow '%%' to know that grammars are inside
		 * Algorith
		 * read each line in file
		 * if it starts with %token, then we have to take them
		 * it line starts with %% then
		 *     we have to read grammars until we reach to another %%
		 * */
		File f = new File(path);
		BufferedReader br = new BufferedReader(new FileReader(f));
		String line;
		boolean grammarCase = false;
		while(br.ready() && !grammarCase){
			line = br.readLine();
			if(!grammarCase){
				if(line.startsWith("%token")){
					//read tokens
					line = line.substring("%token ".length(), line.length());
					line = line.trim();
					while(line.startsWith(" "))
						line = line.substring(1, line.length());
					String [] tokens = line.split(" ");
					for(int i=0; i<tokens.length; i++){
						if(!" ".equals(tokens[i])){
							tokens[i] = tokens[i].trim();
							while(tokens[i].startsWith(" "))
								tokens[i] = tokens[i].substring(1, tokens[i].length());
							this.syntaxTokens.add(tokens[i]);
						}
					}
					
				}
				else if(line.startsWith("%%"))
					grammarCase = true;
			}
		}
		boolean finished = false;
		List<String> grammarLines=null;
		String grammarName=null;
		while(br.ready() && !finished){
			line = br.readLine();
			if(line != null && !"".equals(line)){
				if(line.startsWith("%%"))
					finished = true;
				else{
					if(line.contains(":")){
						grammarLines = new ArrayList<String>();
						grammarName = line.substring(0, line.indexOf(":"));
					}
					else{
						if(line.startsWith(";")){
							readGrammarWithAllPossibleRights(grammarName, grammarLines);
							grammarName = null;
							grammarLines = null;
						}
						else
							grammarLines.add(line);
					}
				}
			}
		}
		br.close();
		
	}
	private void readGrammarWithAllPossibleRights(String grammarName, List<String> lines)throws Exception{
		/*
		 * Maher
		 * This function is to add single grammar with all right possibles
		 *  grammarName:
		 *      bossible_1
		 *      |bossible_2
		 *      |bossible_3
		 *      ;
		 * The input will contains grammar name as first parameter and list of this grammar' right grammars
		 * 
		 * Steps
		 * 1.enhance right grammars [remove spaces from beginning and ending
		 * */
		for(int i=0;i<lines.size(); i++){
			String line = lines.get(i);
			line = line.trim();
			String executer = null;
			if(line.contains("{")){
				executer = line.substring(line.indexOf("{")+1, line.indexOf("}"));
				executer = executer.trim();
				line = line.substring(0, line.indexOf("{")-1);
			}
			
			
			while(line != null && line.startsWith(" ") && line.length() > 0)
				line = line.substring(1, line.length());
			while(line != null && line.endsWith(" ") && line.length() > 0)
				line = line.substring(0, line.length()-1);
			if(line.startsWith("|"))
				line = line.substring(1, line.length());
			
			//============================================================
			Grammar g = new Grammar(grammarName);
			g.setGrammarExecuter(executer);
			int wStart = 0;
			line = line.trim();
			for(int k=0; k<line.length(); k++){
				if(line.charAt(k) == ' ' || line.charAt(k) == '\t' || line.charAt(k) == '\r'){
					String w = line.substring(wStart, k);
					if(w != null && !"".equals(w)){
						Item itm = new Item();
						itm.setName(w);
						for(int t=0;t<syntaxTokens.size()-1;t++)
							if(syntaxTokens.get(t).equalsIgnoreCase(w))
								itm.setType(1);
							else
								itm.setType(2);
						g.getGrammarItems().add(itm);
					}
					wStart = k+1;
				}
			}
			String w = line.substring(wStart, line.length());
			if(w != null && !"".equals(w)){
				Item itm = new Item();
				itm.setName(w);
				g.getGrammarItems().add(itm);
				this.syntaxGrammars.add(g);
			}
		}
	}
	
	public void print(){
		System.out.println("Tokens:");
		for(int i=0; i<this.syntaxTokens.size(); i++){
			System.out.println(this.syntaxTokens.get(i));
		}
		System.out.println("============================");
		System.out.println("Grammars");
		for(int i=0;i<this.syntaxGrammars.size(); i++){
			System.out.println(this.syntaxGrammars.get(i).getGrammarName()+", Executer:"+this.syntaxGrammars.get(i).getGrammarExecuter());
			for(int j=0; j<this.syntaxGrammars.get(i).getGrammarItems().size(); j++){
				System.out.println("\t"+this.syntaxGrammars.get(i).getGrammarItems().get(j).getName());
			}
		}
	}
	
	public Stack getStack() {
		return stack;
	}
	public List<Grammar> getSyntaxGrammars() {
		return syntaxGrammars;
	}
	public void setSyntaxGrammars(List<Grammar> syntaxGrammars) {
		this.syntaxGrammars = syntaxGrammars;
	}
	public List<String> getSyntaxTokens() {
		return syntaxTokens;
	}
	public void setSyntaxTokens(List<String> syntaxTokens) {
		this.syntaxTokens = syntaxTokens;
	}
	
	public void pushToStack(String token, String realWord){
		boolean checkIfReduceIsAvailable = beforPush(token);
		this.getStack().push(token, realWord);
		afterPush(checkIfReduceIsAvailable, realWord);
	}
	
	public StackItem popFromStack(){
		beforPop();
		StackItem si = this.getStack().pop();
		afterPop();
		return si;
	}
	
	private boolean beforPush(String token){
		return true;
	}
	
	private void afterPush(boolean isReduceAvialable, String realWord){
		if(isReduceAvialable){
			System.out.println("afterPush===============================");
			/*Algorithm:
			 * while ok 
			 *  ok = false
			 *  get stack items
			 *  put them oredernarly in list 'tempList'
			 *  while tempList is not finished
			 *  	check if list matches grammars (one by one)
			 *  	if ok, reduce list with matched grammar
			 *  		ok = true
			 *  	if not ok, remove first item from list and put it again into stack
			 *  		then repeate the check again
			 *  	
			*/
			
			boolean ok = true;
			while(ok){
				ok = false;
				//First Stage: Put All Stack Items into List 'tempList'
				List<StackItem> tempList1 = new ArrayList<StackItem>();
				System.out.println("Stack:");
				while(!getStack().isEmpty()){
					tempList1.add(getStack().pop());
					System.out.print(tempList1.get(tempList1.size()-1).item +" ");
				}
				System.out.println();
				//Add items to list correctly
				List<StackItem> tempList = new ArrayList<StackItem>();
				
				for(int i=tempList1.size(); i > 0 ; i--){
					tempList.add(tempList1.get(i-1));
				}
				while(tempList.size() != 0 && !ok){
					Grammar matchedGrammar = null;
					for(int k=0;k<syntaxGrammars.size() && matchedGrammar == null; k++){
						if(match(tempList, syntaxGrammars.get(k)))
							matchedGrammar = syntaxGrammars.get(k);
					}
					boolean mustReduce = false;
					if(matchedGrammar != null){
						mustReduce = true;
						if(tempList.get(0).item.toLowerCase().equals("vgp")&&
								(counter < predicates.length-1)){
							vgpAppears = true;
							mustReduce = false;
						}
						else{
							if(matchedGrammar.getGrammarName().equals("subject") && vgpAppears){
								mustReduce = false;
							}
						}
					}
					if(mustReduce){
						boolean spicialCase = false;
						//Added 14-7-2013:
						//Check if it is vgp np pp, then make it vgp np
						//which np is np pp
						if(tempList.size() == 3){
							if (	tempList.get(0).item.toLowerCase().equals("vgp")			&&
									tempList.get(1).item.toLowerCase().equals("np")			&&
									tempList.get(2).item.toLowerCase().equals("pp")){
								spicialCase = true;
							}
						}
						if(spicialCase){
							List<StackItem> l = new ArrayList<StackItem>();
							l.add(tempList.get(1));//Add np
							l.add(tempList.get(2));//Add pp
							StackItem part2 = new StackItem(l);
							part2.item = "np";
							part2.next = null;
							part2.realItemValue = realWord;
							part2.executer = null;
							tempList.remove(1);//Remove np
							tempList.remove(1);//Remove pp
							tempList.add(part2);
							//Now tempList is [0]:vgp, [1]:np
						//	getStack().push(part2);
						}
						//End Adding 14-7-2013
						else{
							StackItem newSI = new StackItem(tempList);
							newSI.item = matchedGrammar.getGrammarName();
							newSI.next = null;
							newSI.realItemValue = realWord;
							newSI.executer = matchedGrammar.getGrammarExecuter();
							getStack().push(newSI);
							ok = true;
						}
					}
					else{
						StackItem si = tempList.get(0);
						getStack().push(si);
						tempList.remove(0);
					}
				}
			}
			System.out.println("/afterPush===============================");
		}
	}
	
	private boolean match(List<StackItem> tempList, Grammar g){
		boolean ok = true;
		if(tempList == null && g.getGrammarItems() != null && g.getGrammarItems().size() > 0)
			ok = false;
		if(ok && tempList != null && (g.getGrammarItems() == null || g.getGrammarItems().size() == 0))
			ok = false;
		if(ok && tempList.size() != g.getGrammarItems().size())
			ok = false;
		if(ok){
			for(int i=0; i<tempList.size() && ok; i++){
				Item item = g.getGrammarItems().get(i);
				if(tempList.get(i).item == null)
					 ok = false;
				if(item == null || item.getName() == null || "".equals(item.getName()))
					ok = false;
				if(ok && (! tempList.get(i).item.toLowerCase().equals(item.getName().toLowerCase())))
					ok = false;
			}
		}
		return ok;
	}
	
	private void beforPop(){
		
	}
	
	private void afterPop(){
		
	}
	
	public void applySample(String samplePath){
		/*
		 * Start read the file
		 * Load DataBase.xml File
		 * start reading, check for word, push it in stack (Token, ReadlWord)
		 * in each push check the best grammar, and execute the mached function
		*/
		
	}
	
	public Map<String, Object> applySample(){
		/*
		 * start pushing the data into the stack
		 * for each push operation, check if there is ability to replace right grammars with its left one
		 * do this operations untill predicates array is finished
		 * If there is one grammar in stack, return its executes
		 * else return err
		 * */
		//For each sentence empty the stack
		getStack().setTop(null);
		vgpAppears = false;
		Map<String, Object> obj = new HashMap<String, Object>();
		if(predicates == null || realWords == null 
				|| predicates.length == 0 || realWords.length == 0){
			obj.put("error", "Empty Predicate or realWords Arraies");
		}
		else{
			counter = 0;
			while(counter<predicates.length){
				String currentPredicate = predicates[counter];
				if(currentPredicate != null && !"".equals(currentPredicate)){
					this.pushToStack(currentPredicate, realWords[counter]);
				}
				counter++;
			}
			if(this.getStack().isEmpty() || this.getStack().getTop().next != null)
				obj.put("error", "Not Correct Language, Stack Is Empty Or items number > 1");
			else{
				//One Item in Stack
				if(getStack().getTop().item.equals("statement")){
					obj.put("from_lang","true");
					//Get Subject as array
					
					StackItem subStkItem = getStack().getTop().comeFrom.get(0);
										
					//Get Predicate As Array
					StackItem predicate = getStack().getTop().comeFrom.get(1);
					StackItem verpGrammar = predicate.comeFrom.get(0);
					String executer = verpGrammar.executer;
					
					obj.put("subject", subStkItem);
					obj.put("verp", verpGrammar);
					obj.put("executer", executer);
					
				}
				else{
					obj.put("from_lang","false");
				}
			}
		}
		return obj;
	}

	public String[] getPredicates() {
		return predicates;
	}

	public void setPredicates(String[] predicates) {
		this.predicates = predicates;
	}

	public String[] getRealWords() {
		return realWords;
	}

	public void setRealWords(String[] realWords) {
		this.realWords = realWords;
	}
}