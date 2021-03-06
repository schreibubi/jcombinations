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
import org.schreibubi.visitor.VArrayList;

/**
 * Seti content
 * 
 * @author Jörg Werner
 * 
 */
public class Content {

	private String index;

	private String shortname;

	private String description;

	private String defaultvalue;

	private VArrayList<Trim> trimmings = new VArrayList<Trim>();

	/**
	 * @return Returns the defaultvalue.
	 */
	public String getDefaultvalue() {
		return this.defaultvalue;
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
	 * @return Returns the shortname.
	 */
	public String getShortname() {
		return this.shortname;
	}

	/**
	 * @return Returns the trimmings.
	 */
	public VArrayList<Trim> getTrimmings() {
		return this.trimmings;
	}

	/**
	 * @param defaultvalue
	 *            The defaultvalue to set.
	 */
	public void setDefaultvalue(String defaultvalue) {
		this.defaultvalue = Helper.sanitizeString(defaultvalue);
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
	 * @param shortname
	 *            The shortname to set.
	 */
	public void setShortname(String shortname) {
		this.shortname = Helper.sanitizeString(shortname);
	}

	/**
	 * @param trimmings
	 *            The trimmings to set.
	 */
	public void setTrimmings(VArrayList<Trim> trimmings) {
		this.trimmings = trimmings;
	}

}
