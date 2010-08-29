package kites.exceptions;

/**
 * This exception is thrown when something is wrong with a node in a syntax tree.
 * This refers to problems such as an unknown node type.
 * More information will be provided as message.
 */
public class NodeException extends Exception {
	private static final long serialVersionUID = -3568616556543406755L;

	public NodeException() {
		super();
	}

	public NodeException(String arg0) {
		super(arg0);;
	}

	public NodeException(Throwable arg0) {
		super(arg0);
	}

	public NodeException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
