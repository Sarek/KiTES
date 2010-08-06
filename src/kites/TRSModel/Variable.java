package kites.TRSModel;

import java.awt.Component;
import kites.visual.NodeLabel;

public class Variable extends ASTNode {
	public Variable(String name) {
		super(name);
	}

	@Override
	public Component toLabel() {
		return new NodeLabel(this);
	}
}
