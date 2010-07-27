package kites.TRSModel;

import java.util.Iterator;
import java.util.LinkedList;

public class RuleList {
	LinkedList<Rule> rules;
	
	public void add(Rule rule) {
		rules.add(rule);
	}
	
	public Iterator<Rule> getRules() {
		return rules.iterator();
	}
}
