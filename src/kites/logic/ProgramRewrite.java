/**
 * 
 */
package kites.logic;

import java.util.HashMap;
import java.util.Iterator;

import kites.TRSModel.ASTNode;
import kites.TRSModel.Constant;
import kites.TRSModel.Function;
import kites.TRSModel.Rule;
import kites.TRSModel.TRSFile;
import kites.TRSModel.Variable;
import kites.exceptions.NoChildrenException;
import kites.exceptions.NoRewritePossibleException;
import kites.exceptions.SyntaxErrorException;
import kites.visual.MsgBox;

/**
 * @author sarek
 * Rewrite a (sub-) tree using a rule.
 * This transforms the subtree following the rule, i. e. symbols are replaced
 * and variables are assigned their values.
 * 
 * @return The transformed (reduced) (sub-) tree.
 */
public class ProgramRewrite {
	boolean rewriteDone;
	int strategy;
	ASTNode instance;
	TRSFile rulelist;
	
	/**
	 * Initialize class for use.
	 * 
	 * @param strategy The strategy (LO, RO, LI, RO) to be used
	 * @param instance The instance to be rewritten
	 * @param rulelist The <code>RuleList</code> used for finding possible rewrites
	 */
	public ProgramRewrite(int strategy, ASTNode instance, TRSFile rulelist) {
		this.strategy = strategy;
		this.instance = instance;
		this.rewriteDone = false;
		this.rulelist = rulelist;
	}
	
	/**
	 * Find and perform a rewrite <b>in program execution mode</b>
	 * 
	 * @return The rewritten tree
	 * @throws NoChildrenException
	 * @throws SyntaxErrorException
	 * @throws NoRewritePossibleException
	 */
/*	public ASTNode findRewrite() throws NoChildrenException, SyntaxErrorException, NoRewritePossibleException {
		ASTNode retval = null;
		switch(strategy) {
		case Decomposition.S_LO:
			retval = findRewriteLO(instance);
			break;
		case Decomposition.S_LI:
			retval = findRewriteLI(instance);
			break;
		case Decomposition.S_RO:
			retval = findRewriteRO(instance);
			break;
		case Decomposition.S_RI:
			retval = findRewriteRI(instance);
			break;
		}
		
		if(!this.rewriteDone) {
			throw new NoRewritePossibleException("No rules could be applied");
		}
		return retval;
	}
*/
	/**
	 * Find and perform a RI rewrite
	 * 
	 * @param node The node to be checked (and possibly be rewritten) - Initialize with root node.
	 * @return The rewritten tree or original tree with rewritten subtree
	 * @throws SyntaxErrorException
	 * @throws NoChildrenException
	 */
/*	private ASTNode findRewriteRI(ASTNode node) throws SyntaxErrorException, NoChildrenException {
		ASTNode retval = null;
		boolean tryChildren = true;
		
		if(node instanceof Function) {
			retval = new Function(node.getName());
			Iterator<ASTNode> childIt = node.getRevChildIterator();
			retval.add(findRewriteRI(childIt.next())); // try to rewrite rightmost child
			
			// try to rewrite self
			if(!rewriteDone) {
				// can we apply a rule?
				Iterator<Rule> ruleIt = rulelist.getRules();
				while(ruleIt.hasNext()) {
					Rule rule = ruleIt.next();
					if(Decomposition.match(rule.getLeft(), node)) {
						// yes, we can. Go on and rewrite the tree
						HashMap<String, ASTNode> assignments = getVarAssignments(node, rule.getLeft(), new HashMap<String, ASTNode>());
						rewriteDone = true;
						tryChildren = false;
						retval = buildTree(rule.getRight(), assignments);
						retval.reverse();
					}
				}
			}
			
			if(tryChildren) {
				// try to rewrite other children
				while(childIt.hasNext()) {
					retval.add(findRewriteLI(childIt.next()));
				}
			}

		}
		else if(node instanceof Constant) {
			retval = new Constant(node.getName());
			
			// try to rewrite self
			if(!rewriteDone) {
				// can we apply a rule?
				Iterator<Rule> ruleIt = rulelist.getRules();
				while(ruleIt.hasNext()) {
					Rule rule = ruleIt.next();
					if(Decomposition.match(rule.getLeft(), node)) {
						// yes, we can. Go on and rewrite the tree
						HashMap<String, ASTNode> assignments = getVarAssignments(node, rule.getLeft(), new HashMap<String, ASTNode>());
						rewriteDone = true;
						retval = buildTree(rule.getRight(), assignments);
						retval.reverse();
					}
				}
			}
		}
		else {
			throw new SyntaxErrorException("Invalid instance given");
		}
		
		retval.reverse();
		return retval;
	}*/

