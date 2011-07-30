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
package org.schreibubi.JCombinationsTools.coordinatorPatch;

import org.schreibubi.JCombinationsTools.settings.SettingsInterface;

public enum CoordinatorPatchOptions implements SettingsInterface {
	VERSION("version", "version", false, "", "v", "", false), PRETAGS(
			"pretags", "[in] pre tags for file searching", true, "pre-tags",
			"e", "", false), POSTTAGS("posttags",
			"[in] post tags for file searching", true, "post-tags", "f", "",
			false), SILENT("silent", "do not print warnings", false, "", "l",
			"false", false), CONDITIONTEMPLATE("conditiontemplate",
			"[in] condition template", true, "file", "t", "", false), CONDITIONHEADER(
			"conditionheader", "[in] condition header", true, "file", "j", "",
			false), CONDITIONPREFIX("conditionprefix",
			"[in] condition file prefix", true, "file", "i", "pcf", false), COMBINATIONS(
			"combinations", "[in] combinations file", true, "file", "c", "",
			true), CONDITION("condition", "[out] condition file name", true,
			"file", "b", "", false), NOPATGEN("nopatgen",
			"do not start pattern generation", false, "", "o", "false", false), NOPCF(
			"nopcf", "do not generate pcf", false, "", "u", "false", false), PCFDIR(
			"pcfdir", "[out] output dir for generated pcf files", true,
			"directory", "q", ".", false), MPADIR("mpadir",
			"[out] output dir for generated mpa files", true, "directory", "d",
			".", false), EXEC("exec", "compile command", true, "command", "x",
			"", true), NOREMOVE("noremove",
			"do not remove generated pat and mpa files", false, "", "r",
			"false", false), HELP("help", "Help", false, "gives you help", "h",
			"false", false);

	String longArgName;
	String explanation;
	boolean hasArgs;
	String argName;
	String shortArgName;
	String defaultValue;
	boolean required;

	CoordinatorPatchOptions(String longArgName, String explanation,
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