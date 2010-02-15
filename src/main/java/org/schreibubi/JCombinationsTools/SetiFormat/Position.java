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

import java.math.BigInteger;

import org.schreibubi.JCombinationsTools.util.Helper;

/**
 * Seti position
 * 
 * @author Jörg Werner
 * 
 */
public class Position {

	private Chain chain;

	private String length;

	private String length_base;

	private String defaultValue;

	private String defaultValue_base;

	private String type;

	private String fuse;

	private String position;

	private String position_base;

	private Variable fuse_disable_bit;

	/**
	 * @return Returns the chain.
	 */
	public Chain getChain() {
		return this.chain;
	}

	/**
	 * @return Returns the decoded length.
	 */
	public BigInteger getDecodedDefaultValue() {
		if (this.defaultValue_base.equalsIgnoreCase("dec")) {
			return new BigInteger(this.position, 10);
		} else if (this.defaultValue_base.equalsIgnoreCase("hex")) {
			return new BigInteger(this.position, 16);
		} else if (this.defaultValue_base.equalsIgnoreCase("bin")) {
			return new BigInteger(this.position, 2);
		}
		return BigInteger.ZERO;
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
	 * @return Returns the decoded length.
	 */
	public int getDecodedPosition() {
		if (this.position_base.equalsIgnoreCase("dec")) {
			return Integer.parseInt(this.position);
		} else if (this.position_base.equalsIgnoreCase("hex")) {
			return Integer.parseInt(this.position, 16);
		} else if (this.position_base.equalsIgnoreCase("bin")) {
			return Integer.parseInt(this.position, 2);
		}
		return 0;
	}

	/**
	 * @return Returns the default value.
	 */
	public String getDefaultValue() {
		return this.defaultValue;
	}

	/**
	 * @return Returns the default value base.
	 */
	public String getDefaultValue_base() {
		return this.defaultValue_base;
	}

	/**
	 * @return Returns the fuse.
	 */
	public String getFuse() {
		return this.fuse;
	}

	public Variable getFuse_disable_bit() {
		return fuse_disable_bit;
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
	 * @return Returns the position.
	 */
	public String getPosition() {
		return this.position;
	}

	/**
	 * @return Returns the position_base.
	 */
	public String getPosition_base() {
		return this.position_base;
	}

	/**
	 * @return Returns the type.
	 */
	public String getType() {
		return this.type;
	}

	/**
	 * @param chain
	 *            The chain to set.
	 */
	public void setChain(Chain chain) {
		this.chain = chain;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public void setDefaultValue_base(String defaultValue_base) {
		this.defaultValue_base = defaultValue_base;
	}

	/**
	 * @param fuse
	 *            The fuse to set.
	 */
	public void setFuse(String fuse) {
		this.fuse = Helper.sanitizeString(fuse);
	}

	public void setFuse_disable_bit(Variable fuse_disable_bit) {
		this.fuse_disable_bit = fuse_disable_bit;
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
	 * @param position
	 *            The position to set.
	 */
	public void setPosition(String position) {
		this.position = Helper.sanitizeString(position);
	}

	/**
	 * @param position_base
	 *            The position_base to set.
	 */
	public void setPosition_base(String position_base) {
		this.position_base = position_base;
	}

	/**
	 * @param type
	 *            The type to set.
	 */
	public void setType(String type) {
		this.type = Helper.sanitizeString(type);
	}

}
