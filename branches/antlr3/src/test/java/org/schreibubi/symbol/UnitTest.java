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

import org.schreibubi.symbol.SymbolDouble;
import org.schreibubi.symbol.Unit;
import org.testng.annotations.Test;

/**
 * @author Jörg Werner
 * 
 */
public class UnitTest {

	/**
	 * Class under test for String toString()
	 */
	@Test()
	public void divideTest() throws Exception {
		SymbolDouble d = new SymbolDouble("1.5V");
		SymbolDouble e = new SymbolDouble("0.5V");
		d.div(e);
		assert d.getValue() == 3.0;
		assert d.getUnit().toString().equals("");
	}

	/**
	 * Class under test for String toString()
	 */
	@Test()
	public void testToString() {
		Unit u = new Unit("V");
		u.addNominator("A");
		u.addDenominator("A");
		assert u.toString().contentEquals("V");
		Unit v = new Unit();
		assert v.toString().contentEquals("");
		Unit w = new Unit();
		w.addDenominator("A");
		assert w.toString().contentEquals("1/(A)");
	}

}
