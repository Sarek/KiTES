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
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileWriter;
import java.net.URISyntaxException;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import kites.TRSModel.ASTNode;
import kites.TRSModel.Rule;
import kites.TRSModel.TRSFile;
import kites.exceptions.NoRewritePossibleException;
import kites.exceptions.SyntaxErrorException;
import kites.logic.CheckTRS;
import kites.logic.Decomposition;

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
 * This is the interpreter window. It is opened by the <code>MainWindow</code>.
 * The <code>TRSFile</code> object is loaded and displayed in the top. If an
 * instance was given it will also be displayed in the second to top editor pane.
 * From here interpretation can be started, the interpretation modes can be chosen
 * and interpretation results will be displayed.
 */
public class InterpreterWindow extends JFrame {
	/**
	 * Eclipse wants this class to have a serial version id
	 */
	private static final long serialVersionUID = -8983460115872591238L;

	private TRSFile rulelist;

    private final JRadioButtonMenuItem menuInterpretationNonDet;
    private final JRadioButtonMenuItem menuInterpretationProg;
    private final JRadioButtonMenuItem menuInterpretationTRS;
    
    private final JRadioButtonMenuItem menuStrategyLI;
    private final JRadioButtonMenuItem menuStrategyLO;
    private final JRadioButtonMenuItem menuStrategyRI;
    private final JRadioButtonMenuItem menuStrategyRO;
    
    private JPanel results, leftSide;
    private JScrollPane scrollResults;
    private StepRewrite steprewrite;
    private JTextField txtSteps, txtSize;
    private JEditorPane instance;
    
    private Rule rule;
	private ASTNode node;
	private String programDirectory;


