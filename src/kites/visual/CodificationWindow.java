package kites.visual;

import java.awt.Color;
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
import kites.logic.Codification;

public class CodificationWindow extends JFrame {
	private TRSFile toCodify;
	
	public CodificationWindow(TRSFile toCodify) {
		this.toCodify = toCodify;
		this.setTitle("KiTES - Kodierung");
	    Toolkit tk = Toolkit.getDefaultToolkit();
	    Dimension screenSize = tk.getScreenSize();
		
		setSize(1000, 600);
	    setLocation((screenSize.width - this.getWidth()) / 2, (screenSize.height - this.getHeight()) / 2);
	    
	    JPanel code = new JPanel();
	    code.setLayout(new BoxLayout(code, BoxLayout.X_AXIS));
	    code.setAlignmentX(JPanel.TOP_ALIGNMENT);
	    
	    Codification codification = new Codification(toCodify);
	    codification.codify();
	    
	    JPanel rulelist = new JPanel();
	    rulelist.setBorder(BorderFactory.createTitledBorder("Regelsatz"));
	    rulelist.add((Component) codification.getCodifiedRuleList().toLabel());
	    code.add(rulelist);
	    code.add(Box.createHorizontalStrut(15));
	    rulelist.addMouseListener(new PopupListener(rulelist));
	    JPanel instance = new JPanel();
	    instance.setBorder(BorderFactory.createTitledBorder("Term"));
	    instance.add((Component) codification.getCodifiedInstance().toLabel());
	    instance.addMouseListener(new PopupListener(instance));
	    code.add(instance);
	    
	    JScrollPane scrollCode = new JScrollPane(code);
	    this.add(scrollCode);
	    this.setVisible(true);
	}
	
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
