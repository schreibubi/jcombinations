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
package org.schreibubi.JCombinationsTools.evalGenCombinations;

import java.io.File;
import java.io.StringReader;

import org.schreibubi.JCombinationsTools.evalGenCombinations.EvalGenCombinations;
import org.schreibubi.symbol.Symbol;
import org.schreibubi.symbol.SymbolDouble;
import org.schreibubi.symbol.Symbol.SymType;
import org.schreibubi.visitor.VArrayList;
import org.schreibubi.visitor.VHashMap;
import org.testng.annotations.Test;


/**
 * @author Jörg Werner
 * 
 */
public class EvalGenCombinationsTest {

	/**
	 * Class under test for String toString()
	 * 
	 * @throws Exception
	 */
	@Test()
	public void testUnitBehaviour() throws Exception {
		VHashMap<String> optionsFromEvalGenWalker = null;
		VArrayList<VHashMap<Symbol>> r = EvalGenCombinations.exec(new StringReader("{CLOCK=8ns \n TADQ=3*CLOCK/4 \n}"),
				new File("."), optionsFromEvalGenWalker);
		Symbol s = r.get(0).get("TADQ");
		assert s.getName().equals("TADQ");
		assert s.getType() == SymType.DOUBLE;
		SymbolDouble sd = s.convertToDouble();
// System.out.println("Value: " + sd.getValue());
		assert sd.getValue() - 6e-9 < 0.00000001;
	}

}
