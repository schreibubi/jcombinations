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
 * SymbolVisitorDiffVal class. Implements a visitor which generates a list of patch chunks which are used for: -
 * generating the two different source files by using the template engine and run the compiler on them - for relating
 * the resulting binary diff patch chunks back to the variables. The class is implements a Visitor for an Array or Hash
 * which contains Symbols
 */
public class SymbolVisitorDiffVal extends SymbolVisitor {

	private VArrayList<Chunk>	allPossibleChunks	= null;

	private VArrayList<Symbol>	removeSymbolList	= null;

	private VArrayList<Symbol>	replaceSymbolList	= null;

	private Mangle				mangle				= null;

	/**
	 * Constructor
	 * 
	 * @param allPossibleChunks
	 * @param removeSymbolList
	 * @param replaceSymbolList
	 * @param mangle
	 */
	public SymbolVisitorDiffVal(VArrayList<Chunk> allPossibleChunks, VArrayList<Symbol> removeSymbolList,
			VArrayList<Symbol> replaceSymbolList, Mangle mangle) {
		this.allPossibleChunks = allPossibleChunks;
		this.removeSymbolList = removeSymbolList;
		this.replaceSymbolList = replaceSymbolList;
		this.mangle = mangle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.SymbolVisitor#visit(org.schreibubi.symbol.SymbolDouble)
	 */
	@Override
	public void visit(SymbolDouble s) throws Exception {
		VArrayList<SymbolDouble> removeReplace = this.mangle.getNextPatchDoublePair(s);
		SymbolDouble remove = removeReplace.get(0);
		SymbolDouble replace = removeReplace.get(1);
		remove.setName(s.getName());
		replace.setName(s.getName());
		this.removeSymbolList.add(remove);
		this.replaceSymbolList.add(replace);

		SymbolDouble removeMangled = this.mangle.mangle(remove);
		SymbolDouble replaceMangled = this.mangle.mangle(replace);
		this.allPossibleChunks.add(new Chunk(s.getName(), removeMangled.getBinaryLength(), 0, 0, removeMangled
				.getBinaryRepresentation(), replaceMangled.getBinaryRepresentation()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.SymbolVisitor#visit(org.schreibubi.symbol.SymbolInteger)
	 */
	@Override
	public void visit(SymbolInteger s) throws Exception {
		VArrayList<SymbolInteger> removeAdd = this.mangle.getNextPatchIntegerPair(s);
		SymbolInteger remove = removeAdd.get(0);
		SymbolInteger replace = removeAdd.get(1);
		remove.setName(s.getName());
		replace.setName(s.getName());
		this.removeSymbolList.add(remove);
		this.replaceSymbolList.add(replace);

		SymbolInteger removeMangled = this.mangle.mangle(remove);
		SymbolInteger replaceMangled = this.mangle.mangle(replace);
		this.allPossibleChunks.add(new Chunk(s.getName(), removeMangled.getBinaryLength(), 0, 0, removeMangled
				.getBinaryRepresentation(), replaceMangled.getBinaryRepresentation()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.SymbolVisitor#visit(org.schreibubi.symbol.SymbolString)
	 */
	@Override
	public void visit(SymbolString s) throws Exception {
		VArrayList<SymbolString> removeReplace = this.mangle.getNextPatchStringPair(s);

		SymbolString remove = removeReplace.get(0);
		SymbolString replace = removeReplace.get(1);
		remove.setName(s.getName());
		replace.setName(s.getName());
		this.removeSymbolList.add(remove);
		this.replaceSymbolList.add(replace);
		SymbolString removeMangled = this.mangle.mangle(remove);
		SymbolString replaceMangled = this.mangle.mangle(replace);
		this.allPossibleChunks.add(new Chunk(s.getName(), removeMangled.getBinaryLength(), 0, 0, removeMangled
				.getBinaryRepresentation(), replaceMangled.getBinaryRepresentation()));
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
