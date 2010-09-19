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
