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
 * This class provides means for generating decompositions in
 * two modes:
 * <ul>
 * <li>Decomposition as term rewrite system</li>
 * <li>Decomposition as non-deterministic program</li>
 * </ul>
 * 
 * Decomposition for execution in program mode is performed at
 * the same time as the rewrite and can therefore be found in
 * <code> ProgramRewrite</code>.
 * 
 * Non-deterministic decomposition is available in two different
 * flavours: LO and RO.
 * 
 * For a detailed description of execution modes and strategies
 * please refer to my bachelor thesis.
 * 
 * @author sarek
 */
public class Decomposition {
	/** Use LO decomposition */
	public static final int S_LO = 1;
	/** Use RO decomposition */
	public static final int S_RO = 2;
	/** Use LI decomposition */
	public static final int S_LI = 5;
	/** Use RI decomposition */
	public static final int S_RI = 6;
	
	/** Execution in program mode */
	public static final int M_PROGRAM = 0;
	/** Execution in non-deterministic mode */
	public static final int M_NONDET = 1;
	/** Execution in term rewrite system mode */
	public static final int M_TRS = 2;
	
	/**
	 * find all possible nodes that can be rewritten and return them
	 * along with the rule that has to be used for performing the rewrite.
	 * 
	 * @param type The execution mode
	 * @param rulelist The <code>RuleList</code> to be applied
	 * @param instance The instance to be rewritten
	 * @return Map of nodes and the rules which can be applied unto them
	 * @throws DecompositionException If a non-existing decomposition is to be used
	 * @throws SyntaxErrorException If a syntax error in the <code>RuleList</code> or instance is encountered.
	 */
	public static LinkedHashMap<ASTNode, LinkedList<Rule>> getDecomp(int type, RuleList rulelist, ASTNode instance) throws DecompositionException, SyntaxErrorException {
		LinkedHashMap<ASTNode, LinkedList<Rule>> matches = new LinkedHashMap<ASTNode, LinkedList<Rule>>();
		
		switch(type) {
		case M_NONDET:
			return ndetDecomp(rulelist, instance);
			
		case M_TRS:
			return trsDecomp(rulelist, instance, matches);
		default:
			throw new DecompositionException("No such decomposition is available");
		}
	}

	/**
	 * Return possible rewrites in TRS mode - which is all possible rewrites.
	 * 
	 * @param rulelist The <code>RuleList</code> to be applied
	 * @param node The node to be checked. Initialize with root of tree.
	 * @param matches Map of possible rewrites that were already found. Initialize as empty.
	 * @return Map of nodes and the rules which can be applied unto them.
	 * @throws SyntaxErrorException
	 */
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

	/**
	 * This checks for all possible rewrites according to the non-deterministic
	 * decomposition rules.
	 * Such a decomposition consists of all outermost rewrite possibilities.
	 * 
	 * @param rulelist The rule set specifying the rewrites
	 * @param node The tree to be checked
	 * @return
	 * @throws SyntaxErrorException 
	 */
	private static LinkedHashMap<ASTNode, LinkedList<Rule>> ndetDecomp(RuleList rulelist, ASTNode node) throws SyntaxErrorException {
		// check, if this node is a match
		Iterator<Rule> ruleChildren = rulelist.getRules();
		LinkedHashMap<ASTNode,LinkedList<Rule>> matches = new LinkedHashMap<ASTNode, LinkedList<Rule>>();
		boolean match = false;
		
		while(ruleChildren.hasNext()) {
			Rule rule = ruleChildren.next();
			if(match(rule.getLeft(), node)) {
				if(!matches.containsKey(node)) {
					matches.put(node, new LinkedList<Rule>());
				}
				matches.get(node).add(rule);
				match = true;
			}
		}
		
		if(!match) {
			try {
				Iterator<ASTNode> childIt = node.getChildIterator();
				Iterator<ASTNode> revChildIt = node.getRevChildIterator();
				ASTNode left = childIt.next();
				ASTNode right = revChildIt.next();
				
				// if the root node has only one child we have to call this
				// method again. If we would not do that, the match would be
				// noted twice and we would not get both the leftmost and
				// rightmost decompositions.
				if(left == right) {
					System.out.println("Left == right, using the left");
					matches = ndetDecomp(rulelist, left);
				}
				else {
					matches = ndetLODecomp(rulelist, node, matches);
					matches = ndetRODecomp(rulelist, node, matches);
				}
			}
			catch(NoChildrenException e) {
				// Do nothing, we simply reached a leaf node
				// yes, it really is a leaf although it is the root node
			}
		}
		
		return matches;
	}
	
