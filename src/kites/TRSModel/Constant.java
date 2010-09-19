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

import java.util.LinkedHashMap;
import java.util.LinkedList;

import kites.visual.NodeContainer;
import kites.visual.NodeLabel;

/**
 * Represents a constant symbol in a syntax tree.
 * A constant is like a <code>Function</code> it just does not have
 * any children.
 * 
 * @author sarek
 */
public class Constant extends ASTNode {
	/**
	 * Create a constant with a name
	 * @param name The constant's name
	 */
	public Constant(String name) {
		super(name);
	}

	/**
	 * @see ASTNode.toLabel()
	 * @return The constant's graphical representation 
	 */
	@Override
	public NodeContainer toLabel() {
		return new NodeLabel(this);
	}

	/**
	 * @see ASTNode.toLabelWithRule()
	 * @param decomp list of rules that can be applied to this node
	 * @param highlight shall the node be initially highlighted
	 * @return The constant's graphical representation
	 */
	@Override
	public NodeContainer toLabelWithRule(LinkedHashMap<ASTNode, LinkedList<Rule>> decomp, boolean highlight) {
		// is this constant reducible?
		if(decomp.containsKey(this)) {
			return new NodeLabel(this, decomp.get(this), highlight);
		}
		else {
			return new NodeLabel(this);
		}
	}
}
