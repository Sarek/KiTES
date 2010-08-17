package kites.logic;

import java.util.HashMap;
import java.util.Iterator;

import kites.TRSModel.ASTNode;
import kites.TRSModel.CodificationContainer;
import kites.TRSModel.Constant;
import kites.TRSModel.Function;
import kites.TRSModel.Rule;
import kites.TRSModel.RuleList;
import kites.TRSModel.Variable;
import kites.exceptions.NoChildrenException;

public class Codification {
	private HashMap<String,CodificationContainer> codes;
	private int curNumFunction;
	private int curNumVariable;
	private RuleList rulelist;
	private ASTNode instance;

	public Codification(RuleList rulelist, ASTNode instance) {
		this.codes = new HashMap<String,CodificationContainer>();
		this.curNumFunction = 0;
		this.curNumVariable = 0;
		this.rulelist = rulelist;
		this.instance = instance;
	}
	
	public void codify() {
		// Create standard form of the rulelist
			// Create translation map of functions and variables in rules
		Iterator<Rule> ruleIt = rulelist.getRules();
		while(ruleIt.hasNext()) {
			Rule rule = ruleIt.next();
			// Look at left side
			createMap(rule.getLeft());
			
			// Look at right side
			createMap(rule.getRight());
		}
		
		// Create new list of rules in standard form using translation map
		codifyRuleList();
		
		// Create standard form of the instance
			// (Possibly) extend translation map of functions and variables
		createMap(instance);
			// Create new instance tree in standard form
		codifyTree(instance, true);
	}

	private ASTNode codifyTree(ASTNode node, boolean rightmost) {
		// new cons
		ASTNode cons = new Function("cons");
		cons.add(genCode(node));
		
		try {
			Iterator<ASTNode> childIt = node.getChildIterator();
			
			while(childIt.hasNext()) {
				cons.add(codifyTree(childIt.next(), !childIt.hasNext() && rightmost));
			}
		}
		catch(NoChildrenException e) {
			ASTNode empty = new Constant("empty");
			cons.add(empty);
		}
		
		return cons;
	}
	
	private ASTNode genCode(ASTNode node) {
		ASTNode retval;
		if(node instanceof Variable) {
			retval = new Function("var");
		}
		else {
			retval = new Function("fun");
		}
		
		retval.add(genNumber(codes.get(node.getName()).number));
		
		return retval;
	}
	
	private ASTNode genNumber(int number) {
		if(number > 0) {
			ASTNode retval = new Function("suc");
			retval.add(genNumber(number - 1));
			return retval;
		}
		else {
			return new Constant("zero");
		}
	}

	private void codifyRuleList() {
		ASTNode firstCons, secondCons, retval = null, toAdd = null;
		
		Iterator<Rule> ruleIt = rulelist.getRules();
		while(ruleIt.hasNext()) {
			Rule rule = ruleIt.next();
			firstCons = new Function("cons");
			secondCons = new Function("cons");
			
			secondCons.add(codifyTree(rule.getLeft(), true));
			secondCons.add(codifyTree(rule.getRight(), true));
			firstCons.add(secondCons);

			if(retval == null) {
				retval = firstCons;
				toAdd = retval;
			}
			else {
				toAdd.add(firstCons);
				toAdd = firstCons;
			}
		}
	}

	private void createMap(ASTNode node) {
		if(!codes.containsKey(node.getName())) {
			if(node instanceof Variable) {
				CodificationContainer data = new CodificationContainer(node.getParamCount(), curNumVariable);
				curNumVariable++;
				codes.put(node.getName(), data);
			}
			else {
				CodificationContainer data = new CodificationContainer(node.getParamCount(), curNumFunction);
				curNumFunction++;
				codes.put(node.getName(), data);
			}
		}
		
		try {
			Iterator<ASTNode> childIt = node.getChildIterator();
			while(childIt.hasNext()) {
				createMap(childIt.next());
			}
		}
		catch(NoChildrenException e) {
			// do nothing. we simply reached a leaf node
		}
	}
}
