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

package kites.TRSModel;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import kites.visual.NodeBox;
import kites.visual.NodeContainer;
import kites.visual.NodeLabel;

/**
 * This class represents a function symbol in a TRS.
 * Function symbols can have one or more children and are
 * derived from <code>ASTNode</code>
 * 
 * @see kites.TRSModel.ASTNode
 */
public class Function extends ASTNode {
	private LinkedList<ASTNode> children;
	private boolean reversed;
	
	/**
	 * Create a new function with a name
	 * @param name The function's name
	 */
	public Function(String name) {
		super(name);
		
		children = new LinkedList<ASTNode>();
		reversed = false;
	}
	
	/**
	 * Add a child (in form of an <code>ASTNode</code> to this function.
	 * @param child The child node (or possibly tree) to add
	 */
	@Override
	public void add(ASTNode child) {
		children.add(child);
	}
	
	/**
	 * Give a string representation of this function
	 * 
	 * @return The function's string representation
	 */
	public String toString() {
		String retval = name + "(";

		Iterator<ASTNode> it = getChildIterator();
		
		while(it.hasNext()) {
			retval += it.next().toString();
			if(it.hasNext()) {
				retval += ", ";
			}
		}
		
		return retval + ")";
	}
	
	/**
	 * Gives an iterator over all the function's children
	 * 
	 * @return The iterator over the children
	 */
	public Iterator<ASTNode> getChildIterator() {
		if(reversed) {
			return this.children.descendingIterator();
		}
		else {
			return this.children.iterator();
		}
	}
	
	/**
	 * Gives a reversed iterator over all the function's children
	 * 
	 * @return The reversed iterator
	 */
	public Iterator<ASTNode> getRevChildIterator() {
		if(reversed) {
			return this.children.iterator();
		}
		else {
			return this.children.descendingIterator();
		}
	}
	
	/**
	 * Give the number of parameters (i. e. children) this function has.
	 * 
	 * @return The number of children.
	 */
	public int getParamCount() {
		return children.size();
	}
	
	/**
	 * Give the size of this function.
	 * This is different from <code>getParentCount()</code>, as
	 * a child can also be another function, which has a size
	 * greater than 1.
	 * 
	 * @return The subtrees size
	 */
	public int getSize() {
		int retval = 1;
		Iterator<ASTNode> childrenIt = getChildIterator();
		
		while(childrenIt.hasNext()) {
			retval += childrenIt.next().getSize();
		}
		
		return retval;
	}
	
	/**
	 * Reverse the order of the children
	 */
	@Override
	public void reverse() {
		reversed = !reversed;
	}

	/**
	 * Return a graphical representation of this function
	 * 
	 * @return The graphical representation
	 */
	@Override
	public NodeContainer toLabel() {
		NodeBox nodeBox = new NodeBox(getParamCount());
		nodeBox.setHead(new NodeLabel(this));
		
		Iterator<ASTNode> childIt = getChildIterator();
		while(childIt.hasNext()) {
			nodeBox.addChild(childIt.next().toLabel());
		}
		
		return nodeBox;
	}
	
	/**
	 * Return a graphical representation of this function and embed
	 * information about the rules which can be applied to this node.
	 * 
	 * @param highlight Shall the function be highlighted initially if rules apply?
	 */
	@Override
	public NodeContainer toLabelWithRule(LinkedHashMap<ASTNode, LinkedList<Rule>> decomp, boolean highlight) {
		NodeBox nodeBox = new NodeBox(getParamCount());
		NodeLabel head;
		
		if(decomp.containsKey(this)) {
			head = new NodeLabel(this, decomp.get(this), highlight);
		}
		else {
			head = new NodeLabel(this);
		}
		
		nodeBox.setHead(head);
		Iterator<ASTNode> childIt = getChildIterator();
		while(childIt.hasNext()) {
			nodeBox.addChild(childIt.next().toLabelWithRule(decomp, highlight));
		}
		
		return nodeBox;
	}
}
