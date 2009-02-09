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
import org.schreibubi.visitor.Visitor;

/**
 * Class for storing Strings.
 * 
 * @author Jörg Werner
 * 
 */
public class SymbolString extends Symbol {
	private static String	STANDARD_FORMAT	= "";

	/**
	 * Formats a hex number
	 * 
	 * @param i
	 *            number to format
	 * @param len
	 *            length of resulting string, is padded with zeros if necessary
	 * @return formatted string
	 */
	private static String formatHexNumber(int i, int len) {
		String res = Integer.toHexString(i);
		int hexlen = res.length();
		if (hexlen < len)
			for (i = 0; i < len - hexlen; i++)
				res = "0" + res;
		return res;
	}

	private String	value	= "";

	/**
	 * Constructor
	 * 
	 * @param value
	 *            String for this symbol
	 */
	public SymbolString(String value) {
		super("", SymType.STRING);
		setValue(value);
	}

	/**
	 * Constructor
	 * 
	 * @param name
	 *            name for this symbol
	 * @param value
	 *            String for this symbol
	 */
	public SymbolString(String name, String value) {
		super(name, SymType.STRING);
		setValue(value);
	}

	/**
	 * Copy constructor
	 * 
	 * @param s
	 *            Symbol
	 */
	public SymbolString(SymbolString s) {
		super(s.getName(), SymType.STRING);
		setValue(s.getValue());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#abs()
	 */
	@Override
	public Symbol abs() throws Exception {
		throw new Exception("not possible for string");
	}

	/**
	 * @param v
	 * @throws Exception
	 */
	public void accept(Visitor<Symbol> v) throws Exception {
		v.visit(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#acos()
	 */
	@Override
	public Symbol acos() throws Exception {
		throw new Exception("not possible for string");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#add(org.schreibubi.symbol.Symbol)
	 */
	@Override
	public Symbol add(Symbol s) throws Exception {
		if (s instanceof SymbolString) {
			String o = ((SymbolString) s).getValue();
			this.value = this.value + o;
			return this;
		} else
			throw new Exception("wrong variable type");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#and(org.schreibubi.symbol.Symbol)
	 */
	@Override
	public Symbol and(Symbol s) throws Exception {
		throw new Exception("not possible for string");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#asin()
	 */
	@Override
	public Symbol asin() throws Exception {
		throw new Exception("not possible for string");
	}

	/**
	 * Assign symbol
	 * 
	 * @param s
	 *            Symbol
	 */
	public void assign(SymbolString s) {
		setValue(s.getValue());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#atan()
	 */
	@Override
	public Symbol atan() throws Exception {
		throw new Exception("not possible for string");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#clone()
	 */
	@Override
	public Symbol clone() {
		return new SymbolString(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#convert(org.schreibubi.symbol.Symbol)
	 */
	@Override
	public Symbol convert(Symbol s) throws Exception {
		return s.convertToString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#convertToFloat()
	 */
	@Override
	public SymbolDouble convertToDouble() throws Exception {
		return new SymbolDouble(getName(), Float.parseFloat(this.value));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#convertToInt()
	 */
	@Override
	public SymbolInteger convertToInt() throws Exception {
		return new SymbolInteger(getName(), Integer.parseInt(this.value));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#convertToString()
	 */
	@Override
	public SymbolString convertToString() throws Exception {
		return new SymbolString(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#cos()
	 */
	@Override
	public Symbol cos() throws Exception {
		throw new Exception("not possible for string");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#div(org.schreibubi.symbol.Symbol)
	 */
	@Override
	public Symbol div(Symbol s) throws Exception {
		throw new Exception("not possible for string");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (!(obj instanceof SymbolString))
			return false;
		if (!super.equals(obj))
			return false;
		SymbolString that = (SymbolString) obj;
		if (!(this.value.equals(that.value)))
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#exp()
	 */
	@Override
	public Symbol exp() throws Exception {
		throw new Exception("not possible for string");
	}

	@Override
	public boolean ge(Symbol s) throws Exception {
		throw new Exception("not possible for string");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#getBinaryLength()
	 */
	@Override
	public int getBinaryLength() {
		return getValue().length();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#getBinaryRepresentation()
	 */
	@Override
	public VArrayList<Integer> getBinaryRepresentation() {
		String val = getValue();
		VArrayList<Integer> b = new VArrayList<Integer>();
		for (int i = 0; i < val.length(); i++)
			b.add(new Integer(val.charAt(i)));
		return b;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#getBinaryRepresentation(int)
	 */
	@Override
	public VArrayList<Integer> getBinaryRepresentation(int len) {
		String val = getValue();
		VArrayList<Integer> b = new VArrayList<Integer>();
		for (int i = 0; i < len; i++)
			if (i < val.length())
				b.add(new Integer(val.charAt(i)));
			else
				b.add(0);
		return b;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#getUnit()
	 */
	@Override
	public Unit getUnit() throws Exception {
		throw new Exception("not possible for string");
	}

	/**
	 * Returns string value stored in this class
	 * 
	 * @return string value stored
	 */
	public String getValue() {
		return this.value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#getValueString()
	 */
	@Override
	public String getValueString() {
		return getValueString(SymbolString.STANDARD_FORMAT);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#getValueString(java.lang.String)
	 */
	@Override
	public String getValueString(String format) {
		String res = "";
		for (int i = 0; i < this.value.length(); i++) {
			char c = this.value.charAt(i);
			if ((c < 0x20) | (c > 0x7E))
				res += "\\u" + formatHexNumber(c, 4);
			else
				res += c;
		}
		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#getValueUnitString()
	 */
	@Override
	public String getValueUnitString() {
		return getValueUnitString(SymbolString.STANDARD_FORMAT);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#getValueUnitString(java.lang.String)
	 */
	@Override
	public String getValueUnitString(String format) {
		return this.value;
	}

	@Override
	public boolean gt(Symbol s) throws Exception {
		throw new Exception("not possible for string");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#hashCode()
	 */
	@Override
	public int hashCode() {
		int hc = 17;
		int hashMultiplier = 59;
		hc = hc * hashMultiplier + super.hashCode();
		hc = hc * hashMultiplier + value.hashCode();
		return hc;
	}

	@Override
	public boolean le(Symbol s) throws Exception {
		throw new Exception("not possible for string");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#ln()
	 */
	@Override
	public Symbol ln() throws Exception {
		throw new Exception("not possible for string");
	}

	@Override
	public boolean lt(Symbol s) throws Exception {
		throw new Exception("not possible for string");
	}

	@Override
	public Symbol max(Symbol s) throws Exception {
		throw new Exception("not possible for string");
	}

	@Override
	public Symbol min(Symbol s) throws Exception {
		throw new Exception("not possible for string");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#mul(org.schreibubi.symbol.Symbol)
	 */
	@Override
	public Symbol mul(Symbol s) throws Exception {
		throw new Exception("not possible for string");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#not()
	 */
	@Override
	public Symbol not() throws Exception {
		throw new Exception("not possible for string");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#or(org.schreibubi.symbol.Symbol)
	 */
	@Override
	public Symbol or(Symbol s) throws Exception {
		throw new Exception("not possible for string");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#postDec()
	 */
	@Override
	public Symbol postDec() throws Exception {
		throw new Exception("not possible for string");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#postInc()
	 */
	@Override
	public Symbol postInc() throws Exception {
		throw new Exception("not possible for string");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#pow(org.schreibubi.symbol.Symbol)
	 */
	@Override
	public Symbol pow(Symbol s) throws Exception {
		throw new Exception("not possible for string");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#preDec()
	 */
	@Override
	public Symbol preDec() throws Exception {
		throw new Exception("not possible for string");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#preInc()
	 */
	@Override
	public Symbol preInc() throws Exception {
		throw new Exception("not possible for string");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#setUnit(java.lang.String)
	 */
	@Override
	public void setUnit(Unit unit) throws Exception {
		throw new Exception("not possible for string");
	}

	/**
	 * Sets string value stored in this class
	 * 
	 * @param value
	 *            string value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#sin()
	 */
	@Override
	public Symbol sin() throws Exception {
		throw new Exception("not possible for string");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#sqrt()
	 */
	@Override
	public Symbol sqrt() throws Exception {
		throw new Exception("not possible for string");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#sub(org.schreibubi.symbol.Symbol)
	 */
	@Override
	public Symbol sub(Symbol s) throws Exception {
		throw new Exception("not possible for string");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#tan()
	 */
	@Override
	public Symbol tan() throws Exception {
		throw new Exception("not possible for string");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getName() + "=\"" + getValueString() + "\"";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#uminus()
	 */
	@Override
	public Symbol uminus() throws Exception {
		throw new Exception("not possible for string");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#uplus()
	 */
	@Override
	public Symbol uplus() throws Exception {
		throw new Exception("not possible for string");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#xor(org.schreibubi.symbol.Symbol)
	 */
	@Override
	public Symbol xor(Symbol s) throws Exception {
		throw new Exception("not possible for string");
	}

}