	/**
	 * Create and open the interpreter window.
	 * Before opening the interpreter window some syntax checks on the rulest
	 * are performed and the choice of interpretation modes will be restricted accordingly.
	 * 
	 * @param rulelist The rulelist to use for interpretation
	 */
	public InterpreterWindow(final TRSFile rulelist) {
		super();
		
		try {
			File jarFile = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
			programDirectory = jarFile.getParent();
		}
		catch(URISyntaxException e) {
			MsgBox.error("Cannot find program path. As now everything will go horribly wrong, I quit!");
			System.exit(-1);
		}

		setRuleList(rulelist);
		try {
			javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch(Exception e) {
			MsgBox.error(e);
		}
		
        setSize(1000, 500);
        setTitle("KiTES");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        leftSide = new JPanel();
        leftSide.setLayout(new GridBagLayout());
        
        results = new JPanel();
        results.setBackground(Color.WHITE);
        results.setLayout(new BoxLayout(results, BoxLayout.Y_AXIS));
        
        final JPopupMenu resultsPopup = new JPopupMenu();
        JMenuItem resultsPopupClear = new JMenuItem("Ergebnis löschen");
        JMenuItem resultsPopupCopy = new JMenuItem("Ergebnis kopieren");
        resultsPopup.add(resultsPopupCopy);
        resultsPopup.add(resultsPopupClear);
        
        /**
         * This listener listens for right mouse click in the results pane
         * to display a popup menu from which the results can be copied to
         * the system clipboard or the pane can be cleared.
         */
        class PopupListener implements MouseListener {

    		@Override
    		public void mouseClicked(MouseEvent arg0) {
    			if(arg0.getButton() == 3) {
    				resultsPopup.show(getResultsPanel(), arg0.getX(), arg0.getY());
    			}
    		}

    		@Override
    		public void mouseEntered(MouseEvent arg0) {
    		}

    		@Override
    		public void mouseExited(MouseEvent arg0) {
    		}

    		@Override
    		public void mousePressed(MouseEvent arg0) {
    		}

    		@Override
    		public void mouseReleased(MouseEvent arg0) {
    		}
    	}
        results.addMouseListener(new PopupListener());
        
        scrollResults = new JScrollPane(getResultsPanel());
        instance = new JEditorPane();
        instance.setToolTipText("Hier eine Instanz des Regelsystems eingeben");

        JScrollPane scrollInstance = new JScrollPane(instance);
        
        JEditorPane source = new JEditorPane();
        source.setEditable(false);
        JScrollPane scrollSource = new JScrollPane(source);
        
        final JEditorPane endResult = new JEditorPane();
        endResult.setEditable(false);
        final JScrollPane scrollEndResult = new JScrollPane(endResult);
        
        scrollResults.setVisible(false);
        scrollEndResult.setVisible(false);
        
        GridBagConstraints a = new GridBagConstraints();
        a.fill = GridBagConstraints.BOTH;
        a.insets = new Insets(2, 2, 2, 2);
        a.gridheight = 1;
        a.gridwidth = 1;
        a.gridx = 0;
        a.gridy = 0;
        a.weightx = 1;
        a.weighty = 0.15;
        leftSide.add(scrollSource, a);
        a.gridy = 1;
        a.weighty = 0.1;
        leftSide.add(scrollInstance, a);
        a.gridy = 2;
        a.weighty = 0.7;
        leftSide.add(scrollResults, a);
        a.gridy = 3;
        a.weighty = 0.05;
        leftSide.add(scrollEndResult, a);
        
        if(rulelist.getInstance() != null) {
        	instance.setText(rulelist.getInstance().toString());
        }
        source.setText(rulelist.toString());

        ImageIcon icoStep = new ImageIcon(programDirectory + File.separator + "icons" + File.separator + "step-big.png");
        final JButton btnStep = new JButton("Schritt", icoStep);
        btnStep.setToolTipText("Eine Reduktion durchführen");
        
        ImageIcon icoGo = new ImageIcon(programDirectory + File.separator + "icons" + File.separator + "run-big.png");
        final JButton btnGo = new JButton("Ausführen", icoGo);
        btnGo.setToolTipText("Alle möglichen Reduktionen durchführen");
        
        ImageIcon icoCodify = new ImageIcon(programDirectory + File.separator + "icons" + File.separator + "codify-big.png");
        JButton btnCodify = new JButton("Kodifizieren", icoCodify);
        btnCodify.setToolTipText("Regelsystem und Instanz in Normalform überführen");
        
        JPanel pane = new JPanel();
        JPanel paneStatistics = new JPanel();
        TitledBorder titlePaneStatistics = BorderFactory.createTitledBorder("Statistik");
        paneStatistics.setBorder(titlePaneStatistics);
        JLabel lblSteps = new JLabel("Schritte");
        JLabel lblSize = new JLabel("Max. Größe");
        txtSteps = new JTextField();
        txtSteps.setToolTipText("Durchgeführte Reduktionsschritte");
        txtSize = new JTextField();
        txtSize.setToolTipText("Größte Anzahl von Symbolen während der Ausführung");
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
        pane.add(leftSide, c);
        
        c.fill = GridBagConstraints.BOTH;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.gridy = 4;
        c.weightx = 1;
        c.weighty = 0.2;
        //pane.add(scrollInstance, c);
        
        c.gridx = 1;
        c.gridy = 0;
        c.weighty = 0.5;
        c.weightx = 0;
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
        JMenuItem menuEditSave = new JMenuItem("Regeln und Term speichern");
        
        /**
         * This listener is able to clear the results pane
         */
        class ClearAction implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent arg0) { 
				scrollResults.setVisible(false);
				scrollEndResult.setVisible(false);
				leftSide.revalidate();
				getResultsPanel().removeAll();
				getResultsPanel().invalidate();
				getResultsPanel().repaint();
				getStepRewrite().setFirst();
			}
        }
        menuEditClear.addActionListener(new ClearAction());
        resultsPopupClear.addActionListener(new ClearAction());
        
        /**
         * This listener copies the contents of the results pane
         * to the system clipboard.
         */
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
        resultsPopupCopy.addActionListener(new CopyAction());
        
        /**
         * Saves the current rulelist and instance in a file
         */
        class SaveAction implements ActionListener {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser();
				int diaRetval = fc.showSaveDialog(InterpreterWindow.this);
				
