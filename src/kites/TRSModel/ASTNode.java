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

import kites.exceptions.NoChildrenException;
import kites.visual.NodeContainer;

/**
 * Abstract class representing a node in a syntax tree.
 * 
 * @author sarek
 *
 */
public abstract class ASTNode {
	protected String name;
	
	/**
	 * Create a node with a name.
	 * 
	 * @param name The node's name
	 */
	public ASTNode(String name) {
		this.name = name;
	}
	
	/**
	 * @return The node's name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Not used at the moment
	 * @deprecated
	 * @return always 0
	 */
	public int getLine() {
		return 0;
	}
	
	/**
	 * @return a string representation of the node (the name)
	 */
	public String toString() {
		return name;
	}
	
	/**
	 * return an indented string representation of the node (the name)
	 * @param indent The number of spaces to indent
	 * @return the indented string representations
	 */
	public String toString(String indent) {
		return indent + name;
	}
	
	/**
	 * Placeholder. Only used for <code>Function</code>s.
	 * 
	 * @param a no function
	 */
	public void add(ASTNode a) {
		// Do nothing
	}
	
	/**
	 * Return an iterator of the node's children.
	 * Must be overridden to do something useful.
	 * 
	 * @return none
	 * @throws NoChildrenException Always thrown
	 */
	public Iterator<ASTNode> getChildIterator() throws NoChildrenException {
		throw new NoChildrenException();
	}
	
	/**
	 * Return a reverse iterator of the node's children.
	 * Must be overridden to do something useful.
	 * 
	 * @return none
	 * @throws NoChildrenException Always thrown
	 */
	public Iterator<ASTNode> getRevChildIterator() throws NoChildrenException {
		throw new NoChildrenException();
	}
	
	/**
	 * Return the number of the node's children.
	 * 
	 * @return always 0
	 */
	public int getParamCount() {
		return 0;
	}
	
	/**
	 * Used to compute the size of a term.
	 * An ASTNode has a size of 1.
	 * 
	 * @return always 1
	 */
	public int getSize() {
		return 1;
	}
	
	/**
	 * Reverse the order of the node's children.
	 */
	public void reverse() {
		// Do nothing
	}
	
	/**
	 * Return the graphical representation of this node.
	 * 
	 * @return The node's graphical representation
	 */
	public abstract NodeContainer toLabel();
	
	/**
	 * Return the graphical representation of this node and also
	 * embed information about the possible rewrites into it.
	 * 
	 * @param decomp map of the nodes and their possible rewrites
	 * @return The node's graphical representation
	 */
	public abstract NodeContainer toLabelWithRule(LinkedHashMap<ASTNode, LinkedList<Rule>> decomp, boolean highlight);
}
