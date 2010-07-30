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
import kites.TRSModel.Variable;
import kites.exceptions.NoChildrenException;
import kites.exceptions.SyntaxErrorException;

/**
 * @author sarek
 * Rewrite a (sub-) tree using a rule.
 * This transforms the subtree following the rule, i. e. symbols are replaced
 * and variables are assigned their values.
 * 
 * @return The transformed (reduced) (sub-) tree.
 */
public class Rewrite {
	public static void rewrite(ASTNode instance, Rule rule) throws SyntaxErrorException {
		// gather variable assignments
		HashMap<String, ASTNode> assignments = getVarAssignments(instance, rule.getLeft(), new HashMap<String, ASTNode>());
		
		// build new (sub-) tree and replace old one with it
		instance = buildTree(rule.getRight(), assignments);
	}
	
	private static ASTNode buildTree(ASTNode right,	HashMap<String, ASTNode> assignments) throws SyntaxErrorException {
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

	private static HashMap<String, ASTNode> getVarAssignments(ASTNode instance, ASTNode rule, HashMap<String, ASTNode> assignments) {
		if(instance instanceof Variable) {
			assignments.put(rule.getName(), instance);
		}
		else {
			try {
				Iterator<ASTNode> ruleIt = rule.getChildIterator();
				Iterator<ASTNode> instanceIt = instance.getChildIterator();
				
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
}
