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
package org.schreibubi.JCombinations.logic.io;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.swing.tree.TreePath;

import org.schreibubi.JCombinations.logic.DataModel;

/**
 * @author Jörg Werner
 * 
 */
public interface ExportDataInterface {

	/**
	 * Write Method
	 * 
	 * @param dm
	 *            Data Model
	 * @param out
	 *            stream to write the data to
	 * @param nodes
	 *            which nodes to output
	 * @throws IOException
	 */
	public void writeData(DataModel dm, OutputStream out,
			ArrayList<TreePath> nodes) throws IOException;

}
