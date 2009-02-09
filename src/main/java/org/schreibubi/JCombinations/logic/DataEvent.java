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
package org.schreibubi.JCombinations.logic;

import java.util.EventObject;

/**
 * @author Jörg Werner
 */
public class DataEvent extends EventObject {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 3258135760375918902L;

	String						message				= "";

	int							pos					= -1;

	boolean						error				= false;

	/**
	 * Constructor
	 * 
	 * @param arg0
	 */
	public DataEvent(Object arg0) {
		super(arg0);
	}

	/**
	 * Constructor
	 * 
	 * @param arg0
	 * @param message
	 * @param error
	 */
	public DataEvent(Object arg0, String message, boolean error) {
		super(arg0);
		this.message = message;
		this.error = error;
	}

	/**
	 * Constructor
	 * 
	 * @param arg0
	 * @param message
	 * @param pos
	 * @param error
	 */
	public DataEvent(Object arg0, String message, int pos, boolean error) {
		super(arg0);
		this.message = message;
		this.pos = pos;
		this.error = error;
	}

	/**
	 * @return error
	 */
	public boolean getError() {
		return this.error;
	}

	/**
	 * @return message
	 */
	public String getMessage() {
		return this.message;
	}

	/**
	 * get position in string where error occured
	 * 
	 * @return position in string where error occured, -1 when not applicable
	 */
	public int getPos() {
		return this.pos;
	}
}
