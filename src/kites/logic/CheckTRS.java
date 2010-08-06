/**
 * 
 */
package kites.logic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

import kites.TRSModel.ASTNode;
import kites.TRSModel.Rule;
import kites.TRSModel.RuleList;
import kites.TRSModel.Variable;
import kites.exceptions.NoChildrenException;
import kites.exceptions.SyntaxErrorException;

/**
 * @author sarek
 *
 */
public class CheckTRS {
	RuleList rulelist;
	
	public CheckTRS(RuleList rulelist) {
		this.rulelist = rulelist;
	}
	
	/**
	 * Check an instance for its compliance with a given signature.
	 * If a violation occurs, a <code>SyntaxErrorException</code> is thrown.
	 * 
	 * @param node The instance to be checked
	 * @param signature The signature to be checked against.
	 * @throws SyntaxErrorException
	 */
	public static void instanceCheck(ASTNode node, HashMap<String, Integer> signature) throws SyntaxErrorException {
		if(signature.containsKey(node.getName()) && signature.get(node.getName()) != node.getParamCount()) {
			throw new SyntaxErrorException("Parameter count for \"" + node.toString() + "\" violates signature. Expecting " + signature.get(node.getName()) + " parameters");
		}
		else {
			try {
				Iterator<ASTNode> childrenIt = node.getChildIterator();
				while(childrenIt.hasNext()) {
					instanceCheck(childrenIt.next(), signature);
				}
			}
			catch(NoChildrenException e) {
				// Do nothing, we simply reached a leaf node
			}
		}
	}
	
	/**
	 * Checks whether two rules in the rule set are unifiable.
	 * If that is the case a <code>SyntaxErrorException</code> is thrown.
	 * 
	 * @throws SyntaxErrorException
	 */
	public void unifiabilityCheck() throws SyntaxErrorException{
		Iterator<Rule> rules = rulelist.getRules();
		LinkedList<Rule> rules2 = rulelist.getRulesList();
		
		int pos = 1;
		
		while(rules.hasNext() && pos <= rules2.size()) {
			Iterator<Rule> it = rules2.listIterator(pos);
			Rule rule1 = rules.next();
			
			while(it.hasNext()) {
				ASTNode rightNode = it.next().getLeft();
				if(unifiable(rule1.getLeft(), rightNode, true)) {
					throw new SyntaxErrorException("The rules " + rule1.getLeft() + " and " + rightNode + " are unifiable");
				}
			}
			pos++;
		}
	}
	
	/**
	 * Checks whether two trees are unifiable.
	 * 
	 * @param left The one tree
	 * @param right The other tree
	 * @param match Initialize as true, unless you know what you are doing.
	 * @return <code>true</code> when trees are unifiable, <code>false</code> otherwise.
	 */
	private boolean unifiable(ASTNode left, ASTNode right, boolean match) {
		if(!(left instanceof Variable || right instanceof Variable) && left.getName() != right.getName()) {
			match = false;
		}
		else {
			try {
				Iterator<ASTNode> leftChildren = left.getChildIterator();
				Iterator<ASTNode> rightChildren = right.getChildIterator();
				
				while(leftChildren.hasNext() && rightChildren.hasNext()) {
					match = unifiable(leftChildren.next(), rightChildren.next(), match);
					if(match == false)
						break;		// stop going through the tree if we already know that it is not unifiable
									// with the other one.
				}
			}
			catch(NoChildrenException e) {
				// just return, maybe there are other nodes with children
				// if not we will return to the original caller.
			}
		}
		
		return match;
	}

	/**
	 * Check whether the rule system is consistent, i. e. whether nodes with the same
	 * name always have the same parameter count.
	 * The first occurence of a node determines the signature. If
	 * a mismatch is encountered this throws a <code>SyntaxErrorException</code>.
	 * 
	 * @return The signature of the rule set
	 * @throws SyntaxErrorException
	 */
	public HashMap<String, Integer> signatureCheck() throws SyntaxErrorException {
		HashMap<String, Integer> signature = new HashMap<String, Integer>();
		
		Iterator<Rule> it = rulelist.getRules();
		while(it.hasNext()) {
			Rule r = it.next();
			sigCheckNode(signature, r.getLeft());
			sigCheckNode(signature, r.getRight());
		}
		
		return signature;
	}