				if(diaRetval == JFileChooser.APPROVE_OPTION) {
					try {
						FileWriter writer = new FileWriter(fc.getSelectedFile());
						writer.write(getRuleList().toString());
						writer.write("\n\n#instance " + getInstance().getText());
						writer.close();
					}
					catch(Exception e) {
						MsgBox.error(e);
					}
				}
			}
        }
        menuEditSave.addActionListener(new SaveAction());
        
        menuEdit.add(menuEditCopy);
        menuEdit.add(menuEditClear);
        menuEdit.add(new JSeparator());
        menuEdit.add(menuEditSave);
        
        menuBar.add(menuEdit);
        
        this.setJMenuBar(menuBar);
        
        /**
         * Steps through the interpretation
         */
        class StepAction implements ActionListener {
        	private InterpreterWindow wnd;
        	boolean rewriteState;
        	
        	public StepAction(InterpreterWindow wnd) {
        		this.wnd = wnd;
        		this.rewriteState = true;
				wnd.getStepRewrite().setMode(getMode());
				wnd.getStepRewrite().setStrategy(getStrategy());
        	}
			@Override
			public void actionPerformed(ActionEvent arg0) {
				wnd.getStepRewrite().setMode(getMode());
				wnd.getStepRewrite().setStrategy(getStrategy());
				
				switch(getMode()) {
				case Decomposition.M_NONDET:
				case Decomposition.M_TRS:
					clickableStep(arg0);
					break;
					
				case Decomposition.M_PROGRAM:
					programStep(arg0);
					break;
				
				default:
					MsgBox.error("Unbekannter Interpretations-Modus!");
				}
			}
			
			private void reset() {
				rewriteState = true;
			}
			
			private void clickableStep(ActionEvent arg0) {
				try {
					wnd.getStepRewrite().run();
					((JButton) arg0.getSource()).setEnabled(false);
				}
				catch(Exception e) {
					MsgBox.error(e);
				}
			}
			
			private void programStep(ActionEvent arg0) {
				if(rewriteState) {
					// do the rewrite
					try {
						wnd.getStepRewrite().run();
						rewriteState = !rewriteState;
						if(getWindow().getStepRewrite().isStepPossible()) {
							((JButton) arg0.getSource()).setText("Ersetzung\nfinden");
						}
						else {
							((JButton) arg0.getSource()).setEnabled(false);
							endResult.setText(getWindow().getStepRewrite().getInstanceTree().toString());
							scrollEndResult.setVisible(true);
							leftSide.revalidate();
						}
					}
					catch(Exception e) {
						MsgBox.error(e);
					}
				}
				else {
					try {
						// colorize the last element in the results pane
						NodeContainer comp = (NodeContainer)getResultsPanel().getComponent(getResultsPanel().getComponentCount() - 2);
						comp.colorize();
						getResultsPanel().revalidate();
						rewriteState = !rewriteState;
						((JButton) arg0.getSource()).setText("Ersetzen");
					}
					catch (Exception e) {
						MsgBox.error(e);
					}
				}
			}
        }

        /**
         * Update the display according to the currently chosen interpretation
         * method. If an interpretation was currently underway, it will be aborted
         * and the results pane cleared.
         */
        class StrategyAction implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(menuInterpretationProg.isSelected()) {
					menuStrategy.setEnabled(true);
					getStepRewrite().setFirst();
					getResultsPanel().removeAll();
					scrollResults.setVisible(false);
					endResult.setText("");
					scrollEndResult.setVisible(false);
					leftSide.revalidate();
					btnStep.setEnabled(true);
					((StepAction) btnStep.getActionListeners()[0]).reset();
					btnGo.setEnabled(true);
				}
				else {
					menuStrategy.setEnabled(false);
					getStepRewrite().setFirst();
					getResultsPanel().removeAll();
					scrollResults.setVisible(false);
					endResult.setText("");
					scrollEndResult.setVisible(false);
					leftSide.revalidate();
					btnStep.setEnabled(true);
					((StepAction) btnStep.getActionListeners()[0]).reset();
					btnStep.setText("Schritt");
					btnGo.setEnabled(false);
				}
				updateTitle();
			}
        }
        
        StrategyAction stratAction = new StrategyAction();
        menuInterpretationProg.addActionListener(stratAction);
        menuInterpretationNonDet.addActionListener(stratAction);
        menuInterpretationTRS.addActionListener(stratAction);
        
        /**
         * This listener is added to the instance entry field.
         * Whenever the instance is changed, it will abort an interpretation
         * that is underway and will clear the results pane.
         */
        class ResetAction implements KeyListener {
        	private InterpreterWindow wnd;
        	
        	public ResetAction(InterpreterWindow wnd) {
        		this.wnd = wnd;
        	}
			@Override
			public void keyPressed(KeyEvent arg0) {
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				wnd.getStepRewrite().setFirst();
				wnd.getResultsPanel().removeAll();
				wnd.scrollResults.setVisible(false);
				scrollEndResult.setVisible(false);
				wnd.getResultsPanel().repaint();
				wnd.scrollResults.repaint();
				scrollEndResult.repaint();
				btnStep.setEnabled(true);
				((StepAction) btnStep.getActionListeners()[0]).reset();
				btnStep.setText("Schritt");
				txtSteps.setText("");
				txtSize.setText("");
			}
        }
        
        /**
         * Does the complete interpretation, until no more rewrites are possible.
         * This is only possible in program interpretation mode.
         */
		class RunAction implements ActionListener {
			private InterpreterWindow wnd;
			
			public RunAction(InterpreterWindow wnd) {
				this.wnd = wnd;
			}
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// clear the results pane
				wnd.getResultsPanel().removeAll();
				wnd.getResultsPanel().invalidate();
				wnd.getResultsPanel().repaint();
				
				wnd.getStepRewrite().setFirst();
				wnd.getStepRewrite().setMode(getMode());
				wnd.getStepRewrite().setStrategy(getStrategy());
				try {
					while(true) {
						wnd.getStepRewrite().run();
						
						if(Integer.valueOf(txtSteps.getText()) % 250 == 0) {
							if(MsgBox.question("Es wurden " + txtSteps.getText() + " Reduktionen durchgeführt.\nSoll die Berechnung abgebrochen werden?")) {
								break;
							}
						}
					}
				}
				catch(NoRewritePossibleException e) {
					Component[] comps = wnd.getResultsPanel().getComponents();
					for(int i = 0; i < wnd.getResultsPanel().getComponentCount(); i++) {
						if(comps[i] instanceof NodeContainer) {
							((NodeContainer) comps[i]).colorize();
						}
					}
					endResult.setText(wnd.getStepRewrite().getInstanceTree().toString());
					scrollEndResult.setVisible(true);
					leftSide.revalidate();
				}
				catch(Exception e) {
					MsgBox.error(e);
				}
			}
		}
		
		instance.addKeyListener(new ResetAction(this));
    	
		/**
		 * Open the <code>CodificationWindow</code> and display the
		 * codified ruleset and instance.
		 */
		class CodifyAction implements ActionListener {
			@Override
			@SuppressWarnings("unused") // codeWin is never read
			public void actionPerformed(ActionEvent arg0) {
				StepRewrite steprwrt = getStepRewrite();
				try {
					steprwrt.parseInstance();
					getRuleList().setInstance(steprwrt.getInstanceTree());
				}
				catch(Exception e) {
					MsgBox.error(e);
				}
				CodificationWindow codeWin = new CodificationWindow(getRuleList());
			}
    	}
    	btnCodify.addActionListener(new CodifyAction());
        
        // Check syntax of rules
        HashMap<String, Integer> signature;
        
        btnStep.setEnabled(false);
    	btnGo.setEnabled(false);
    	CheckTRS checktrs = new CheckTRS(this.getRuleList());
    	signature = null;
    	menuInterpretationTRS.setEnabled(false);
    	menuInterpretationNonDet.setEnabled(false);
    	menuInterpretationProg.setEnabled(false);
    	
        try {
        	// check for TRS execution mode
        	checktrs.variableCheck();
        	signature = checktrs.signatureCheck();
		
        	menuInterpretationTRS.setEnabled(true);
        	menuInterpretationTRS.setSelected(true);
        	btnStep.setEnabled(true);
        	
        	try {
            	// check for non-deterministic execution
            	checktrs.isSigmaGammaSystem();
            	menuInterpretationNonDet.setEnabled(true);
            	menuInterpretationNonDet.setSelected(true);
            	
            	try {
            		checktrs.checkLeftLinear();
            		checktrs.unifiabilityCheck();
            		menuInterpretationProg.setEnabled(true);
            		menuInterpretationProg.setSelected(true);
            		btnGo.setEnabled(true);
            	}
            	catch(SyntaxErrorException e) {
            		MsgBox.info("Das Termersetzungssystem kann nicht als Programm ausgeführt werden: \n" + e.getLocalizedMessage());
            	}
            }
            catch(SyntaxErrorException e) {
            	MsgBox.info("Das Termersetzungssystem kann nicht als nicht-deterministisches Programm ausgeführt werden: \n" + e.getLocalizedMessage());
            }
            
        	this.setVisible(true);
        	steprewrite = new StepRewrite(getRuleList(), signature, this);
        	btnStep.addActionListener(new StepAction(this));
        	btnGo.addActionListener(new RunAction(this));
        	updateTitle();
        }
        catch(SyntaxErrorException e) {
        	// if not even the basic requirements are fulfilled, we only print an
        	// error message and destroy this window.
        	MsgBox.error(e);
        	this.dispose();
        	System.gc();
        }
	}

	/**
	 * Set the rulelist to be interpreted
	 * @param rulelist the rulelist to set
	 */
	public void setRuleList(TRSFile rulelist) {
		this.rulelist = rulelist;
	}

	/**
	 * Gives the current rulelist
	 * @return the rulelist
	 */
	public TRSFile getRuleList() {
		return rulelist;
	}
	
	/**
	 * Gives the current interpretation mode-
	 * Values for this can be found in the <code>Decomposition</code> class
	 * @return current interpretation mode
	 * @see kites.logic.Decomposition
	 */
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
	
	/**
	 * Get the current strategy for interpretation in program mode.
	 * @return the current strategy
	 */
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

	/**
	 * Add a tree's graphical representation to the results pane.
	 * @param label the tree's graphical representation to add
	 */
	public void addToResults(NodeContainer label) {
		scrollResults.setVisible(true);
		label.setInterpreterWindow(this);
		
		// deactivate popup-menus of already added containers
		Component[] comps = getResultsPanel().getComponents();
		for(int i = 0; i < getResultsPanel().getComponentCount(); i++) {
			if(comps[i] instanceof NodeContainer) {
				((NodeContainer)comps[i]).deactivate();
			}
		}
		
		getResultsPanel().add((Component)label);
		getResultsPanel().add(Box.createVerticalStrut(15));
		getResultsPanel().revalidate();
		
		// for some reason Java misses the lowest NodeContainer when calculating the height of the
		// results panel. To work around that, just simply double the height to be sure to get to the
		// bottom...
		getResultsPanel().scrollRectToVisible(new Rectangle(0, getResultsPanel().getHeight() * 2, 1, 1));
		leftSide.revalidate();
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
	
	/**
	 * Update the title bar of he window, according to the currently chosen interpretation mode.
	 */
	public void updateTitle() {
		String title = "KiTES - Interpretation als ";
		switch(getMode()) {
		case Decomposition.M_NONDET:
			title += "ndet. ";
		case Decomposition.M_PROGRAM:
			title += "Programm";
			break;
		case Decomposition.M_TRS:
			title += "Termersetzungssystem";
			break;
		default:
			title = "KiTES";
		}
		this.setTitle(title);
	}
}
