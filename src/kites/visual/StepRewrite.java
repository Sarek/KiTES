package kites.visual;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

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
import kites.logic.Rewrite;
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
			instanceTree = parser.function();
			
			// syntax check instance
			CheckTRS.instanceCheck(instanceTree, signature);
			
			results.setText(instanceTree.toString());
			steps.setText("1");
			size.setText(String.valueOf(instanceTree.getSize()));
			firstStep = false;
		}
		
		System.out.println("\n" + instanceTree);
		System.out.println(Decomposition.M_PROGRAM == mode);
		
		if(instanceTree != null) {
			if(mode == Decomposition.M_PROGRAM) {
				System.out.println("Doing rewrite.");
				LinkedHashMap<ASTNode, LinkedList<Rule>> decomp = Decomposition.getDecomp(strategy, rulelist, instanceTree);
				if(decomp.isEmpty()) {
					throw new NoRewritePossibleException("No more rules applicable");
				}
				System.out.println(decomp);
				Rewrite.rewrite(decomp.keySet().iterator().next(), decomp.entrySet().iterator().next().getValue().element());
				
				results.setText(results.getText() + "\n\n" + instanceTree.toString());
				steps.setText(String.valueOf(Integer.parseInt(steps.getText()) + 1));
				size.setText(String.valueOf(instanceTree.getSize()));
			}
		}
		else {
			throw new SyntaxErrorException("Empty instance was given");
		}
	}
}