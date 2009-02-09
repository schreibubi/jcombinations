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
package org.schreibubi.JCombinationsTools.util;

import org.schreibubi.visitor.VArrayList;

/**
 * Helper functions
 * 
 * @author Jörg Werner
 * 
 */
public class Helper {

	/**
	 * Takes a byte list and converts it to a string
	 * 
	 * @param l
	 *            the ByteList
	 * @return the converted string
	 */
	public static String ByteListToString(VArrayList<Integer> l) {
		String str = "";
		for (int j = 0; j < l.size(); j++) {
			str = str + (char) l.get(j).intValue();
		}
		return str;
	}

	/**
	 * Prints the contents of line, which is a list of Integers as Hex
	 * 
	 * @param line
	 *            ArrayList of Integers
	 * @return Hexstring
	 */
	public static String printHexLine(VArrayList<Integer> line) {
		String str = "";
		for (int j = 0; j < line.size(); j++) {
			int i = line.get(j).intValue();
			if (i / 16 == 0) {
				str = str + "0x0" + Integer.toHexString(i).toUpperCase();
			} else {
				str = str + "0x" + Integer.toHexString(i).toUpperCase();
			}
			if (j < line.size() - 1) {
				str = str + " ";
			}
		}
		return str;
	}

	/**
	 * Removes newlines and tabs from a string
	 * 
	 * @param s
	 * @return sanitized string
	 */
	public static String sanitizeString(String s) {
		String n = null;
		if (s != null) {
			n = s.replaceAll("\t", "");
			n = n.replaceAll("\n", "");
		}
		return n;
	}
}
