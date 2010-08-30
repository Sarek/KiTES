package kites.visual;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.antlr.runtime.RecognitionException;

import kites.TRSModel.ASTNode;
import kites.TRSModel.Constant;
import kites.TRSModel.Rule;
import kites.TRSModel.RuleList;
import kites.exceptions.DecompositionException;
import kites.exceptions.NoRewritePossibleException;
import kites.exceptions.SyntaxErrorException;
import kites.logic.CheckTRS;
import kites.logic.Decomposition;

public class InterpreterWindow extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8983460115872591238L;

	private RuleList rulelist;

    private final JRadioButtonMenuItem menuInterpretationNonDet;
    private final JRadioButtonMenuItem menuInterpretationProg;
    private final JRadioButtonMenuItem menuInterpretationTRS;
    
    private final JRadioButtonMenuItem menuStrategyLI;
    private final JRadioButtonMenuItem menuStrategyLO;
    private final JRadioButtonMenuItem menuStrategyRI;
    private final JRadioButtonMenuItem menuStrategyRO;
    
    private JPanel results;
    private StepRewrite steprewrite;
    private JTextField txtSteps;
    private JTextField txtSize;
    private JEditorPane instance;
    
    private Rule rule;
	private ASTNode node;


	public InterpreterWindow(final RuleList rulelist, final int mode) {
		super();
		setRuleList(rulelist);
		try {
			javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch(Exception e) {
			e.printStackTrace();
		}
		
        setSize(1000, 500);
        setTitle("KiTES v0.1");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        results = new JPanel();
        results.setBackground(Color.WHITE);
        results.setLayout(new BoxLayout(results, BoxLayout.Y_AXIS));
        
        JScrollPane scrollResults = new JScrollPane(getResultsPanel());
        instance = new JEditorPane();
        JScrollPane scrollInstance = new JScrollPane(instance);
        JButton btnStep = new JButton("Schritt");
        JButton btnGo = new JButton("Ausführen");
        JButton btnCodify = new JButton("Kodifizieren");
        JPanel pane = new JPanel();
        JPanel paneStatistics = new JPanel();
        TitledBorder titlePaneStatistics = BorderFactory.createTitledBorder("Statistik");
        paneStatistics.setBorder(titlePaneStatistics);
        JLabel lblSteps = new JLabel("Schritte");
        JLabel lblSize = new JLabel("Max. Größe");
        txtSteps = new JTextField();
        txtSize = new JTextField();
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
        pane.add(scrollResults, c);
        c.fill = GridBagConstraints.BOTH;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.gridy = 4;
        c.weightx = 1;
        c.weighty = 0.2;
        
        pane.add(scrollInstance, c);
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
        
        menuInterpretationNonDet = new JRadioButtonMenuItem("Nicht-deterministisches Programm");
        menuInterpretationProg = new JRadioButtonMenuItem("Programm");
        menuInterpretationTRS = new JRadioButtonMenuItem("Termersetzungssystem");
        
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
        
        menuStrategyLI = new JRadioButtonMenuItem("Leftmost-Innermost");
        menuStrategyLO = new JRadioButtonMenuItem("Leftmost-Outermost");
        menuStrategyRI = new JRadioButtonMenuItem("Rightmost-Innermost");
        menuStrategyRO = new JRadioButtonMenuItem("Rightmost-Outermost");
        
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
        
        JMenu menuEdit = new JMenu("Bearbeiten");
        
        JMenuItem menuEditCopy = new JMenuItem("Ergebnis kopieren");
        JMenuItem menuEditClear = new JMenuItem("Ergebnis löschen");
        
        class ClearAction implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				getResultsPanel().removeAll();
				getResultsPanel().invalidate();
				getResultsPanel().repaint();
			}
        }
        menuEditClear.addActionListener(new ClearAction());
        
        class CopyAction implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String results = "";
				
				Object[] objects = getResultsPanel().getComponents();
				for(int i = 0; i < getResultsPanel().getComponentCount(); i++) {
					if(objects[i] instanceof NodeContainer) {
						results += objects[i].toString() + System.getProperty("line.separator");
					}
				}
				
				MainWindow.writeToClipboard(results, null);
			}
        }
        menuEditCopy.addActionListener(new CopyAction());
        
        menuEdit.add(menuEditCopy);
        menuEdit.add(menuEditClear);
        
        menuBar.add(menuEdit);
        
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
        
        class StepAction implements ActionListener {
        	private InterpreterWindow wnd;
        	
        	public StepAction(InterpreterWindow wnd) {
        		this.wnd = wnd;
        	}
			@Override
			public void actionPerformed(ActionEvent arg0) {
				wnd.getStepRewrite().setMode(getMode());
				wnd.getStepRewrite().setStrategy(getStrategy());
				try {
					wnd.getStepRewrite().run();
				}
				catch (Exception e) {
					MsgBox.error(e);
					e.printStackTrace();
				}
			}
        }
        
        class ResetAction implements KeyListener {
        	private InterpreterWindow wnd;
        	
        	public ResetAction(InterpreterWindow wnd) {
        		this.wnd = wnd;
        	}
			@Override
			public void keyPressed(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				int num = wnd.getResultsPanel().getComponentCount();
				System.out.println("Results panel contains " + num + "components before clearing");
				wnd.getStepRewrite().setFirst();
				System.out.println("Removing everything from results panel...");
				wnd.getResultsPanel().removeAll();
				wnd.getResultsPanel().repaint();
				txtSteps.setText("");
				txtSize.setText("");
			}
        }
        
		class RunAction implements ActionListener {
			private InterpreterWindow wnd;
			
			public RunAction(InterpreterWindow wnd) {
				this.wnd = wnd;
			}
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				wnd.getStepRewrite().setMode(getMode());
				wnd.getStepRewrite().setStrategy(getStrategy());
				try {
					while(true) {
						wnd.getStepRewrite().run();
					}
				}
				catch(NoRewritePossibleException e) {
					System.out.println("Caught the fricking exception...");
					// Do nothing. Execution is complete.
				}
				catch(Exception e) {
					System.out.println("Caught the other fricking exception...");
					MsgBox.error(e);
				}
			}
		}
		
		instance.addKeyListener(new ResetAction(this));
    	btnStep.addActionListener(new StepAction(this));
    	btnGo.addActionListener(new RunAction(this));
        
        // Check syntax of rules
        final HashMap<String, Integer> signature;
        try {
        	btnStep.setEnabled(false);
        	btnGo.setEnabled(false);
        	
        	CheckTRS checktrs = new CheckTRS(this.getRuleList());
        	
        	checktrs.variableCheck();
        	signature = checktrs.signatureCheck();
        	
        	steprewrite = new StepRewrite(getRuleList(), signature, this);
			
        	btnStep.setEnabled(true);
        	btnGo.setEnabled(true);
        	this.setVisible(true);
        }
        catch(SyntaxErrorException e) {
        	MsgBox.error(e);
        	this.dispose();
        	System.gc();
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
	
	public int getMode() {
		if(menuInterpretationNonDet.isSelected()) {
			return Decomposition.M_NONDET;
		}
		else if(menuInterpretationProg.isSelected()) {
			return Decomposition.M_PROGRAM;
		}
		else if(menuInterpretationTRS.isSelected()) {
			return Decomposition.M_TRS;
		}
		else {
			return -1;
		}
	}
	
	public int getStrategy() {
		if(menuStrategyLI.isSelected()) {
			return Decomposition.S_LI;
		}
		else if(menuStrategyLO.isSelected()) {
			return Decomposition.S_LO;
		}
		else if(menuStrategyRI.isSelected()) {
			return Decomposition.S_RI;
		}
		else if(menuStrategyRO.isSelected()) {
			return Decomposition.S_RO;
		}
		else {
			return -1;
		}
	}
	
	public JPanel getResultsPanel() {
		return results;
	}
	
	public StepRewrite getStepRewrite() {
		return steprewrite;
	}

	public JTextField getStepsField() {
		return txtSteps;
	}
	
	public JTextField getSizeField() {
		return txtSize;
	}

	public void addToResults(NodeContainer label) {
		label.setInterpreterWindow(this);
		
		getResultsPanel().add((Component)label);
		getResultsPanel().add(Box.createVerticalStrut(15));
		getResultsPanel().revalidate();
	}
	
	public JEditorPane getInstance() {
		return instance;
	}
	
	public Rule getRule() {
		return rule;
	}
	
	public ASTNode getNode() {
		return node;
	}
	
	public void nextStep(ASTNode node, Rule rule) {
		this.node = node;
		this.rule = rule;
		try {
			steprewrite.run();
		}
		catch(Exception e) {
			MsgBox.error(e);
		}
	}
	
	public InterpreterWindow getWindow() {
		return this;
	}
}
