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
import java.util.Iterator;
import java.util.LinkedList;

import kites.TRSModel.ASTNode;
import kites.TRSModel.Constant;
import kites.TRSModel.Function;
import kites.TRSModel.Rule;
import kites.TRSModel.TRSFile;
import kites.TRSModel.Variable;
import kites.exceptions.NoChildrenException;
import kites.exceptions.NodeException;

/**
 * This class provides everything necessary to transform a <code>RuleList</code> and an
 * instance of it to the normal form.
 * 
 * This means that all function, constant and variable symbols will be replaced so
 * a very specific signature consisting only of the symbols <code>fun</code>, <code>var</code>,
 * <code>suc</code> and <code>zero</code> is used.
 * 
 * All symbols will get a number associated with them, beginning with 0 (zero). For example
 * function 3 is represented as:
 * 
 * <code>fun(suc(suc(suc(zero))))</code>
 * 
 * These terms are then put together using several <code>cons</code> terms. For details
 * please refer to my bachelor thesis.
 * 
 * @author sarek
 */
public class Codification {
	private HashMap<String,Integer> codes;
	private int curNumFunction;
	private int curNumVariable;
	private TRSFile rulelist;
	private ASTNode codifiedRuleList;
	private ASTNode codifiedInstance;

	/**
	 * Initialize the class to perform the codification
	 * 
	 * @param rulelist The <code>RuleList</code> to be codified
	 */
	public Codification(TRSFile rulelist) {
		this.codes = new HashMap<String,Integer>();
		this.curNumFunction = 0;
		this.curNumVariable = 0;
		this.rulelist = rulelist;
		this.codifiedInstance = null;
		this.codifiedRuleList = null;
	}
	
	/**
	 * Performs the actual codification by first generating a mapping
	 * between the original symbols and ascending numbers.
	 * Then both the <code>RuleList</code> and afterwards the instance
	 * will be codified. Symbols present in the instance, but not in the
	 * <code>RuleList</code> will also be codified. Their associated numbers
	 * will be the highest ones. 
	 * @throws NodeException If the given ruleset is empty (i. e. no rules)
	 */
	public void codify() throws NodeException {
		// Create standard form of the rulelist
			// Create translation map of functions and variables in rules
		
		if(rulelist.getRulesList().size() > 0) {
			Iterator<Rule> ruleIt = rulelist.getRules();
			while(ruleIt.hasNext()) {
				Rule rule = ruleIt.next();
				// Look at left side
				createMap(rule.getLeft());
				
				// Look at right side
				createMap(rule.getRight());
			}
			
			// Create new list of rules in standard form using translation map
			
			codifiedRuleList = codifyRuleList();

			if(rulelist.getInstance() != null) {
				// Create standard form of the instance
					// (Possibly) extend translation map of functions and variables
				createMap(rulelist.getInstance());
					// Create new instance tree in standard form
				codifiedInstance = codifyTree(genFlatTree(rulelist.getInstance()));
			}
		}
		else {
			throw new NodeException("No ruleset to codify given!");
		}
	}

	/**
	 * This method transforms a given tree to its encoded standard form.
	 * It is supposed the mapping has already been created.
	 * 
	 * It takes a flattened list structure and then transforms
	 * that structure into a new "tree-list" with cons.
	 * See my bachelor thesis for details.
	 * 
	 * @see #genFlatTree(ASTNode) This method flattens the tree
	 * @param flatTree A flattened tree
	 * @return The normal form of the tree
	 */
	@SuppressWarnings("unchecked")
	private ASTNode codifyTree(LinkedList<Object> flatTree) {
		Iterator<Object> it = flatTree.iterator();
		Function firstCons = new Function("cons");
		ASTNode retval = firstCons;
		
		while(it.hasNext()) {
			Object entry = it.next();
			if(entry instanceof LinkedList<?>) {
				firstCons.add(codifyTree((LinkedList<Object>) entry));
			}
			else {
				firstCons.add(genCode((ASTNode) entry));
			}
			
			// second parameter
			if(!it.hasNext()) {
				firstCons.add(new Constant("empty"));
			}
			else {
				Function secCons = new Function("cons");
				firstCons.add(secCons);
				firstCons = secCons;
			}
		}
		
		return retval;
	}
	
