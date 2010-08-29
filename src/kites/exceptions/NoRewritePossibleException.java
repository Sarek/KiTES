package kites.exceptions;

/**
 * This exception is thrown when no rewrite can be performed on a syntax tree
 * This is a non-critical exception and merely signifies the end of an
 * execution chain.
 */
public class NoRewritePossibleException extends Exception {
	private static final long serialVersionUID = -9060583215581412930L;

	public NoRewritePossibleException() {
		// TODO Auto-generated constructor stub
	}

	public NoRewritePossibleException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public NoRewritePossibleException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public NoRewritePossibleException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

}
