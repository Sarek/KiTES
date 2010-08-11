package kites.visual;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Component;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class NodeBox extends JPanel implements NodeContainer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4314879515239771459L;
	private GridBagConstraints constraints;
	private JLabel openPar;
	private JLabel closePar;
	private LinkedList<NodeContainer> children;
	private NodeLabel head;
	private int params;
	
	public NodeBox(int params) {
		super();
		this.setLayout(new GridBagLayout());
		this.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		this.setAlignmentY(JPanel.TOP_ALIGNMENT);
		this.setBackground(Color.WHITE);
		openPar = new JLabel("(");
		openPar.setAlignmentX(JLabel.LEFT_ALIGNMENT);
		openPar.setHorizontalAlignment(JLabel.LEFT);
		openPar.setAlignmentY(JLabel.TOP_ALIGNMENT);
		openPar.setVerticalAlignment(JLabel.TOP);
		openPar.invalidate();
		closePar = new JLabel(")");
		closePar.setAlignmentX(JLabel.LEFT_ALIGNMENT);
		closePar.setHorizontalAlignment(JLabel.LEFT);
		closePar.setAlignmentY(JLabel.TOP_ALIGNMENT);
		closePar.setVerticalAlignment(JLabel.BOTTOM);
		children = new LinkedList<NodeContainer>();
		this.params = params;

		constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridheight = 1;
		constraints.gridwidth = 1;
		constraints.weightx = 1.0;
		constraints.weighty = 1.0;
		
		constraints.gridx = 1;
		constraints.gridy = 0;
		this.add(openPar, constraints);
		
		constraints.gridx = 3;
		for(int i = 0; i < params - 1; i++) {
			constraints.gridy = i;
			JLabel comma = new JLabel(",");
			comma.setAlignmentX(JLabel.LEFT_ALIGNMENT);
			comma.setHorizontalAlignment(JLabel.LEFT);
			comma.setAlignmentY(JLabel.TOP_ALIGNMENT);
			comma.setVerticalAlignment(JLabel.BOTTOM);
			this.add(comma, constraints);
		}
		constraints.gridy = params - 1;
		this.add(closePar, constraints);
		this.invalidate();
	}
	
	public void setHead(NodeLabel head) {
		constraints.gridx = 0;
		constraints.gridy = 0;
		
		this.add(head, constraints);
		this.head = head;
		this.invalidate();
	}
	
	public void addChild(NodeContainer child) {
		if(children.size() < params) {
			constraints.gridx = 2;
			constraints.gridy = children.size();
			this.add((Component)child, constraints);
			children.add(child);
		}
		this.invalidate();
	}
	
	public NodeLabel getHead() {
		return head;
	}
	
	@Override
	public Dimension getPreferredSize() {
		return this.getMinimumSize();
	}
	
	public Dimension getMaximumSize() {
		return this.getMinimumSize();
	}
	
	public void setInterpreterWindow(InterpreterWindow wnd) {
		Iterator<NodeContainer> it = children.iterator();
		
		head.setInterpreterWindow(wnd);
		while(it.hasNext()) {
			NodeContainer cont = it.next();
			cont.setInterpreterWindow(wnd);
		}
	}
}
