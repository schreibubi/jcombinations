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
package org.schreibubi.JCombinationsTools.mergeResults;

import org.schreibubi.JCombinationsTools.settings.SettingsInterface;

public enum MergeResultsOptions implements SettingsInterface {
	PRETAGS("pretags", "[in] pre tags for file searching", true, "pre-tags",
			"e", "", false), POSTTAGS("posttags",
			"[in] post tags for file searching", true, "post-tags", "f", "",
			false), PREFIX("prefix", "result file prefix", true, "prefix", "p",
			"lotresults_", false), POSTFIX("postfix", "result file postfix",
			true, "postfix", "q", ".xml", false), DCLOG("dclog",
			"[in] dclog-files", true, "file", "d", "", false), COMBINATIONS(
			"combinations", "[in] combinations file", true, "file", "c", "",
			true), OUTPUT("output", "[out] output file", true, "file", "o", "",
			false), TRIM("trim", "Column name of trim column", true, "column",
			"t", "TRIM", false), MEASURE("measure",
			"Column name of measure column", true, "column", "m", "MEASURE",
			false), XVALS("xvals", "Column name containing the x-values", true,
			"column", "x",
			"COM_MEASURE,TRIM_SETI,TRIM_RELATIVE,TRIM_ABS,FORCE_VALUE", false), ABSOLUTE(
			"absolute", "Absolute values", true, "column", "a", "TRIM_ABS",
			false), RELATIVE("relative",
			"Column name containing the relative changes", true, "column", "r",
			"TRIM_RELATIVE", false), SUBTITLE("subtitle",
			"Column name containing the subtitles for the shmoos", true,
			"column", "s", "COMMENT", false), ZIP("zip", "zip output file",
			false, "", "z", "false", false), DUT("dut",
			"process only selected DUTs", true, "column", "u", "1-32", false), DUTFILEOFFSET(
			"dutFileOffset",
			"dut offset when merging measurements from multiple files", true,
			"offset", "g", "128", false), DUTTOUCHDOWNOFFSET(
			"dutTouchdownOffset",
			"dut offset when merging different touchdowns from one file", true,
			"offset", "w", "32", false), VERSION("version", "version", false,
			"", "v", "", false), MATLAB("matlab",
			"output also in matlab file format", false, "", "b", "", false), TESTNAME(
			"testname", "Name of Variable containing the name of the test",
			true, "test name variable", "n", "TESTNAME", false), HELP("help",
			"Help", false, "gives you help", "h", "false", false);

	String longArgName;
	String explanation;
	boolean hasArgs;
	String argName;
	String shortArgName;
	String defaultValue;
	boolean required;

	MergeResultsOptions(String longArgName, String explanation,
			boolean hasArgs, String argName, String shortArgName,
			String defaultValue, boolean required) {
		this.longArgName = longArgName;
		this.explanation = explanation;
		this.hasArgs = hasArgs;
		this.argName = argName;
		this.shortArgName = shortArgName;
		this.defaultValue = defaultValue;
		this.required = required;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.schreibubi.JCombinationsTools.mergeResults.SettingsInterface#getArgName
	 * ()
	 */
	public String getArgName() {
		return argName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.schreibubi.JCombinationsTools.mergeResults.SettingsInterface#
	 * getDefaultValue()
	 */
	public String getDefaultValue() {
		return defaultValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.schreibubi.JCombinationsTools.mergeResults.SettingsInterface#
	 * getExplanation()
	 */
	public String getExplanation() {
		return explanation;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.schreibubi.JCombinationsTools.mergeResults.SettingsInterface#
	 * getLongArgName()
	 */
	public String getLongArgName() {
		return longArgName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.schreibubi.JCombinationsTools.mergeResults.SettingsInterface#
	 * getShortArgName()
	 */
	public String getShortArgName() {
		return shortArgName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.schreibubi.JCombinationsTools.mergeResults.SettingsInterface#isHasArgs
	 * ()
	 */
	public boolean hasArgs() {
		return hasArgs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.schreibubi.JCombinationsTools.settings.SettingsInterface#required()
	 */
	public boolean required() {
		return required;
	}

}