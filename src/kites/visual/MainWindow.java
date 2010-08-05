package kites.visual;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JEditorPane;
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

	public MainWindow() {
		super();
		
		try {
			javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		//setLocationRelativeTo(null);
        setSize(1000, 500);
        setTitle("KiTES v0.1");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        final JEditorPane editor = new JEditorPane();
        editor.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
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
        
        JMenuItem menuFileOpen = new JMenuItem("Öffnen...");
        JMenuItem menuFileSave = new JMenuItem("Speichern");
        JMenuItem menuFileSaveAs = new JMenuItem("Speichern unter...");
        JMenuItem menuFileQuit = new JMenuItem("Beenden");
        
        JMenuItem menuEditCopy = new JMenuItem("Kopieren");
        JMenuItem menuEditCut = new JMenuItem("Ausschneiden");
        JMenuItem menuEditPaste = new JMenuItem("Einfügen");
        JMenu	  menuEditHeaders = new JMenu("Regelsätze laden");
        JMenuItem menuEditHeadersLists = new JMenuItem("Listenregeln");
        JMenuItem menuEditHeadersIf = new JMenuItem("If-then-else");
        JMenuItem menuEditHeadersEqual = new JMenuItem("Equal");
        
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
        menuEdit.add(menuSeparator2);
        menuEdit.add(menuEditHeaders);
        menuEditHeaders.add(menuEditHeadersLists);
        menuEditHeaders.add(menuEditHeadersIf);
        menuEditHeaders.add(menuEditHeadersEqual);
        
        /*
        menuInterpretation.add(menuInterpretationStart);
        menuInterpretation.add(menuSeparator3);
        menuInterpretation.add(menuInterpretationNonDet);
        menuInterpretation.add(menuInterpretationProg);
        menuInterpretation.add(menuInterpretationNonDet);
        menuInterpretation.add(menuInterpretationTRS);
        */
        
        menuHelp.add(menuHelpAbout);
        
        /*
         * Build toolbar
         */
        JToolBar toolBar = new JToolBar(JToolBar.HORIZONTAL);
        JButton tbSave = new JButton("Speichern");
        JButton tbOpen = new JButton("Öffnen");
        JButton tbRun = new JButton("Interpreter starten");
        
        toolBar.add(tbSave);
        toolBar.add(tbOpen);
        toolBar.add(tbRun);
        
        this.add(toolBar, BorderLayout.PAGE_START);
        
        /*
         * Actions
         */
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
				} catch (RecognitionException e) {
					e.printStackTrace();
				}
        	}
        }
        tbRun.addActionListener(new RunAction());
   }

	
	/**
	 * @param args will be ignored
	 */
	public static void main(String[] args) {
		MainWindow simple = new MainWindow();
        simple.setVisible(true);

	}

}
