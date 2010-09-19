package kites.TRSModel;

import java.util.LinkedHashMap;
import java.util.LinkedList;

import kites.visual.NodeContainer;
import kites.visual.NodeLabel;

/**
 * This class represents a variable symbol in a term.
 * It is derived from an <code>ASTNode</code>
 * 
 * @see kites.TRSModel.ASTNode
 */
public class Variable extends ASTNode {
	/**
	 * Create a new variable with a name.
	 * 
	 * @param name The variable's name
	 */
	public Variable(String name) {
		super(name);
	}

	/**
	 * Gives the graphical representation of this variable
	 * 
	 * @return The graphical representation
	 */
	@Override
	public NodeContainer toLabel() {
		return new NodeLabel(this);
	}

	/**
	 * Gives the graphical representation of this variable and
	 * embeds information about applicable rules in it.
	 * As variables alone can never be reduced, this is exactly the
	 * same as <code>toLabel()</code>
	 * 
	 * @param highlight Shall the node be initially highlighted if
	 *        one or more rules can be applied
	 * @return the graphical representation
	 */
	@Override
	public NodeContainer toLabelWithRule(LinkedHashMap<ASTNode, LinkedList<Rule>> decomp, boolean highlight) {
		return new NodeLabel(this);
	}
}
