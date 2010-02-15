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
package org.schreibubi.JCombinationsTools.settings;

import java.util.HashMap;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

/**
 * @author Jörg Werner
 * 
 */
public class SettingsSingleton {

	private final Properties[] propArray = new Properties[3];

	private static SettingsSingleton myInstance = null;

	public static SettingsSingleton getInstance() throws Exception {
		if (myInstance == null) {
			throw new Exception("SettingsSingleton was not initialized!");
		}
		return myInstance;
	}

	public static void initialize(String programName,
			SettingsInterface[] settingsEnum) throws Exception {
		if (myInstance != null) {
			throw new Exception("SettingsSingletion was already initialized!");
		}
		myInstance = new SettingsSingleton(programName, settingsEnum);

	}

	private String programName = null;

	private SettingsInterface[] settingsEnum = null;

	private Options options = null;

	private final HashMap<SettingsInterface, Boolean> optionSet = new HashMap<SettingsInterface, Boolean>();

	@SuppressWarnings("static-access")
	private SettingsSingleton(String programName,
			SettingsInterface[] settingsEnum) {
		this.programName = programName;
		this.settingsEnum = settingsEnum;

		propArray[0] = new Properties();
		propArray[1] = new Properties(propArray[0]);
		propArray[2] = new Properties(propArray[1]);
		// put default settings into lowest level...
		for (SettingsInterface a : settingsEnum) {
			propArray[0].setProperty(a.getLongArgName(), a.getDefaultValue());
		}

		options = new Options();
		// build up options
		for (SettingsInterface a : settingsEnum) {
			OptionBuilder o = OptionBuilder.withLongOpt(a.getLongArgName());
			if (a.hasArgs()) {
				o = o.withDescription(
						a.getExplanation() + ", default \""
								+ a.getDefaultValue() + "\"").hasArg()
						.withArgName(a.getArgName());
			} else {
				o = o.withDescription(a.getExplanation());
			}
			options.addOption(o.create(a.getShortArgName()));
		}

	}

	public boolean areRequiredOptionsSet() {
		for (SettingsInterface a : settingsEnum) {
			if (a.required()) {
				if ((optionSet.get(a) == null) || (optionSet.get(a) == false)) {
					System.err.println("Required option " + a.getLongArgName()
							+ " is missing.");
					return false;
				}
			}
		}
		return true;
	};

	/**
	 * Display the help
	 */
	public void displayHelp() {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp(programName, options);
	}

	public String getProperty(String name) {
		return getProperty(name, 2);
	}

	public String getProperty(String name, int level) {
		if ((level >= 0) && (level <= 2)) {
			return propArray[level].getProperty(name);
		}
		return null;
	}

	public void parseArguments(String[] args, int level) throws ParseException {
		CommandLineParser CLparser = new PosixParser();

		CommandLine line;
		try {
			line = CLparser.parse(options, args);

			for (SettingsInterface a : settingsEnum) {
				if (line.hasOption(a.getShortArgName())) {
					if (a.hasArgs()) {
						setProperty(a.getLongArgName(), line.getOptionValue(a
								.getShortArgName()), level);
					} else {
						setProperty(a.getLongArgName(), "true", level);
					}
					optionSet.put(a, true);
				}
			}
		} catch (ParseException e) {
			displayHelp();
			throw e;
		}

	}

	private void setProperty(String name, String value, int level) {
		if ((level >= 0) && (level <= 2)) {
			propArray[level].setProperty(name, value);
		}
	}
}
