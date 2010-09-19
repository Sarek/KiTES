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
