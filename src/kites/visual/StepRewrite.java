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
import kites.TRSModel.TRSFile;
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
	private TRSFile rulelist;
	private ASTNode instanceTree;
	private int mode;
	private int strategy;
	private HashMap<String, Integer> signature;
	private InterpreterWindow wnd;
	
	public StepRewrite(TRSFile rulelist, HashMap<String, Integer> signature, InterpreterWindow wnd) {
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
	
	public boolean isStepPossible() {
		try {
			Decomposition decomp = new Decomposition(rulelist);
			LinkedHashMap<ASTNode, LinkedList<Rule>> rewriteOption = decomp.getDecomp(mode, strategy, instanceTree);
			return !rewriteOption.isEmpty();
		}
		catch (Exception e) {
			return false;
		}
	}
	
	public void run() throws SyntaxErrorException, DecompositionException, NoRewritePossibleException, RecognitionException, NoChildrenException, NodeException {
		if(firstStep) {
			parseInstance();
		}
		Decomposition decomp = new Decomposition(rulelist);
		
		NodeContainer nodelabel;
		if(!firstStep) {
			if(mode == Decomposition.M_PROGRAM) {
				LinkedHashMap<ASTNode, LinkedList<Rule>> rewriteOption = decomp.getDecomp(mode, strategy, instanceTree);
				Iterator<ASTNode> keyIt = rewriteOption.keySet().iterator();
				if(keyIt.hasNext()) {
					ASTNode node = keyIt.next();
					Rule rule = rewriteOption.get(node).getFirst();
					instanceTree = ProgramRewrite.rewrite(instanceTree, node, rule);
					nodelabel = instanceTree.toLabelWithRule(decomp.getDecomp(mode, strategy, instanceTree), false);
				}
				else {
					throw(new NoRewritePossibleException("Keine weiteren Reduktionen möglich."));
				}
			}
			else {
				instanceTree = ProgramRewrite.rewrite(instanceTree, wnd.getNode(), wnd.getRule());
				nodelabel = instanceTree.toLabelWithRule(decomp.getDecomp(mode, strategy, instanceTree), true);
			}
			
			wnd.getStepsField().setText(String.valueOf(Integer.valueOf(wnd.getStepsField().getText()) + 1));
			if(Integer.valueOf(wnd.getSizeField().getText()) < instanceTree.getSize()) {
				wnd.getSizeField().setText(String.valueOf(instanceTree.getSize()));
			}
		}
		else {
			if(mode == Decomposition.M_PROGRAM) {
				nodelabel = instanceTree.toLabelWithRule(decomp.getDecomp(mode, strategy, instanceTree), false);
			}
			else {
				nodelabel = instanceTree.toLabelWithRule(decomp.getDecomp(mode, strategy, instanceTree), true);
			}
			firstStep = false;
			
			wnd.getStepsField().setText("1");
			wnd.getSizeField().setText(String.valueOf(instanceTree.getSize()));
		}

		wnd.addToResults(nodelabel);
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
			String errors = "Fehler während des Lexings festgestellt:\n\n";
			Iterator<String> errIt = lexerErrors.iterator();
			while(errIt.hasNext()) {
				errors += errIt.next() + "\n";
			}
			throw new SyntaxErrorException(errors);
		}
		
		List<String> parseErrors = parser.getErrors();
		if(!parseErrors.isEmpty()) {
			String errors = "Fehler während des Parsings festgestellt:\n\n";
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