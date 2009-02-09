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

import org.schreibubi.visitor.Host;
import org.schreibubi.visitor.VArrayList;

/**
 * Symbol class. The parent class for all symbols. A symbol has a name and supports the visitor pattern
 * 
 * @author Jörg Werner
 */
public abstract class Symbol implements Host<Symbol>, Cloneable {
	/**
	 * Symboltype enum
	 */
	public enum SymType {
		/**
		 * String symbol
		 */
		STRING,
		/**
		 * Double symbol
		 */
		DOUBLE,
		/**
		 * Integer symbol
		 */
		INTEGER
	}

	/**
	 * Create a symbol of type
	 * 
	 * @param type
	 *            type of symbol
	 * @return Symbol
	 */
	public static Symbol createSymbolFactory(int type) {
		if (type == SymType.STRING.ordinal())
			return new SymbolString("");
		else if (type == SymType.DOUBLE.ordinal())
			return new SymbolDouble(0.0);
		else if (type == SymType.INTEGER.ordinal())
			return new SymbolInteger(0);
		else
			return null;
	};

	/**
	 * Gets Symboltype from String
	 * 
	 * @param s
	 *            String
	 * @return SymbolType
	 */
	public static SymType getTypeFromString(String s) {
		return SymType.valueOf(s);
	}

	private String			name;

	private final SymType	type;

	Symbol(String name, SymType type) {
		setName(name);
		this.type = type;
	}

	/**
	 * absolute value of symbol
	 * 
	 * @return result
	 * @throws Exception
	 */
	public abstract Symbol abs() throws Exception;

	/**
	 * acosine of symbol
	 * 
	 * @return result
	 * @throws Exception
	 */
	public abstract Symbol acos() throws Exception;

	/**
	 * Add symbols
	 * 
	 * @param s
	 *            symbol to add
	 * @return result
	 * @throws Exception
	 */
	public abstract Symbol add(Symbol s) throws Exception;

	/**
	 * and symbols
	 * 
	 * @param s
	 *            symbol to and
	 * @return result
	 * @throws Exception
	 */
	public abstract Symbol and(Symbol s) throws Exception;

	/**
	 * asine of symbol
	 * 
	 * @return result
	 * @throws Exception
	 */
	public abstract Symbol asin() throws Exception;

	/**
	 * atangens of symbol
	 * 
	 * @return result
	 * @throws Exception
	 */
	public abstract Symbol atan() throws Exception;

	/**
	 * clones the object
	 */
	@Override
	public abstract Symbol clone();

	/**
	 * Convert another Symbol to this symbols subclass
	 * 
	 * @param s
	 * @return a symbol of the same type as this class
	 * @throws Exception
	 */
	public abstract Symbol convert(Symbol s) throws Exception;

	/**
	 * Convert Symbol to SymbolDouble
	 * 
	 * @return SymbolString
	 * @throws Exception
	 */
	public abstract SymbolDouble convertToDouble() throws Exception;

	/**
	 * Convert Symbol to SymbolInteger
	 * 
	 * @return SymbolString
	 * @throws Exception
	 */
	public abstract SymbolInteger convertToInt() throws Exception;

	/**
	 * Convert Symbol to SymbolString
	 * 
	 * @return SymbolString
	 * @throws Exception
	 */
	public abstract SymbolString convertToString() throws Exception;

	/**
	 * cosine of symbol
	 * 
	 * @return result
	 * @throws Exception
	 */
	public abstract Symbol cos() throws Exception;

