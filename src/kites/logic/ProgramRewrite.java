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
