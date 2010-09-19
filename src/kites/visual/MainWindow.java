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

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileWriter;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JToolBar;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.TokenStream;

import kites.TRSModel.TRSFile;
import kites.parser.TRSLexer;
import kites.parser.TRSParser;

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
 * Main window class for KiTES.
 * This is the entrypoint for all users, they are able to enter
 * rulesets here, save them and load them again.
 * Also, the interpreter for specific expressions can be started here.
 * @author Sebastian Schäfer <sarek@uliweb.>
 *
 */
public class MainWindow extends JFrame {

	/**
	 * Eclipse thinks every serializable class should have a field called serialVersionUID
	 */
	private static final long serialVersionUID = -5132318887749930073L;
	private File curFile;
	private boolean hasChanged;
	private String programDirectory;

	/**
	 * Create and display the window
	 */
	public MainWindow() {		
		super();
		
		try {
			File jarFile = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
			programDirectory = jarFile.getParent();
		}
		catch(URISyntaxException e) {
			MsgBox.error("Cannot find program path. As now everything will go horribly wrong, I quit!");
			System.exit(-1);
		}
		
		try {
			javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		//setLocationRelativeTo(null);
        setSize(1000, 500);
        setTitle("KiTES");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        final JEditorPane editor = new JEditorPane();
        editor.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        editor.setToolTipText("Regelsystem mit Regeln der Form \"linke Seite\" --> \"rechte Seite\" eingeben");
        JScrollPane scrollEditor = new JScrollPane(editor);
        this.add(scrollEditor, BorderLayout.CENTER);
        
        /*
         * Construct the menu
         */
        JMenuBar menuBar = new JMenuBar();
        JMenu menuFile = new JMenu("Datei");
        JMenu menuEdit = new JMenu("Bearbeiten");
        //JMenu menuInterpretation = new JMenu("Interpretation");
        JMenu menuHelp = new JMenu("Hilfe");
        
        menuBar.add(menuFile);
        menuBar.add(menuEdit);
        //menuBar.add(menuInterpretation);
        menuBar.add(menuHelp);
        this.setJMenuBar(menuBar);
        
        // Create menu items
        JSeparator menuSeparator = new JSeparator();
        
        ImageIcon icoOpenSmall = new ImageIcon(programDirectory + File.pathSeparator + "icons" + File.pathSeparator + "open-small.png");
        JMenuItem menuFileOpen = new JMenuItem("Öffnen...", icoOpenSmall);
        
        ImageIcon icoSaveSmall = new ImageIcon(programDirectory + File.pathSeparator + "icons" + File.pathSeparator + "save-small.png");
        JMenuItem menuFileSave = new JMenuItem("Speichern", icoSaveSmall);
        
        ImageIcon icoSaveAsSmall = new ImageIcon(programDirectory + File.pathSeparator + "icons" + File.pathSeparator + "saveas-small.png");
        JMenuItem menuFileSaveAs = new JMenuItem("Speichern unter...", icoSaveAsSmall);
        JMenuItem menuFileQuit = new JMenuItem("Beenden");
        
        JMenuItem menuEditCopy = new JMenuItem("Kopieren");
        JMenuItem menuEditCut = new JMenuItem("Ausschneiden");
        JMenuItem menuEditPaste = new JMenuItem("Einfügen");
        JMenu	  menuEditHeaders = new JMenu("Regelsätze laden");
        final JMenuItem menuEditHeadersLists = new JMenuItem("Listenregeln");
        final JMenuItem menuEditHeadersIf = new JMenuItem("If-then-else");
        final JMenuItem menuEditHeadersInterpreter = new JMenuItem("Interpreter");
        final JMenuItem menuEditHeadersBoolean = new JMenuItem("Boolesche Operationen");
        final JMenuItem menuEditHeadersComparison = new JMenuItem("Vergleichsoperationen");
        final JMenuItem menuEditHeadersFormula = new JMenuItem("Aussagenlogische Formeln");
        final JMenuItem menuEditHeadersMaximum = new JMenuItem("Maximum");
        final JMenuItem menuEditHeadersNdetInt = new JMenuItem("Nicht-deterministischer Interpreter");
        final JMenuItem menuEditHeadersPCP = new JMenuItem("PCP-Problem");
        
        ButtonGroup grpInterpretation = new ButtonGroup();
        //JMenuItem menuInterpretationStart = new JMenuItem("Interpreter starten");
        JRadioButtonMenuItem menuInterpretationNonDet = new JRadioButtonMenuItem("Nicht-deterministisches Programm");
        grpInterpretation.add(menuInterpretationNonDet);
        JRadioButtonMenuItem menuInterpretationProg = new JRadioButtonMenuItem("Programm");
        grpInterpretation.add(menuInterpretationProg);
        menuInterpretationProg.setSelected(true);
        JRadioButtonMenuItem menuInterpretationTRS = new JRadioButtonMenuItem("Termersetzungssystem");
        grpInterpretation.add(menuInterpretationTRS);
        
        JMenuItem menuHelpAbout = new JMenuItem("Über");
        
        // Add items to menus
        menuFile.add(menuFileOpen);
        menuFile.add(menuFileSave);
        menuFile.add(menuFileSaveAs);
        menuFile.add(menuSeparator);
        menuFile.add(menuFileQuit);
        
        menuEdit.add(menuEditCut);
        menuEdit.add(menuEditCopy);
        menuEdit.add(menuEditPaste);
        menuEdit.add(new JSeparator());
        menuEdit.add(menuEditHeaders);
        menuEditHeaders.add(menuEditHeadersLists);
        menuEditHeaders.add(menuEditHeadersIf);
        menuEditHeaders.add(menuEditHeadersInterpreter);
        menuEditHeaders.add(menuEditHeadersBoolean);
        menuEditHeaders.add(menuEditHeadersComparison);
        menuEditHeaders.add(menuEditHeadersFormula);
        menuEditHeaders.add(menuEditHeadersMaximum);
        menuEditHeaders.add(menuEditHeadersNdetInt);
        menuEditHeaders.add(menuEditHeadersPCP);
        
        menuHelp.add(menuHelpAbout);
        
        /*
         * Build toolbar
         */
        JToolBar toolBar = new JToolBar(JToolBar.HORIZONTAL);
        
        ImageIcon icoSaveBig = new ImageIcon(programDirectory + File.separator + "icons" + File.separator + "save-big.png");
        JButton tbSave = new JButton("Speichern", icoSaveBig);
        tbSave.setVerticalTextPosition(AbstractButton.BOTTOM);
        tbSave.setHorizontalTextPosition(AbstractButton.CENTER);
        
        ImageIcon icoOpenBig = new ImageIcon(programDirectory + File.separator + "icons" + File.separator + "open-big.png");
        JButton tbOpen = new JButton("Öffnen", icoOpenBig);
        tbOpen.setVerticalTextPosition(AbstractButton.BOTTOM);
        tbOpen.setHorizontalTextPosition(AbstractButton.CENTER);
        
        ImageIcon icoRun = new ImageIcon(programDirectory + File.separator + "icons" + File.separator + "startint-big.png");
        JButton tbRun = new JButton("Interpreter starten", icoRun);
        tbRun.setVerticalTextPosition(AbstractButton.BOTTOM);
        tbRun.setHorizontalTextPosition(AbstractButton.CENTER);
        
        toolBar.add(tbSave);
        toolBar.add(tbOpen);
        toolBar.add(tbRun);
        
        this.add(toolBar, BorderLayout.PAGE_START);
        
        /*
         * Actions
         */
        
        /**
         * Open the about window
         */
        class AboutAction implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				AboutWindow wnd = new AboutWindow();
				wnd.setVisible(true);
			}
        }
        
