/**
 * 
 */
package kites.exceptions;

/**
 * This exception is thrown when two <code>Rule</code>s in a <code>RuleList</code>
 * can be unified.
 * 
 * @author sarek
 */
public class UnificationException extends Exception {

	/**
	 * Eclipse wants this class to have this
	 */
	private static final long serialVersionUID = 4643895786901315520L;

	/**
	 * @param message
	 */
	public UnificationException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
}
