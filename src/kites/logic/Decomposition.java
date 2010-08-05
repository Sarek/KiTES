/**
 * 
 */
package kites.logic;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import kites.TRSModel.Constant;
import kites.TRSModel.Rule;
import kites.TRSModel.ASTNode;
import kites.TRSModel.RuleList;
import kites.TRSModel.Variable;
import kites.exceptions.DecompositionException;
import kites.exceptions.NoChildrenException;
import kites.exceptions.SyntaxErrorException;

/**
 * @author sarek
 *
 */
public class Decomposition {
	public static final int S_LO = 1;
	public static final int S_RO = 2;
	public static final int S_NDET = 3;
	public static final int S_TRS = 4;
	public static final int S_LI = 5;
	public static final int S_RI = 6;
	
	public static int M_PROGRAM = 0;
	public static int M_NONDET = 1;
	public static int M_TRS = 2;
	
	
	public static LinkedHashMap<ASTNode, LinkedList<Rule>> getDecomp(int type, RuleList rulelist, ASTNode instance) throws DecompositionException, SyntaxErrorException {
		LinkedHashMap<ASTNode, LinkedList<Rule>> matches = new LinkedHashMap<ASTNode, LinkedList<Rule>>();
		
		switch(type) {
		case S_LO:
			return loDecomp(rulelist, instance);
		
		case S_RO:
			return roDecomp(rulelist, instance);
			
		case S_NDET:
			return ndetDecomp(rulelist, instance, matches);
			
		case S_TRS:
			return trsDecomp(rulelist, instance, matches);
		
		case S_LI:
			return liDecomp(rulelist, instance);
			
		case S_RI:
			return riDecomp(rulelist, instance);
			
		default:
			throw new DecompositionException("No such decomposition is available");
		}
	}

	private static LinkedHashMap<ASTNode, LinkedList<Rule>> trsDecomp(RuleList rulelist, ASTNode node, LinkedHashMap<ASTNode,LinkedList<Rule>> matches) throws SyntaxErrorException {
		// The decomposition for the execution as a TRS is quite easy, because we have no specific execution order
		// and can apply whatever rule we want at whatever position in the tree we want
		// So basically we just gather all possible matches for all nodes.
		
		Iterator<Rule> ruleIterator = rulelist.getRules();
		while(ruleIterator.hasNext()) {
			Rule rule = ruleIterator.next();
			if(match(rule.getLeft(), node)) {
				if(!matches.containsKey(node)) {
					matches.put(node, new LinkedList<Rule>());
				}
				matches.get(node).add(rule);
			}
		}
		
		// Add matches for children
		try {
			Iterator<ASTNode> childrenIt = node.getChildIterator();
			while(childrenIt.hasNext()) {
				matches = trsDecomp(rulelist, childrenIt.next(), matches);
			}
		}
		catch(NoChildrenException e) {
			// Do nothing. We simply reached a leaf node.
		}
		
		return matches;
	}

	private static LinkedHashMap<ASTNode, LinkedList<Rule>> ndetDecomp(RuleList rulelist, ASTNode node, LinkedHashMap<ASTNode,LinkedList<Rule>> matches) {
		// TODO Auto-generated method stub
		return null;
	}

	private static LinkedHashMap<ASTNode, LinkedList<Rule>> roDecomp(RuleList rulelist, ASTNode instance) throws SyntaxErrorException {
		LinkedHashMap<ASTNode, LinkedList<Rule>> retval = new LinkedHashMap<ASTNode, LinkedList<Rule>>();
		
		Iterator<Rule> ruleIt = rulelist.getRules();
		while(ruleIt.hasNext()) {
			Rule rule = ruleIt.next();
			if(match(rule.getLeft(), instance)) {
				if(!retval.containsKey(instance)) {
					retval.put(instance, new LinkedList<Rule>());
				}
				retval.get(instance).add(rule);
			}
		}
		
		if(retval.isEmpty()) {
			try {
				retval = riDecomp(rulelist, instance.getRevChildIterator().next());
			}
			catch(NoChildrenException e) {
				retval = new LinkedHashMap<ASTNode, LinkedList<Rule>>();
			}
		}
		
		return retval;
	}

