package kites.TRSModel;

import java.awt.Component;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import kites.visual.NodeLabel;

public class Variable extends ASTNode {
	public Variable(String name) {
		super(name);
	}

	@Override
	public Component toLabel() {
		return new NodeLabel(this);
	}

	@Override
	public Component toLabelWithRule(LinkedHashMap<ASTNode, LinkedList<Rule>> decomp) {
		return new NodeLabel(this);
	}
}
