package kites.visual;

import java.awt.Color;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import kites.TRSModel.ASTNode;
import kites.TRSModel.Rule;

public class NodeLabel extends JLabel implements NodeContainer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9139677250114966677L;
	
	private ASTNode node;
	private LinkedList<Rule> rules;
	private InterpreterWindow wnd;
	JPopupMenu popup;
	PopupListener popupListener;
	
	public NodeLabel(ASTNode node) {
		super();
		this.setBackground(Color.LIGHT_GRAY);
		this.setText(node.getName());
		this.setVisible(true);
		this.node = node;
		this.rules = null;
		this.popupListener = null;
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
		
		popup = new JPopupMenu();
		Iterator<Rule> ruleIt = rules.iterator();
		while(ruleIt.hasNext()) {
			Rule rule = ruleIt.next();
			JMenuItem item = new JMenuItem(rule.toString());
			item.addActionListener(new MenuAction(getNode(), rule));
			popup.add(item);
		}
		
		this.popupListener = new PopupListener();
		this.addMouseListener(this.popupListener);
		
		this.invalidate();
	}
	
	public ASTNode getNode() {
		return node;
	}

	public LinkedList<Rule> getRules() {
		return rules;
	}
	
	public InterpreterWindow getInterpreterWindow() {
		return wnd;
	}
	
	public void setInterpreterWindow(InterpreterWindow wnd) {
		this.wnd = wnd;
	}
	
	public NodeLabel getThis() {
		return this;
	}
	
	private class PopupListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent arg0) {
			// TODO Auto-generated method stub
			popup.show(getThis(), arg0.getX(), arg0.getY());
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
	}
	
	private class MenuAction implements ActionListener {
		private ASTNode node;
		private InterpreterWindow wnd;
		private Rule rule;
		
		public MenuAction(ASTNode node, Rule rule) {
			this.node = node;
			this.rule = rule;
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			System.out.println("Node: " + node);
			System.out.println("Rule: " + rule);
			System.out.println("Window: " + wnd);
			getThis().setBackground(Color.GREEN);
			getThis().setBorder(BorderFactory.createLineBorder(Color.GREEN));
			getInterpreterWindow().nextStep(node, rule);
		}
		
	}
	
	@Override
	public String toString() {
		return getNode().toString();
	}
	
	public void disablePopupMenu() {
		this.removeMouseListener(this.popupListener);
	}
}
