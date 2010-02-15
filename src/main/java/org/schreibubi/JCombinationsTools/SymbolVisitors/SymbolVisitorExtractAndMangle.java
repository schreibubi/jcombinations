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
package org.schreibubi.JCombinationsTools.SymbolVisitors;

import java.util.Iterator;
import java.util.ListIterator;

import org.schreibubi.JCombinationsTools.chunk.Chunk;
import org.schreibubi.JCombinationsTools.mangle.Mangle;
import org.schreibubi.symbol.Symbol;
import org.schreibubi.symbol.SymbolDouble;
import org.schreibubi.symbol.SymbolInteger;
import org.schreibubi.symbol.SymbolString;
import org.schreibubi.symbol.SymbolVisitor;
import org.schreibubi.visitor.VArrayList;
import org.schreibubi.visitor.VArrayListMultimap;
import org.schreibubi.visitor.VHashMap;
import org.schreibubi.visitor.VLinkedHashMap;
import org.schreibubi.visitor.VTreeMap;

/**
 * SymbolVisitorPrint calls toString on each symbol visited.
 * 
 * @author Jörg Werner
 */
public class SymbolVisitorExtractAndMangle extends SymbolVisitor {
	VArrayList<Chunk> chunks = null;

	Mangle mangle = null;

	/**
	 * @param chunks
	 * @param mangle
	 */
	public SymbolVisitorExtractAndMangle(VArrayList<Chunk> chunks, Mangle mangle) {
		this.chunks = chunks;
		this.mangle = mangle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.schreibubi.symbol.SymbolVisitor#visit(org.schreibubi.symbol.SymbolDouble
	 * )
	 */
	@Override
	public void visit(SymbolDouble s) {
		SymbolDouble n = this.mangle.mangle(s);
		s.assign(n);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.schreibubi.symbol.SymbolVisitor#visit(org.schreibubi.symbol.SymbolInteger
	 * )
	 */
	@Override
	public void visit(SymbolInteger s) {
		SymbolInteger n = this.mangle.mangle(s);
		s.assign(n);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.schreibubi.symbol.SymbolVisitor#visit(org.schreibubi.symbol.SymbolString
	 * )
	 */
	@Override
	public void visit(SymbolString s) {
		SymbolString n = this.mangle.mangle(s);
		s.assign(n);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.schreibubi.symbol.SymbolVisitor#visit(org.schreibubi.visitor.VArrayList
	 * )
	 */
	@Override
	public void visit(VArrayList<Symbol> s) throws Exception {
		for (ListIterator<Symbol> i = s.listIterator(); i.hasNext();) {
			Symbol sym = i.next();
			boolean found = false;
			for (ListIterator<Chunk> iter = this.chunks.listIterator(); iter
					.hasNext();) {
				Chunk c = iter.next();
				if (c.getName().equals(sym.getName())) {
					found = true;
				}
			}
			if (found) {
				sym.accept(this);
			} else {
				i.remove();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.schreibubi.visitor.Visitor#visit(org.schreibubi.visitor.
	 * VArrayListMultimap)
	 */
	public void visit(VArrayListMultimap<Symbol> s) throws Exception {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.schreibubi.symbol.SymbolVisitor#visit(org.schreibubi.visitor.VHashMap
	 * )
	 */
	@Override
	public void visit(VHashMap<Symbol> s) throws Exception {
		for (Iterator<Symbol> i = s.values().iterator(); i.hasNext();) {
			Symbol sym = i.next();
			boolean found = false;
			for (ListIterator<Chunk> iter = this.chunks.listIterator(); iter
					.hasNext();) {
				Chunk c = iter.next();
				if (c.getName().equals(sym.getName())) {
					found = true;
				}
			}
			if (found) {
				sym.accept(this);
			} else {
				i.remove();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.schreibubi.symbol.SymbolVisitor#visit(org.schreibubi.visitor.
	 * VLinkedHashMap)
	 */
	@Override
	public void visit(VLinkedHashMap<Symbol> s) throws Exception {
		for (Iterator<Symbol> i = s.values().iterator(); i.hasNext();) {
			Symbol sym = i.next();
			boolean found = false;
			for (ListIterator<Chunk> iter = this.chunks.listIterator(); iter
					.hasNext();) {
				Chunk c = iter.next();
				if (c.getName().equals(sym.getName())) {
					found = true;
				}
			}
			if (found) {
				sym.accept(this);
			} else {
				i.remove();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.schreibubi.symbol.SymbolVisitor#visit(org.schreibubi.visitor.VTreeMap
	 * )
	 */
	@Override
	public void visit(VTreeMap<Symbol> s) throws Exception {
		for (Iterator<Symbol> i = s.values().iterator(); i.hasNext();) {
			Symbol sym = i.next();
			boolean found = false;
			for (ListIterator<Chunk> iter = this.chunks.listIterator(); iter
					.hasNext();) {
				Chunk c = iter.next();
				if (c.getName().equals(sym.getName())) {
					found = true;
				}
			}
			if (found) {
				sym.accept(this);
			} else {
				i.remove();
			}
		}
	}

}
