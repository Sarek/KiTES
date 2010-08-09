package kites.TRSModel;

import java.awt.Component;

import kites.visual.NodeLabel;

public class Constant extends ASTNode {
	public Constant(String name) {
		super(name);
	}

	@Override
	public Component toLabel() {
		return new NodeLabel(this);
	}
}
