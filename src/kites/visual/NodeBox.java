package kites.visual;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Component;
import java.util.LinkedList;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class NodeBox extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4314879515239771459L;
	private GridBagConstraints constraints;
	private JLabel openPar;
	private JLabel closePar;
	private JLabel comma;
	private LinkedList<Component> children;
	private NodeLabel head;
	private int params;
	
	public NodeBox(int params) {
		super();
		this.setLayout(new GridBagLayout());
		openPar = new JLabel("(");
		closePar = new JLabel(")");
		comma = new JLabel(",");
		children = new LinkedList<Component>();
		this.params = params;
		
		constraints = new GridBagConstraints();
		constraints.gridheight = params;
		constraints.gridwidth = 4;
		
		constraints.gridx = 1;
		constraints.gridy = 0;
		this.add(openPar, constraints);
		
		constraints.gridx = 3;
		for(int i = 0; i < params - 1; i++) {
			constraints.gridy = i;
			this.add(comma, constraints);
		}
		constraints.gridy++;
		this.add(closePar, constraints);
	}
	
	public void setHead(NodeLabel head) {
		constraints.gridx = 0;
		constraints.gridy = 0;
		
		this.add(head, constraints);
		this.head = head;
	}
	
	public void addChild(Component child) {
		if(children.size() < params) {
			constraints.gridx = 2;
			constraints.gridy = children.size();
			this.add(child, constraints);
		}
	}
	
	public NodeLabel getHead() {
		return head;
	}
}
