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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;

import kites.TRSModel.TRSFile;
import kites.exceptions.NodeException;
import kites.logic.Codification;

/*
 * I hate GUI programming. That is the reason why some objects in the
 * window are declared final (to be used in embedded Listener classes)
 * and others have a getter.
 * Unfortunately I do not have enough time until I have to hand in this
 * code to tidy up the mess, so this has to wait till version 1.1.
 * 
 * Just pretend that every final object in here has a getter method and
 * everything will be alright ;-)
 */

/**
 * In this class's window the codified standard form of a term is
 * displayed.
 */
public class CodificationWindow extends JFrame {
	/**
	 * A serial to stop Eclipse from jammering all over the place...
	 */
	private static final long serialVersionUID = 2314314138782455490L;
	
	/**
	 * Create and display the codified standard form
	 * @param toCodify The rulelist and instance to display in codified form
	 */
	public CodificationWindow(TRSFile toCodify) {
		try {
			Codification codification = new Codification(toCodify);
		    codification.codify();
		    
			this.setTitle("KiTES - Kodierung");
		    Toolkit tk = Toolkit.getDefaultToolkit();
		    Dimension screenSize = tk.getScreenSize();
			
			setSize(1000, 600);
		    setLocation((screenSize.width - this.getWidth()) / 2, (screenSize.height - this.getHeight()) / 2);
		    
		    JPanel code = new JPanel();
		    code.setLayout(new BoxLayout(code, BoxLayout.X_AXIS));
		    code.setAlignmentX(JPanel.TOP_ALIGNMENT);
		    
		    
		    
		    JPanel rulelist = new JPanel();
		    rulelist.setBorder(BorderFactory.createTitledBorder("Regelsatz"));
		    rulelist.add((Component) codification.getCodifiedRuleList().toLabel());
		    code.add(rulelist);
		    code.add(Box.createHorizontalStrut(15));
		    rulelist.addMouseListener(new PopupListener(rulelist));
		    
		    if(codification.getCodifiedInstance() != null) {
			    JPanel instance = new JPanel();
			    instance.setBorder(BorderFactory.createTitledBorder("Term"));
			    instance.add((Component) codification.getCodifiedInstance().toLabel());
			    instance.addMouseListener(new PopupListener(instance));
			    code.add(instance);
		    }
		    
		    JScrollPane scrollCode = new JScrollPane(code);
		    this.add(scrollCode);
		    this.setVisible(true);
		}
		catch(NodeException e) {
			MsgBox.error(e);
		}
	}
	
	/**
	 * Watch for mouse clicks and open up a popup menu if a
	 * right mouse button click is seen.
	 */
	private class PopupListener implements MouseListener {
		private JPanel source;
		
		public PopupListener(JPanel source) {
			this.source = source;
		}
		
		@Override
		public void mouseClicked(MouseEvent arg0) {
			if(arg0.getButton() == 3) {
				JPopupMenu popupMenu = new JPopupMenu();
			    JMenuItem popupCopy = new JMenuItem("Kopieren");
			    popupCopy.addActionListener(new CopyAction(source));
			    popupMenu.add(popupCopy);
				popupMenu.show((Component)arg0.getSource(), arg0.getX(), arg0.getY());
			}
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
	 * Copy both codified terms to the system clipboard.
	 */
    private class CopyAction implements ActionListener {
    	private JPanel source;
    	
    	public CopyAction(JPanel source) {
    		this.source = source;
    	}
    	
		@Override
		public void actionPerformed(ActionEvent arg0) {
			String results = "";
			
			Object[] objects = source.getComponents();
			for(int i = 0; i < source.getComponentCount(); i++) {
				if(objects[i] instanceof NodeContainer) {
					results += objects[i].toString() + System.getProperty("line.separator");
				}
			}
			
			MainWindow.writeToClipboard(results, null);
		}
    }
}
