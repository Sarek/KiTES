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
			System.out.println("Returning LO decomp");
			return loDecomp(rulelist, instance, matches);
		
		case S_RO:
			System.out.println("Returning RO decomp");
			return roDecomp(rulelist, instance, matches);
			
		case S_NDET:
			System.out.println("Returning NDET decomp");
			return ndetDecomp(rulelist, instance, matches);
			
		case S_TRS:
			System.out.println("Returning TRS decomp");
			return trsDecomp(rulelist, instance, matches);
		
		case S_LI:
			System.out.println("Returning LI decomp");
			return liDecomp(rulelist, instance, matches);
			
		case S_RI:
			System.out.println("Retutning RI decomp");
			//return riDecomp(rulelist, instance, matches); // TODO: riDecomp()
			
		default:
			throw new DecompositionException("No such decomposition is available");
		}
	}

	private static LinkedHashMap<ASTNode, LinkedList<Rule>> liDecomp(
			RuleList rulelist, ASTNode instance,
			LinkedHashMap<ASTNode, LinkedList<Rule>> matches) {
		// TODO Auto-generated method stub
		return null;
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

	private static LinkedHashMap<ASTNode, LinkedList<Rule>> roDecomp(RuleList rulelist, ASTNode node, LinkedHashMap<ASTNode,LinkedList<Rule>> matches) throws SyntaxErrorException {
		Iterator<Rule> ruleIterator = rulelist.getRules();
		while(ruleIterator.hasNext()) {
			Rule rule = ruleIterator.next();
			if(match(rule.getLeft(), node)) {
				if(!matches.containsKey(node)) {
					matches.put(node, new LinkedList<Rule>());
				}
				matches.get(node).add(rule);
				return matches;
			}
		}
		
		try {
			Iterator<ASTNode> childrenIt = node.getRevChildIterator(); // Traverse children from right to left
			while(childrenIt.hasNext()) {
				matches = loDecomp(rulelist, childrenIt.next(), matches);
				if(!matches.isEmpty()) {
					return matches;
				}
			}
		}
		catch(NoChildrenException e) {
			// Do nothing. We simply reached a leaf node.
		}
		
		return matches;
	}

	private static LinkedHashMap<ASTNode, LinkedList<Rule>> loDecomp(RuleList rulelist, ASTNode node, LinkedHashMap<ASTNode, LinkedList<Rule>> matches) throws SyntaxErrorException {
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
		
		try {
			Iterator<ASTNode> childrenIt = node.getChildIterator(); // Traverse children from left to right
			while(childrenIt.hasNext()) {
				matches = loDecomp(rulelist, childrenIt.next(), matches);
				if(!matches.isEmpty()) {
				}
			}
		}
		catch(NoChildrenException e) {
			// Do nothing. We simply reached a leaf node.
		}
		
		return matches;
	}
	
	private static boolean match(ASTNode rule, ASTNode node) throws SyntaxErrorException {
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
