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

import java.util.Collections;

import org.schreibubi.JCombinationsTools.util.Helper;
import org.schreibubi.visitor.Host;
import org.schreibubi.visitor.VArrayList;
import org.schreibubi.visitor.Visitor;


/**
 * Chunk class contains all the information of one chunk of a whole patch
 */
public class Chunk implements Host<Chunk> {

	private String	name	= "";

	private int		start, stop, before, after;

	private VArrayList<Integer>	remove, replace;

	/**
	 * Constructor of a chunk.
	 * 
	 * @param start
	 * @param stop
	 *            Start and stop position of the chunk
	 * @param remove
	 *            List of binary values which are removed
	 * @param replace
	 *            List of binary values which are replaced
	 * @throws Exception
	 */
	public Chunk(int start, int stop, int before, int after, VArrayList<Integer> remove, VArrayList<Integer> replace)
			throws Exception {
		this.remove = remove;
		this.replace = replace;
		this.start = start;
		this.stop = stop;
		this.before = before;
		this.after = after;
		if ((remove.size() < (stop - start + before + after + 1))
				| (replace.size() < (stop - start + before + after + 1)) | (replace.size() != remove.size())) {
			System.out.println("Remove:");
			Helper.printHexLine(remove);
			System.out.println("Replace:");
			Helper.printHexLine(replace);

			throw new Exception("Length of chunk is inconsistent with data. Size: "
					+ (stop - start + before + after + 1) + " remove size: " + remove.size() + " replace size: "
					+ replace.size());
		}
	}

	/**
	 * Constructor of a chunk.
	 * 
	 * @param name
	 *            Name of Chunk
	 * @param len
	 *            Length of the Chunk
	 * @param remove
	 *            List of binary values which are removed
	 * @param replace
	 *            List of binary values which are replaced
	 * @throws Exception
	 */
	public Chunk(String name, int len, int before, int after, VArrayList<Integer> remove, VArrayList<Integer> replace)
			throws Exception {
		this.name = name;
		this.remove = remove;
		this.replace = replace;
		this.start = 0;
		this.stop = len - 1;
		this.before = before;
		this.after = after;
		if ((remove.size() < (this.stop - this.start + this.before + this.after + 1))
				| (replace.size() < (this.stop - this.start + this.before + this.after + 1))
				| (replace.size() != remove.size())) {
			System.out.println("Remove:");
			Helper.printHexLine(remove);
			System.out.println("Replace:");
			Helper.printHexLine(replace);
			throw new Exception("Length of chunk is inconsistent with data. Size: "
					+ (this.stop - this.start + this.before + this.after + 1) + " remove size: " + remove.size()
					+ " replace size: " + replace.size());

		}
	}

	/**
	 * Constructor of a chunk.
	 * 
	 * @param pos
	 *            Start and stop position of the chunk
	 * @param remove
	 *            List of binary values which are removed
	 * @param replace
	 *            List of binary values which are replaced
	 * @throws Exception
	 */
	public Chunk(VArrayList<Integer> pos, VArrayList<Integer> remove, VArrayList<Integer> replace) throws Exception {
		this.remove = remove;
		this.replace = replace;
		this.start = pos.get(0).intValue();
		this.stop = pos.get(1).intValue();
		if (pos.size() == 4) {
			this.before = pos.get(2);
			this.after = pos.get(3);
		} else {
			this.before = 0;
			this.after = 0;
		}
		if ((remove.size() < (this.stop - this.start + this.before + this.after + 1))
				| (replace.size() < (this.stop - this.start + this.before + this.after + 1))
				| (replace.size() != remove.size())) {
			System.out.println("Remove:");
			Helper.printHexLine(remove);
			System.out.println("Replace:");
			Helper.printHexLine(replace);
			throw new Exception("Length of chunk is inconsistent with data. Size: "
					+ (this.stop - this.start + this.before + this.after + 1) + " remove size: " + remove.size()
					+ " replace size: " + replace.size());
		}
	}

	/**
	 * @param v
	 *            Visitor
	 * @throws Exception
	 */
	public void accept(Visitor<Chunk> v) throws Exception {
		v.visit(this);
	}

	/**
	 * Applies the chunk to a binary.
	 * 
	 * @param binary
	 *            Binary array to which the chunk is applied
	 * @return True if the chunk applied, False if it failed
	 */
	public boolean apply(byte[] binary) {
		if ((binary.length > this.start) & (binary.length >= this.stop)) {
			int j = 0;
			for (int i = this.start; i <= this.stop; i++) {
				byte rem, rep;
				rem = convertIntToByte(this.remove.get(before + j).intValue());
				rep = convertIntToByte(this.replace.get(before + j).intValue());
				if (binary[i] == rem)
					binary[i] = rep;
				else
					return false;
				j++;
			}
			return true;
		} else
			return false;
	}

	/**
	 * Convert an integer to byte
	 * 
	 * @param i
	 *            integer to convert
	 * @return converted byte
	 */
	public byte convertIntToByte(int i) {
		if (i > 127)
			return (byte) (i - 256);
		else
			return (byte) i;
	}

	/**
	 * Compare two chunks if they are equal
	 * 
	 * @param o
	 *            other chunk to compare (possibleChunks)
	 * @param exactOnly
	 *            if true do only exact matching and no partial matching
	 * @return true if they are equal
	 */
	public boolean equivalentTo(Chunk o) {
		int remMatch = Collections.indexOfSubList(this.remove, o.getRemove());
		int repMatch = Collections.indexOfSubList(this.replace, o.getReplace());
		if ((remMatch == repMatch) & (remMatch > -1)) {
			this.remove = o.getRemove();
			this.replace = o.getReplace();
			this.start = this.start - this.before + remMatch;
			this.stop = this.start + this.remove.size() - 1;
			this.before = 0;
			this.after = 0;
			return true;
		} else
			return false;
	}

	public int getAfter() {
		return after;
	}

	public int getBefore() {
		return before;
	}

	/**
	 * Get name
	 * 
	 * @return name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Get byte values which have to be removed
	 * 
	 * @return byte values to remove
	 */
	public VArrayList<Integer> getRemove() {
		return this.remove;
	}

	/**
	 * Get byte values which will replace the removed byte values
	 * 
	 * @return replacing byte values
	 */
	public VArrayList<Integer> getReplace() {
		return this.replace;
	}

	/**
	 * Get start of diff
	 * 
	 * @return start of diff
	 */
	public int getStart() {
		return this.start;
	}

	/**
	 * Get end of diff
	 * 
	 * @return end of diff
	 */
	public int getStop() {
		return this.stop;
	}

	/**
	 * Set name
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
}
