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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Unit class, which allows to calculate with units.
 * 
 * @author Jörg Werner
 */
public class Unit {

	List<String> nominator = new ArrayList<String>();

	List<String> denominator = new ArrayList<String>();

	/**
	 * Constructor
	 */
	public Unit() {
	}

	/**
	 * Constructor
	 * 
	 * @param u
	 *            Unit
	 */
	public Unit(String u) {
		this.nominator.add(u);
	}

	public Unit(Unit other) {
		if (other != null) {
			this.nominator = new ArrayList<String>(other.getNominator());
			this.denominator = new ArrayList<String>(other.getDenominator());
		}
	}

	/**
	 * Add denominator
	 * 
	 * @param denominator
	 *            denominator
	 */
	public void addDenominator(String denominator) {
		this.denominator.add(denominator);
		reduce();
	}

	/**
	 * Add nominator
	 * 
	 * @param nominator
	 *            nominator
	 */
	public void addNominator(String nominator) {
		this.nominator.add(nominator);
		reduce();
	}

	/**
	 * Compare units
	 * 
	 * @param o
	 *            other unit
	 * @return true: equal
	 */
	public boolean compareTo(Unit o) {
		List<String> on = o.getNominator();
		List<String> od = o.getDenominator();
		return (this.nominator.containsAll(on) & this.denominator
				.containsAll(od));
	}

	/**
	 * Divide units
	 * 
	 * @param o
	 *            other unit
	 */
	public void divide(Unit o) {
		if (o != null) {
			this.nominator.addAll(o.getDenominator());
			this.denominator.addAll(o.getNominator());
		}
		reduce();
	}

	/**
	 * Returns if the unit is empty
	 * 
	 * @return true if empty
	 */
	public boolean empty() {
		return ((denominator.size() == 0) & (nominator.size() == 0));
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof Unit)) {
			return false;
		}
		Unit that = (Unit) obj;
		if (!this.denominator.equals(that.denominator)) {
			return false;
		}
		if (!this.nominator.equals(that.nominator)) {
			return false;
		}
		return true;
	}

	/**
	 * Get denominator
	 * 
	 * @return denominator
	 */
	public List<String> getDenominator() {
		return this.denominator;
	}

	/**
	 * Get nominator
	 * 
	 * @return nominator
	 */
	public List<String> getNominator() {
		return this.nominator;
	}

	@Override
	public int hashCode() {
		int hc = 17;
		int hashMultiplier = 59;
		hc = hc * hashMultiplier + denominator.hashCode();
		hc = hc * hashMultiplier + nominator.hashCode();
		return hc;
	}

	public void invert() {
		List<String> tmp = this.nominator;
		nominator = denominator;
		denominator = tmp;
	}

	/**
	 * Multiply units
	 * 
	 * @param o
	 *            other unit
	 */
	public void multiply(Unit o) {
		if (o != null) {
			this.nominator.addAll(o.getNominator());
			this.denominator.addAll(o.getDenominator());
		}
		reduce();
	}

	/**
	 * reduces the fraction of units
	 */
	public void reduce() {
		for (Iterator<String> a = nominator.iterator(); a.hasNext();) {
			String c = a.next();
			for (Iterator<String> b = denominator.iterator(); b.hasNext();) {
				String d = b.next();
				if (c.contentEquals(d)) {
					a.remove();
					b.remove();
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String res = "";
		if ((!this.nominator.isEmpty()) | (!this.denominator.isEmpty())) {
			if (this.nominator.size() > 0) {
				for (String string : this.nominator) {
					res = res + string;
				}
			} else {
				res = "1";
			}
			if (this.denominator.size() > 0) {
				res = res + "/(";
				for (String string : this.denominator) {
					res = res + string;
				}
				res = res + ")";
			}
		}
		return res;
	}

}
