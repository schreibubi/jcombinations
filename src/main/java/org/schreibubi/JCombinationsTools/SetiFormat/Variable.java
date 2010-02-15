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
 * Seti variable
 * 
 * @author Jörg Werner
 * 
 */
public class Variable {

	private String shortname;

	private String index;

	private Position position = null;

	private VArrayList<Position> positions = new VArrayList<Position>();

	private String signal_name;

	private String description;

	private String io;

	private String wait_time;

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

	public String getIo() {
		return io;
	}

	/**
	 * @return Returns the positions.
	 */
	public Position getPosition() {
		if (this.position == null) {
			return this.positions.get(0);
		} else {
			return this.position;
		}
	}

	/**
	 * @return Returns the shortname.
	 */
	public String getShortname() {
		return this.shortname;
	}

	/**
	 * @return Returns the signal_name.
	 */
	public String getSignal_name() {
		return this.signal_name;
	}

	public String getWait_time() {
		return wait_time;
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

	public void setIo(String io) {
		this.io = io;
	}

	/**
	 * @param positions
	 *            The positions to set.
	 */
	public void setPosition(Position position) {
		this.position = position;
	}

	public void setPositions(VArrayList<Position> positions) {
		this.positions = positions;
	}

	/**
	 * @param shortname
	 *            The shortname to set.
	 */
	public void setShortname(String shortname) {
		this.shortname = Helper.sanitizeString(shortname);
	}

	/**
	 * @param signal_name
	 *            The signal_name to set.
	 */
	public void setSignal_name(String signal_name) {
		this.signal_name = Helper.sanitizeString(signal_name);
	}

	public void setWait_time(String wait_time) {
		this.wait_time = wait_time;
	}

}
