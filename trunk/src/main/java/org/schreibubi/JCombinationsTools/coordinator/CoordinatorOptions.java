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
package org.schreibubi.JCombinationsTools.coordinator;

import org.schreibubi.JCombinationsTools.settings.SettingsInterface;

public enum CoordinatorOptions implements SettingsInterface {
	VERSION("version", "version", false, "", "v", "", false), SILENT("silent", "do not print warnings", false, "", "l",
			"false", false), IGNOREMISSINGCBMPOS("ignoremissingcbmpos", "[in] ignore missing cbmpos files", false, "",
			"m", "false", false), CBMOFFSET("cbmOffset", "[in] cbm file numbering offset", true, "number", "n", "0",
			false), CONDITIONPREFIX("conditionprefix", "[in] condition file prefix", true, "file", "i", "pcf", false), CONDITIONTEMPLATE(
			"conditiontemplate", "[in] condition template", true, "file", "t", "", false), CONDITIONHEADER(
			"conditionheader", "[in] condition header", true, "file", "j", "", false), PRETAGS("pretags",
			"[in] pre tags for file searching", true, "pre-tags", "e", "", false), POSTTAGS("posttags",
			"[in] post tags for file searching", true, "post-tags", "f", "", false), COMBINATIONS("combinations",
			"[in] combinations file", true, "file", "c", "", true), CONDITION("condition", "[out] condition file name",
			true, "file", "b", "", false), PATTERNDIR("patterndir", "[in] directory where the patterns are searched",
			true, "directory", "p", ".", false), CBMDIR("cbmdir", "[out] output dir for generated cbm dat files", true,
			"directory", "d", ".", false), PCFDIR("pcfdir", "[out] output dir for generated pcf files", true,
			"directory", "q", ".", false), SETI("seti", "[in] seti.xml", true, "file", "s", "", true), NOCBMGEN(
			"nocbmgen", "do not start cbm generation", false, "", "o", "false", false), NOPCF("nopcf",
			"do not generate pcf", false, "", "u", "false", false), MERGESETI("mergeseti",
			"merge seti chains if possible", false, "", "r", "false", false), HELP("help", "Help", false,
			"gives you help", "h", "false", false);

	String	longArgName;
	String	explanation;
	boolean	hasArgs;
	String	argName;
	String	shortArgName;
	String	defaultValue;
	boolean	required;

	CoordinatorOptions(String longArgName, String explanation, boolean hasArgs, String argName, String shortArgName,
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
	 * @see org.schreibubi.JCombinationsTools.mergeResults.SettingsInterface#getArgName()
	 */
	public String getArgName() {
		return argName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.JCombinationsTools.mergeResults.SettingsInterface#getDefaultValue()
	 */
	public String getDefaultValue() {
		return defaultValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.JCombinationsTools.mergeResults.SettingsInterface#getExplanation()
	 */
	public String getExplanation() {
		return explanation;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.JCombinationsTools.mergeResults.SettingsInterface#getLongArgName()
	 */
	public String getLongArgName() {
		return longArgName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.JCombinationsTools.mergeResults.SettingsInterface#getShortArgName()
	 */
	public String getShortArgName() {
		return shortArgName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.JCombinationsTools.mergeResults.SettingsInterface#isHasArgs()
	 */
	public boolean hasArgs() {
		return hasArgs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.JCombinationsTools.settings.SettingsInterface#required()
	 */
	@Override
	public boolean required() {
		return required;
	}

}