/*
 * This file is part of KiTES.
 * 
 * Copyright 2010 Sebastian Schäfer <sarek@uliweb.de>
 *
 *   KiTES is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   KiTES is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with KiTES.  If not, see <http://www.gnu.org/licenses/>.
 */

package kites.logic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

import kites.TRSModel.ASTNode;
import kites.TRSModel.Rule;
import kites.TRSModel.TRSFile;
import kites.TRSModel.Variable;
import kites.exceptions.NoChildrenException;
import kites.exceptions.NoRewritePossibleException;
import kites.exceptions.SyntaxErrorException;

/**
 * This class is able to perform various syntax checks on TRSs.
 * These syntax checks are needed to decide whether the TRS can be
 * interpreted at all, and in which mode.
 */
public class CheckTRS {
	TRSFile rulelist;
	
	/**
	 * @param rulelist The TRS object that the checks shall be performed upon 
	 */
	public CheckTRS(TRSFile rulelist) {
		this.rulelist = rulelist;
	}

	/**
	 * Checks whether an instance conforms to the Sigma-
	 * Gamma-signatures given by the rule set.
	 * If not, a <code>SyntaxErrorException</code> with a
	 * meaningful message is thrown.
	 * 
	 * Basically, this just tests whether the root node
	 * of the instance is a symbol from the Gamma signature.
	 * @param instance The instance to be checked
	 * @throws SyntaxErrorException
	 */
	public void isSigmaGammaInstance(ASTNode instance) throws SyntaxErrorException {
		HashMap<String, Integer> gamma = getGammaSignature();
		if(!gamma.containsKey(instance.getName())) {
			throw new SyntaxErrorException("Die Instanz " + instance + " ist keine gültige Sigma-Gamma-Programm-Instanz");
		}
	}
	
	/**
	 * Checks whether the given rule set is a Sigma-Gamma-
	 * program system.
	 * First it creates both signatures then performs the
	 * following checks:
	 * - do they overlap? This would mean there is at least one
	 *   rule that starts with a Sigma-symbol
	 * 
	 * Throws a SyntaxErrorException with a meaningful message
	 * if any check fails.
	 * 
	 * @throws SyntaxErrorException
	 */
	public void isSigmaGammaSystem() throws SyntaxErrorException {
		HashMap<String, Integer> sigma = getSigmaSignature();
		HashMap<String, Integer> gamma = getGammaSignature();
		
		Iterator<String> sigmaIt = sigma.keySet().iterator();
		while(sigmaIt.hasNext()) {
			String symbol = sigmaIt.next();
			if(gamma.containsKey(symbol)) {
				throw new SyntaxErrorException("Symbol " + symbol + " ist sowohl in der Sigma-, als auch der Gamma-Signatur.");
			}
		}
	}
	
	/**
	 * Create the Gamma-signature of the rule set.
	 * In a program system the root node in the tree representing
	 * a left-hand side of a rule needs to belong to the Gamma-
	 * signature, which is created by this method
	 * 
	 * @return the signature as a map: name --> arity
	 * @throws SyntaxErrorException
	 */
	private HashMap<String, Integer> getGammaSignature() throws SyntaxErrorException {
		Iterator<Rule> rules = rulelist.getRules();
		HashMap<String, Integer> retval = new HashMap<String, Integer>();
		
		while(rules.hasNext()) {
			retval = gammaSigNode(rules.next().getLeft(), retval);
		}
		
		return retval;
	}
	
	/**
	 * Create the Gamma-signature for a tree.
	 * Checks whether the root node is already in the signature.
	 * If not, it is added. If a node is found to contradict the
	 * signature a <code>SyntaxErrorException</code> is thrown.
	 * This is also the case when a tree with a <code>Variable</code>
	 * as root is detected.
	 * 
	 * @param node The tree to genereate the signature from
	 * @param signature The signature to which entries shall be added
	 * @return The new signature
	 * @throws SyntaxErrorException
	 */
	private HashMap<String, Integer> gammaSigNode(ASTNode node, HashMap<String, Integer> signature) throws SyntaxErrorException {
		if(node instanceof Variable) {
			throw new SyntaxErrorException("Linke Regelseiten dürfen nicht nur aus Variablen bestehen.");
		}
		
		if(signature.containsKey(node.getName())) {
			if(signature.get(node.getName()) != node.getParamCount()) {
				throw new SyntaxErrorException("Falsche Parameter-Anzahl für Knoten: " + node);
			}
		}
		else {
			signature.put(node.getName(), node.getParamCount());
		}
		
		return signature;
	}
	
	/**
	 * Create the Sigma-signature of the rule set.
	 * In a program system all nodes except the root node in the tree
	 * representing a left-hand side of a rule need to belong to the
	 * Sigma-signature, which is created by this method.
	 * 
	 * @return The Sigma-signature of the rule set
	 * @throws SyntaxErrorException When signature is found to be contradicted
	 * 		by a wrong parameter count
	 */
	private HashMap<String, Integer> getSigmaSignature() throws SyntaxErrorException {
		Iterator<Rule> rules = rulelist.getRules();
		HashMap<String, Integer> retval = new HashMap<String, Integer>();
		
		while(rules.hasNext()) {
			retval = sigmaSigNode(rules.next().getLeft(), retval, true);
		}
		
		return retval;
	}
	
