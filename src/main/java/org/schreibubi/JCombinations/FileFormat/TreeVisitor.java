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
package org.schreibubi.JCombinations.FileFormat;

/**
 * @author Jörg Werner
 * 
 */
public interface TreeVisitor {

	/**
	 * Visit
	 * 
	 * @param a
	 * @throws Exception
	 */
	public void visit(Alternative a) throws Exception;

	/**
	 * Visit
	 * 
	 * @param a
	 * @throws Exception
	 */
	public void visit(Asdap a) throws Exception;

	/**
	 * Visit
	 * 
	 * @param s
	 * @throws Exception
	 */
	public void visit(Shmoo s) throws Exception;

	/**
	 * Visit
	 * 
	 * @param d
	 * @throws Exception
	 */
	public void visit(Ydata d) throws Exception;

}