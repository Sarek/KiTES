package kites.visual;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Component;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
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
	private JPanel realContainer;
	
	public NodeBox(int params) {
		super();
		realContainer = new JPanel();
		realContainer.setBackground(new Color(240, 240, 240));
		realContainer.setLayout(new GridBagLayout());
		realContainer.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		realContainer.setAlignmentY(JPanel.TOP_ALIGNMENT);
		this.add(realContainer);
		this.setBackground(new Color(240, 240, 240));
		this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		this.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		this.setAlignmentY(JPanel.TOP_ALIGNMENT);
		openPar = new JLabel("(");
		openPar.setAlignmentX(JLabel.LEFT_ALIGNMENT);
		openPar.setHorizontalAlignment(JLabel.LEFT);
		openPar.setAlignmentY(JLabel.TOP_ALIGNMENT);
		openPar.setVerticalAlignment(JLabel.TOP);
		openPar.invalidate();
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
		realContainer.add(openPar, constraints);
		
		constraints.gridx = 3;
		constraints.gridy = params - 1;
		this.invalidate();
	}
	
	public void setHead(NodeLabel head) {
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.weighty = 0.0;
		
		realContainer.add(head, constraints);
		this.head = head;
		this.invalidate();
	}
	
	public void addChild(NodeContainer child) {
		if(children.size() < params) {
			if(children.size() < params - 1) {
				child.addComma();
			}
			else {
				child.addClosePar();
			}
			constraints.gridx = 2;
			constraints.weightx = 0.0;
			constraints.gridy = children.size();
			realContainer.add((Component)child, constraints);
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
	
	public Iterator<NodeContainer> getChildIterator() {
		return children.iterator();
	}
	
	/**
	 * Return a string representation of the tree beginning from this node.
	 * As an ASTNode is saved in this object, there is no need to iterate
	 * over this node's children again, as they will be the same as the
	 * ASTNode's children.
	 * 
	 * @return The string representation
	 */
	@Override
	public String toString() {
		return getHead().toString();
	}

	@Override
	public void deactivate() {
		getHead().deactivate();
		Iterator<NodeContainer> childIt = getChildIterator();
		while(childIt.hasNext()) {
			childIt.next().deactivate();
		}
	}
	
	public void addClosePar() {
		children.getLast().addClosePar();
	}
	
	public void addComma() {
		children.getLast().addComma();
	}
	
	public void activate() {
		getHead().activate();
		Iterator<NodeContainer> childIt = getChildIterator();
		while(childIt.hasNext()) {
			childIt.next().activate();
		}
	}
	
	public void colorize() {
		getHead().colorize();
		Iterator<NodeContainer> childIt = getChildIterator();
		while(childIt.hasNext()) {
			childIt.next().colorize();
		}
	}
}
