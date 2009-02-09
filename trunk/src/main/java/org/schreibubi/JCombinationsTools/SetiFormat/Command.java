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
package org.schreibubi.JCombinationsTools.SetiFormat;

import org.schreibubi.JCombinationsTools.util.Helper;

/**
 * Seti command
 * 
 * @author Jörg Werner
 * 
 */
public class Command {

	private String	shortname;

	private String	index;

	private String	client;

	private String	signal_name;

	private String	location;

	private String	description;

	private String	code;

	private String	code_base;

	public String getClient() {
		return client;
	}

	/**
	 * @return Returns the code.
	 */
	public String getCode() {
		return this.code;
	}

	/**
	 * @return Returns the code_base.
	 */
	public String getCode_base() {
		return this.code_base;
	}

	/**
	 * @return Returns the decoded code.
	 */
	public int getDecodedCode() {
		if (this.code_base.equalsIgnoreCase("dec"))
			return Integer.parseInt(this.code);
		else if (this.code_base.equalsIgnoreCase("hex"))
			return Integer.parseInt(this.code, 16);
		else if (this.code_base.equalsIgnoreCase("bin"))
			return Integer.parseInt(this.code, 2);
		return 0;
	}

	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * @return Returns the index.
	 */
	public String getIndex() {
		return this.index;
	}

	/**
	 * @return Returns the location.
	 */
	public String getLocation() {
		return this.location;
	}

	/**
	 * @return Returns the shortname.
	 */
	public String getShortname() {
		return this.shortname;
	}

	public String getSignal_name() {
		return signal_name;
	}

	public void setClient(String client) {
		this.client = client;
	}

	/**
	 * @param code
	 *            The code to set.
	 */
	public void setCode(String code) {
		this.code = Helper.sanitizeString(code);
	}

	/**
	 * @param code_base
	 *            The code_base to set.
	 */
	public void setCode_base(String code_base) {
		this.code_base = code_base;
	}

	/**
	 * @param description
	 *            The description to set.
	 */
	public void setDescription(String description) {
		this.description = Helper.sanitizeString(description);
	}

	/**
	 * @param index
	 *            The index to set.
	 */
	public void setIndex(String index) {
		this.index = Helper.sanitizeString(index);
	}

	/**
	 * @param location
	 *            The location to set.
	 */
	public void setLocation(String location) {
		this.location = Helper.sanitizeString(location);
	}

	/**
	 * @param shortname
	 *            The shortname to set.
	 */
	public void setShortname(String shortname) {
		this.shortname = Helper.sanitizeString(shortname);
	}

	public void setSignal_name(String signal_name) {
		this.signal_name = signal_name;
	}

}
