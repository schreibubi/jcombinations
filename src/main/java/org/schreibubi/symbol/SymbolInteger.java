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

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import org.schreibubi.visitor.VArrayList;
import org.schreibubi.visitor.Visitor;


/**
 * Class for storing integer.
 */
public class SymbolInteger extends Symbol {
	private static String	STANDARD_FORMAT	= "###";

	private int				value;

	private Unit			unit			= null;

	/**
	 * Constructor
	 * 
	 * @param value
	 *            int value for this symbol
	 */
	public SymbolInteger(int value) {
		super(null, SymType.INTEGER);
		setValue(value);
		setUnit(null);
	}

	/**
	 * Constructor
	 * 
	 * @param value
	 *            int value for this symbol
	 */
	public SymbolInteger(String value) {
		super(null, SymType.INTEGER);
		parseValueUnitString(value);
	}

	/**
	 * Constructor
	 * 
	 * @param name
	 *            name for this symbol
	 * @param value
	 *            int value for this symbol
	 */
	public SymbolInteger(String name, int value) {
		super(name, SymType.INTEGER);
		setValue(value);
		setUnit(null);
	}

	/**
	 * Constructor
	 * 
	 * @param name
	 *            name for this symbol
	 * @param value
	 *            int value for this symbol
	 * @param unit
	 *            Unit
	 */
	public SymbolInteger(String name, int value, Unit unit) {
		super(name, SymType.INTEGER);
		setValue(value);
		setUnit(unit);
	}

	/**
	 * Constructor
	 * 
	 * @param name
	 *            name of the symbol
	 * 
	 * @param value
	 *            int value for this symbol
	 */
	public SymbolInteger(String name, String value) {
		super(name, SymType.INTEGER);
		parseValueUnitString(value);
	}

