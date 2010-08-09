package kites.TRSModel;

import java.awt.Component;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import kites.exceptions.NoChildrenException;
import kites.visual.NodeLabel;

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
	
	public void add(ASTNode a) {
		// Do nothing
	}
	
	public Iterator<ASTNode> getChildIterator() throws NoChildrenException {
		throw new NoChildrenException();
	}
	
	public Iterator<ASTNode> getRevChildIterator() throws NoChildrenException {
		throw new NoChildrenException();
	}
	
	public int getParamCount() {
		return 0;
	}
	
	public int getSize() {
		return 1;
	}
	
	public void reverse() {
		// Do nothing
	}
	
	public abstract Component toLabel();
	
	public Component toLabelWithRule(LinkedHashMap<ASTNode, LinkedList<Rule>> decomp) {
		// is this constant reducible?
		if(decomp.containsKey(this)) {
			return new NodeLabel(this, decomp.get(this));
		}
		else {
			return new NodeLabel(this);
		}
	}
}
