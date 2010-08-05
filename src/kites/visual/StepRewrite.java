package kites.visual;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JEditorPane;
import javax.swing.JTextField;

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
	private JEditorPane instance, results;
	private JTextField steps, size;
	
	public StepRewrite(RuleList rulelist, HashMap<String, Integer> signature, JEditorPane instance, JEditorPane results, JTextField steps, JTextField size) {
		firstStep = true;
		this.rulelist = rulelist;

		this.signature = signature;
		this.instance = instance;
		this.results = results;
		this.steps = steps;
		this.size = size;
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
		
		if(instanceTree != null) {
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
		}
		else {
			throw new SyntaxErrorException("Empty instance was given");
		}
	}
}