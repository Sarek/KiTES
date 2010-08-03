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
	public static ASTNode rewrite(ASTNode instance, Rule rule, int strategy) throws SyntaxErrorException, NoChildrenException {
		System.out.println("Rewriting node " + instance);
		System.out.println("using rule " + rule);
		// gather variable assignments
		HashMap<String, ASTNode> assignments = getVarAssignments(instance, rule.getLeft(), new HashMap<String, ASTNode>());
		System.out.println("Assignments: " + assignments);
		
		// Traverse instance to reach point of rewrite
		ASTNode newInstance = findRewrite(instance, rule.getLeft(), strategy, rule.getRight(), assignments);
		
		// build new (sub-) tree and replace old one with it
		System.out.println("Instance rewritten to: " + newInstance);
		return newInstance;
	}
	
	private static ASTNode findRewrite(ASTNode instance, ASTNode left, int strategy, ASTNode rule, HashMap<String, ASTNode> assignments) throws NoChildrenException, SyntaxErrorException {
		switch(strategy) {
		case Decomposition.S_LO:
			return findRewriteLO(instance, left, rule, assignments);
		case Decomposition.S_LI:
			return findRewriteLI(instance, left, rule, assignments);
		case Decomposition.S_RO:
			return findRewriteRO(instance, left, rule, assignments);
		case Decomposition.S_RI:
			return findRewriteRI(instance, left, rule, assignments);
		}
		return null;
	}

	private static ASTNode findRewriteRI(ASTNode instance, ASTNode left, ASTNode rule, HashMap<String, ASTNode> assignments) {
		// TODO Auto-generated method stub
		return null;
	}

	private static ASTNode findRewriteRO(ASTNode instance, ASTNode left, ASTNode rule, HashMap<String, ASTNode> assignments) {
		// TODO Auto-generated method stub
		return null;
	}

	private static ASTNode findRewriteLI(ASTNode instance, ASTNode left, ASTNode rule, HashMap<String, ASTNode> assignments) {
		// TODO Auto-generated method stub
		return null;
	}

	private static ASTNode findRewriteLO(ASTNode instance, ASTNode ruleLHS, ASTNode ruleRHS, HashMap<String, ASTNode> assignments) throws NoChildrenException, SyntaxErrorException {
		if(instance.getName().equals(ruleLHS.getName())) {
			// we have reached the subtree to rewrite
			return buildTree(ruleRHS, assignments);
		}
		else {
			if(instance instanceof Function) {
				ASTNode newNode = new Function(instance.getName());
				Iterator<ASTNode> childIt = instance.getChildIterator();
				newNode.add(findRewriteLO(childIt.next(), ruleLHS, ruleRHS, assignments));
				
				while(childIt.hasNext()) {
					newNode.add(childIt.next());
				}
				return newNode;
			}
			else if(instance instanceof Constant) {
				return new Constant(instance.getName());
			}
			else {
				throw new SyntaxErrorException("Invalid instance given");
			}
		}
	}

	private static ASTNode buildTree(ASTNode right,	HashMap<String, ASTNode> assignments) throws SyntaxErrorException {
		// we need to determine what kind of node we want to create
		// and the creation of every kind of node is different
		if(right instanceof Constant) {
			// Constants have no children and their sole property is their name
			System.out.println("Creating new contant");
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
		if(rule instanceof Variable) {
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
