package kites.exceptions;

/**
 * This exception will be thrown when an error during a decomposition occurs.
 * This specifically refers to a decomposition error, not a syntax error.
 */
public class DecompositionException extends Exception {
	private static final long serialVersionUID = -3467221112911036245L;

	public DecompositionException(String msg) {
		super(msg);
	}
}
