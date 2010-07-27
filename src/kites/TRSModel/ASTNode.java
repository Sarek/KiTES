package kites.TRSModel;

import kites.exceptions.NodeException;

public abstract class ASTNode {
	protected String name;
	
	public ASTNode(String name) {
		this.name = name;
	}
	
	public String toString() {
		return name;
	}
	
	public String toString(String indent) {
		return indent + name;
	}
	
	public abstract void add(ASTNode a);
}
