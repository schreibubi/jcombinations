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
package org.schreibubi.JCombinationsTools.mangle;

import org.schreibubi.symbol.SymbolDouble;
import org.schreibubi.symbol.SymbolInteger;
import org.schreibubi.symbol.SymbolString;
import org.schreibubi.visitor.VArrayList;

/**
 * The mangle class transforms the values of the patterns into the values found in the binary file.
 * 
 * @author Jörg Werner
 * 
 */
public abstract class Mangle {

	/**
	 * returns the expected minimum length of bytes for the diff.
	 * 
	 * @return minimum length of diff
	 */
	public abstract int diffLength();

	/**
	 * Generates a test value used for the binary differences
	 * 
	 * @param s
	 *            The double to generate the test values for
	 * @return returns test values as an arraylist of symbol
	 * @throws Exception
	 *             if no more test values are available
	 */
	public abstract VArrayList<SymbolDouble> getNextPatchDoublePair(SymbolDouble s) throws Exception;

	/**
	 * Generates a test value used for the binary differences
	 * 
	 * @param s
	 *            The integer to generate the test values for
	 * @return returns test values as an arraylist of symbol
	 * @throws Exception
	 *             if no more test values are available
	 */
	public abstract VArrayList<SymbolInteger> getNextPatchIntegerPair(SymbolInteger s) throws Exception;

	/**
	 * Generates a test value used for the binary differences
	 * 
	 * @param s
	 *            The String to generate the test values for
	 * @return returns test values as an arraylist of symbol
	 * @throws Exception
	 *             if no more test values are available
	 */
	public abstract VArrayList<SymbolString> getNextPatchStringPair(SymbolString s) throws Exception;

	/**
	 * Mangles a Double
	 * 
	 * @param s
	 *            Double to be mangled
	 * @return a copy of the mangled Double
	 */
	public abstract SymbolDouble mangle(SymbolDouble s);

	/**
	 * Mangles an Integer
	 * 
	 * @param s
	 *            Integer to be mangled
	 * @return a copy of the mangled Integer
	 */
	public abstract SymbolInteger mangle(SymbolInteger s);

	/**
	 * Mangles a String
	 * 
	 * @param s
	 *            String to be mangled
	 * @return a copy of the mangled String
	 */
	public abstract SymbolString mangle(SymbolString s);
}