	/**
	 * Find and perform a RO rewrite
	 * 
	 * @param node The node to be checked (and possibly be rewritten) - Initialize with root node.
	 * @return The rewritten tree or original tree with rewritten subtree
	 * @throws SyntaxErrorException
	 * @throws NoChildrenException
	 */
/*	private ASTNode findRewriteRO(ASTNode node) throws SyntaxErrorException, NoChildrenException {
		ASTNode retval = null;
		
		if(!rewriteDone) {
			// can we apply a rule?
			Iterator<Rule> ruleIt = rulelist.getRules();
			while(ruleIt.hasNext()) {
				Rule rule = ruleIt.next();
				if(Decomposition.match(rule.getLeft(), node)) {
					// yes, we can. Go on and rewrite the tree
					HashMap<String, ASTNode> assignments = getVarAssignments(node, rule.getLeft(), new HashMap<String, ASTNode>());
					rewriteDone = true;
					retval = buildTree(rule.getRight(), assignments);
					retval.reverse();
				}
			}
		}
		
		if(retval == null && node instanceof Function) {
			retval = new Function(node.getName());
			Iterator<ASTNode> childIt = node.getRevChildIterator();
			while(childIt.hasNext()) {
				retval.add(findRewriteRO(childIt.next()));
			}
		}
		else if(retval == null && node instanceof Constant) {
			retval = new Constant(node.getName());
		}
		else if(retval == null) {
			throw new SyntaxErrorException("Invalid instance given");
		}

		retval.reverse();
		return retval;
	}*/

	/**
	 * Find and perform a LI rewrite
	 * 
	 * @param node The node to be checked (and possibly be rewritten) - Initialize with root node.
	 * @return The rewritten tree or original tree with rewritten subtree
	 * @throws SyntaxErrorException
	 * @throws NoChildrenException
	 */
/*	private ASTNode findRewriteLI(ASTNode node) throws NoChildrenException, SyntaxErrorException {
		ASTNode retval = null;
		boolean tryChildren = true;
		
		if(node instanceof Function) {
			retval = new Function(node.getName());
			Iterator<ASTNode> childIt = node.getChildIterator();
			retval.add(findRewriteLI(childIt.next())); // try to rewrite leftmost child
			
			// try to rewrite self
			if(!rewriteDone) {
				// can we apply a rule?
				Iterator<Rule> ruleIt = rulelist.getRules();
				while(ruleIt.hasNext()) {
					Rule rule = ruleIt.next();
					if(Decomposition.match(rule.getLeft(), node)) {
						// yes, we can. Go on and rewrite the tree
						HashMap<String, ASTNode> assignments = getVarAssignments(node, rule.getLeft(), new HashMap<String, ASTNode>());
						rewriteDone = true;
						tryChildren = false;
						retval = buildTree(rule.getRight(), assignments);
					}
				}
			}
			if(tryChildren) {
				// try to rewrite other children
				while(childIt.hasNext()) {
					retval.add(findRewriteLI(childIt.next()));
				}
			}
		}
		else if(node instanceof Constant) {
			retval = new Constant(node.getName());
			
			// try to rewrite self
			if(!rewriteDone) {
				// can we apply a rule?
				Iterator<Rule> ruleIt = rulelist.getRules();
				while(ruleIt.hasNext()) {
					Rule rule = ruleIt.next();
					if(Decomposition.match(rule.getLeft(), node)) {
						// yes, we can. Go on and rewrite the tree
						HashMap<String, ASTNode> assignments = getVarAssignments(node, rule.getLeft(), new HashMap<String, ASTNode>());
						rewriteDone = true;
						retval = buildTree(rule.getRight(), assignments);
					}
				}
			}
		}
		else {
			throw new SyntaxErrorException("Invalid instance given");
		}
		
		return retval;
	}*/

	/**
	 * Find and perform a LO rewrite
	 * 
	 * @param node The node to be checked (and possibly be rewritten) - Initialize with root node.
	 * @return The rewritten tree or original tree with rewritten subtree
	 * @throws SyntaxErrorException
	 * @throws NoChildrenException
	 */
/*	private ASTNode findRewriteLO(ASTNode node) throws NoChildrenException, SyntaxErrorException {
		ASTNode retval = null;
		
		if(!rewriteDone) {
			// can we apply a rule?
			Iterator<Rule> ruleIt = rulelist.getRules();
			while(ruleIt.hasNext()) {
				Rule rule = ruleIt.next();
				if(Decomposition.match(rule.getLeft(), node)) {
					// yes, we can. Go on and rewrite the tree
					HashMap<String, ASTNode> assignments = getVarAssignments(node, rule.getLeft(), new HashMap<String, ASTNode>());
					rewriteDone = true;
					retval = buildTree(rule.getRight(), assignments);
				}
			}
		}
		
		if(retval == null && node instanceof Function) {
			retval = new Function(node.getName());
			Iterator<ASTNode> childIt = node.getChildIterator();
			while(childIt.hasNext()) {
				retval.add(findRewriteLO(childIt.next()));
			}
		}
		else if(retval == null && node instanceof Constant) {
			retval = new Constant(node.getName());
		}
		else if(retval == null) {
			throw new SyntaxErrorException("Invalid instance given");
		}
		return retval;
	}*/

