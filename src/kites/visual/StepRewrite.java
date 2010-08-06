package kites.visual;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import kites.TRSModel.ASTNode;
import kites.TRSModel.Rule;
import kites.TRSModel.RuleList;
import kites.exceptions.DecompositionException;
import kites.exceptions.NoRewritePossibleException;
import kites.exceptions.SyntaxErrorException;
import kites.logic.CheckTRS;
import kites.logic.Decomposition;
import kites.logic.ProgramRewrite;
import kites.parser.TRSLexer;
import kites.parser.TRSParser;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.TokenStream;

public class StepRewrite {
	private boolean firstStep;
	private RuleList rulelist;
	private ASTNode instanceTree;
	private int mode;
	private int strategy;
	private HashMap<String, Integer> signature;
	private InterpreterWindow wnd;
	
	public StepRewrite(RuleList rulelist, HashMap<String, Integer> signature, InterpreterWindow wnd) {
		firstStep = true;
		this.rulelist = rulelist;
		this.wnd = wnd;

		this.signature = signature;
	}
	
	public void setFirst() {
		this.firstStep = true;
	}
	
	public void setMode(int mode) {
		this.mode = mode;
	}
	
	public void setStrategy(int strategy) {
		this.strategy = strategy;
	}
	
	public void run() throws SyntaxErrorException, DecompositionException, NoRewritePossibleException, RecognitionException {
		if(firstStep) {
			TRSLexer lexer = new TRSLexer(new ANTLRStringStream(wnd.getInstance().getText()));
			TokenStream tokenStream = new CommonTokenStream(lexer);
			TRSParser parser = new TRSParser(tokenStream);
			instanceTree = parser.instance();
			
			List<String> lexerErrors = lexer.getErrors();
    		if(!lexerErrors.isEmpty()) {
    			System.out.println("Displaying lexer errors");
    			String errors = "Detected errors during lexing:\n\n";
    			Iterator<String> errIt = lexerErrors.iterator();
    			while(errIt.hasNext()) {
    				errors += errIt.next() + "\n";
    			}
    			MsgBox.error(errors);
    			return;
    		}
    		List<String> parseErrors = parser.getErrors();
    		System.out.println(parseErrors);
    		if(!parseErrors.isEmpty()) {
    			String errors = "Detected errors during parsing:\n\n";
    			Iterator<String> errIt = parseErrors.iterator();
    			while(errIt.hasNext()) {
    				errors += errIt.next() + "\n";
    			}
    			MsgBox.error(errors);
    			return;
    		}
			
			// syntax check instance
			CheckTRS.instanceCheck(instanceTree, signature);
			
			try {
				javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
			} catch(Exception e) {
				e.printStackTrace();
			}
			Component nodelabel = instanceTree.toLabel();
			nodelabel.validate();
			System.out.println(nodelabel);
			wnd.addToResults(nodelabel);
			wnd.getStepsField().setText("1");
			wnd.getSizeField().setText(String.valueOf(instanceTree.getSize()));
	
			firstStep = false;
		}
		/**
		 * A JEditorPane is unusable for what I want to do (i. e. non-deterministic programs.
		 * Therefore, this is mostly crap. (At least anything that has anything to do with output)
		 */
		/*
		if(firstStep) {
			TRSLexer lexer = new TRSLexer(new ANTLRStringStream(instance.getText()));
			TokenStream tokenStream = new CommonTokenStream(lexer);
			TRSParser parser = new TRSParser(tokenStream);
			instanceTree = parser.instance();
			
			List<String> lexerErrors = lexer.getErrors();
    		if(!lexerErrors.isEmpty()) {
    			System.out.println("Displaying lexer errors");
    			String errors = "Detected errors during lexing:\n\n";
    			Iterator<String> errIt = lexerErrors.iterator();
    			while(errIt.hasNext()) {
    				errors += errIt.next() + "\n";
    			}
    			MsgBox.error(errors);
    			return;
    		}
    		List<String> parseErrors = parser.getErrors();
    		System.out.println(parseErrors);
    		if(!parseErrors.isEmpty()) {
    			String errors = "Detected errors during parsing:\n\n";
    			Iterator<String> errIt = parseErrors.iterator();
    			while(errIt.hasNext()) {
    				errors += errIt.next() + "\n";
    			}
    			MsgBox.error(errors);
    			return;
    		}
			
			// syntax check instance
			CheckTRS.instanceCheck(instanceTree, signature);
			
			results.setText(instanceTree.toString());
			steps.setText("1");
			size.setText(String.valueOf(instanceTree.getSize()));
			firstStep = false;
		}
		
		if(mode == Decomposition.M_PROGRAM) {
			try {
				ProgramRewrite rwrt = new ProgramRewrite(strategy, instanceTree, rulelist);
				ASTNode newTree = rwrt.findRewrite();
				
				results.setText(results.getText() + "\n\n" + newTree.toString());
				steps.setText(String.valueOf(Integer.parseInt(steps.getText()) + 1));
				Integer newSize = new Integer(newTree.getSize());
				if(newSize.compareTo(Integer.parseInt(size.getText())) > 0) {
					size.setText(String.valueOf(newTree.getSize()));
				}
				instanceTree = newTree;
			}
			catch(Exception e) {
				MsgBox.error(e);
				e.printStackTrace();
			}
		}
		*/
	}
}