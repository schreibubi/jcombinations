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
package org.schreibubi.visitor;

/**
 * Host interface for the visitor pattern. All classes which want to support the visitor pattern have to implement this
 * interface.
 * 
 * @author Jörg Werner
 * @param <T>
 *            Class of host
 */
public interface Host<T> {

	/**
	 * accept method for the visitor pattern
	 * 
	 * @param v
	 *            Visitor
	 * @throws Exception
	 */
	public abstract void accept(Visitor<T> v) throws Exception;

}