	/**
	 * Create the signature for a (sub-) tree.
	 * Checks the tree for compliance with an already existing signature
	 * and adds new nodes that are not yet present in the signature.
	 * If a mismatch is encountered, this throws a <code>SyntaxErrorException</code>
	 *  
	 * @param signature The pre-existing signature
	 * @param node The node to be checked
	 * @throws SyntaxErrorException
	 */
	private void sigCheckNode(HashMap<String, Integer> signature, ASTNode node) throws SyntaxErrorException {
		if(signature.containsKey(node.getName())) {
			if(signature.get(node.getName()) != node.getParamCount())
				throw new SyntaxErrorException("Wrong parameter count for " + node + " on line " + node.getLine());
		}
		else {
			signature.put(node.getName(), node.getParamCount());
		}
		
		try {
			Iterator<ASTNode> children = node.getChildIterator();
			
			while(children.hasNext()) {
				sigCheckNode(signature, children.next());
			}
		}
		catch(NoChildrenException e) {
			// Do nothing
		}
	}
	
	/**
	 * Check the set of rules for their compliance with left-linearity
	 * and also check whether there are variables used on the right-hand
	 * side of a rule that were not used on the left-hand side.
	 * When violations occur, a <code>SyntaxErrorException</code> is thrown.
	 * 
	 * @throws SyntaxErrorException
	 */
	public void variableCheck() throws SyntaxErrorException {
		Iterator<Rule> it = rulelist.getRules();
		
		while(it.hasNext()) {
			variableCheckRule(it.next());
		}
	}
	
	/**
	 * Check for left-linearity of a rule.
	 * 
	 * This gathers a list of the variables used on the left-hand side of a rule.
	 * If a variable is used more than once on the left-hand side this throws a
	 * <code>SyntaxErrorException</code> with an appropriate message.
	 * Then the method goes on and checks if a variable that was not used on the
	 * left-hand side was used on the right-hand side. If this is the case, a
	 * <code>SyntaxErrorException</code> with an appropriate message is thrown.
	 * 
	 * @param rule The rule to be checked
	 * @throws SyntaxErrorException See description
	 */
	private void variableCheckRule(Rule rule) throws SyntaxErrorException {
		// Create a list of variables on left side
		// and check for multiple usage at the same time
		HashSet<String> variables = GetVariables(rule.getLeft(), new HashSet<String>());
			
		// Check for usage of variables on the right side which were
		// not used on left side.
		CheckVarRightSide(rule.getRight(), variables);
	}
	
	/**
	 * Checks whether variables not in <code>variables</code> is present in the tree
	 * <code>node</code> and throws a <code>SyntaxErrorException</code> if this is
	 * the case.
	 * 
	 * @param node The (sub-) tree to be checked
	 * @param variables Set of variables to check against
	 * @throws SyntaxErrorException
	 */
	private void CheckVarRightSide(ASTNode node, HashSet<String> variables) throws SyntaxErrorException {
		try {
			Iterator<ASTNode> children = node.getChildIterator();
			while(children.hasNext()) {
				CheckVarRightSide(children.next(), variables);
			}
		}
		catch(NoChildrenException e) {
			if(node instanceof Variable) {
				if(!variables.contains(node.getName())) {
					throw new SyntaxErrorException("Variable " + node.getName() + "used on right hand side of rule in spite of not being declared on left hand side. Line: " + node.getLine());
				}
			}
		}
		
	}

	/**
	 * Creates a set of variables used in a tree.
	 * If a variable is encountered more than once, a <code>SyntaxErrorException</code>
	 * is thrown, as this violates left-linearity.
	 * 
	 * @param node The tree to be checked
	 * @param variables Temporary cache of already gathered variables. Initialize with
	 * 		an empty <code>HashSet<String></code> unless you know what you are doing.
	 * @return The set of variables used in the tree
	 * @throws SyntaxErrorException
	 */
	private HashSet<String> GetVariables(ASTNode node, HashSet<String> variables) throws SyntaxErrorException {
		try {
			Iterator<ASTNode> children = node.getChildIterator();
			while(children.hasNext()) {
				variables = GetVariables(children.next(), variables);
			}
		}
		catch(NoChildrenException e) {
			if(node instanceof Variable) {
				if(variables.contains(node.getName())) {
					throw new SyntaxErrorException("Rule on line " + node.getLine() + " is not left-linear.");
				}
				else {
					variables.add(node.getName());
				}
			}
		}
		
		return variables;
	}
}
