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
