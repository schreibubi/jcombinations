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
 * SymbolVisitorMax class. Implements a visitor for Symbols which extracts the biggest value (or longest string for
 * SymbolString) found for a certain symbol name. The biggest value is saved as a symbol in maxHash.
 */
public class SymbolVisitorMax extends SymbolVisitor {

	/**
	 * maxHash contains the biggest value for each symbol found up to date
	 */
	private VHashMap<Symbol>	maxHash	= null;

	/**
	 * Constructor
	 */
	public SymbolVisitorMax() {
		super();
	}

	/**
	 * Constructor
	 * 
	 * @param maxHash
	 *            is passed in by reference and contains the biggst value for each symbol found up-to-date
	 */
	public SymbolVisitorMax(VHashMap<Symbol> maxHash) {
		super();
		this.maxHash = maxHash;
	}

	/**
	 * @return Returns the maxHash.
	 */
	public VHashMap<Symbol> getMaxHash() {
		return this.maxHash;
	}

	/**
	 * @param maxHash
	 *            The maxHash to set.
	 */
	public void setMaxHash(VHashMap<Symbol> maxHash) {
		this.maxHash = maxHash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.SymbolVisitor#visit(org.schreibubi.symbol.SymbolDouble)
	 */
	@Override
	public void visit(SymbolDouble s) throws Exception {
		Symbol o = this.maxHash.get(s.getName());
		if (o != null) { // already found in the HashTable
			if (o instanceof SymbolDouble) { // both are of the same type
				SymbolDouble oi = (SymbolDouble) o;
				if (s.getValue() > oi.getValue()) {
					// older value
					this.maxHash.put(s.getName(), s);
				}
			} else {
				throw new Exception("variable type is inconsistent for variable " + s.getName() + ": "
						+ s.getTypeString() + "!=" + o.getTypeString());
			}
		} else {
			this.maxHash.put(s.getName(), s);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.SymbolVisitor#visit(org.schreibubi.symbol.SymbolInteger)
	 */
	@Override
	public void visit(SymbolInteger s) throws Exception {
		Symbol o = this.maxHash.get(s.getName());
		if (o != null) { // already found in the HashTable
			if (o instanceof SymbolInteger) { // both are of the same type
				SymbolInteger oi = (SymbolInteger) o;
				if (s.getValue() > oi.getValue()) {
					// older value
					this.maxHash.put(s.getName(), s);
				}
			} else {
				throw new Exception("variable type is inconsistent for variable " + s.getName() + ": "
						+ s.getTypeString() + "!=" + o.getTypeString());
			}
		} else {
			this.maxHash.put(s.getName(), s);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.SymbolVisitor#visit(org.schreibubi.symbol.SymbolString)
	 */
	@Override
	public void visit(SymbolString s) throws Exception {
		Symbol o = this.maxHash.get(s.getName());
		if (o != null) { // already found in the HashTable
			if (o instanceof SymbolString) { // both are of the same type
				SymbolString oi = (SymbolString) o;
				if (s.getValue().length() > oi.getValue().length()) {
					// Value bigger than older value
					this.maxHash.put(s.getName(), s);
				}
			} else {
				throw new Exception("variable type is inconsistent for variable " + s.getName() + ": "
						+ s.getTypeString() + "!=" + o.getTypeString());
			}
		} else {
			this.maxHash.put(s.getName(), s);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.SymbolVisitor#visit(org.schreibubi.visitor.VArrayList)
	 */
	@Override
	public void visit(VArrayList<Symbol> s) throws Exception {
		for (Symbol sym : s) {
			sym.accept(this);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.visitor.Visitor#visit(org.schreibubi.visitor.VArrayListMultimap)
	 */
	public void visit(VArrayListMultimap<Symbol> s) throws Exception {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.SymbolVisitor#visit(org.schreibubi.visitor.VHashMap)
	 */
	@Override
	public void visit(VHashMap<Symbol> s) throws Exception {
		for (Symbol sym : s.values()) {
			sym.accept(this);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.SymbolVisitor#visit(org.schreibubi.visitor.VLinkedHashMap)
	 */
	@Override
	public void visit(VLinkedHashMap<Symbol> s) throws Exception {
		for (Symbol sym : s.values()) {
			sym.accept(this);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.SymbolVisitor#visit(org.schreibubi.visitor.VTreeMap)
	 */
	@Override
	public void visit(VTreeMap<Symbol> s) throws Exception {
		for (Symbol sym : s.values()) {
			sym.accept(this);
		}
	}

}
