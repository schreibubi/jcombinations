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
 * Visitor Interface. Implements the visitor pattern for visiting a class which
 * implements the {@link Host} interface. Additionally convenience methods for
 * visiting a whole {@link VHashMap} or {@link VArrayList} exist.
 * 
 * @param <T>
 *            type of visitor
 */
public interface Visitor<T> {

	/**
	 * @param s
	 * @throws Exception
	 */
	public abstract void visit(T s) throws Exception;

	/**
	 * @param s
	 * @throws Exception
	 */
	public void visit(VArrayList<T> s) throws Exception;

	/**
	 * @param s
	 * @throws Exception
	 */
	public void visit(VArrayListMultimap<T> s) throws Exception;

	/**
	 * @param s
	 * @throws Exception
	 */
	public void visit(VHashMap<T> s) throws Exception;

	/**
	 * @param s
	 * @throws Exception
	 */
	public void visit(VLinkedHashMap<T> s) throws Exception;

	/**
	 * @param s
	 * @throws Exception
	 */
	public void visit(VTreeMap<T> s) throws Exception;

}
