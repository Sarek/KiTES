/*
 * This file is part of KiTES.
 * 
 * Copyright 2010 Sebastian Schäfer <sarek@uliweb.de>
 *
 *   KiTES is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   KiTES is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with KiTES.  If not, see <http://www.gnu.org/licenses/>.
 */

package kites.visual;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

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

/**
 * This class handles doing a step in the interpretation
 */
public class StepRewrite {
	private boolean firstStep;
	private TRSFile rulelist;
	private ASTNode instanceTree;
	private int mode;
	private int strategy;
	private HashMap<String, Integer> signature;
	private InterpreterWindow wnd;
	
	/**
	 * Create a new instance of this class.
	 * 
	 * @param rulelist the rulelist the interpretation will be based on
	 * @param signature the signature of the rulelist
	 * @param wnd the interpreter window the results shall be displayed in
	 */
	public StepRewrite(TRSFile rulelist, HashMap<String, Integer> signature, InterpreterWindow wnd) {
		firstStep = true;
		this.rulelist = rulelist;
		this.wnd = wnd;
		this.signature = signature;
		this.instanceTree = null;
	}
	
	/**
	 * Abort the current interpretation and reset.
	 */
	public void setFirst() {
		this.firstStep = true;
		this.instanceTree = null;
	}
	
	/**
	 * Set the interpretation mode.
	 * @see kites.logic.Decomposition
	 * @param mode the mode from <code>kites.logic.Decomposition</code>
	 */
	public void setMode(int mode) {
		this.mode = mode;
	}
	
	/**
	 * Set the interpretation strategy. Only applicable/used
	 * when in program interpretation mode.
	 * 
	 * @see kites.logic.Decomposition
	 * @param strategy the strategy from <code>kites.logic.Decomposition</code>
	 */
	public void setStrategy(int strategy) {
		this.strategy = strategy;
	}
	
	/**
	 * Determine whether another interpretative step using the rules
	 * in the current mode and strategy is possible.
	 * 
	 * @return true if a step is possible, false otherwise
	 */
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
	
	/**
	 * Perform a step in the interpretation.
	 * In the first step, the instance in the interpreter window is fetched
	 * and parsed.
	 * 
	 * @throws SyntaxErrorException if an error in the tree was found
	 * @throws DecompositionException if an error with the decomposition is found
	 * @throws NoRewritePossibleException if no rewrite is possible
	 * @throws RecognitionException if there was a lexer or parser error (only thrown during first step)
	 * @throws NoChildrenException if an error in the tree was found 
	 * @throws NodeException if an error in a node was found
	 */
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
	
	/**
	 * Parse the instance in the interpreter window.
	 * If an interpretation is already underway, it will be aborted and the object reset.
	 * 
	 * @throws RecognitionException If a lexer/parser error was encountered
	 * @throws SyntaxErrorException If an error in the tree structure was encountered
	 * @throws NodeException If an error in a node was encountered
	 */
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

	/**
	 * Get the current instance.
	 * If one or more steps in the interpretation was performed, this is the
	 * transformed/interpreted instance, not the original one.
	 * 
	 * @return the instance
	 */
	public ASTNode getInstanceTree() {
		return instanceTree;
	}
}