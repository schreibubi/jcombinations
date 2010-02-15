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
 * Seti chain
 * 
 * @author Jörg Werner
 * 
 */
public class Chain {

	private String shortname;

	private String name;

	private String index;

	private String client;

	private String description;

	private String address;

	private String address_base;

	private String length;

	private String length_base;

	/**
	 * @return Returns the address.
	 */
	public String getAddress() {
		return this.address;
	}

	/**
	 * @return Returns the address_base.
	 */
	public String getAddress_base() {
		return this.address_base;
	}

	public String getClient() {
		return client;
	}

	/**
	 * @return Returns the decoded address.
	 */
	public int getDecodedAddress() {
		if (this.address_base.equalsIgnoreCase("dec")) {
			return Integer.parseInt(this.address);
		} else if (this.address_base.equalsIgnoreCase("hex")) {
			return Integer.parseInt(this.address, 16);
		} else if (this.address_base.equalsIgnoreCase("bin")) {
			return Integer.parseInt(this.address, 2);
		}
		return 0;
	}

	/**
	 * @return Returns the decoded length.
	 */
	public int getDecodedLength() {
		if (this.length_base.equalsIgnoreCase("dec")) {
			return Integer.parseInt(this.length);
		} else if (this.length_base.equalsIgnoreCase("hex")) {
			return Integer.parseInt(this.length, 16);
		} else if (this.length_base.equalsIgnoreCase("bin")) {
			return Integer.parseInt(this.length, 2);
		}
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
	 * @return Returns the length.
	 */
	public String getLength() {
		return this.length;
	}

	/**
	 * @return Returns the length_base.
	 */
	public String getLength_base() {
		return this.length_base;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @return Returns the shortname.
	 */
	public String getShortname() {
		return this.shortname;
	}

	/**
	 * @param address
	 *            The address to set.
	 */
	public void setAddress(String address) {
		this.address = Helper.sanitizeString(address);
	}

	/**
	 * @param address_base
	 *            The address_base to set.
	 */
	public void setAddress_base(String address_base) {
		this.address_base = address_base;
	}

	public void setClient(String client) {
		this.client = client;
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
	 * @param length
	 *            The length to set.
	 */
	public void setLength(String length) {
		this.length = Helper.sanitizeString(length);
	}

	/**
	 * @param length_base
	 *            The length_base to set.
	 */
	public void setLength_base(String length_base) {
		this.length_base = length_base;
	}

	/**
	 * @param name
	 *            The name to set.
	 */
	public void setName(String name) {
		this.name = Helper.sanitizeString(name);
	}

	/**
	 * @param shortname
	 *            The shortname to set.
	 */
	public void setShortname(String shortname) {
		this.shortname = Helper.sanitizeString(shortname);
	}

}
