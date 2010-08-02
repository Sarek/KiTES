package kites.visual;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import javax.swing.JEditorPane;
import javax.swing.JTextField;

import kites.TRSModel.ASTNode;
import kites.TRSModel.Rule;
import kites.TRSModel.RuleList;
import kites.logic.CheckTRS;
import kites.logic.Decomposition;
import kites.logic.Rewrite;
import kites.parser.TRSLexer;
import kites.parser.TRSParser;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
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
		this.results = instance;
		this.results = results;
		this.steps = steps;
		this.size = size;
	}
	
	public void setMode(int mode) {
		this.mode = mode;
	}
	
	public void setStrategy(int strategy) {
		this.strategy = strategy;
	}
	
	public void run() {
		if(instanceTree != null) {
			if(firstStep) {
				System.out.println("Parsing instance...");
				TRSLexer lexer = new TRSLexer(new ANTLRStringStream(instance.getText()));
				TokenStream tokenStream = new CommonTokenStream(lexer);
				TRSParser parser = new TRSParser(tokenStream);
				try {
					instanceTree = parser.function();
					
					// syntax check instance
					CheckTRS.instanceCheck(instanceTree, signature);
					
					results.setText(instanceTree.toString());
					steps.setText("1");
					size.setText(String.valueOf(instanceTree.getSize()));
					firstStep = false;
				} catch (Exception e) {
					MsgBox.error(e);
					return;
				}
			}
			
			if(mode == Decomposition.M_PROGRAM) {
				try {
					LinkedHashMap<ASTNode, LinkedList<Rule>> decomp = Decomposition.getDecomp(strategy, rulelist, instanceTree);
					Rewrite.rewrite(decomp.keySet().iterator().next(), decomp.entrySet().iterator().next().getValue().element());
				}
				catch(Exception e) {
					MsgBox.error(e);
				}
			}
		}
	}
}