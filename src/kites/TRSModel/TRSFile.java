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

package kites.TRSModel;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * This class holds a complete TRS, including its instance,
 * if specified in the source file
 */
public class TRSFile {
	/** The list of rules which comprise the TRS */
	LinkedList<Rule> rules;
	/** The instance given in the source file */
	ASTNode instance;
	
	/**
	 * Creates a TRS without rules or instance.
	 */
	public TRSFile() {
		rules = new LinkedList<Rule>();
		instance = null;
	}
	
	/**
	 * Add a rule to the TRS.
	 * 
	 * @param rule The rule to add
	 */
	public void add(Rule rule) {
		rules.add(rule);
	}
	
	/**
	 * Gives the instance of this TRS
	 * 
	 * @return The TRS's instance if previously set, <code>null</code> otherwise
	 */
	public ASTNode getInstance() {
		return instance;
	}
	
	/**
	 * Set the TRS's instance.
	 * 
	 * @param instance The instance to set
	 */
	public void setInstance(ASTNode instance) {
		this.instance = instance;
	}
	
	/**
	 * Gives an iterator over the TRS's rules.
	 * 
	 * @return The iterator
	 */
	public Iterator<Rule> getRules() {
		return rules.iterator();
	}
	
	/**
	 * Gives a list of all the rules in the TRS.
	 * 
	 * @return The list
	 */
	public LinkedList<Rule> getRulesList() {
		return rules;
	}
	
	/**
	 * Gives the String representation of this TRS
	 * 
	 * @return The String representation
	 */
	public String toString() {
		String retval = "";
		Iterator<Rule> it = this.getRules();
		
		while(it.hasNext()) {
			Rule r = it.next();
			retval += r.toString() + "\n";
		}
		
		return retval;
	}
}