	/**
	 * This method creates a flat list from a node tree
	 * 
	 * See my bachelor thesis for details.
	 * 
	 * @param node The tree to be flattened
	 * @return The flattened tree
	 */
	private LinkedList<Object> genFlatTree(ASTNode node) {
		LinkedList<Object> retval = new LinkedList<Object>();
		
		retval.add(node);
		
		// this will only be used when the tree only consists of a variable symbol
		if(node instanceof Variable) {
			return retval; // we are finished, the tree consists only of a variable
		}
		else {
			try {
				// look at children
				Iterator<ASTNode> childIt = node.getChildIterator();
				
				while(childIt.hasNext()) {
					ASTNode child = childIt.next();
					if(child instanceof Variable) {
						retval.add(child);
					}
					else {
						retval.add(genFlatTree(child));
					}
				}
			}
			catch(NoChildrenException e) {
				// do nothing we simply reached a leaf node
			}
		}
		
		return retval;
	}
	
	/**
	 * Generate the codified term for a specific node.
	 * Here a node may not be a tree - No checks are performed!
	 * 
	 * @param node The node to be codified
	 * @return Term [fun | var](suc(...(zero)...)
	 */
	private ASTNode genCode(ASTNode node) {
		ASTNode retval;
		if(node instanceof Variable) {
			retval = new Function("var");
		}
		else {
			retval = new Function("fun");
		}
		
		retval.add(genNumber(codes.get(node.getName())));
		
		return retval;
	}
	
	/**
	 * Generate a number in term form using <code>suc</code>
	 * and <code>zero</code>
	 * @param number
	 * @return The number's term, e. g. <code>suc(suc(zero))</code>
	 */
	private ASTNode genNumber(int number) {
		if(number > 0) {
			ASTNode retval = new Function("suc");
			retval.add(genNumber(number - 1));
			return retval;
		}
		else {
			return new Constant("zero");
		}
	}

	/**
	 * Create the encoded standard form of a <code>RuleList</code>.
	 * The existence of a mapping is pre-supposed.
	 * 
	 * This builds a list of all the rules in the form:
	 * <pre>
	 * cons( pair( rule1.left,
	 *             rule1.right),
	 *       cons( pair( rule2.left,
	 *                   rule2.right),
	 *             ...
	 *                   cons( pair( ruleN.left,
	 *                               ruleN.right),
	 *                         empty) ... ))
	 * </pre>
	 * 
	 * @see #codifyTree(LinkedList)
	 * 
	 * @return The encoded rulelist
	 */
	private ASTNode codifyRuleList() {
		ASTNode firstCons, rulePair, retval = null, toAdd = null;
		
		Iterator<Rule> ruleIt = rulelist.getRules();
		while(ruleIt.hasNext()) {
			Rule rule = ruleIt.next();
			firstCons = new Function("cons");
			rulePair = new Function("pair");
			
			rulePair.add(codifyTree(genFlatTree(rule.getLeft())));
			rulePair.add(codifyTree(genFlatTree(rule.getRight())));
			firstCons.add(rulePair);

			if(retval == null) {
				retval = firstCons;
				toAdd = retval;
			}
			else {
				toAdd.add(firstCons);
				toAdd = firstCons;
			}
			
		}
		
		toAdd.add(new Constant("empty"));
		return retval;
	}

	/**
	 * Create a mapping between function symbols and numbers for
	 * a tree.
	 * 
	 * @param node The tree a mapping shall be created for
	 */
	private void createMap(ASTNode node) {
		if(!codes.containsKey(node.getName())) {
			if(node instanceof Variable) {
				codes.put(node.getName(), curNumVariable);
				curNumVariable++;
			}
			else {
				codes.put(node.getName(), curNumFunction);
				curNumFunction++;
			}
		}
		
		try {
			Iterator<ASTNode> childIt = node.getChildIterator();
			while(childIt.hasNext()) {
				createMap(childIt.next());
			}
		}
		catch(NoChildrenException e) {
			// do nothing. we simply reached a leaf node
		}
	}

	/**
	 * Returns the previously computed codified rulelist.
	 * @return null if the list was not computed previously, else the list
	 */
	public ASTNode getCodifiedRuleList() {
		return codifiedRuleList;
	}

	/**
	 * Returns the previously computed codified instance.
	 * @return null if the list was not computed previously, else the instance
	 */
	public ASTNode getCodifiedInstance() {
		return codifiedInstance;
	}
}
