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
package org.schreibubi.symbol;

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.ListIterator;

import org.schreibubi.visitor.VArrayList;
import org.schreibubi.visitor.VArrayListMultimap;
import org.schreibubi.visitor.VHashMap;
import org.schreibubi.visitor.VLinkedHashMap;
import org.schreibubi.visitor.VTreeMap;

/**
 * SymbolVisitorPrint calls toString on each symbol visited.
 */
public class SymbolVisitorPrint extends SymbolVisitor {
	PrintWriter pw = null;

	/**
	 * Constructor
	 * 
	 * @param pw
	 *            PrintWriter to write to
	 */
	public SymbolVisitorPrint(PrintWriter pw) {
		this.pw = pw;
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
		this.pw.print(s.toString());
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
		this.pw.print(s.toString());
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
		this.pw.print(s.toString());
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
			i.next().accept(this);
			if (i.hasNext()) {
				this.pw.print(" ");
			}
		}
		this.pw.println();
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
			i.next().accept(this);
			if (i.hasNext()) {
				pw.print(" ");
			}
		}
		this.pw.println();
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
			i.next().accept(this);
			if (i.hasNext()) {
				this.pw.print(" ");
			}
		}
		this.pw.println();
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
			i.next().accept(this);
			if (i.hasNext()) {
				this.pw.print(" ");
			}
		}
		this.pw.println();
	}

}