	/**
	 * Create the Sigma-signature of a tree, adding to a previously
	 * existing signature.
	 * 
	 * @param node The tree to be checked
	 * @param signature The pre-existing signature
	 * @param root Indicates whether we are at the root of the tree
	 * @throws SyntaxErrorException On encountering a node contradicting
	 * 		the signature
	 */
	private HashMap<String, Integer> sigmaSigNode(ASTNode node, HashMap<String, Integer> signature, boolean root) throws SyntaxErrorException {
		if(!root) {
			if(signature.containsKey(node.getName())) {
				if(signature.get(node.getName()) != node.getParamCount()) {
					throw new SyntaxErrorException("Falsche Parameter-Anzahl für Knoten: " + node);
				}
			}
			else if(!(node instanceof Variable)) { // we don't want variables in our signature
				signature.put(node.getName(), node.getParamCount());
			}
		}
		
		try {
			Iterator<ASTNode> childIt = node.getChildIterator();
			while(childIt.hasNext()) {
				signature = sigmaSigNode(childIt.next(), signature, false);
			}
		}
		catch(NoChildrenException e) {
			// Do nothing. We just reached a leaf node
		}
		
		return signature;
	}
	
	/**
	 * Check an instance for its compliance with a given signature.
	 * This only checks for correct parameter count, not for consistency
	 * with a Gamma-Signature as needed for interpretation as a program.
	 * Use <code>instanceCheckGamma</code> for that!
	 * 
	 * This also checks that there are no <code>Variable</code>s present
	 * in the instance.
	 * If a violation occurs, a <code>SyntaxErrorException</code> is thrown.
	 * 
	 * @param node The instance to be checked
	 * @param signature The signature to be checked against.
	 * @throws SyntaxErrorException
	 */
	public static void instanceCheck(ASTNode node, HashMap<String, Integer> signature) throws SyntaxErrorException {
		if(node instanceof Variable) {
			throw new SyntaxErrorException("Es ist nicht erlaubt, Variablen in einer Instanz zu benutzen.");
		}
		if(signature.containsKey(node.getName()) && signature.get(node.getName()) != node.getParamCount()) {
			throw new SyntaxErrorException("Parameter-Anzahl für \"" + node.toString() + "\" verstößt gegen Signatur. Erwarte " + signature.get(node.getName()) + " Parameter");
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
				if(unifiable(rule1.getLeft(), rightNode)) {
					throw new SyntaxErrorException("Die Regeln " + rule1.getLeft() + " und " + rightNode + " sind unifizierbar");
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
	 * @return <code>true</code> when trees are unifiable, <code>false</code> otherwise.
	 * @throws SyntaxErrorException 
	 */
	private boolean unifiable(ASTNode left, ASTNode right) throws SyntaxErrorException {
		try {
			Decomposition.match(left, right);
			return true;
		}
		catch(NoRewritePossibleException e) {
			return false;
		}
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
				throw new SyntaxErrorException("Falsche Parameter-Anzahl für " + node);
		}
		else if(!(node instanceof Variable)) { // we don't want variables in our signature
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
					throw new SyntaxErrorException("Variable " + node.getName() + " wurde auf der rechten Regelseite benutzt, obwohl sie nicht auf der linken Regelseite aufgeführt wurde.");
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
				variables.add(node.getName());
			}
		}
		
		return variables;
	}

	/**
	 * Check whether all the rules in the ruleset are left-linear.
	 * This means that a variable may not appear more than once on the
	 * left-hand side of a rule.
	 * This is a prerequisite for interpretation in program mode, as systems
	 * without this property may not be confluent.
	 * 
	 * @throws SyntaxErrorException If a rule is not left-linear.
	 */
	public void checkLeftLinear() throws SyntaxErrorException {
		Iterator<Rule> ruleIt = rulelist.getRules();
		
		while(ruleIt.hasNext()) {
			Rule rule = ruleIt.next();
			try {
				isLeftLinear(rule.getLeft(), new HashSet<String>());
			}
			catch(SyntaxErrorException e) {
				throw new SyntaxErrorException("Regel " + rule.toString() + " ist nicht links-linear.");
			}
		}
	}
	
	/**
	 * Checks whether a tree is linear, i. e. that no variable appears more
	 * than once in it.
	 * 
	 * @param tree The tree to be checked
	 * @param varSet A pre-existing number of variables. This should be initialized as an empty set.
	 * @throws SyntaxErrorException If the tree is not linear.
	 */
	private void isLeftLinear(ASTNode tree, HashSet<String> varSet) throws SyntaxErrorException{
		if(tree instanceof Variable) {
			if(varSet.contains(tree.getName())) {
				throw(new SyntaxErrorException("nicht links-linear"));
			}
			else {
				varSet.add(tree.getName());
			}
		}
		else {
			try {
				Iterator<ASTNode> childIt = tree.getChildIterator();
				while(childIt.hasNext()) {
					isLeftLinear(childIt.next(), varSet);
				}
			}
			catch(NoChildrenException e) {
				// do nothing. we simply reached a leaf node
			}
		}
	}
}
