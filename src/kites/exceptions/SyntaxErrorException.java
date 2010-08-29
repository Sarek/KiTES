package kites.exceptions;

/**
 * This exception is thrown on encountering a syntax error in a syntax tree.
 * It is mainly used when doing syntax checks (this was obvious, wasn't it?)
 * and there triggers the rewrite modes that can be used.
 * 
 * @author sarek
 */
public class SyntaxErrorException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7418626446976212243L;

	public SyntaxErrorException(String arg0) {
		super(arg0);
	}
}
