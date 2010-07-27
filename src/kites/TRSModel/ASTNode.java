package kites.TRSModel;

import kites.exceptions.NodeException;

public abstract class ASTNode {
	protected String name;
	
	public ASTNode(String name) {
		this.name = name;
	}
	
	public String toString(String indent) {
		return indent + name;
	}
	
	public void add(ASTNode a) {
		// This is only for abstraction purposes here.
		// Will be implemented in subtypes.
	}
}
