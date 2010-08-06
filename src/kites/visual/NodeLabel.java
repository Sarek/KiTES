package kites.visual;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

import kites.TRSModel.ASTNode;

public class NodeLabel extends JLabel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9139677250114966677L;
	
	private ASTNode node;
	
	public NodeLabel(ASTNode node) {
		super("Test");
		this.setText(node.getName());
		this.setVisible(true);
		this.invalidate();
		this.node = node;
		this.setAlignmentX(JLabel.LEFT_ALIGNMENT);
		this.setHorizontalAlignment(JLabel.LEFT);
		this.setAlignmentY(JLabel.TOP_ALIGNMENT);
		this.setVerticalAlignment(JLabel.TOP);
	}
	
	public ASTNode getNode() {
		return node;
	}

}