	private static LinkedHashMap<ASTNode, LinkedList<Rule>> riDecomp(RuleList rulelist, ASTNode instance) throws SyntaxErrorException {
		LinkedHashMap<ASTNode, LinkedList<Rule>> retval;
		try {
			retval = riDecomp(rulelist, instance.getRevChildIterator().next());
		}
		catch(NoChildrenException e) {
			retval = new LinkedHashMap<ASTNode, LinkedList<Rule>>();
		}
		
		if(retval.isEmpty()) {
			Iterator<Rule> ruleIt = rulelist.getRules();
			while(ruleIt.hasNext()) {
				Rule rule = ruleIt.next();
				if(match(rule.getLeft(), instance)) {
					if(!retval.containsKey(instance)) {
						retval.put(instance, new LinkedList<Rule>());
					}
					retval.get(instance).add(rule);
				}
			}
		} 
		return retval;
	}

	
	private static LinkedHashMap<ASTNode, LinkedList<Rule>> loDecomp(RuleList rulelist, ASTNode instance) throws SyntaxErrorException {
		LinkedHashMap<ASTNode, LinkedList<Rule>> retval = new LinkedHashMap<ASTNode, LinkedList<Rule>>();
		
		Iterator<Rule> ruleIt = rulelist.getRules();
		while(ruleIt.hasNext()) {
			Rule rule = ruleIt.next();
			if(match(rule.getLeft(), instance)) {
				if(!retval.containsKey(instance)) {
					retval.put(instance, new LinkedList<Rule>());
				}
				retval.get(instance).add(rule);
			}
		}
		
		if(retval.isEmpty()) {
			try {
				retval = riDecomp(rulelist, instance.getChildIterator().next());
			}
			catch(NoChildrenException e) {
				retval = new LinkedHashMap<ASTNode, LinkedList<Rule>>();
			}
		}
		
		return retval;
	}
	
	private static LinkedHashMap<ASTNode, LinkedList<Rule>> liDecomp(RuleList rulelist, ASTNode instance) throws SyntaxErrorException {
		LinkedHashMap<ASTNode, LinkedList<Rule>> retval;
		try {
			retval = riDecomp(rulelist, instance.getChildIterator().next());
		}
		catch(NoChildrenException e) {
			retval = new LinkedHashMap<ASTNode, LinkedList<Rule>>();
		}
		
		if(retval.isEmpty()) {
			Iterator<Rule> ruleIt = rulelist.getRules();
			while(ruleIt.hasNext()) {
				Rule rule = ruleIt.next();
				if(match(rule.getLeft(), instance)) {
					if(!retval.containsKey(instance)) {
						retval.put(instance, new LinkedList<Rule>());
					}
					retval.get(instance).add(rule);
				}
			}
		} 
		return retval;
	}

	
	public static boolean match(ASTNode rule, ASTNode node) throws SyntaxErrorException {
		boolean retval = false;
		
		if(rule instanceof Variable || node instanceof Variable) {
			return true;
		}
		if(rule.getName().equals(node.getName())) {
			retval = true;
			
			try {
				Iterator<ASTNode> ruleChildren = rule.getChildIterator();
				Iterator<ASTNode> nodeChildren = node.getChildIterator();

				while(ruleChildren.hasNext()) {
					if(!match(ruleChildren.next(), nodeChildren.next())) {
						retval = false;
					}
				}
			}
			catch(NoChildrenException e) {
				if(rule instanceof Constant && node instanceof Constant) {
					retval = true; // we are still inside the if, so the names match
				}
				else {
					// Something very weird happened:
					// The elements have the same names, but are not of the same type
					// We should really do better syntax checking to prevent this!
					throw new SyntaxErrorException("Mismatch between equally named nodes: One is a constant, the other a function! Rule: " + rule + ", Node: " + node);
				}
			}
			catch(NoSuchElementException e) {
				// Now something equally weird happened:
				// The elements number of parameters is not same, although their names are equal.
				throw new SyntaxErrorException("The number of parameters of " + rule + " and " + node + "is not equal, although they have the same names");
			}
		}
		return retval;
	}

}