	/**
	 * Copy constructor
	 * 
	 * @param s
	 *            Symbol
	 */
	public SymbolInteger(SymbolInteger s) {
		super(s.getName(), SymType.INTEGER);
		setValue(s.getValue());
		setUnit(s.getUnit());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#abs()
	 */
	@Override
	public SymbolInteger abs() {
		this.value = Math.abs(this.value);
		return this;
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
	public SymbolDouble acos() {
		return new SymbolDouble(getName(), Math.acos(this.value), this.unit);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#add(org.schreibubi.symbol.Symbol)
	 */
	@Override
	public SymbolInteger add(Symbol s) throws Exception {
		if (s instanceof SymbolInteger) {
			SymbolInteger os = (SymbolInteger) s;
			if ((os.getUnit() != null) && (getUnit() != null) && (!os.getUnit().compareTo(getUnit()))) {
				throw new Exception("Operation on numbers with different units");
			}
			int o = os.getValue();
			this.value = this.value + o;
			return this;
		} else {
			throw new Exception("wrong variable type");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#and(org.schreibubi.symbol.Symbol)
	 */
	@Override
	public SymbolInteger and(Symbol s) throws Exception {
		if (s instanceof SymbolInteger) {
			SymbolInteger os = (SymbolInteger) s;
			if ((os.getUnit() != null) && (getUnit() != null) && (!os.getUnit().compareTo(getUnit()))) {
				throw new Exception("Operation on numbers with different units");
			}
			int o = os.getValue();
			this.value = this.value & o;
			return this;
		} else {
			throw new Exception("wrong variable type");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#asin()
	 */
	@Override
	public SymbolDouble asin() {
		return new SymbolDouble(getName(), Math.asin(this.value), this.unit);
	}

	/**
	 * Assign symbol
	 * 
	 * @param s
	 *            Symbol
	 */
	public void assign(SymbolInteger s) {
		setValue(s.getValue());
		setUnit(s.getUnit());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#atan()
	 */
	@Override
	public SymbolDouble atan() {
		return new SymbolDouble(getName(), Math.atan(this.value), this.unit);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#clone()
	 */
	@Override
	public Symbol clone() {
		return new SymbolInteger(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#convert(org.schreibubi.symbol.Symbol)
	 */
	@Override
	public Symbol convert(Symbol s) throws Exception {
		return s.convertToInt();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#convertToFloat()
	 */
	@Override
	public SymbolDouble convertToDouble() throws Exception {
		return new SymbolDouble(getName(), this.value, this.unit);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#convertToInt()
	 */
	@Override
	public SymbolInteger convertToInt() throws Exception {
		return new SymbolInteger(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#convertToString()
	 */
	@Override
	public SymbolString convertToString() throws Exception {
		return new SymbolString(getName(), getValueUnitString());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#cos()
	 */
	@Override
	public SymbolDouble cos() {
		return new SymbolDouble(getName(), Math.cos(this.value), this.unit);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#div(org.schreibubi.symbol.Symbol)
	 */
	@Override
	public SymbolInteger div(Symbol s) throws Exception {
		if (s instanceof SymbolInteger) {
			SymbolInteger os = (SymbolInteger) s;
			int o = os.getValue();
			this.value = this.value / o;
			if (this.unit != null) {
				this.unit.divide(os.getUnit());
			} else if (os.getUnit() != null) {
				this.unit = (new Unit(os.getUnit()));
				this.unit.invert();
			}
			return this;
		} else {
			throw new Exception("wrong variable type");
		}
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
		if (!(obj instanceof SymbolInteger))
			return false;
		if (!super.equals(obj))
			return false;
		SymbolInteger that = (SymbolInteger) obj;
		if (!(this.value == that.value))
			return false;
		if (!this.unit.equals(that.unit))
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#exp()
	 */
	@Override
	public SymbolDouble exp() {
		return new SymbolDouble(getName(), Math.exp(this.value), this.unit);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#gt(org.schreibubi.symbol.Symbol)
	 */
	@Override
	public boolean ge(Symbol s) throws Exception {
		if (s instanceof SymbolInteger) {
			SymbolInteger os = (SymbolInteger) s;
			if ((os.getUnit() != null) && (getUnit() != null) && (!os.getUnit().compareTo(getUnit()))) {
				throw new Exception("Operation on numbers with different units");
			}
			int o = os.getValue();
			return this.value >= o;
		} else {
			throw new Exception("wrong variable type");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#getBinaryLength()
	 */
	@Override
	public int getBinaryLength() {
		int bytes = 1;
		int val = getValue();
		val = val / 256;
		while (val > 0) {
			val = val / 256;
			bytes++;
		}
		return bytes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#getBinaryRepresentation()
	 */
	@Override
	public VArrayList<Integer> getBinaryRepresentation() {
		int val = getValue();
		VArrayList<Integer> b = new VArrayList<Integer>();
		for (int i = 0; i < getBinaryLength(); i++) {
			b.add(0, val % 256);
			val = val / 256;
		}
		return b;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#getBinaryRepresentation(int)
	 */
	@Override
	public VArrayList<Integer> getBinaryRepresentation(int len) {
		int val = getValue();
		VArrayList<Integer> b = new VArrayList<Integer>();
		for (int i = 0; i < len; i++) {
			b.add(0, val % 256);
			val = val / 256;
		}
		return b;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#getUnit()
	 */
	@Override
	public Unit getUnit() {
		return this.unit;
	}

	/**
	 * Returns int value stored in this class
	 * 
	 * @return int value stored
	 */
	public int getValue() {
		return this.value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#getValueString()
	 */
	@Override
	public String getValueString() {
		return getValueString(SymbolInteger.STANDARD_FORMAT);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#getValueString(java.lang.String)
	 */
	@Override
	public String getValueString(String format) {
		NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
		DecimalFormat myFormatter = (DecimalFormat) nf;
		myFormatter.applyPattern(format);
		return myFormatter.format(getValue());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#getValueUnitString()
	 */
	@Override
	public String getValueUnitString() {
		return getValueUnitString(SymbolInteger.STANDARD_FORMAT);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#getValueUnitString(java.lang.String)
	 */
	@Override
	public String getValueUnitString(String format) {
		String res = getValueString(format);
		if ((getUnit() != null) && !getUnit().empty()) {
			res = res + getUnit().toString();
		}
		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#gt(org.schreibubi.symbol.Symbol)
	 */
	@Override
	public boolean gt(Symbol s) throws Exception {
		if (s instanceof SymbolInteger) {
			SymbolInteger os = (SymbolInteger) s;
			if ((os.getUnit() != null) && (getUnit() != null) && (!os.getUnit().compareTo(getUnit()))) {
				throw new Exception("Operation on numbers with different units");
			}
			int o = os.getValue();
			return this.value > o;
		} else {
			throw new Exception("wrong variable type");
		}
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
		hc = hc * hashMultiplier + unit.hashCode();
		hc = hc * hashMultiplier + value;
		return hc;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#lt(org.schreibubi.symbol.Symbol)
	 */
	@Override
	public boolean le(Symbol s) throws Exception {
		if (s instanceof SymbolInteger) {
			SymbolInteger os = (SymbolInteger) s;
			if ((os.getUnit() != null) && (getUnit() != null) && (!os.getUnit().compareTo(getUnit()))) {
				throw new Exception("Operation on numbers with different units");
			}
			int o = os.getValue();
			return this.value <= o;
		} else {
			throw new Exception("wrong variable type");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#ln()
	 */
	@Override
	public SymbolDouble ln() {
		return new SymbolDouble(getName(), Math.log(this.value), this.unit);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#lt(org.schreibubi.symbol.Symbol)
	 */
	@Override
	public boolean lt(Symbol s) throws Exception {
		if (s instanceof SymbolInteger) {
			SymbolInteger os = (SymbolInteger) s;
			if ((os.getUnit() != null) && (getUnit() != null) && (!os.getUnit().compareTo(getUnit()))) {
				throw new Exception("Operation on numbers with different units");
			}
			int o = os.getValue();
			return this.value < o;
		} else {
			throw new Exception("wrong variable type");
		}
	}

	@Override
	public Symbol max(Symbol s) throws Exception {
		if (s instanceof SymbolInteger) {
			int o = ((SymbolInteger) s).getValue();
			this.value = Math.min(this.value, o);
			return this;
		} else {
			throw new Exception("wrong variable type");
		}
	}

	@Override
	public Symbol min(Symbol s) throws Exception {
		if (s instanceof SymbolInteger) {
			int o = ((SymbolInteger) s).getValue();
			this.value = Math.min(this.value, o);
			return this;
		} else {
			throw new Exception("wrong variable type");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#mul(org.schreibubi.symbol.Symbol)
	 */
	@Override
	public SymbolInteger mul(Symbol s) throws Exception {
		if (s instanceof SymbolInteger) {
			SymbolInteger os = (SymbolInteger) s;
			int o = os.getValue();
			this.value = this.value * o;
			if (this.unit != null) {
				this.unit.multiply(os.getUnit());
			} else if (os.getUnit() != null) {
				this.unit = new Unit(os.getUnit());
			}
			return this;
		} else {
			throw new Exception("wrong variable type");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#not()
	 */
	@Override
	public SymbolInteger not() {
		this.value = ~this.value;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#or(org.schreibubi.symbol.Symbol)
	 */
	@Override
	public SymbolInteger or(Symbol s) throws Exception {
		if (s instanceof SymbolInteger) {
			SymbolInteger os = (SymbolInteger) s;
			if ((os.getUnit() != null) && (getUnit() != null) && (!os.getUnit().compareTo(getUnit()))) {
				throw new Exception("Operation on numbers with different units");
			}
			int o = os.getValue();
			this.value = this.value | o;
			return this;
		} else {
			throw new Exception("wrong variable type");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#postDec()
	 */
	@Override
	public Symbol postDec() throws Exception {
		SymbolInteger s = new SymbolInteger(this);
		this.value--;
		return s;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#postInc()
	 */
	@Override
	public Symbol postInc() throws Exception {
		SymbolInteger s = new SymbolInteger(this);
		this.value++;
		return s;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#pow(org.schreibubi.symbol.Symbol)
	 */
	@Override
	public SymbolInteger pow(Symbol s) throws Exception {
		if (s instanceof SymbolInteger) {
			int o = ((SymbolInteger) s).getValue();
			this.value = (int) Math.pow(this.value, o);
			return this;
		} else {
			throw new Exception("wrong variable type");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#preDec()
	 */
	@Override
	public Symbol preDec() throws Exception {
		this.value--;
		return new SymbolInteger(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#preInc()
	 */
	@Override
	public Symbol preInc() throws Exception {
		this.value++;
		return new SymbolInteger(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#setUnit(java.lang.String)
	 */
	@Override
	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	/**
	 * Sets int value stored in this class
	 * 
	 * @param value
	 *            int value to set
	 */
	public void setValue(int value) {
		this.value = value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#sin()
	 */
	@Override
	public SymbolDouble sin() {
		return new SymbolDouble(getName(), Math.sin(this.value), this.unit);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#sqrt()
	 */
	@Override
	public SymbolDouble sqrt() {
		return new SymbolDouble(getName(), Math.sqrt(this.value), this.unit);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#sub(org.schreibubi.symbol.Symbol)
	 */
	@Override
	public SymbolInteger sub(Symbol s) throws Exception {
		if (s instanceof SymbolInteger) {
			SymbolInteger os = (SymbolInteger) s;
			if ((os.getUnit() != null) && (getUnit() != null) && (!os.getUnit().compareTo(getUnit()))) {
				throw new Exception("Operation on numbers with different units");
			}
			int o = os.getValue();
			this.value = this.value - o;
			return this;
		} else {
			throw new Exception("wrong variable type");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#tan()
	 */
	@Override
	public SymbolDouble tan() {
		return new SymbolDouble(getName(), Math.tan(this.value), this.unit);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getName() + "=" + getValueString("###");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#uminus()
	 */
	@Override
	public SymbolInteger uminus() {
		this.value = -this.value;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#uplus()
	 */
	@Override
	public SymbolInteger uplus() {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#xor(org.schreibubi.symbol.Symbol)
	 */
	@Override
	public SymbolInteger xor(Symbol s) throws Exception {
		if (s instanceof SymbolInteger) {
			SymbolInteger os = (SymbolInteger) s;
			if ((os.getUnit() != null) && (getUnit() != null) && (!os.getUnit().compareTo(getUnit()))) {
				throw new Exception("Operation on numbers with different units");
			}
			int o = os.getValue();
			this.value = this.value ^ o;
			return this;
		} else {
			throw new Exception("wrong variable type");
		}
	}

	private void parseValueUnitString(String value) {
		char last = value.charAt(value.length() - 1);
		if (Character.isDigit(last)) {
			setValue(Integer.parseInt(value));
			setUnit(this.unit);
		} else {
			setUnit(new Unit(Character.toString(last)));
			setValue(Integer.parseInt(value.substring(0, value.length() - 1)));
		}
	}

}
