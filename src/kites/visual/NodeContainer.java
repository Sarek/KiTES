package kites.visual;

/**
 * This interface defines the basic function a graphical representation of a term has to perform
 */
public interface NodeContainer {
	/**
	 * Set the interpreter window this container will be placed in.
	 * @param wnd the window
	 */
	public void setInterpreterWindow(InterpreterWindow wnd);
	
	/**
	 * Give the string representation of this container
	 * @return the string representation
	 */
	public String toString();
	
	/**
	 * deactivate this container's popup menu.
	 * If it has children, also deactivate them.
	 */
	public void deactivate();
	
	/**
	 * Add a comma at the textual end of this container.
	 */
	public void addComma();
	
	/**
	 * Add a closing parenthesis at the textual end of this container.
	 */
	public void addClosePar();
	
	/**
	 * Activate this container's popup menu.
	 * If it has children, also activate them.
	 * Activation means: if a rule can be applied to this container,
	 * then activation is possible and a popup menu, displaying the
	 * applicable rules shall be openable.
	 */
	public void activate();
	
	/**
	 * Colorize this container.
	 * If it has children, also colorize them.
	 * Colorization means: if a rule can be applied to this containe,
	 * then colorization is possible, set a yellow background color.
	 */
	public void colorize();
}
