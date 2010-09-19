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
