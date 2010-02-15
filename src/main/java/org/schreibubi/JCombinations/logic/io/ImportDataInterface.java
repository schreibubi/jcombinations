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
import java.io.InputStream;

import org.schreibubi.JCombinations.logic.DataModel;

/**
 * Interface for importing data
 * 
 * @author Jörg Werner
 * 
 */
public interface ImportDataInterface {

	/**
	 * Read Method
	 * 
	 * @param dm
	 *            Data model
	 * @param in
	 *            stream to read data from
	 * @throws IOException
	 */
	public void readData(DataModel dm, InputStream in) throws IOException;

}
