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

import java.io.PrintWriter;

import org.schreibubi.JCombinationsTools.util.Helper;
import org.schreibubi.visitor.VArrayList;
import org.schreibubi.visitor.VArrayListMultimap;
import org.schreibubi.visitor.VHashMap;
import org.schreibubi.visitor.VLinkedHashMap;
import org.schreibubi.visitor.VTreeMap;
import org.schreibubi.visitor.Visitor;

/**
 * ChunkVisitorDiffTemplate is a visitor for chunks which generates a unified
 * diff output from the visited chunks.
 */
public class ChunkVisitorDiffTemplate implements Visitor<Chunk> {

	PrintWriter pw = null;

	/**
	 * Constructor
	 * 
	 * @param pw
	 *            PrintWriter to output to
	 */
	public ChunkVisitorDiffTemplate(PrintWriter pw) {
		this.pw = pw;
	}

	public void visit(Chunk s) {
		this.pw.println("@ " + s.getStart() + ", " + s.getStop());
		this.pw.println("-" + Helper.printHexLine(s.getRemove()));
		this.pw.println("+$" + s.getName() + "$%HEX%");
	}

	public void visit(VArrayList<Chunk> s) throws Exception {
		for (Chunk c : s) {
			c.accept(this);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.schreibubi.visitor.Visitor#visit(org.schreibubi.visitor.
	 * VArrayListMultimap)
	 */
	public void visit(VArrayListMultimap<Chunk> s) throws Exception {
		// TODO Auto-generated method stub

	}

	public void visit(VHashMap<Chunk> s) throws Exception {
		for (Chunk c : s.values()) {
			c.accept(this);
		}
	}

	public void visit(VLinkedHashMap<Chunk> s) throws Exception {
		for (Chunk c : s.values()) {
			c.accept(this);
		}
	}

	public void visit(VTreeMap<Chunk> s) throws Exception {
		for (Chunk c : s.values()) {
			c.accept(this);
		}
	}

}
