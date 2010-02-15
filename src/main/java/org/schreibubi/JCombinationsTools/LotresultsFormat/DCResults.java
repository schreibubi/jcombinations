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
package org.schreibubi.JCombinationsTools.LotresultsFormat;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.regex.Pattern;

import org.schreibubi.symbol.Symbol;
import org.schreibubi.symbol.SymbolDouble;
import org.schreibubi.visitor.VArrayList;
import org.schreibubi.visitor.VLinkedHashMap;

/**
 * @author Jörg Werner
 * 
 */
public class DCResults {

	int touchdown;
	int dut;
	String test;
	VLinkedHashMap<VLinkedHashMap<VLinkedHashMap<Symbol>>> results = new VLinkedHashMap<VLinkedHashMap<VLinkedHashMap<Symbol>>>();
	VArrayList<Integer> activeDuts = new VArrayList<Integer>();
	int fileDutOffset = 0;
	int touchdownDutOffset = 0;
	static Pattern p = Pattern.compile(";");

	public int getDut() {
		return dut;
	}

	public VLinkedHashMap<VLinkedHashMap<VLinkedHashMap<Symbol>>> getResults() {
		return results;
	}

	public String getTest() {
		return test;
	}

	/**
	 * @return the touchdown
	 */
	public int getTouchdown() {
		return touchdown;
	}

	public String getValue() {
		return null;
	}

	public void setActiveDuts(VArrayList<Integer> activeDuts) {
		this.activeDuts = activeDuts;
	}

	public void setDut(int dut) {
		this.dut = dut;
	}

	public void setFileDutOffset(int dutOffset) {
		this.fileDutOffset = dutOffset;
	}

	public void setTest(String test) {
		this.test = test;
	}

	/**
	 * @param touchdown
	 *            the touchdown to set
	 */
	public void setTouchdown(int touchdown) {
		this.touchdown = touchdown;
	}

	/**
	 * @param touchdownOffset
	 *            the touchdownOffset to set
	 */
	public void setTouchdownDutOffset(int touchdownOffset) {
		this.touchdownDutOffset = touchdownOffset;
	}

	public void setValue(String value) {
		int dutWithOffset = dut + (touchdown - 1) * touchdownDutOffset
				+ fileDutOffset;
		if (activeDuts.contains(dutWithOffset)) {
			NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
			DecimalFormat myFormatter = (DecimalFormat) nf;
			myFormatter.applyPattern("00");

			VLinkedHashMap<VLinkedHashMap<Symbol>> t = results.get(test);
			if (t == null) {
				t = new VLinkedHashMap<VLinkedHashMap<Symbol>>();
			}
			VLinkedHashMap<Symbol> d = t.get("DUT"
					+ myFormatter.format(dutWithOffset));
			if (d == null) {
				d = new VLinkedHashMap<Symbol>();
			}
			String[] values = p.split(value, 0);
			for (int i = 0; i < values.length; i++) {
				if (values[i].contains("**")) {
					d.put("S" + i, new SymbolDouble("DUT"
							+ myFormatter.format(dutWithOffset), 0.0));
				} else {
					try {
						d.put("S" + i, new SymbolDouble("DUT"
								+ myFormatter.format(dutWithOffset), values[i]
								.trim()));
					} catch (NumberFormatException e) {
						d.put("S" + i, new SymbolDouble("DUT"
								+ myFormatter.format(dutWithOffset), 0.0));
					}
				}
			}
			t.put("DUT" + myFormatter.format(dutWithOffset), d);
			results.put(test, t);
		}
	}
}
