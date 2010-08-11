package kites.TRSModel;

import java.awt.Component;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import kites.visual.NodeContainer;
import kites.visual.NodeLabel;

public class Constant extends ASTNode {
	public Constant(String name) {
		super(name);
	}

	@Override
	public NodeContainer toLabel() {
		return new NodeLabel(this);
	}

	@Override
	public NodeContainer toLabelWithRule(LinkedHashMap<ASTNode, LinkedList<Rule>> decomp) {
		// is this constant reducible?
		if(decomp.containsKey(this)) {
			return new NodeLabel(this, decomp.get(this));
		}
		else {
			return new NodeLabel(this);
		}

	}
}
