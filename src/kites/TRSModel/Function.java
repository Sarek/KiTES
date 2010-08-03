package kites.TRSModel;

import java.util.Iterator;
import java.util.LinkedList;

import kites.exceptions.NoChildrenException;

public class Function extends ASTNode {
	private LinkedList<ASTNode> children;
	
	public Function(String name) {
		super(name);
		
		children = new LinkedList<ASTNode>();
	}
	
	public void add(ASTNode child) {
		children.add(child);
	}
		
	public String toString() {
		String retval = name + "(";

		Iterator<ASTNode> it = children.iterator();
		
		while(it.hasNext()) {
			retval += it.next().toString();
			if(it.hasNext()) {
				retval += ", ";
			}
		}
		
		return retval + ")";
	}
	
	public Iterator<ASTNode> getChildIterator() {
		return this.children.iterator();
	}
	
	public Iterator<ASTNode> getRevChildIterator() {
		return this.children.descendingIterator();
	}
	
	public int getParamCount() {
		return children.size();
	}
	
	public int getSize() {
		int retval = 1;
		Iterator<ASTNode> childrenIt = getChildIterator();
		
		while(childrenIt.hasNext()) {
			retval += childrenIt.next().getSize();
		}
		
		return retval;
	}
}
