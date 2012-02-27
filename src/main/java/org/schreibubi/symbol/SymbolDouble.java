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
 * Class for storing doubles.
 */
public class SymbolDouble extends Symbol {
	private static String STANDARD_FORMAT = "###.###E0";

	private double value;

	private Unit unit = null;

	/**
	 * Constructor
	 * 
	 * @param value
	 *            double value for this symbol
	 */
	public SymbolDouble(double value) {
		super(null, SymType.DOUBLE);
		setValue(value);
		setUnit(null);
	}

	/**
	 * Constructor
	 * 
	 * @param value
	 *            double value for this symbol
	 */
	public SymbolDouble(String value) {
		super(null, SymType.DOUBLE);
		parseValueUnitString(value);
	}

	/**
	 * Constructor
	 * 
	 * @param name
	 *            name for this symbol
	 * @param value
	 *            double value for this symbol
	 */
	public SymbolDouble(String name, double value) {
		super(name, SymType.DOUBLE);
		setValue(value);
		setUnit(null);
	}

	/**
	 * Constructor
	 * 
	 * @param name
	 *            name for this symbol
	 * @param value
	 *            double value for this symbol
	 * @param unit
	 *            Unit
	 */
	public SymbolDouble(String name, double value, Unit unit) {
		super(name, SymType.DOUBLE);
		setValue(value);
		setUnit(unit);
	}

	/**
	 * Constructor
	 * 
	 * @param name
	 *            symbol name
	 * 
	 * @param value
	 *            double value for this symbol
	 */
	public SymbolDouble(String name, String value) {
		super(name, SymType.DOUBLE);
		parseValueUnitString(value);
	}

