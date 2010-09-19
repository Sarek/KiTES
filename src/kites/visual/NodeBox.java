/*
 * This file is part of KiTES.
 * 
 * Copyright 2010 Sebastian Schäfer <sarek@uliweb.de>
 *
 *   KiTES is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   KiTES is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with KiTES.  If not, see <http://www.gnu.org/licenses/>.
 */

package kites.visual;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Component;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * This is the graphical representation of a <code>Function</code>.
 * It contains a head symbol and one or more children of arbitrary
 * (i. e. of type <code>NodeContainer</code>) type.
 */
public class NodeBox extends JPanel implements NodeContainer {
	/**
	 * Eclipse still wants to have a serial everywhere...
	 */
	private static final long serialVersionUID = 4314879515239771459L;
	private GridBagConstraints constraints;
	private JLabel openPar;
	private LinkedList<NodeContainer> children;
	private NodeLabel head;
	private int params;
	private JPanel realContainer;
	
	/**
	 * Create a new <code>NodeBox</code> with a specified number of children.
	 * @param params the number of children
	 */
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
	
	/**
	 * Set the head symbol of this box.
	 * It will be displayed in the upper-left corner.
	 * @param head the graphical representation of the head symbol
	 */
	public void setHead(NodeLabel head) {
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.weighty = 0.0;
		
		realContainer.add(head, constraints);
		this.head = head;
		this.invalidate();
	}
	
	/**
	 * Add a child to this box.
	 * @param child the child to add
	 */
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
	
	/**
	 * Gives the current head symbol.
	 * @return the current head symbol
	 */
	public NodeLabel getHead() {
		return head;
	}
	
	/**
	 * Overridden to always be displayed as compact as possible
	 */
	@Override
	public Dimension getPreferredSize() {
		return this.getMinimumSize();
	}
	
	/**
	 * Overridden to always be displayed as compact as possible.
	 */
	public Dimension getMaximumSize() {
		return this.getMinimumSize();
	}
	
	/**
	 * Set the interpreter window this box will be placed in.
	 * We need this because of the popup listener, which will start
	 * a new interpretation step.
	 * This is also passed down the line to all the children.
	 * 
	 * @param wnd the window
	 */
	public void setInterpreterWindow(InterpreterWindow wnd) {
		Iterator<NodeContainer> it = children.iterator();
		
		head.setInterpreterWindow(wnd);
		while(it.hasNext()) {
			NodeContainer cont = it.next();
			cont.setInterpreterWindow(wnd);
		}
	}
	
	/**
	 * Gives an iterator over all the children
	 * @return the iterator
	 */
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

	/**
	 * Deactivate all popup menus in this box and its children
	 */
	@Override
	public void deactivate() {
		getHead().deactivate();
		Iterator<NodeContainer> childIt = getChildIterator();
		while(childIt.hasNext()) {
			childIt.next().deactivate();
		}
	}
	
	/**
	 * Add a closing parenthesis to the last child of this box.
	 * This needs to be called if this box is itself placed inside a box as its last
	 * child and therefore needs a closing parenthesis to textually
	 * close the box it lives in.
	 */
	public void addClosePar() {
		children.getLast().addClosePar();
	}
	
	/**
	 * Add a comma to the last child of this box.
	 * This needs to be called if this box is itself placed inside a box and is not
	 * the last child. Then it has to have a comma at its end, to textually
	 * "lead in" a new child of the box it lives in.
	 */
	public void addComma() {
		children.getLast().addComma();
	}
	
	/**
	 * Activate popup menus in this box and its children.
	 */
	public void activate() {
		getHead().activate();
		Iterator<NodeContainer> childIt = getChildIterator();
		while(childIt.hasNext()) {
			childIt.next().activate();
		}
	}
	
	/**
	 * Try to colorize the head symbol in this box and its children.
	 * Color will only be added, if a rule is applicable.
	 */
	public void colorize() {
		getHead().colorize();
		Iterator<NodeContainer> childIt = getChildIterator();
		while(childIt.hasNext()) {
			childIt.next().colorize();
		}
	}
}
