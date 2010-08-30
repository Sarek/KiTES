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
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.LinkedList;
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
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.TokenStream;

import kites.TRSModel.RuleList;
import kites.parser.TRSLexer;
import kites.parser.TRSParser;

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

	public MainWindow() {
		super();
		
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
        JSeparator menuSeparator2 = new JSeparator();
        
        ImageIcon icoOpenSmall = new ImageIcon("icons/open-small.png");
        JMenuItem menuFileOpen = new JMenuItem("Öffnen...", icoOpenSmall);
        
        ImageIcon icoSaveSmall = new ImageIcon("icons/save-small.png");
        JMenuItem menuFileSave = new JMenuItem("Speichern", icoSaveSmall);
        
        ImageIcon icoSaveAsSmall = new ImageIcon("icons/saveas-small.png");
        JMenuItem menuFileSaveAs = new JMenuItem("Speichern unter...", icoSaveAsSmall);
        JMenuItem menuFileQuit = new JMenuItem("Beenden");
        
        JMenuItem menuEditCopy = new JMenuItem("Kopieren");
        JMenuItem menuEditCut = new JMenuItem("Ausschneiden");
        JMenuItem menuEditPaste = new JMenuItem("Einfügen");
        JMenu	  menuEditHeaders = new JMenu("Regelsätze laden");
        final JMenuItem menuEditHeadersLists = new JMenuItem("Listenregeln");
        final JMenuItem menuEditHeadersIf = new JMenuItem("If-then-else");
        final JMenuItem menuEditHeadersInterpreter = new JMenuItem("Interpreter");
        
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
        
        //menuEdit.add(menuEditCut);
        //menuEdit.add(menuEditCopy);
        //menuEdit.add(menuEditPaste);
        //menuEdit.add(menuSeparator2);
        menuEdit.add(menuEditHeaders);
        menuEditHeaders.add(menuEditHeadersLists);
        menuEditHeaders.add(menuEditHeadersIf);
        menuEditHeaders.add(menuEditHeadersInterpreter);
        
        menuHelp.add(menuHelpAbout);
        
        /*
         * Build toolbar
         */
        JToolBar toolBar = new JToolBar(JToolBar.HORIZONTAL);
        
        ImageIcon icoSaveBig = new ImageIcon("icons/save-big.png");
        JButton tbSave = new JButton("Speichern", icoSaveBig);
        tbSave.setVerticalTextPosition(AbstractButton.BOTTOM);
        tbSave.setHorizontalTextPosition(AbstractButton.CENTER);
        
        ImageIcon icoOpenBig = new ImageIcon("icons/open-big.png");
        JButton tbOpen = new JButton("Öffnen", icoOpenBig);
        tbOpen.setVerticalTextPosition(AbstractButton.BOTTOM);
        tbOpen.setHorizontalTextPosition(AbstractButton.CENTER);
        
        ImageIcon icoRun = new ImageIcon("icons/startint.png");
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
        class AboutAction implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				AboutWindow wnd = new AboutWindow();
				wnd.setVisible(true);
			}
        }
        
        menuHelpAbout.addActionListener(new AboutAction());
        
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
				
				editor.setText(addString + editor.getText());
			}
        }
        
        menuEditHeadersLists.addActionListener(new IncludeAction());
        menuEditHeadersIf.addActionListener(new IncludeAction());
        menuEditHeadersInterpreter.addActionListener(new IncludeAction());
        
        class OpenAction implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(hasChanged()) {
					boolean answer = MsgBox.question("Die derzeitig geöffnete Datei wurde verändert.\n\nMöchte Sie die Änderungen sichern?");
					if(answer) {
						MsgBox.error("Bähähä, doing nothing");
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
        class QuitAction implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
				
			}
        	
        }
        menuFileQuit.addActionListener(new QuitAction());
        
        class RunAction implements ActionListener {
        	@Override
        	@SuppressWarnings({"unused"})
        	public void actionPerformed(ActionEvent arg0) {
        		TRSLexer lexer = new TRSLexer(new ANTLRStringStream(editor.getText()));
        		TokenStream tokenStream = new CommonTokenStream(lexer);
        		TRSParser parser = new TRSParser(tokenStream);
        		
        		try {
					RuleList rulelist = parser.rulelist();
					
					List<String> lexerErrors = lexer.getErrors();
	        		if(!lexerErrors.isEmpty()) {
	        			String errors = "Detected errors during lexing:\n\n";
	        			Iterator<String> errIt = lexerErrors.iterator();
	        			while(errIt.hasNext()) {
	        				errors += errIt.next() + "\n";
	        			}
	        			MsgBox.error(errors);
	        			return;
	        		}
	        		List<String> parseErrors = parser.getErrors();
	        		if(!parseErrors.isEmpty()) {
	        			String errors = "Detected errors during parsing:\n\n";
	        			Iterator<String> errIt = parseErrors.iterator();
	        			while(errIt.hasNext()) {
	        				errors += errIt.next() + "\n";
	        			}
	        			MsgBox.error(errors);
	        			return;
	        		}
	        		
					InterpreterWindow wndInterpreter = new InterpreterWindow(rulelist, 0);
					wndInterpreter = null;
					System.gc();
				} catch (Exception e) {
					MsgBox.error(e);
				}
				catch (Error e) {
					MsgBox.error("Error during include:\n\n" + e.getMessage());
				}
        	}
        }
        tbRun.addActionListener(new RunAction());
   }

	
	/**
	 * @param args will be ignored
	 */
	public static void main(String[] args) {
		MainWindow main = new MainWindow();
        main.setVisible(true);
	}


	public void setCurFile(File curFile) {
		this.curFile = curFile;
	}


	public File getCurFile() {
		return curFile;
	}


	public void setChanged(boolean changed) {
		this.hasChanged = changed;
		updateTitle();
	}

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
	
	public boolean hasChanged() {
		return hasChanged;
	}
	
	public static void writeToClipboard(String s, ClipboardOwner owner)
	{
	  Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	  Transferable transferable = new StringSelection(s);
	  clipboard.setContents(transferable, owner);
	}
}
