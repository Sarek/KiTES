package kites.visual;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;

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
	 * Eclipse thinks every class should have a field called serialVersionUID
	 */
	private static final long serialVersionUID = -5132318887749930073L;

	public MainWindow() {
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
        
        JEditorPane editor = new JEditorPane();
        editor.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        System.out.println(editor.getFont().getFontName());
        this.add(editor, BorderLayout.CENTER);
        
        /*
         * Construct the menu
         */
        JMenuBar menuBar = new JMenuBar();
        JMenu menuFile = new JMenu("Datei");
        JMenu menuEdit = new JMenu("Bearbeiten");
        JMenu menuInterpretation = new JMenu("Interpretation");
        JMenu menuHelp = new JMenu("Hilfe");
        
        menuBar.add(menuFile);
        menuBar.add(menuEdit);
        menuBar.add(menuInterpretation);
        menuBar.add(menuHelp);
        this.setJMenuBar(menuBar);
        
        // Create menu items
        JSeparator menuSeparator = new JSeparator();
        JSeparator menuSeparator2 = new JSeparator();
        JSeparator menuSeparator3 = new JSeparator();
        
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
        JMenuItem menuInterpretationStart = new JMenuItem("Interpreter starten");
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
        
        menuInterpretation.add(menuInterpretationStart);
        menuInterpretation.add(menuSeparator3);
        menuInterpretation.add(menuInterpretationNonDet);
        menuInterpretation.add(menuInterpretationProg);
        menuInterpretation.add(menuInterpretationNonDet);
        menuInterpretation.add(menuInterpretationTRS);
        
        menuHelp.add(menuHelpAbout);
        
        /*
         * Build toolbar
         */
        JToolBar toolBar = new JToolBar(JToolBar.HORIZONTAL);
        JButton tbSave = new JButton("Save");
        JButton tbOpen = new JButton("Open");
        JButton tbRun = new JButton("Run Int");
        
        toolBar.add(tbSave);
        toolBar.add(tbOpen);
        toolBar.add(tbRun);
        
        this.add(toolBar, BorderLayout.PAGE_START);
        
        /*
         * Actions
         */
        //TODO: Actions
   }

	
	/**
	 * @param args will be ignored
	 */
	public static void main(String[] args) {
		MainWindow simple = new MainWindow();
        simple.setVisible(true);

	}

}
