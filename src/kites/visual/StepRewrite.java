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
import kites.exceptions.NoChildrenException;
import kites.exceptions.NoRewritePossibleException;
import kites.exceptions.NodeException;
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
		this.instanceTree = null;
	}
	
	public void setFirst() {
		this.firstStep = true;
		this.instanceTree = null;
	}
	
	public void setMode(int mode) {
		this.mode = mode;
	}
	
	public void setStrategy(int strategy) {
		this.strategy = strategy;
	}
	
	public void run() throws SyntaxErrorException, DecompositionException, NoRewritePossibleException, RecognitionException, NoChildrenException, NodeException {
		ASTNode newTree;
		if(firstStep) {
			parseInstance();
		}
		Decomposition decomp = new Decomposition(rulelist);
		
		NodeContainer nodelabel;
		if(!firstStep) {
			newTree = ProgramRewrite.rewrite(instanceTree, wnd.getNode(), wnd.getRule());
			if(mode == Decomposition.M_PROGRAM) {
				nodelabel = newTree.toLabelWithRule(decomp.getDecomp(mode, strategy, newTree), false);
			}
			else {
				nodelabel = newTree.toLabelWithRule(decomp.getDecomp(mode, strategy, newTree), true);
			}
			instanceTree = newTree;
		}
		else {
			if(mode == Decomposition.M_PROGRAM) {
				nodelabel = instanceTree.toLabelWithRule(decomp.getDecomp(mode, strategy, instanceTree), false);
			}
			else {
				nodelabel = instanceTree.toLabelWithRule(decomp.getDecomp(mode, strategy, instanceTree), true);
			}
		}

		wnd.addToResults(nodelabel);
		wnd.getStepsField().setText("1");
		wnd.getSizeField().setText(String.valueOf(instanceTree.getSize()));
		firstStep = false;
	}
	
	public void parseInstance() throws RecognitionException, SyntaxErrorException, NodeException {
		if(instanceTree != null) {
			// the object has already been used. When parsing a possibly new instance
			// we then have to reset the object
			setFirst();
		}
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
			throw new SyntaxErrorException(errors);
		}
		
		List<String> parseErrors = parser.getErrors();
		System.out.println(parseErrors);
		if(!parseErrors.isEmpty()) {
			String errors = "Detected errors during parsing:\n\n";
			Iterator<String> errIt = parseErrors.iterator();
			while(errIt.hasNext()) {
				errors += errIt.next() + "\n";
			}
			throw new SyntaxErrorException(errors);
		}
		
		// syntax check instance
		CheckTRS.instanceCheck(instanceTree, signature);
	}

	public ASTNode getInstanceTree() {
		return instanceTree;
	}
}