	/**
	 * Calculate a leftmost-outermost decomposition for use with non-deterministic interpretation.
	 * 
	 * @param rulelist The rule set specifying the rewrites
	 * @param node The tree to be checked
	 * @param matches A set of pre-existing matches
	 * @return
	 * @throws SyntaxErrorException 
	 */
	private static LinkedHashMap<ASTNode, LinkedList<Rule>> ndetLODecomp(RuleList rulelist, ASTNode node, LinkedHashMap<ASTNode, LinkedList<Rule>> matches) throws SyntaxErrorException {
		System.out.println("ndetLODecomp: " + node);
		// check for a match in this node
		boolean match = false;
		Iterator<Rule> ruleChildren = rulelist.getRules();
		
		while(ruleChildren.hasNext()) {
			Rule rule = ruleChildren.next();
			if(match(rule.getLeft(), node)) {
				System.out.println("Match found");
				if(!matches.containsKey(node)) {
					matches.put(node, new LinkedList<Rule>());
				}
				matches.get(node).add(rule);
				match = true;
			}
		}
		
		// no match, go deeper into the tree
		if(!match) {
			try {
				Iterator<ASTNode> childIt = node.getChildIterator();
				matches = ndetLODecomp(rulelist, childIt.next(), matches);
			}
			catch(NoChildrenException e) {
				// Do nothing. We simply reached a leaf node.
			}
		}
		
		return matches;
	}

	/**
	 * Calculate a rightmost-outermost decomposition for use with non-deterministic interpretation.
	 * 
	 * @param rulelist The rule set specifying the rewrites
	 * @param node The tree to be checked
	 * @param matches A set of pre-existing matches
	 * @return
	 * @throws SyntaxErrorException 
	 */

	private static LinkedHashMap<ASTNode, LinkedList<Rule>> ndetRODecomp(RuleList rulelist, ASTNode node, LinkedHashMap<ASTNode, LinkedList<Rule>> matches) throws SyntaxErrorException {
		System.out.println("ndetRODecomp: " + node);
		// check for a match in this node
		boolean match = false;
		Iterator<Rule> ruleChildren = rulelist.getRules();
		
		while(ruleChildren.hasNext()) {
			Rule rule = ruleChildren.next();
			if(match(rule.getLeft(), node)) {
				System.out.println("Match found");
				if(!matches.containsKey(node)) {
					matches.put(node, new LinkedList<Rule>());
				}
				matches.get(node).add(rule);
				match = true;
			}
		}
		
		// no match found, go deeper into the tree
		if(!match) {
			try {
				Iterator<ASTNode> childIt = node.getRevChildIterator();
				matches = ndetRODecomp(rulelist, childIt.next(), matches);
			}
			catch(NoChildrenException e) {
				// Do nothing. We simply reached a leaf node.
			}
		}
		
		return matches;
	}

	/**
	 * Checks whether two trees match.
	 * A match is achieved if they are structurally equal and the symbols used
	 * match each other. The only exception are <code>Variable</code>s which match
	 * everything including subtrees.
	 * 
	 * @param rule The rule (or tree 1)
	 * @param node The tree (or tree 2)
	 * @return true if both trees match, false otherwise
	 * @throws SyntaxErrorException
	 */
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
