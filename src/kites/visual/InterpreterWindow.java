package kites.visual;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextField;

import kites.TRSModel.RuleList;

public class InterpreterWindow extends JFrame {
	private RuleList rulelist;
	
	public InterpreterWindow(RuleList rulelist, int mode) {
		super();
		try {
			javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		setLocationRelativeTo(getParent());
        setSize(1000, 500);
        setTitle("KiTES v0.1");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        JEditorPane results = new JEditorPane();
        JTextField instance = new JTextField();
        JButton btnStep = new JButton("Schritt");
        JButton btnGo = new JButton("Ausführen");
        JButton btnCodify = new JButton("Kodifizieren");
        JPanel pane = new JPanel();
        
        pane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridheight = 3;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 1;
        pane.add(results, c);
        c.fill = GridBagConstraints.BOTH;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.gridy = 3;
        c.weightx = 0;
        c.weighty = 0;
        pane.add(instance, c);
        c.gridx = 1;
        c.gridy = 0;
        c.weighty = 0.5;
        pane.add(btnStep, c);
        c.gridy = 1;
        pane.add(btnGo, c);
        c.gridy = 2;
        pane.add(btnCodify, c);
        
        this.add(pane);
        
        JMenuBar menuBar = new JMenuBar();
        JMenu menuInterpretation = new JMenu("Interpretation");
        ButtonGroup grpInterpretation = new ButtonGroup();
        JRadioButtonMenuItem menuInterpretationNonDet = new JRadioButtonMenuItem("Nicht-deterministisches Programm");
        grpInterpretation.add(menuInterpretationNonDet);
        JRadioButtonMenuItem menuInterpretationProg = new JRadioButtonMenuItem("Programm");
        grpInterpretation.add(menuInterpretationProg);
        menuInterpretationProg.setSelected(true);
        JRadioButtonMenuItem menuInterpretationTRS = new JRadioButtonMenuItem("Termersetzungssystem");
        grpInterpretation.add(menuInterpretationTRS);
        
        menuInterpretation.add(menuInterpretationNonDet);
        menuInterpretation.add(menuInterpretationProg);
        menuInterpretation.add(menuInterpretationNonDet);
        menuInterpretation.add(menuInterpretationTRS);
        menuBar.add(menuInterpretation);
        this.setJMenuBar(menuBar);
	}
}
