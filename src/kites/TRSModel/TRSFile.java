package kites.TRSModel;

import java.util.Iterator;
import java.util.LinkedList;

public class TRSFile {
	LinkedList<Rule> rules;
	ASTNode instance;
	
	public TRSFile() {
		rules = new LinkedList<Rule>();
		instance = null;
	}
	
	public void add(Rule rule) {
		rules.add(rule);
	}
	
	public void addInstance(ASTNode node) {
		instance = node;
	}
	
	public ASTNode getInstance() {
		return instance;
	}
	
	public Iterator<Rule> getRules() {
		return rules.iterator();
	}
	
	public LinkedList<Rule> getRulesList() {
		return rules;
	}
	
	public String toString() {
		String retval = "";
		Iterator<Rule> it = this.getRules();
		
		while(it.hasNext()) {
			Rule r = it.next();
			retval += r.toString() + "\n";
		}
		
		return retval;
	}
}
