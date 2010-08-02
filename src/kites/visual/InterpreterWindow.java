package kites.visual;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.TokenStream;

import kites.TRSModel.ASTNode;
import kites.TRSModel.RuleList;
import kites.exceptions.SyntaxErrorException;
import kites.logic.CheckTRS;
import kites.parser.TRSLexer;
import kites.parser.TRSParser;

public class InterpreterWindow extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8983460115872591238L;
	private RuleList rulelist;
	
	public InterpreterWindow(RuleList rulelist, int mode) {
		super();
		setRuleList(rulelist);
		try {
			javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		//setLocationRelativeTo(getParent());
        setSize(1000, 500);
        setTitle("KiTES v0.1");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        final JEditorPane results = new JEditorPane();
        results.setEditable(false);
        final JEditorPane instance = new JEditorPane();
        JButton btnStep = new JButton("Schritt");
        JButton btnGo = new JButton("Ausführen");
        JButton btnCodify = new JButton("Kodifizieren");
        JPanel pane = new JPanel();
        JPanel paneStatistics = new JPanel();
        TitledBorder titlePaneStatistics = BorderFactory.createTitledBorder("Statistik");
        paneStatistics.setBorder(titlePaneStatistics);
        JLabel lblSteps = new JLabel("Schritte");
        JLabel lblSize = new JLabel("Max. Größe");
        final JTextField txtSteps = new JTextField();
        final JTextField txtSize = new JTextField();
        txtSteps.setEditable(false);
        txtSize.setEditable(false);
        paneStatistics.setLayout(new GridLayout(2, 2));
        paneStatistics.add(lblSteps);
        paneStatistics.add(txtSteps);
        paneStatistics.add(lblSize);
        paneStatistics.add(txtSize);
        
        pane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(2, 2, 2, 2);
        c.gridheight = 4;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 0.8;
        pane.add(results, c);
        c.fill = GridBagConstraints.BOTH;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.gridy = 4;
        c.weightx = 1;
        c.weighty = 0.2;
        
        pane.add(instance, c);
        c.gridx = 1;
        c.gridy = 0;
        c.weighty = 0.5;
        pane.add(btnStep, c);
        c.gridy = 1;
        pane.add(btnGo, c);
        c.gridy = 2;
        pane.add(btnCodify, c);
        c.gridheight = 1;
        c.gridy = 3;
        c.weighty = 0;
        pane.add(paneStatistics, c);
        
        this.add(pane);
        
        JMenuBar menuBar = new JMenuBar();
        JMenu menuInterpretation = new JMenu("Interpretationsmodus");
        ButtonGroup grpInterpretation = new ButtonGroup();
        
        final JRadioButtonMenuItem menuInterpretationNonDet = new JRadioButtonMenuItem("Nicht-deterministisches Programm");
        final JRadioButtonMenuItem menuInterpretationProg = new JRadioButtonMenuItem("Programm");
        final JRadioButtonMenuItem menuInterpretationTRS = new JRadioButtonMenuItem("Termersetzungssystem");
        
        grpInterpretation.add(menuInterpretationNonDet);
        grpInterpretation.add(menuInterpretationProg);
        grpInterpretation.add(menuInterpretationTRS);
        
        menuInterpretationProg.setSelected(true);
        
        menuInterpretation.add(menuInterpretationNonDet);
        menuInterpretation.add(menuInterpretationProg);
        menuInterpretation.add(menuInterpretationNonDet);
        menuInterpretation.add(menuInterpretationTRS);
        
        menuBar.add(menuInterpretation);
        
        final JMenu menuStrategy = new JMenu("Reduktionsstrategie");
        ButtonGroup grpStrategy = new ButtonGroup();
        
        JRadioButtonMenuItem menuStrategyLI = new JRadioButtonMenuItem("Leftmost-Innermost");
        JRadioButtonMenuItem menuStrategyLO = new JRadioButtonMenuItem("Leftmost-Outermost");
        JRadioButtonMenuItem menuStrategyRI = new JRadioButtonMenuItem("Rightmost-Innermost");
        JRadioButtonMenuItem menuStrategyRO = new JRadioButtonMenuItem("Rightmost-Outermost");
        
        menuStrategyLO.setSelected(true);
        
        grpStrategy.add(menuStrategyLI);
        grpStrategy.add(menuStrategyLO);
        grpStrategy.add(menuStrategyRI);
        grpStrategy.add(menuStrategyRO);
        
        menuStrategy.add(menuStrategyLI);
        menuStrategy.add(menuStrategyLO);
        menuStrategy.add(menuStrategyRI);
        menuStrategy.add(menuStrategyRO);
        
        menuBar.add(menuStrategy);
        
        class StrategyAction implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(menuInterpretationProg.isSelected()) {
					System.out.println("Aktiviere Menü");
					menuStrategy.setEnabled(true);
				}
				else {
					System.out.println("Deaktiviere Menü");
					menuStrategy.setEnabled(false);
				}
			}
        }
        
        StrategyAction stratAction = new StrategyAction();
        menuInterpretationProg.addActionListener(stratAction);
        menuInterpretationNonDet.addActionListener(stratAction);
        menuInterpretationTRS.addActionListener(stratAction);
        
        this.setJMenuBar(menuBar);
        
        // Check syntax of rules
        HashMap<String, Integer> signature;
        try {
        	CheckTRS checktrs = new CheckTRS(this.getRuleList());
        	
        	checktrs.variableCheck();
        	signature = checktrs.signatureCheck();
        }
        catch(SyntaxErrorException e) {
        	MsgBox.error(e);
        }
        
        class StepAction implements ActionListener {
        	private boolean firstStep;
        	ASTNode instanceTree;
        	
        	public StepAction() {
        		firstStep = true;
        		// parse instance
        		System.out.println("Parsing instance...");
        		TRSLexer lexer = new TRSLexer(new ANTLRStringStream(instance.getText()));
        		TokenStream tokenStream = new CommonTokenStream(lexer);
        		TRSParser parser = new TRSParser(tokenStream);
        		try {
					instanceTree = parser.function();
				} catch (RecognitionException e) {
					MsgBox.error(e);
				}
        	}
        	
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(instanceTree != null) {
					if(firstStep) {
						results.setText(instanceTree.toString());
						txtSteps.setText("1");
						txtSize.setText(String.valueOf(instanceTree.getSize()));
						firstStep = false;
					}
					
					// get possible reductions according to strategy
					// perform reduction
				}
			}
        }
        
	}

	/**
	 * @param rulelist the rulelist to set
	 */
	public void setRuleList(RuleList rulelist) {
		this.rulelist = rulelist;
	}

	/**
	 * @return the rulelist
	 */
	public RuleList getRuleList() {
		return rulelist;
	}
}
