/**
 * 
 */
package kites.logic;

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
	public void unifiabilityCheck(RuleList rulelist) throws UnificationException{
		Iterator<Rule> rules = rulelist.getRules();
		LinkedList<Rule> rules2 = rulelist.getRulesList();
		
		int pos = 1;
		
		while(rules.hasNext() && pos <= rules2.size()) {
			Iterator<Rule> it = rules2.listIterator(pos);
			Rule rule1 = rules.next();
			
			while(it.hasNext()) {
				unifiable(rule1.getLeft(), it.next().getLeft());
			}
		}
	}
	

	
	
	private void unifiable(ASTNode left, ASTNode right) throws UnificationException {
				
	}




	public void syntaxCheck(RuleList rulelist) throws SyntaxErrorException {
		Iterator<Rule> it = rulelist.getRules();
		
		while(it.hasNext()) {
			CheckRule(it.next());
		}
	}
	
	private void CheckRule(Rule rule) throws SyntaxErrorException {
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
					throw new SyntaxErrorException("Variable " + node.getName() + "used multiple times on a left-hand side of a rule. Line: " + node.getLine());
				}
				else {
					variables.add(node.getName());
				}
			}
		}
		
		return variables;
	}
}
