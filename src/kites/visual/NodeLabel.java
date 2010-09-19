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

/**
 * This is the graphical representation of an
 * <code>ASTNode</code> without children (i. e. variable
 * or constant).
 */
public class NodeLabel extends JLabel implements NodeContainer {
	/**
	 * Eclipse still wants a serial field
	 */
	private static final long serialVersionUID = 9139677250114966677L;
	
	private ASTNode node;
	private LinkedList<Rule> rules;
	private InterpreterWindow wnd;
	JPopupMenu popup;
	PopupListener popupListener;
	private boolean canHighlight;
	
	/**
	 * Create a new graphical representation for a node
	 * @param node the node this label is for
	 */
	public NodeLabel(ASTNode node) {
		super();
		this.setBackground(new Color(240, 240, 240));
		this.setText(node.getName());
		this.setVisible(true);
		this.node = node;
		this.rules = null;
		this.popupListener = null;
		this.setAlignmentX(JLabel.LEFT_ALIGNMENT);
		this.setHorizontalAlignment(JLabel.LEFT);
		this.setAlignmentY(JLabel.TOP_ALIGNMENT);
		this.setVerticalAlignment(JLabel.TOP);
		//this.setBorder(BorderFactory.createLineBorder(Color.RED));
		this.invalidate();
	}
	
	/**
	 * Create a new graphical representation for a node and embed
	 * information about rules applicable to this node
	 * @param node the node this label is for
	 * @param rules list of applicable rules
	 * @param activate activate immediately
	 */
	public NodeLabel(ASTNode node, LinkedList<Rule> rules, boolean activate) {
		super();
		this.setBackground(new Color(240, 240, 240));
		this.canHighlight = !rules.isEmpty();
		this.setText(node.getName());
		this.setVisible(true);
		this.setOpaque(true);

		this.node = node;
		this.rules = rules;
		this.setAlignmentX(JLabel.LEFT_ALIGNMENT);
		this.setHorizontalAlignment(JLabel.LEFT);
		this.setAlignmentY(JLabel.TOP_ALIGNMENT);
		this.setVerticalAlignment(JLabel.TOP);
		
		if(activate)
			activate();
		
		this.invalidate();
	}
	
	/**
	 * Activate this node.
	 * @see kites.visual.NodeContainer.activate()
	 */
	public void activate() {
		if(canHighlight) {
			colorize();
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
	}
	
	/**
	 * Colorize this node.
	 * @see kites.visual.NodeContainer.colorize()
	 */
	public void colorize() {
		if(canHighlight) {
			this.setBackground(Color.YELLOW);
			this.setBorder(BorderFactory.createLineBorder(Color.YELLOW));
		}
	}
	
	/**
	 * Gives the node this label represents.
	 * @return the node
	 */
	public ASTNode getNode() {
		return node;
	}

	/**
	 * Gives a list of applicable rules.
	 * @return list of rules
	 */
	public LinkedList<Rule> getRules() {
		return rules;
	}
	
	/**
	 * Gives the interpreter window this label wants to be displayed in
	 * @return the window
	 */
	public InterpreterWindow getInterpreterWindow() {
		return wnd;
	}
	
	/**
	 * Set the interpreter window this label will be displayed in
	 * @param wnd the window
	 */
	public void setInterpreterWindow(InterpreterWindow wnd) {
		this.wnd = wnd;
	}
	
	/**
	 * Gives this label.
	 * This may seem idiotic, but it is quite nice in conjunction with embedded
	 * listener classes.
	 * @return this
	 */
	public NodeLabel getThis() {
		return this;
	}
	
	/**
	 * Listen on this label, if a right mouse click is seen,
	 * open up the popup menu
	 */
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
	
	/**
	 * Listener in the popup menu for each rule.
	 * When called, begins a new interpretation step using the chosen rule.
	 */
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
			getThis().setBackground(Color.GREEN);
			getThis().setBorder(BorderFactory.createLineBorder(Color.GREEN));
			getInterpreterWindow().nextStep(node, rule);
		}
		
	}

	/**
	 * Gives a string representation of the node
	 * this is the graphical representation of.
	 */
	@Override
	public String toString() {
		return getNode().toString();
	}
	
	/**
	 * Deactivate this label
	 * 
	 * @see kites.visual.NodeContainer.deactivate()
	 * @see kites.visual.NodeContainer.activate()
	 */
	public void deactivate() {
		this.removeMouseListener(this.popupListener);
	}
	
	/**
	 * Add a closing parenthesis to this label.
	 * Needed when it is the last child element of a <code>NodeBox</code>
	 * 
	 * @see kites.visual.NodeContainer.addClosePar()
	 */
	public void addClosePar() {
		this.setText(this.getText() + ")");
		this.revalidate();
	}
	
	/**
	 * Add a comma to this label.
	 * Needed when it is not the last child element of a <code>NodeBox</code>,
	 * but still a child element. 
	 */
	public void addComma() {
		this.setText(this.getText() + ",");
		this.revalidate();
	}
}
