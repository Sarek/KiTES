package kites.TRSModel;

/**
 * This class represents a single rule of a ruleset.
 * It takes two trees, the left-hand side of the rule and
 * the right-hand side.
 */
public class Rule {
	/** The left-hand side of the rule */
	private ASTNode left;
	
	/** The right-hand side of the rule */
	private ASTNode right;
	
	/**
	 * Gives the left-hand side of the rule.
	 * @return The rule's left-hand side
	 */
	public ASTNode getLeft() {
		return left;
	}
	
	/**
	 * Gives the right-hand side of the rule.
	 * 
	 * @return The rule's right-hand side
	 */
	public ASTNode getRight() {
		return right;
	}
	
	/**
	 * Set the left-hand side of the rule
	 * 
	 * @param left The new left-hand side of the rule
	 */
	public void setLeft(ASTNode left) {
		this.left = left;
	}
	
	/**
	 * Set the right-hand side of the rule
	 * 
	 * @param right The new right-hand side of the rule
	 */
	public void setRight(ASTNode right) {
		this.right = right;
	}
	
	/**
	 * Gives the String representation of the rule
	 * in the form "left" --> "right".
	 * 
	 * @return The rule's String representation
	 */
	public String toString() {
		return left.toString() + " --> "+ right.toString();
	}
}
