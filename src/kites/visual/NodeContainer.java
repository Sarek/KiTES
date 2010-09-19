/*
 * This file is part of KiTES.
 * 
 * Copyright 2010 Sebastian Schäfer <sarek@uliweb.de>
 *
 *   KiTES is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   KiTES is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with KiTES.  If not, see <http://www.gnu.org/licenses/>.
 */

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
