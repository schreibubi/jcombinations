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
package org.schreibubi.JCombinationsTools.chunk;

import org.schreibubi.visitor.VArrayList;

/**
 * Patch class. Container class which stores the names of the reference and the file to be patched and the chunks which
 * have to be applied.
 * 
 * @author Jörg Werner
 */
public class Patch {

	private String				referenceName	= "", newName = "";

	private VArrayList<Chunk>	chunks;

	/**
	 * Constructor
	 * 
	 * @param referenceName
	 * @param newName
	 * @param chunks
	 * 
	 */
	public Patch(String referenceName, String newName, VArrayList<Chunk> chunks) {
		this.referenceName = referenceName;
		this.newName = newName;
		this.chunks = chunks;
	}

	/**
	 * @return Returns the chunks.
	 */
	public VArrayList<Chunk> getChunks() {
		return this.chunks;
	}

	/**
	 * @return Returns the newName.
	 */
	public String getNewName() {
		return this.newName;
	}

	/**
	 * @return Returns the referenceName.
	 */
	public String getReferenceName() {
		return this.referenceName;
	}

	/**
	 * @param chunks
	 *            The chunks to set.
	 */
	public void setChunks(VArrayList<Chunk> chunks) {
		this.chunks = chunks;
	}

	/**
	 * @param newName
	 *            The newName to set.
	 */
	public void setNewName(String newName) {
		this.newName = newName;
	}

	/**
	 * @param referenceName
	 *            The referenceName to set.
	 */
	public void setReferenceName(String referenceName) {
		this.referenceName = referenceName;
	}

}
