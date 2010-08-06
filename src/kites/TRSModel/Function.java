package kites.TRSModel;

import java.awt.Component;
import java.util.Iterator;
import java.util.LinkedList;

import kites.exceptions.NoChildrenException;
import kites.visual.NodeBox;
import kites.visual.NodeLabel;

public class Function extends ASTNode {
	private LinkedList<ASTNode> children;
	private boolean reversed;
	
	public Function(String name) {
		super(name);
		
		children = new LinkedList<ASTNode>();
		reversed = false;
	}
	
	@Override
	public void add(ASTNode child) {
		children.add(child);
	}
		
	public String toString() {
		String retval = name + "(";

		Iterator<ASTNode> it = getChildIterator();
		
		while(it.hasNext()) {
			retval += it.next().toString();
			if(it.hasNext()) {
				retval += ", ";
			}
		}
		
		return retval + ")";
	}
	
	public Iterator<ASTNode> getChildIterator() {
		if(reversed) {
			return this.children.descendingIterator();
		}
		else {
			return this.children.iterator();
		}
	}
	
	public Iterator<ASTNode> getRevChildIterator() {
		if(reversed) {
			return this.children.iterator();
		}
		else {
			return this.children.descendingIterator();
		}
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
	
	@Override
	public void reverse() {
		reversed = !reversed;
	}

	@Override
	public Component toLabel() {
		NodeBox nodeBox = new NodeBox(getParamCount());
		nodeBox.setHead(new NodeLabel(this));
		
		Iterator<ASTNode> childIt = getChildIterator();
		while(childIt.hasNext()) {
			nodeBox.addChild(childIt.next().toLabel());
		}
		
		return nodeBox;
	}
}
