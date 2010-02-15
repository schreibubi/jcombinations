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
 * Trim node
 * 
 * @author Jörg Werner
 * 
 */
public class Trim {

	private String trimstring;

	private String trimvalue;

	private String trimvalue_base;

	/**
	 * @return Returns the decoded trimvalue.
	 */
	public int getDecodedTrimvalue() {
		if (this.trimvalue_base.equalsIgnoreCase("dec")) {
			return Integer.parseInt(this.trimvalue);
		} else if (this.trimvalue_base.equalsIgnoreCase("hex")) {
			return Integer.parseInt(this.trimvalue, 16);
		} else if (this.trimvalue_base.equalsIgnoreCase("bin")) {
			return Integer.parseInt(this.trimvalue, 2);
		}
		return 0;
	}

	/**
	 * @return Returns the trimstring.
	 */
	public String getTrimstring() {
		return this.trimstring;
	}

	/**
	 * @return Returns the trimvalue.
	 */
	public String getTrimvalue() {
		return this.trimvalue;
	}

	/**
	 * @return Returns the trimvalue_base.
	 */
	public String getTrimvalue_base() {
		return this.trimvalue_base;
	}

	/**
	 * @param trimstring
	 *            The trimstring to set.
	 */
	public void setTrimstring(String trimstring) {
		this.trimstring = Helper.sanitizeString(trimstring);
	}

	/**
	 * @param trimvalue
	 *            The trimvalue to set.
	 */
	public void setTrimvalue(String trimvalue) {
		this.trimvalue = Helper.sanitizeString(trimvalue);
	}

	/**
	 * @param trimvalue_base
	 *            The trimvalue_base to set.
	 */
	public void setTrimvalue_base(String trimvalue_base) {
		this.trimvalue_base = trimvalue_base;
	}

}
