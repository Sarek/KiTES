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
