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
package org.schreibubi.JCombinations;

/**
 * @author Jörg Werner
 * 
 */
public class Info {

	/**
	 * Version string
	 * 
	 * 1.0 and 1.1 was Konstanz
	 * 
	 * 1.2 was Meersburg
	 * 
	 * 1.3 was Hagnau
	 * 
	 * Changes: - Matlab export added - Overwrite warning only given once in
	 * Coordinator - include statements are now possible outside the {...} block
	 * - Big fix for calculations with units - Merge results now uses the
	 * TESTNAME to look up the results - Combination file is looked up according
	 * to Makepackage rules in Coordinator and MergeResults - ignore parse
	 * errors for numbers in lotresults (CHIPID) - implemented option statement
	 * in combination files to set command line switches - fix double defined
	 * command line options in Coordinator - Mavenized build - library updates -
	 * support for multiple touchdowns - Support for setirel_211 - fixed help
	 * for MergeResults - added mergeseti option
	 */
	public static final String version = "Version 1.3 (\"Hagnau\", SVN: $Rev: 183 $)";

	/**
	 * Authors
	 */
	public static final String authors = "Jörg Werner";

	/**
	 * Copyright
	 */
	public static final String copyright = "(C) 2009 Jörg Werner";

	/**
	 * Returns the version string
	 * 
	 * @param name
	 *            of the program
	 * @return version string for the program
	 */
	public static String getVersionString(String name) {
		return name + "\n" + Info.version + "\nWritten by " + Info.authors
				+ "\n" + Info.copyright + "\n";
	}

	/**
	 * Prints out the version message
	 * 
	 * @param name
	 *            of the program
	 */
	public static void printVersion(String name) {
		System.out.println(getVersionString(name));
	}

}
