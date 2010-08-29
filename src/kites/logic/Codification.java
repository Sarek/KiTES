package kites.logic;

import java.util.HashMap;
import java.util.Iterator;

import kites.TRSModel.ASTNode;
import kites.TRSModel.CodificationContainer;
import kites.TRSModel.Constant;
import kites.TRSModel.Function;
import kites.TRSModel.Rule;
import kites.TRSModel.RuleList;
import kites.TRSModel.Variable;
import kites.exceptions.NoChildrenException;

/**
 * This class provides everything necessary to transform a <code>RuleList</code> and an
 * instance of it to the normal form.
 * 
 * This means that all function, constant and variable symbols will be replaced so
 * a very specific signature consisting only of the symbols <code>fun</code>, <code>var</code>,
 * <code>suc</code> and <code>zero</code> is used.
 * 
 * All symbols will get a number associated with them, beginning with 0 (zero). For example
 * function 3 is represented as:
 * 
 * <code>fun(suc(suc(suc(zero))))</code>
 * 
 * These terms are then put together using several <code>cons</code> terms. For details
 * please refer to my bachelor thesis.
 * 
 * @author sarek
 */
public class Codification {
	private HashMap<String,CodificationContainer> codes;
	private int curNumFunction;
	private int curNumVariable;
	private RuleList rulelist;
	private ASTNode instance;

	/**
	 * Initialize the class to perform the codification
	 * 
	 * @param rulelist The <code>RuleList</code> to be codified
	 * @param instance The instance to be codified
	 */
	public Codification(RuleList rulelist, ASTNode instance) {
		this.codes = new HashMap<String,CodificationContainer>();
		this.curNumFunction = 0;
		this.curNumVariable = 0;
		this.rulelist = rulelist;
		this.instance = instance;
	}
	
	/**
	 * Performs the actual codification by first generating a mapping
	 * between the original symbols and ascending numbers.
	 * Then both the <code>RuleList</code> and afterwards the instance
	 * will be codified. Symbols present in the instance, but not in the
	 * <code>RuleList</code> will also be codified. Their associated numbers
	 * will be the highest ones. 
	 */
	public void codify() {
		// Create standard form of the rulelist
			// Create translation map of functions and variables in rules
		Iterator<Rule> ruleIt = rulelist.getRules();
		while(ruleIt.hasNext()) {
			Rule rule = ruleIt.next();
			// Look at left side
			createMap(rule.getLeft());
			
			// Look at right side
			createMap(rule.getRight());
		}
		
		// Create new list of rules in standard form using translation map
		codifyRuleList();
		
		// Create standard form of the instance
			// (Possibly) extend translation map of functions and variables
		createMap(instance);
			// Create new instance tree in standard form
		codifyTree(instance, true);
	}

	/**
	 * This method transforms a given tree to its normal form.
	 * It is supposed the mapping has already been created.
	 * 
	 * @param node The tree to be performed
	 * @param rightmost Is this a rightmost node? - Initialize with true
	 * @return The normal form of the tree
	 */
	private ASTNode codifyTree(ASTNode node, boolean rightmost) {
		// new cons
		ASTNode cons = new Function("cons");
		cons.add(genCode(node));
		
		try {
			Iterator<ASTNode> childIt = node.getChildIterator();
			
			while(childIt.hasNext()) {
				cons.add(codifyTree(childIt.next(), !childIt.hasNext() && rightmost));
			}
		}
		catch(NoChildrenException e) {
			ASTNode empty = new Constant("empty");
			cons.add(empty);
		}
		
		return cons;
	}
	
	/**
	 * Generate the codified term for a specific node.
	 * Here a node may not be a tree - No checks are performed!
	 * 
	 * @param node The node to be codified
	 * @return Term [fun | var](suc(...(zero)...)
	 */
	private ASTNode genCode(ASTNode node) {
		ASTNode retval;
		if(node instanceof Variable) {
			retval = new Function("var");
		}
		else {
			retval = new Function("fun");
		}
		
		retval.add(genNumber(codes.get(node.getName()).number));
		
		return retval;
	}
	
	/**
	 * Generate a number in term form using <code>suc</code>
	 * and <code>zero</code>
	 * @param number
	 * @return The number's term, e. g. <code>suc(suc(zero))</code>
	 */
	private ASTNode genNumber(int number) {
		if(number > 0) {
			ASTNode retval = new Function("suc");
			retval.add(genNumber(number - 1));
			return retval;
		}
		else {
			return new Constant("zero");
		}
	}

	/**
	 * Create the normal form of a <code>RuleList</code>.
	 * The existence of a mapping is pre-supposed.
	 */
	private void codifyRuleList() {
		ASTNode firstCons, secondCons, retval = null, toAdd = null;
		
		Iterator<Rule> ruleIt = rulelist.getRules();
		while(ruleIt.hasNext()) {
			Rule rule = ruleIt.next();
			firstCons = new Function("cons");
			secondCons = new Function("cons");
			
			secondCons.add(codifyTree(rule.getLeft(), true));
			secondCons.add(codifyTree(rule.getRight(), true));
			firstCons.add(secondCons);

			if(retval == null) {
				retval = firstCons;
				toAdd = retval;
			}
			else {
				toAdd.add(firstCons);
				toAdd = firstCons;
			}
		}
	}

	/**
	 * Create a mapping between function symbols and numbers for
	 * a tree.
	 * 
	 * @param node The tree a mapping shall be created for
	 */
	private void createMap(ASTNode node) {
		if(!codes.containsKey(node.getName())) {
			if(node instanceof Variable) {
				CodificationContainer data = new CodificationContainer(node.getParamCount(), curNumVariable);
				curNumVariable++;
				codes.put(node.getName(), data);
			}
			else {
				CodificationContainer data = new CodificationContainer(node.getParamCount(), curNumFunction);
				curNumFunction++;
				codes.put(node.getName(), data);
			}
		}
		
		try {
			Iterator<ASTNode> childIt = node.getChildIterator();
			while(childIt.hasNext()) {
				createMap(childIt.next());
			}
		}
		catch(NoChildrenException e) {
			// do nothing. we simply reached a leaf node
		}
	}
}