	/**
	 * Copy constructor
	 * 
	 * @param s
	 */
	public SymbolDouble(SymbolDouble s) {
		super(s.getName(), SymType.DOUBLE);
		setValue(s.getValue());
		setUnit(new Unit(s.getUnit()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#abs()
	 */
	@Override
	public SymbolDouble abs() {
		this.value = Math.abs(this.value);
		return this;
	}

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
		this.value = Math.acos(this.value);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#add(org.schreibubi.symbol.Symbol)
	 */
	@Override
	public SymbolDouble add(Symbol s) throws Exception {
		if (s instanceof SymbolDouble) {
			SymbolDouble os = (SymbolDouble) s;
			if ((os.getUnit() != null) && (getUnit() != null) && (!os.getUnit().compareTo(getUnit()))) {
				throw new Exception("Operation on numbers with different units");
			}
			double o = os.getValue();
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
	public SymbolDouble and(Symbol s) throws Exception {
		if (s instanceof SymbolDouble) {
			SymbolDouble os = (SymbolDouble) s;
			if ((os.getUnit() != null) && (getUnit() != null) && (!os.getUnit().compareTo(getUnit()))) {
				throw new Exception("Operation on numbers with different units");
			}
			double o = os.getValue();
			this.value = (int) this.value & (int) o;
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
		this.value = Math.asin(this.value);
		return this;
	}

	/**
	 * Assign symbol
	 * 
	 * @param s
	 *            symbol
	 */
	public void assign(SymbolDouble s) {
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
		this.value = Math.atan(this.value);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#clone()
	 */
	@Override
	public Symbol clone() {
		return new SymbolDouble(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#convert(org.schreibubi.symbol.Symbol)
	 */
	@Override
	public Symbol convert(Symbol s) throws Exception {
		return s.convertToDouble();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#convertToFloat()
	 */
	@Override
	public SymbolDouble convertToDouble() throws Exception {
		return new SymbolDouble(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#convertToInt()
	 */
	@Override
	public SymbolInteger convertToInt() throws Exception {
		return new SymbolInteger(getName(), (int) this.value, this.unit);
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
		this.value = Math.cos(this.value);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#div(org.schreibubi.symbol.Symbol)
	 */
	@Override
	public SymbolDouble div(Symbol s) throws Exception {
		if (s instanceof SymbolDouble) {
			SymbolDouble os = (SymbolDouble) s;
			double o = os.getValue();
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
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof SymbolDouble)) {
			return false;
		}
		if (!super.equals(obj)) {
			return false;
		}
		SymbolDouble that = (SymbolDouble) obj;
		if (!(this.value == that.value)) {
			return false;
		}
		if (!this.unit.equals(that.unit)) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#exp()
	 */
	@Override
	public SymbolDouble exp() {
		this.value = Math.exp(this.value);
		return this;
	}

	@Override
	public boolean ge(Symbol s) throws Exception {
		if (s instanceof SymbolDouble) {
			SymbolDouble os = (SymbolDouble) s;
			if ((os.getUnit() != null) && (getUnit() != null) && (!os.getUnit().compareTo(getUnit()))) {
				throw new Exception("Operation on numbers with different units");
			}
			double o = os.getValue();
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
		int val = (int) getValue();
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
		int val = (int) getValue();
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
		int val = (int) getValue();
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
	 * Returns double value stored in this class
	 * 
	 * @return double value stored
	 */
	public double getValue() {
		return this.value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#getValueString()
	 */
	@Override
	public String getValueString() {
		return getValueString(SymbolDouble.STANDARD_FORMAT);
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
		return getValueUnitString(SymbolDouble.STANDARD_FORMAT);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#getValueUnitString(java.lang.String)
	 */
	@Override
	public String getValueUnitString(String format) {
		double multiplier = 1;
		String order = null;
		if (Math.abs(this.value) < 1.0e-6) {
			multiplier = 1.0e9;
			order = "n";
		} else if (Math.abs(this.value) < 1.0e-3) {
			multiplier = 1.0e6;
			order = "u";
		} else if (Math.abs(this.value) < 1.0) {
			multiplier = 1.0e3;
			order = "m";
		} else if (Math.abs(this.value) > 1.0e6) {
			multiplier = 1.0e-6;
			order = "M";
		} else if (Math.abs(this.value) > 1.0e3) {
			multiplier = 1.0e-3;
			order = "k";
		}
		NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
		DecimalFormat myFormatter = (DecimalFormat) nf;
		myFormatter.applyPattern(format);
		String res;
		if ((getUnit() != null) && !getUnit().empty()) {
			res = myFormatter.format(getValue() * multiplier);
			if (order != null) {
				res = res + order;
			}
			res = res + getUnit().toString();
		} else {
			res = myFormatter.format(getValue());
		}

		return res;
	}

	@Override
	public boolean gt(Symbol s) throws Exception {
		if (s instanceof SymbolDouble) {
			SymbolDouble os = (SymbolDouble) s;
			if ((os.getUnit() != null) && (getUnit() != null) && (!os.getUnit().compareTo(getUnit()))) {
				throw new Exception("Operation on numbers with different units");
			}
			double o = os.getValue();
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
		long v = ((value == 0.0) ? 0L : Double.doubleToLongBits(value));
		hc = hc * hashMultiplier + (int) (v ^ (v >>> 32));
		return hc;
	}

	@Override
	public boolean le(Symbol s) throws Exception {
		if (s instanceof SymbolDouble) {
			SymbolDouble os = (SymbolDouble) s;
			if ((os.getUnit() != null) && (getUnit() != null) && (!os.getUnit().compareTo(getUnit()))) {
				throw new Exception("Operation on numbers with different units");
			}
			double o = os.getValue();
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
		this.value = Math.log(this.value);
		return this;
	}

	@Override
	public boolean lt(Symbol s) throws Exception {
		if (s instanceof SymbolDouble) {
			SymbolDouble os = (SymbolDouble) s;
			if ((os.getUnit() != null) && (getUnit() != null) && (!os.getUnit().compareTo(getUnit()))) {
				throw new Exception("Operation on numbers with different units");
			}
			double o = os.getValue();
			return this.value < o;
		} else {
			throw new Exception("wrong variable type");
		}
	}

	@Override
	public Symbol max(Symbol s) throws Exception {
		if (s instanceof SymbolDouble) {
			double o = ((SymbolDouble) s).getValue();
			this.value = Math.max(this.value, o);
			return this;
		} else {
			throw new Exception("wrong variable type");
		}
	}

	@Override
	public Symbol min(Symbol s) throws Exception {
		if (s instanceof SymbolDouble) {
			double o = ((SymbolDouble) s).getValue();
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
	public SymbolDouble mul(Symbol s) throws Exception {
		if (s instanceof SymbolDouble) {
			SymbolDouble os = (SymbolDouble) s;
			double o = os.getValue();
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
	public SymbolDouble not() {
		this.value = ~(int) this.value;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#or(org.schreibubi.symbol.Symbol)
	 */
	@Override
	public SymbolDouble or(Symbol s) throws Exception {
		if (s instanceof SymbolDouble) {
			SymbolDouble os = (SymbolDouble) s;
			if ((os.getUnit() != null) && (getUnit() != null) && (!os.getUnit().compareTo(getUnit()))) {
				throw new Exception("Operation on numbers with different units");
			}
			double o = os.getValue();
			this.value = (int) this.value | (int) o;
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
		SymbolDouble s = new SymbolDouble(this);
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
		SymbolDouble s = new SymbolDouble(this);
		this.value++;
		return s;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#pow(org.schreibubi.symbol.Symbol)
	 */
	@Override
	public SymbolDouble pow(Symbol s) throws Exception {
		if (s instanceof SymbolDouble) {
			double o = ((SymbolDouble) s).getValue();
			this.value = Math.pow(this.value, o);
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
		return new SymbolDouble(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#preInc()
	 */
	@Override
	public Symbol preInc() throws Exception {
		this.value++;
		return new SymbolDouble(this);
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
	 * Sets double value stored in this class
	 * 
	 * @param value
	 *            double value to set
	 */
	public void setValue(double value) {
		this.value = value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#sin()
	 */
	@Override
	public SymbolDouble sin() {
		this.value = Math.sin(this.value);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#sqrt()
	 */
	@Override
	public SymbolDouble sqrt() {
		this.value = Math.sqrt(this.value);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#sub(org.schreibubi.symbol.Symbol)
	 */
	@Override
	public SymbolDouble sub(Symbol s) throws Exception {
		if (s instanceof SymbolDouble) {
			SymbolDouble os = (SymbolDouble) s;
			if ((os.getUnit() != null) && (getUnit() != null) && (!os.getUnit().compareTo(getUnit()))) {
				throw new Exception("Operation on numbers with different units");
			}
			double o = os.getValue();
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
		this.value = Math.tan(this.value);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getName() + "=" + getValueUnitString("#0.0##");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#uminus()
	 */
	@Override
	public SymbolDouble uminus() {
		this.value = -this.value;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#uplus()
	 */
	@Override
	public SymbolDouble uplus() {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.symbol.Symbol#xor(org.schreibubi.symbol.Symbol)
	 */
	@Override
	public SymbolDouble xor(Symbol s) throws Exception {
		if (s instanceof SymbolDouble) {
			SymbolDouble os = (SymbolDouble) s;
			if ((os.getUnit() != null) && (getUnit() != null) && (!os.getUnit().compareTo(getUnit()))) {
				throw new Exception("Operation on numbers with different units");
			}
			double o = os.getValue();
			this.value = (int) this.value ^ (int) o;
			return this;
		} else {
			throw new Exception("wrong variable type");
		}
	}

	private void parseValueUnitString(String value) {
		char last = value.charAt(value.length() - 1);
		if (Character.isDigit(last)) { // simple double
			setValue(Double.parseDouble(value));
			setUnit(null);
		} else {
			String rem = "";
			if (value.endsWith("Hz")) {
				setUnit(new Unit("Hz"));
				rem = value.substring(0, value.length() - 2);
			} else if (value.endsWith("dB")) {
				setUnit(new Unit("dB"));
				rem = value.substring(0, value.length() - 2);
			} else {
				setUnit(new Unit(Character.toString(last)));
				rem = value.substring(0, value.length() - 1);
			}
			char nextToLast = rem.charAt(rem.length() - 1);
			if (Character.isDigit(nextToLast)) {
				setValue(Double.parseDouble(rem));
			} else {
				double multiplier = 1.0;
				switch (nextToLast) {
				case 'n':
					multiplier = 1.0e-9;
					break;
				case 'u':
					multiplier = 1.0E-6;
					break;
				case 'm':
					multiplier = 1.0E-3;
					break;
				case 'k':
					multiplier = 1.0E3;
					break;
				case 'M':
					multiplier = 1.0E6;
					break;
				default:
					break;
				}
				setValue(multiplier * Double.parseDouble(rem.substring(0, rem.length() - 1)));
			}
		}
	}

}
