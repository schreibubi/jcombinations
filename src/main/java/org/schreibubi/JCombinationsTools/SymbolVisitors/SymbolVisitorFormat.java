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

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.ListIterator;

import org.schreibubi.JCombinationsTools.util.Helper;
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
 * SymbolVisitorFormat outputs a symbol formatted as given by the format string.
 */
public class SymbolVisitorFormat extends SymbolVisitor {

	String format = "";

	PrintWriter pw = null;

	/**
	 * Constructor
	 * 
	 * @param format
	 *            format-string
	 * @param pw
	 *            PrintWriter to output to
	 */
	public SymbolVisitorFormat(String format, PrintWriter pw) {
		this.format = format;
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
		if (this.format.contentEquals("HEX")) {
			this.pw.print(Integer.toHexString((int) s.getValue()));
		} else if (this.format.contentEquals("NOUNIT")) {
			this.pw.print(s.getValueString());
		} else if (this.format != "") {
			this.pw.print(s.getValueUnitString(this.format));
		} else {
			this.pw.print(s.getValueUnitString("#0.0##"));
		}
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
		if (this.format.contentEquals("HEX")) {
			this.pw.print(Integer.toHexString(s.getValue()));
		} else if (this.format.contentEquals("NOUNIT")) {
			this.pw.print(s.getValueString());
		} else if (this.format != "") {
			this.pw.print(s.getValueUnitString(this.format));
		} else {
			this.pw.print(s.getValueUnitString("###"));
		}
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
		if (this.format.contentEquals("HEX")) {
			this.pw.print(Helper.printHexLine(s.getBinaryRepresentation()));
		} else if (this.format.contentEquals("LOWERCASE")) {
			this.pw.print(s.getValueString().toLowerCase());
		} else if (this.format.contentEquals("UPPERCASE")) {
			this.pw.print(s.getValueString().toUpperCase());
		} else {
			this.pw.print(s.getValueString());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.schreibubi.symbol.SymbolVisitor#visit(org.schreibubi.visitor.VArrayList )
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
	 * org.schreibubi.symbol.SymbolVisitor#visit(org.schreibubi.visitor.VHashMap )
	 */
	@Override
	public void visit(VHashMap<Symbol> s) throws Exception {
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
	 * org.schreibubi.symbol.SymbolVisitor#visit(org.schreibubi.visitor.VTreeMap )
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
