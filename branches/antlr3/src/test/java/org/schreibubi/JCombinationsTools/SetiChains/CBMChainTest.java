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
package org.schreibubi.JCombinationsTools.SetiChains;

import java.util.Collections;

import org.schreibubi.JCombinationsTools.SetiChains.SetiChainBuilder;
import org.schreibubi.JCombinationsTools.SetiChains.SetiChain.SetiTypeEnum;
import org.schreibubi.JCombinationsTools.coordinator.CoordinatorOptions;
import org.schreibubi.JCombinationsTools.settings.SettingsSingleton;
import org.schreibubi.symbol.SymbolString;
import org.schreibubi.visitor.VArrayList;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


/**
 * @author Jörg Werner
 * 
 */
public class CBMChainTest {

	@Test()
	public void CBMChainT() throws Exception {
		SetiChainBuilder csc = new SetiChainBuilder("target/test-classes/setichains_512M_GV70.xml");
		SymbolString str = new SymbolString(
				"TMVINTCFG=%1011111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111101");
		VArrayList<Integer> channels = new VArrayList<Integer>();
		Collections.addAll(channels, 20, 19, 18, 0, 1, 7, 23, 22, 21, 25, 2, 5, 4, 3, 6, 24);
		VArrayList<Integer> res = csc.createCBMChain(str, channels, 74, SetiTypeEnum.WRITE);
		StringBuffer sb = new StringBuffer();
		for (Integer i : res) {
			sb.append(i).append(' ');
		}
		assert "131 131 131 131 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 130 131 131 131 131 131 131 131 131 129 134 135 135 175 191 191 191 191 191 191 191 191 191 191 191 191 191 191 191 191 191 191 191 191 191 191 191 191 191 191 191 191 191 191 191 191 191 191 191 191 191 191 191 191 191 191 191 191 191 191 191 191 191 191 191 191 191 191 191 191 191 191 191 183 164 128 128 128 128 128 128 128 128 "
				.equals(sb.toString());
// System.out.println(sb.toString());
	}

	@Test()
	public void CBMCompareChainTest() throws Exception {
		SetiChainBuilder csc = new SetiChainBuilder("target/test-classes/setichains_512M_GV70.xml");
		SymbolString str = new SymbolString("TMPMPVINTCFGUL=%10000000000000000000");
		VArrayList<Integer> channels = new VArrayList<Integer>();
		Collections.addAll(channels, 20, 19, 18, 0, 1, 7, 23, 22, 21, 25, 2, 5, 4, 3, 6, 24);
		VArrayList<Integer> res = csc.createCBMChain(str, channels, 25, SetiTypeEnum.WRITE);
		StringBuffer sb = new StringBuffer();
		for (Integer i : res) {
			sb.append(i).append(' ');
		}
// System.out.println(sb.toString());
		assert "131 131 131 131 0 0 0 0 0 0 128 131 131 131 131 131 131 131 131 131 131 131 131 131 131 129 134 131 135 167 135 135 135 135 135 134 128 128 128 128 128 128 128 128 128 128 128 128 128 128 "
				.equals(sb.toString());
	}

	@Test()
	public void CBMLongChainTest() throws Exception {
		SetiChainBuilder csc = new SetiChainBuilder("target/test-classes/setichains_512M_GV70.xml");
		SymbolString str = new SymbolString(
				"TMPMPVINTCFGUL=%10000000000000000000,TMPMPVINTCFGULM=%10000000000000000000,TMPMPVINTCFGURM=%10000000000000000000,TMPMPVINTCFGUR=%10000000000000000000,TMPMPVINTCFGDL=%10000000000000000000,TMPMPVINTCFGDLM=%10000000000000000000,TMPMPVINTCFGDRM=%10000000000000000000,TMPMPVINTCFGDR=%10000000000000000000");
		VArrayList<Integer> channels = new VArrayList<Integer>();
		Collections.addAll(channels, 20, 19, 18, 0, 1, 7, 23, 22, 21, 25, 2, 5, 4, 3, 6, 24);
		VArrayList<Integer> res = csc.createCBMChain(str, channels, 95, SetiTypeEnum.WRITE);
		StringBuffer sb = new StringBuffer();
		for (Integer i : res) {
			sb.append(i).append(' ');
		}
// System.out.println(sb.toString());
		assert "131 131 131 131 0 0 0 0 0 0 128 131 131 131 131 0 0 0 0 0 0 128 131 131 131 131 0 0 0 0 0 0 128 131 131 131 131 0 0 0 0 0 0 128 131 131 131 131 0 0 0 0 0 0 128 131 131 131 131 0 0 0 0 0 0 128 131 131 131 131 0 0 0 0 0 0 128 131 131 131 131 0 0 0 0 0 0 128 131 131 131 131 131 131 131 129 134 131 135 167 135 135 135 135 135 134 129 134 133 135 167 135 135 135 135 135 134 129 134 134 131 167 135 135 135 135 135 134 129 134 132 131 167 135 135 135 135 135 134 129 134 132 135 167 135 135 135 135 135 134 129 134 134 135 167 135 135 135 135 135 134 129 134 135 131 167 135 135 135 135 135 134 129 134 133 131 167 135 135 135 135 135 134 128 128 128 128 128 128 128 "
				.equals(sb.toString());
	}

	@BeforeClass
	public void setUp() throws Exception {
		CoordinatorOptions[] settings = CoordinatorOptions.values();
		SettingsSingleton.initialize("CBMTest", settings);
	}

}
