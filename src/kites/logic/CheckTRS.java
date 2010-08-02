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
import kites.exceptions.UnificationException;

/**
 * @author sarek
 *
 */
public class CheckTRS {
	RuleList rulelist;
	
	public CheckTRS(RuleList rulelist) {
		this.rulelist = rulelist;
	}
	
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
	
	public void variableCheck() throws SyntaxErrorException {
		Iterator<Rule> it = rulelist.getRules();
		
		while(it.hasNext()) {
			variableCheckRule(it.next());
		}
	}
	
	private void variableCheckRule(Rule rule) throws SyntaxErrorException {
		// Create a list of variables on left side
		// and check for multiple usage at the same time
		HashSet<String> variables = GetVariables(rule.getLeft(), new HashSet<String>());
			
		// Check for usage of variables on the right side which were
		// not used on left side.
		CheckVarRightSide(rule.getRight(), variables);
	}
	
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
