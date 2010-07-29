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
		System.out.println(name + ": Adding child: " + child.toString());
		children.add(child);
	}
		
	public String toString() {
		String retval = name + "(";
		
		//System.out.println(name + ": " + children.size());
		Iterator<ASTNode> it = children.iterator();
		
		while(it.hasNext()) {
			retval += it.next().toString() + ",";
		}
		
		return retval + ")";
	}
	
	public Iterator<ASTNode> getChildIterator() throws NoChildrenException {
		return this.children.iterator();
	}
	
	public int getParamCount() {
		return children.size();
	}
}
