package kites.visual;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.TokenStream;

public class StepAction implements ActionListener {
	private boolean firstStep;
	private RuleList rulelist;
	private ASTNode instanceTree;
	private int mode;
	private int strategy;
	private HashMap<String, Integer> signature;
	private JEditorPane instance, results;
	private JTextField steps, size;
	
	public StepAction(RuleList rulelist, int mode, int strategy, HashMap<String, Integer> signature, JEditorPane instance, JEditorPane results, JTextField steps, JTextField size) {
		firstStep = true;
		this.rulelist = rulelist;
		this.mode = mode;
		this.strategy = strategy;
		this.signature = signature;
		this.results = instance;
		this.results = results;
		this.steps = steps;
		this.size = size;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
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
			
			if(mode == InterpreterWindow.PROGRAM) {
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