package kites.TRSModel;

import java.util.Iterator;
import java.util.LinkedList;

public class RuleList {
	LinkedList<Rule> rules;
	
	public RuleList() {
		rules = new LinkedList<Rule>();
	}
	
	public void add(Rule rule) {
		rules.add(rule);
	}
	
	public Iterator<Rule> getRules() {
		return rules.iterator();
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
