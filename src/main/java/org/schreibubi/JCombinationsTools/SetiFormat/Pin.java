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

/**
 * @author Jörg Werner
 * 
 */
public class Pin {

	private int		bit;

	private String	pinname;

	public int getBit() {
		return bit;
	}

	public String getPinname() {
		return pinname;
	}

	public void setBit(int bit) {
		this.bit = bit;
	}

	public void setPinname(String pinname) {
		this.pinname = pinname;
	}

}
