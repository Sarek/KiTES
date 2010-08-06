package kites.visual;

import javax.swing.JLabel;

import kites.TRSModel.ASTNode;

public class NodeLabel extends JLabel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9139677250114966677L;
	
	private ASTNode node;
	
	public NodeLabel(ASTNode node) {
		super(node.getName());
		
		this.node = node;
	}
	
	public ASTNode getNode() {
		return node;
	}

}
