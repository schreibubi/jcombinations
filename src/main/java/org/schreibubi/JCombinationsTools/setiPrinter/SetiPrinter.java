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
package org.schreibubi.JCombinationsTools.setiPrinter;

import org.apache.commons.cli.ParseException;
import org.schreibubi.JCombinations.Info;
import org.schreibubi.JCombinationsTools.SetiChains.SetiChainBuilder;
import org.schreibubi.JCombinationsTools.SetiChains.SetiChainData;
import org.schreibubi.JCombinationsTools.SetiChains.SetiChain.SetiTypeEnum;
import org.schreibubi.JCombinationsTools.settings.SettingsSingleton;
import org.schreibubi.symbol.SymbolString;
import org.schreibubi.visitor.VArrayList;

/**
 * @author Jörg Werner
 * 
 */
public class SetiPrinter {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			SetiPrinterOptions[] settings = SetiPrinterOptions.values();
			SettingsSingleton.initialize("SetiPrinter", settings);
			SettingsSingleton.getInstance().parseArguments(args, 1);
			if ((SettingsSingleton.getInstance().areRequiredOptionsSet() == false)
					|| (SettingsSingleton.getInstance().getProperty("help")
							.equals("true"))) {
				SettingsSingleton.getInstance().displayHelp();
				Runtime.getRuntime().exit(0);
			}

			if (SettingsSingleton.getInstance().getProperty("version").equals(
					"true")) {
				Info.printVersion("SetiPrinter");
				Runtime.getRuntime().exit(0);
			}

			String setiFile = SettingsSingleton.getInstance().getProperty(
					"seti");
			String command = SettingsSingleton.getInstance().getProperty(
					"testmodes");

			SetiChainBuilder scb = new SetiChainBuilder(setiFile);
			SymbolString s = new SymbolString(command);
			SetiChainData setiChainData = scb.createSerialChain(s,
					SetiTypeEnum.WRITE);
			int len = setiChainData.getSize();

			System.out.print("Command: ");
			for (int i = 0; i < len; i++) {
				System.out.print(setiChainData.getCommandBit(i) ? "1" : "0");
			}
			System.out.println();

			System.out.print("Data:    ");
			for (int i = 0; i < len; i++) {
				System.out.print(setiChainData.getDataBit(i) ? "1" : "0");
			}
			System.out.println();

			System.out.print("Mask:    ");
			for (int i = 0; i < len; i++) {
				System.out.print(setiChainData.getDataMaskBit(i) ? "1" : "0");
			}
			System.out.println();

			VArrayList<Integer> vList = scb.createMRSChain(s, -1,
					SetiTypeEnum.WRITE);
			System.out.println("MRS2SETI: ");
			for (Integer i : vList) {
				System.out.println("#" + String.format("%04x", i));
			}
		} catch (ParseException e) {
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
}