	/**
	 * Build a new tree from the right-hand side of a rule and a map of variable
	 * assignments.
	 * Performed when a rewrite possibility was found.
	 * 
	 * @param right The right-hand side of the rule to be applied
	 * @param assignments The map of variable assignments to be used
	 * @return The new tree
	 * @throws SyntaxErrorException
	 */
	private static ASTNode buildTree(ASTNode right, HashMap<String, ASTNode> assignments) throws SyntaxErrorException {
		// we need to determine what kind of node we want to create
		// and the creation of every kind of node is different
		if(right instanceof Constant) {
			// Constants have no children and their sole property is their name
			return new Constant(right.getName());
		}
		else if(right instanceof Function) {
			// Functions have children and a name
			// So set the name and recursively build their children
			Function newNode = new Function(right.getName());
			Iterator<ASTNode> oldNode = ((Function)right).getChildIterator();
			
			while(oldNode.hasNext()) {
				newNode.add(buildTree(oldNode.next(), assignments));
			}
			return newNode;
		}
		else if(right instanceof Variable) {
			return assignments.get(right.getName());
		}
		else {
			// Something went seriously wrong. The nodes are of an
			// unexpected type. We can do nothing here except print out
			// and error message and die with dignity.
			throw new SyntaxErrorException("Found an unknown node type while rewriting. Node content: " + right + ". Node type: " + right.getClass());
		}
	}

	/**
	 * Find the variable assignments needed to apply a rule.
	 * 
	 * @param node The node to be rewritten
	 * @param rule The rule to be applied
	 * @param assignments Temporary map of known assignments - Initialize as empty
	 * @return Map of Variable-->Tree assignments
	 */
	private static HashMap<String, ASTNode> getVarAssignments(ASTNode node, ASTNode rule, HashMap<String, ASTNode> assignments) {
		if(rule instanceof Variable) {
			assignments.put(rule.getName(), node);
		}
		else {
			try {
				Iterator<ASTNode> ruleIt = rule.getChildIterator();
				Iterator<ASTNode> instanceIt = node.getChildIterator();
				
				while(ruleIt.hasNext()) {
					assignments = getVarAssignments(instanceIt.next(), ruleIt.next(), assignments);
				}
			}
			catch(NoChildrenException e) {
				// do nothing. We simply reached a leaf node.
			}
		}
		
		return assignments;
	}
	
	/**
	 * Rewrite a node using a specific rule.
	 * This is also used when not in program execution mode.
	 * 
	 * @param tree The tree to rewrite
	 * @param node The node to rewrite
	 * @param rule The rule to apply
	 * @return The rewritten tree
	 * @throws SyntaxErrorException 
	 */
	public static ASTNode rewrite(ASTNode tree, ASTNode node, Rule rule) throws SyntaxErrorException {
		ASTNode retval;
		
		// check if we shall rewrite this node
		if(tree == node) {
			try {
				HashMap<String, ASTNode> assignments = Decomposition.match(rule.getLeft(), tree);
				retval = buildTree(rule.getRight(), assignments);
			}
			catch(NoRewritePossibleException e) {
				// this should never happen
				MsgBox.error("The rule " + rule.toString() + " and the tree " + tree.toString() + "did not match, although they were chosen to be rewritten");
				return null;
			}
		}
		else if(tree instanceof Function) {
			// create new node and add new children
			retval = new Function(tree.getName());
			Iterator<ASTNode> childIt = ((Function)tree).getChildIterator();
			
			while(childIt.hasNext()) {
				retval.add(rewrite(childIt.next(), node, rule));
			}
		}
		else {
			// tree has to be a constant or something is seriously wrong
			if(!(tree instanceof Constant)) {
				throw new SyntaxErrorException("Found an unknown node type while rewriting. Node content: " + tree + ". Node type: " + tree.getClass());
			}
			else {
				retval = new Constant(tree.getName());
			}
		}
		
		return retval;
	}
}
