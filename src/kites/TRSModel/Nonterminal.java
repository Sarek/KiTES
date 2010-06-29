package kites.TRSModel;

import java.util.Iterator;
import java.util.LinkedList;

public class Nonterminal extends ASTNode {
	private LinkedList<ASTNode> children;
	
	public Nonterminal(String name) {
		super(name);
		
		children = new LinkedList<ASTNode>();
	}
	
	public void addChild(ASTNode child) {
		children.add(child);
	}
	
	public String toString(String indent) {
		String retval = indent + name + "\n";
		indent += "   ";
		
		Iterator<ASTNode> it = children.iterator();
		
		while(it.hasNext()) {
			retval += it.next().toString(indent) + "\n";
		}
		
		return retval;
	}
}
