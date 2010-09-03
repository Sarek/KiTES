package kites.TRSModel;

import java.awt.Component;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import kites.visual.NodeContainer;
import kites.visual.NodeLabel;

public class Variable extends ASTNode {
	public Variable(String name) {
		super(name);
	}

	@Override
	public NodeContainer toLabel() {
		return new NodeLabel(this);
	}

	@Override
	public NodeContainer toLabelWithRule(LinkedHashMap<ASTNode, LinkedList<Rule>> decomp, boolean highlight) {
		return new NodeLabel(this);
	}
}
