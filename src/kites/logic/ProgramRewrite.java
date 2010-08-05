/**
 * 
 */
package kites.logic;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import kites.TRSModel.ASTNode;
import kites.TRSModel.Constant;
import kites.TRSModel.Function;
import kites.TRSModel.Rule;
import kites.TRSModel.RuleList;
import kites.TRSModel.Variable;
import kites.exceptions.NoChildrenException;
import kites.exceptions.NoRewritePossibleException;
import kites.exceptions.SyntaxErrorException;

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
	RuleList rulelist;
	
	public ProgramRewrite(int strategy, ASTNode instance, RuleList rulelist) {
		this.strategy = strategy;
		this.instance = instance;
		this.rewriteDone = false;
		this.rulelist = rulelist;
	}
	
	public ASTNode findRewrite() throws NoChildrenException, SyntaxErrorException, NoRewritePossibleException {
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

	private ASTNode findRewriteRI(ASTNode node) throws SyntaxErrorException, NoChildrenException {
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
	}

	private ASTNode findRewriteRO(ASTNode node) throws SyntaxErrorException, NoChildrenException {
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
	}

	private ASTNode findRewriteLI(ASTNode node) throws NoChildrenException, SyntaxErrorException {
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
	}

	private ASTNode findRewriteLO(ASTNode node) throws NoChildrenException, SyntaxErrorException {
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
	}

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
}
