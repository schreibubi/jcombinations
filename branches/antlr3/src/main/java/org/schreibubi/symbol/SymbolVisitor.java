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

import org.schreibubi.visitor.VArrayList;
import org.schreibubi.visitor.VHashMap;
import org.schreibubi.visitor.VLinkedHashMap;
import org.schreibubi.visitor.VTreeMap;
import org.schreibubi.visitor.Visitor;

/**
 * The super class for all symbol visitors.
 * 
 * @author Jörg Werner
 */
public abstract class SymbolVisitor implements Visitor<Symbol> {

	/**
	 * Visit Symbol, dispatch routine
	 * 
	 * @param s
	 *            Symbol
	 * @throws Exception
	 */
	public void visit(Symbol s) throws Exception {
		// System.out.println("Over dispatch!");
		if (s instanceof SymbolDouble)
			this.visit((SymbolDouble) s);
		else if (s instanceof SymbolInteger)
			this.visit((SymbolInteger) s);
		else if (s instanceof SymbolString)
			this.visit((SymbolString) s);
	}

	/**
	 * Visit SymbolDouble
	 * 
	 * @param s
	 *            SymbolDouble
	 * @throws Exception
	 */
	public abstract void visit(SymbolDouble s) throws Exception;

	/**
	 * Visit SymbolInteger
	 * 
	 * @param s
	 *            SymbolInteger
	 * @throws Exception
	 */
	public abstract void visit(SymbolInteger s) throws Exception;

	/**
	 * Visit SymbolString
	 * 
	 * @param s
	 *            SymbolString
	 * @throws Exception
	 */
	public abstract void visit(SymbolString s) throws Exception;

	/**
	 * Visit VArrayList of Symbol
	 * 
	 * @param s
	 *            VArrayList of Symbol
	 * @throws Exception
	 */
	public abstract void visit(VArrayList<Symbol> s) throws Exception;

	/**
	 * Visit VHashMap of Symbol
	 * 
	 * @param s
	 *            VHashMap of Symbol
	 * @throws Exception
	 */
	public abstract void visit(VHashMap<Symbol> s) throws Exception;

	/**
	 * Visit VLinkedHashMap of Symbol
	 * 
	 * @param s
	 *            VLinkedHashMap of Symbol
	 * @throws Exception
	 */
	public abstract void visit(VLinkedHashMap<Symbol> s) throws Exception;

	/**
	 * Visit VTreeMap of Symbol
	 * 
	 * @param s
	 *            VTreeMap of Symbol
	 * @throws Exception
	 */
	public abstract void visit(VTreeMap<Symbol> s) throws Exception;

}
