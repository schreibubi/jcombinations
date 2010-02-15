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
package org.schreibubi.JCombinationsTools.mangle;

import org.schreibubi.symbol.SymbolDouble;
import org.schreibubi.symbol.SymbolInteger;
import org.schreibubi.symbol.SymbolString;
import org.schreibubi.visitor.VArrayList;

/**
 * Implementation of the mangle class for MRS-Mangling
 * 
 * @author Jörg Werner
 * 
 */
public class MangleMRS extends Mangle {

	private int opcode = 0;

	private int str_incr = 0, int_incr = 0;

	/**
	 * Constructor
	 * 
	 * @param opcode
	 *            to use for command
	 */
	public MangleMRS(int opcode) {
		this.opcode = opcode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.JCombinationsTools.mangle.Mangle#diffLength()
	 */
	@Override
	public int diffLength() {
		return 4;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.schreibubi.JCombinationsTools.mangle.Mangle#getNextPatchDoublePair
	 * (org.schreibubi.symbol.SymbolDouble)
	 */
	@Override
	public VArrayList<SymbolDouble> getNextPatchDoublePair(SymbolDouble s) {
		SymbolDouble doubRemove = null, doubAdd = null;
		doubRemove = new SymbolDouble(this.int_incr);
		doubAdd = new SymbolDouble((int) Math.pow(2, 16) - 1 - this.int_incr);
		this.int_incr++;
		VArrayList<SymbolDouble> res = new VArrayList<SymbolDouble>();
		res.add(new SymbolDouble(doubRemove));
		res.add(new SymbolDouble(doubAdd));
		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.schreibubi.JCombinationsTools.mangle.Mangle#getNextPatchIntegerPair
	 * (org.schreibubi.symbol.SymbolInteger)
	 */
	@Override
	public VArrayList<SymbolInteger> getNextPatchIntegerPair(SymbolInteger s) {
		SymbolInteger intRemove = null, intAdd = null;
		intRemove = new SymbolInteger(this.int_incr);
		intAdd = new SymbolInteger((int) Math.pow(2, 16) - 1 - this.int_incr);
		this.int_incr++;
		VArrayList<SymbolInteger> res = new VArrayList<SymbolInteger>();
		res.add(new SymbolInteger(intRemove));
		res.add(new SymbolInteger(intAdd));
		return res;
	}

	/**
	 * Generates the byte list of a String with length length. The integer value
	 * i is represented as a string with character from A-M (inverted false) or
	 * N-Z (inverted true). Example: i=2, length=3, inverted=false: AAC Example:
	 * i=2, length=3, inverted=true: ZZX
	 * 
	 * @return an VArray which contains the char values
	 */
	@Override
	public VArrayList<SymbolString> getNextPatchStringPair(SymbolString s) {
		String strRemove = "", strAdd = "";
		int length = s.getValue().length();
		int i = this.str_incr;
		for (int j = 0; j < length; j++) {
			int rem = i % 13;
			strRemove = (char) (('A') + rem) + strRemove;
			strAdd = (char) (('Z') - rem) + strAdd;
			i = i / 13;
		}
		this.str_incr++;
		VArrayList<SymbolString> res = new VArrayList<SymbolString>();
		res.add(new SymbolString(strRemove));
		res.add(new SymbolString(strAdd));
		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.schreibubi.JCombinationsTools.mangle.Mangle#mangle(org.schreibubi
	 * .symbol.SymbolDouble)
	 */
	@Override
	public SymbolDouble mangle(SymbolDouble s) {
		int val = (int) s.getValue();
		val = val | this.opcode;
		SymbolDouble n = new SymbolDouble(s);
		n.setValue(val);
		return n;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.schreibubi.JCombinationsTools.mangle.Mangle#mangle(org.schreibubi
	 * .symbol.SymbolInteger)
	 */
	@Override
	public SymbolInteger mangle(SymbolInteger s) {
		int val = s.getValue();
		val = val | this.opcode;
		SymbolInteger n = new SymbolInteger(s);
		n.setValue(val);
		return n;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.schreibubi.JCombinationsTools.mangle.Mangle#mangle(org.schreibubi
	 * .symbol.SymbolString)
	 */
	@Override
	public SymbolString mangle(SymbolString s) {
		String val = s.getValue();
		int modulo = (val.length() % 4);
		if (modulo > 0) {
			int fillCount = 4 - modulo;
			char c = 0;
			for (int i = 0; i < fillCount; i++) {
				val = val + c;
			}
		}
		SymbolString n = new SymbolString(s);
		n.setValue(val);
		return n;
	}

}