        menuHelpAbout.addActionListener(new AboutAction());
        
        /**
         * Insert a new include in the curently edited file
         */
        class IncludeAction implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String addString = new String();
				
				if(arg0.getSource() == menuEditHeadersLists) {
					addString = "#include lib/lists.trs" + System.getProperty("line.separator");
				}
				else if(arg0.getSource() == menuEditHeadersIf) {
					addString = "#include lib/if-then-else.trs" + System.getProperty("line.separator");
				}
				else if(arg0.getSource() == menuEditHeadersInterpreter) {
					addString = "#include lib/interpreter.trs" + System.getProperty("line.separator");
				}
				else if(arg0.getSource() == menuEditHeadersBoolean) {
					addString = "#include lib/boolean.trs" + System.getProperty("line.separator");
				}
				else if(arg0.getSource() == menuEditHeadersComparison) {
					addString = "#include lib/comparison.trs" + System.getProperty("line.separator");
				}
				else if(arg0.getSource() == menuEditHeadersFormula) {
					addString = "#include lib/formula.trs" + System.getProperty("line.separator");
				}
				else if(arg0.getSource() == menuEditHeadersMaximum) {
					addString = "#include lib/maximum.trs" + System.getProperty("line.separator");
				}
				else if(arg0.getSource() == menuEditHeadersNdetInt) {
					addString = "#include lib/ndet-int.trs" + System.getProperty("line.separator");
				}
				else if(arg0.getSource() == menuEditHeadersPCP) {
					addString = "#include lib/pcp.trs" + System.getProperty("line.separator");
				}
				
