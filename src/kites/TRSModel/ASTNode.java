package kites.TRSModel;

import java.util.Iterator;

import kites.exceptions.NoChildrenException;

public abstract class ASTNode {
	protected String name;
	
	public ASTNode(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public int getLine() {
		return 0;
	}
	
	public String toString() {
		return name;
	}
	
	public String toString(String indent) {
		return indent + name;
	}
	
	public abstract void add(ASTNode a);
	
	public Iterator<ASTNode> getChildIterator() throws NoChildrenException {
		throw new NoChildrenException();
	}
	
	public Iterator<ASTNode> getRevChildIterator() throws NoChildrenException {
		throw new NoChildrenException();
	}
	
	public int getParamCount() {
		return 0;
	}
}
