/**
 * 
 */
package kites.exceptions;

/**
 * This exception will be thrown when there were no children to be found
 * when there should have been some.
 * This is a non-critical exception and thrown quite often when traversing a syntax tree,
 * specifically each time a leaf node is reached.
 * 
 * @author sarek
 */
public class NoChildrenException extends Exception {
	private static final long serialVersionUID = -3593463976170835369L;
}
