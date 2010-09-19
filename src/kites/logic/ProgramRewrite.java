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

import kites.TRSModel.ASTNode;
import kites.TRSModel.Constant;
import kites.TRSModel.Function;
import kites.TRSModel.Rule;
import kites.TRSModel.Variable;
import kites.exceptions.NoRewritePossibleException;
import kites.exceptions.SyntaxErrorException;
import kites.visual.MsgBox;

/**
 * This class performs the actual rewriting.
 * The method <code>rewrite</code> is given a node to rewrite and
 * a rule to rewrite it with. It then builds a mapping between variables
 * and their assignments and build a new tree according to the rule
 * and the variable assignments.
 */
public class ProgramRewrite {
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
	 * The method builds a mapping between the variables used on the left-hand side
	 * of the rule and the respective content in the tree. It then goes on
	 * and builds a new tree out of the right-hand side of the rule and replaces the
	 * variables there using the previously built mapping.
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
