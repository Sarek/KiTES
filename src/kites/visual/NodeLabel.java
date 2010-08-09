package kites.visual;

import java.awt.Color;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

import kites.TRSModel.ASTNode;
import kites.TRSModel.Rule;

public class NodeLabel extends JLabel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9139677250114966677L;
	
	private ASTNode node;
	private LinkedList<Rule> rules;
	
	public NodeLabel(ASTNode node) {
		super();
		this.setText(node.getName());
		this.setVisible(true);
		this.invalidate();
		this.node = node;
		this.rules = null;
		this.setAlignmentX(JLabel.LEFT_ALIGNMENT);
		this.setHorizontalAlignment(JLabel.LEFT);
		this.setAlignmentY(JLabel.TOP_ALIGNMENT);
		this.setVerticalAlignment(JLabel.TOP);
	}
	
	public NodeLabel(ASTNode node, LinkedList<Rule> rules) {
		super();
		this.setText(node.getName());
		this.setVisible(true);
		this.invalidate();
		this.node = node;
		this.rules = rules;
		this.setAlignmentX(JLabel.LEFT_ALIGNMENT);
		this.setHorizontalAlignment(JLabel.LEFT);
		this.setAlignmentY(JLabel.TOP_ALIGNMENT);
		this.setVerticalAlignment(JLabel.TOP);
	}
	
	public ASTNode getNode() {
		return node;
	}

	public LinkedList<Rule> getRules() {
		return rules;
	}
}
