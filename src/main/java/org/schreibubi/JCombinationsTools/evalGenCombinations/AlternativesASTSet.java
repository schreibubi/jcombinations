/**
 * Copyright (C) 2009 Jörg Werner schreibubi@gmail.com
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.schreibubi.JCombinationsTools.evalGenCombinations;

import org.schreibubi.visitor.VArrayList;

import antlr.collections.AST;

/**
 * Container class, which stores for one Key set the differen possible
 * alternatives
 * 
 * @author Jörg Werner
 */
public class AlternativesASTSet {

	private VArrayList<String> keys;

	private VArrayList<VArrayList<AST>> valuesets;

	/**
	 * Constructor
	 */
	public AlternativesASTSet() {
		this.valuesets = new VArrayList<VArrayList<AST>>();
	}

	/**
	 * Adds an alternative set of values
	 * 
	 * @param valueset
	 */
	public void addAlternative(VArrayList<AST> valueset) {
		this.valuesets.add(valueset);
	}

	/**
	 * Gets an alternative set
	 * 
	 * @param n
	 *            number of alternative set to get
	 * @return one alternative set
	 */
	public VArrayList<AST> getAlternative(int n) {
		return this.valuesets.get(n);
	}

	/**
	 * gets the Keys
	 * 
	 * @return keys
	 */
	public VArrayList<String> getKeys() {
		return this.keys;
	}

	/**
	 * Returns the number of alternatives available
	 * 
	 * @return number of alternatives available
	 */
	public int getNumberOfAlternatives() {
		return this.valuesets.size();
	}

	/**
	 * sets the Keys
	 * 
	 * @param keys
	 */
	public void setKeys(VArrayList<String> keys) {
		this.keys = keys;
	}
}
