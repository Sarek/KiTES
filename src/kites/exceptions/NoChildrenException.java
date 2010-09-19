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
