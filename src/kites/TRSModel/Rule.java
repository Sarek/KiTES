package kites.TRSModel;

public class Rule {
	private ASTNode left;
	private ASTNode right;
	
	public ASTNode getLeft() {
		return left;
	}
	
	public ASTNode getRight() {
		return right;
	}
	
	public void setLeft(ASTNode left) {
		this.left = left;
	}
	
	public void setRight(ASTNode right) {
		this.right = right;
	}
	
	public String toString() {
		return left.toString() + " --> "+ right.toString();
	}
}