	/**
	 * Divide symbols
	 * 
	 * @param s
	 *            symbol to divide through
	 * @return result
	 * @throws Exception
	 */
	public abstract Symbol div(Symbol s) throws Exception;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (!(obj instanceof Symbol))
			return false;
		Symbol that = (Symbol) obj;
		if (!this.name.equals(that.name))
			return false;
		if (!this.type.equals(that.type))
			return false;
		return true;
	}

	/**
	 * exponential of symbol
	 * 
	 * @return result
	 * @throws Exception
	 */
	public abstract Symbol exp() throws Exception;

	/**
	 * test if symbol is greater or equal than other symbol
	 * 
	 * @param s
	 *            symbol to and
	 * @return result
	 * @throws Exception
	 */
	public abstract boolean ge(Symbol s) throws Exception;

	/**
	 * Get minimum binary length necessary to represent Symbol in binary
	 * 
	 * @return byte length
	 */
	public abstract int getBinaryLength();

	/**
	 * Get binary representation of symbol
	 * 
	 * @return binary representation
	 */
	public abstract VArrayList<Integer> getBinaryRepresentation();

	/**
	 * Get binary representation of symbol
	 * 
	 * @param len
	 *            length of binary representation
	 * @return binary representation
	 */
	public abstract VArrayList<Integer> getBinaryRepresentation(int len);

	/**
	 * Gets the name of a symbol
	 * 
	 * @return symbol name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Get symbol type
	 * 
	 * @return symbol type
	 */
	public SymType getType() {
		return this.type;
	}

	/**
	 * Get string representation of symboltype
	 * 
	 * @return Symboltype string
	 */
	public String getTypeString() {
		switch (this.type) {
		case STRING:
			return "STRING";
		case DOUBLE:
			return "DOUBLE";
		case INTEGER:
			return "INTEGER";
		default:
			return "Unknown type!";
		}
	}

	/**
	 * Get unit
	 * 
	 * @return Unit
	 * @throws Exception
	 */
	public abstract Unit getUnit() throws Exception;

	/**
	 * Get symbol value as string
	 * 
	 * @return value as string
	 */
	public abstract String getValueString();

	/**
	 * Get symbol value as string formatted according to format
	 * 
	 * @param format
	 *            Format string
	 * @return value as string
	 */
	public abstract String getValueString(String format);

	/**
	 * Get symbol value as string with unit
	 * 
	 * @return value as string
	 */
	public abstract String getValueUnitString();

	/**
	 * Get symbol value as string formatted according to format
	 * 
	 * @param format
	 *            Format string
	 * @return value as string
	 */
	public abstract String getValueUnitString(String format);

	/**
	 * test if symbol is greater than other symbol
	 * 
	 * @param s
	 *            symbol to and
	 * @return result
	 * @throws Exception
	 */
	public abstract boolean gt(Symbol s) throws Exception;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int hc = 17;
		int hashMultiplier = 59;
		hc = hc * hashMultiplier + super.hashCode();
		hc = hc * hashMultiplier + type.hashCode();
		return hc;
	}

	/**
	 * test if symbol is less than or equal to other symbol
	 * 
	 * @param s
	 *            symbol to and
	 * @return result
	 * @throws Exception
	 */
	public abstract boolean le(Symbol s) throws Exception;

	/**
	 * log of symbol
	 * 
	 * @return result
	 * @throws Exception
	 */
	public abstract Symbol ln() throws Exception;

	/**
	 * test if symbol is less than other symbol
	 * 
	 * @param s
	 *            symbol to and
	 * @return result
	 * @throws Exception
	 */
	public abstract boolean lt(Symbol s) throws Exception;

	/**
	 * max of symbol
	 * 
	 * @param s
	 *            to the power of
	 * @return result
	 * @throws Exception
	 */
	public abstract Symbol max(Symbol s) throws Exception;

	/**
	 * min of symbol
	 * 
	 * @param s
	 *            to the power of
	 * @return result
	 * @throws Exception
	 */
	public abstract Symbol min(Symbol s) throws Exception;

	/**
	 * Multiply symbols
	 * 
	 * @param s
	 *            symbol to multiply
	 * @return result
	 * @throws Exception
	 */
	public abstract Symbol mul(Symbol s) throws Exception;

	/**
	 * not symbol
	 * 
	 * @return result
	 * @throws Exception
	 */
	public abstract Symbol not() throws Exception;

	/**
	 * or symbols
	 * 
	 * @param s
	 *            symbol to or
	 * @return result
	 * @throws Exception
	 */
	public abstract Symbol or(Symbol s) throws Exception;

	/**
	 * post decrement of symbol
	 * 
	 * @return result
	 * @throws Exception
	 */
	public abstract Symbol postDec() throws Exception;

	/**
	 * post increment of symbol
	 * 
	 * @return result
	 * @throws Exception
	 */
	public abstract Symbol postInc() throws Exception;

	/**
	 * power of symbol
	 * 
	 * @param s
	 *            to the power of
	 * @return result
	 * @throws Exception
	 */
	public abstract Symbol pow(Symbol s) throws Exception;

	/**
	 * pre decrement of symbol
	 * 
	 * @return result
	 * @throws Exception
	 */
	public abstract Symbol preDec() throws Exception;

	/**
	 * pre increment of symbol
	 * 
	 * @return result
	 * @throws Exception
	 */
	public abstract Symbol preInc() throws Exception;

	/**
	 * Sets the name of the symbol
	 * 
	 * @param s
	 *            name of the symbol
	 */
	public void setName(String s) {
		this.name = s;
	}

	/**
	 * Set unit
	 * 
	 * @param unit
	 *            Unit
	 * @throws Exception
	 */
	public abstract void setUnit(Unit unit) throws Exception;

	/**
	 * sine of symbol
	 * 
	 * @return result
	 * @throws Exception
	 */
	public abstract Symbol sin() throws Exception;

	/**
	 * root of symbol
	 * 
	 * @return result
	 * @throws Exception
	 */
	public abstract Symbol sqrt() throws Exception;

	/**
	 * Subtract symbols
	 * 
	 * @param s
	 *            symbol to subract
	 * @return result
	 * @throws Exception
	 */
	public abstract Symbol sub(Symbol s) throws Exception;

	/**
	 * tangens of symbol
	 * 
	 * @return result
	 * @throws Exception
	 */
	public abstract Symbol tan() throws Exception;

	/**
	 * unary minus symbol
	 * 
	 * @return result
	 * @throws Exception
	 */
	public abstract Symbol uminus() throws Exception;

	/**
	 * unary plus symbol
	 * 
	 * @return result
	 * @throws Exception
	 */
	public abstract Symbol uplus() throws Exception;

	/**
	 * xor symbols
	 * 
	 * @param s
	 *            symbol to xor
	 * @return result
	 * @throws Exception
	 */
	public abstract Symbol xor(Symbol s) throws Exception;
}
