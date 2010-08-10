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
		this.node = node;
		this.rules = null;
		this.setAlignmentX(JLabel.LEFT_ALIGNMENT);
		this.setHorizontalAlignment(JLabel.LEFT);
		this.setAlignmentY(JLabel.TOP_ALIGNMENT);
		this.setVerticalAlignment(JLabel.TOP);
		this.invalidate();
	}
	
	public NodeLabel(ASTNode node, LinkedList<Rule> rules) {
		super();
		System.out.println("Creating node with rule:\n\t" + node);
		this.setText(node.getName());
		this.setVisible(true);
		this.setOpaque(true);
		this.setBackground(Color.YELLOW);
		this.setBorder(BorderFactory.createLineBorder(Color.YELLOW));
		this.node = node;
		this.rules = rules;
		this.setAlignmentX(JLabel.LEFT_ALIGNMENT);
		this.setHorizontalAlignment(JLabel.LEFT);
		this.setAlignmentY(JLabel.TOP_ALIGNMENT);
		this.setVerticalAlignment(JLabel.TOP);
		this.invalidate();
	}
	
	public ASTNode getNode() {
		return node;
	}

	public LinkedList<Rule> getRules() {
		return rules;
	}
}