				editor.setText(addString + editor.getText());
			}
        }
        menuEditHeadersLists.addActionListener(new IncludeAction());
        menuEditHeadersIf.addActionListener(new IncludeAction());
        menuEditHeadersInterpreter.addActionListener(new IncludeAction());
        menuEditHeadersBoolean.addActionListener(new IncludeAction());
        menuEditHeadersComparison.addActionListener(new IncludeAction());
        menuEditHeadersFormula.addActionListener(new IncludeAction());
        menuEditHeadersMaximum.addActionListener(new IncludeAction());
        menuEditHeadersNdetInt.addActionListener(new IncludeAction());
        menuEditHeadersPCP.addActionListener(new IncludeAction());
        
        menuEditCopy.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { editor.copy(); }});
        menuEditCut.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { editor.cut(); }});
        menuEditPaste.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { editor.paste(); }});
        
        /**
         * Save a file. If the file has never been saved before
         * (aka "Untitled") open the Save as-dialog.
         */
        class SaveAction implements ActionListener {
        	private boolean saveAs;
        	
        	public SaveAction(boolean saveAs) {
        		this.saveAs = saveAs;
        	}
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(saveAs) {
					doSaveAs();
				}
				else {
					if(getCurFile() == null) {
						doSaveAs();
					}
					else {
						doSave();
					}
				}
			}
			
			public void doSaveAs() {
				JFileChooser fc = new JFileChooser();
				int diaRetval = fc.showSaveDialog(MainWindow.this);
				
				if(diaRetval == JFileChooser.APPROVE_OPTION) {
					setCurFile(fc.getSelectedFile());
					doSave();
				}
			}
        	
			public void doSave() {
				try {
					FileWriter writer = new FileWriter(getCurFile());
					writer.write(editor.getText());
					writer.close();
					setChanged(false);
				}
				catch(Exception e) {
					MsgBox.error(e);
				}
			}
        }
        tbSave.addActionListener(new SaveAction(false));
        menuFileSave.addActionListener(new SaveAction(false));
        menuFileSaveAs.addActionListener(new SaveAction(true));
        
        /**
         * Open a file, ditch the old one
         */
        class OpenAction implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(hasChanged()) {
					boolean answer = MsgBox.question("Die derzeitig geöffnete Datei wurde verändert.\n\nMöchten Sie die Änderungen sichern?");
					if(answer) {
						SaveAction savac = new SaveAction(false);
						savac.actionPerformed(null);
					}
				}

				JFileChooser fc = new JFileChooser();
				int fcRetval = fc.showOpenDialog(MainWindow.this);
				if(fcRetval == JFileChooser.APPROVE_OPTION) {
					setCurFile(fc.getSelectedFile());
					
					try {
						String nl = System.getProperty("line.separator");
						String editorText = new String();
						Scanner reader = new Scanner(getCurFile());
						while(reader.hasNextLine()) {
							editorText += reader.nextLine() + nl;
						}
						editor.setText(editorText);
					}
					catch(Exception e) {
						MsgBox.error(e);
					}
					setChanged(false);
				}
			}    	
        }
        
        menuFileOpen.addActionListener(new OpenAction());
        tbOpen.addActionListener(new OpenAction());
        
        /**
         * Watch the editor pane, to register when something changes.
         * If it does, the title will be updated to reflect this and to
         * indicate that the file will have to be saved.
         */
        class ChangeListener implements KeyListener {

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
				setChanged(true);
			}
        	
        }
        editor.addKeyListener(new ChangeListener());
        
        /**
         * Exit the program.
         * This is my personal masterpiece ;-)
         */
        class QuitAction implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
				
			}
        	
        }
        menuFileQuit.addActionListener(new QuitAction());

        /**
         * Open up the interpreter window
         */
        class RunAction implements ActionListener {
        	@Override
        	@SuppressWarnings({"unused"})
        	public void actionPerformed(ActionEvent arg0) {
        		TRSLexer lexer = new TRSLexer(new ANTLRStringStream(editor.getText()));
        		TokenStream tokenStream = new CommonTokenStream(lexer);
        		TRSParser parser = new TRSParser(tokenStream);
        		
        		try {
					TRSFile rulelist = parser.rulelist();
					
					List<String> lexerErrors = lexer.getErrors();
	        		if(!lexerErrors.isEmpty()) {
	        			String errors = "Es wurden Fehler während des Lexings festgestellt:\n\n";
	        			Iterator<String> errIt = lexerErrors.iterator();
	        			while(errIt.hasNext()) {
	        				errors += errIt.next() + "\n";
	        			}
	        			MsgBox.error(errors);
	        			return;
	        		}
	        		List<String> parseErrors = parser.getErrors();
	        		if(!parseErrors.isEmpty()) {
	        			String errors = "Es wurden Fehler während des Parsings festgestellt:\n\n";
	        			Iterator<String> errIt = parseErrors.iterator();
	        			while(errIt.hasNext()) {
	        				errors += errIt.next() + "\n";
	        			}
	        			MsgBox.error(errors);
	        			return;
	        		}
	        		
					InterpreterWindow wndInterpreter = new InterpreterWindow(rulelist);
					wndInterpreter = null;
					System.gc();
				} catch (Exception e) {
					MsgBox.error(e);
				}
				catch (Error e) {
					MsgBox.error("Fehler während der Einbindung von Includes:\n\n" + e.getMessage());
				}
        	}
        }
        tbRun.addActionListener(new RunAction());
   }

	
	/**
	 * Application entry point.
	 * Create a new <code>MainWindow</code> and open it.
	 * @param args will be ignored
	 */
	public static void main(String[] args) {		
		MainWindow main = new MainWindow();
        main.setVisible(true);
	}

	/**
	 * Sets the currently edited file.
	 * This does NOT mean that this file is opened or anything.
	 * This is just used for updating the title.
	 * @param curFile the currently edited file
	 */
	public void setCurFile(File curFile) {
		this.curFile = curFile;
	}

	/**
	 * Gives the currently edited file.
	 * 
	 * @return the currently edited file
	 */
	public File getCurFile() {
		return curFile;
	}

	/**
	 * Sets the changed status of the currently edited file.
	 * Called, whenever a change in the editor occurs or the file has been saved.
	 * @param changed true: changes have to be saved to disk; false: file on disk is current
	 */
	public void setChanged(boolean changed) {
		this.hasChanged = changed;
		updateTitle();
	}

	/**
	 * Update the title to reflect current status of
	 * edited file and the name of the file.
	 */
	public void updateTitle() {
		String title = "KiTES - ";
		if(hasChanged())
			title += "* ";
		
		if(getCurFile() != null) {
			title += getCurFile().getName();
		}
		else {
			title += "Unbenannt";
		}
		this.setTitle(title);
	}
	
	/**
	 * Gives the property whether the currently edited file
	 * has to be saved or not.
	 * 
	 * @return true if file needs saving, false otherwise
	 */
	public boolean hasChanged() {
		return hasChanged;
	}
	
	/**
	 * Copy a string to the system clipboard
	 * @param s The string to copy
	 * @param owner The owner of the clipboard
	 */
	public static void writeToClipboard(String s, ClipboardOwner owner)
	{
	  Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	  Transferable transferable = new StringSelection(s);
	  clipboard.setContents(transferable, owner);
	}
}
