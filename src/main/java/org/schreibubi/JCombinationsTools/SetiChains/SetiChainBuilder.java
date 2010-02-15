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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.ListIterator;

import org.jibx.extras.BindingSelector;
import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.JiBXException;
import org.schreibubi.JCombinationsTools.SetiChains.SetiChain.SetiTypeEnum;
import org.schreibubi.JCombinationsTools.SetiFormat.Chain;
import org.schreibubi.JCombinationsTools.SetiFormat.Command;
import org.schreibubi.JCombinationsTools.SetiFormat.Position;
import org.schreibubi.JCombinationsTools.SetiFormat.Seti;
import org.schreibubi.JCombinationsTools.SetiFormat.Variable;
import org.schreibubi.JCombinationsTools.settings.SettingsSingleton;
import org.schreibubi.symbol.SymbolString;
import org.schreibubi.visitor.VArrayList;

/**
 * @author Jörg Werner
 * 
 */
public class SetiChainBuilder {
	// definitions for version attribute on document root element
	public static final String VERSION_URI = null;

	public static final String VERSION_NAME = "format";
	// attribute text strings used for different document versions
	public static final String[] VERSION_TEXTS = { "1.0", "seti_rel200",
			"seti_rel210", "seti_rel211", "seti_rel212" };

	// binding names corresponding to text strings
	public static final String[] VERSION_BINDINGS = {
			"bindings_SetiFormat_1_00", "bindings_SetiFormat_2_00",
			"bindings_SetiFormat_2_10", "bindings_SetiFormat_2_10",
			"bindings_SetiFormat_2_10" };

	private Seti seti = null;

	/**
	 * Constructor
	 * 
	 * @param Seti
	 */
	public SetiChainBuilder(Seti seti) {
		this.seti = seti;
	}

	/**
	 * Convenience Constructor
	 * 
	 * @param file
	 *            name of seti.xml file
	 */
	public SetiChainBuilder(String setiXmlFile) throws JiBXException,
			FileNotFoundException {
		BindingSelector sel = new BindingSelector(VERSION_URI, VERSION_NAME,
				VERSION_TEXTS, VERSION_BINDINGS);
		IUnmarshallingContext con = sel.getContext();
		con.setDocument(new FileInputStream(setiXmlFile), null);
		Seti seti = (Seti) sel.unmarshalVersioned(Seti.class);
		this.seti = seti;
	}

	/**
	 * @param seti
	 * @param setiChainLen
	 * @param varOrChainOrCommandStr
	 * @throws Exception
	 */
	public SetiChain constructSetiChain(String varOrChainOrCommandStr,
			SetiTypeEnum setiType, SetiChainsContainer chainContainer)
			throws Exception {
		boolean needsValue = false;
		switch (setiType) {
		case WRITE:
			needsValue = true;
			break;
		case READ:
		case COMMAND:
			needsValue = false;
			break;
		}

		String varOrChainOrCommandName = null;
		String valueString = null;
		BigInteger valuenum = BigInteger.ZERO;

		if (needsValue) {
			if (varOrChainOrCommandStr.contains("=")) {
				int strpos = varOrChainOrCommandStr.indexOf("=");
				varOrChainOrCommandName = varOrChainOrCommandStr.substring(0,
						strpos);
				valueString = varOrChainOrCommandStr.substring(strpos + 1);

				if (valueString.startsWith("%")) {
					valuenum = new BigInteger(valueString.substring(1), 2);
				} else if (valueString.startsWith("#")) {
					valuenum = new BigInteger(valueString.substring(1), 16);
				} else if (valueString.startsWith("ON")) {
					valuenum = BigInteger.ONE;
				} else if (valueString.startsWith("OFF")) {
					valuenum = BigInteger.ZERO;
				} else {
					valuenum = new BigInteger(valueString, 10);
				}
			} else {
				throw new Exception(
						"You need to assign a value to the Variable or Chain with name "
								+ varOrChainOrCommandName + "!");
			}
		} else {
			varOrChainOrCommandName = varOrChainOrCommandStr;
		}

		SetiChain setiChain = null;
		Chain chain = null;
		switch (setiType) {
		case WRITE:
			Variable foundVar = null;
			if ((foundVar = findVariableWithShortName(varOrChainOrCommandName)) != null) {
				Position position = foundVar.getPosition();
				chain = position.getChain();
				setiChain = chainContainer.retrieveSetiChain(seti, chain,
						setiType);
				setiChain.setVariable(foundVar, valuenum);
				Variable fuse_disable_bit = position.getFuse_disable_bit();

				// automatically set the fuse disable bit also if possible!
				if (fuse_disable_bit != null) {
					setiChain.setVariable(fuse_disable_bit, BigInteger.ONE);
				}
			} else if ((chain = findChainWithShortName(varOrChainOrCommandName)) != null) {
				setiChain = new SetiChain(seti, chain, setiType);
				setiChain.setChain(valuenum);
			} else {
				throw new Exception("No Variable or Chain with name "
						+ varOrChainOrCommandName + " found!");
			}
			break;
		case READ:
			if ((chain = findChainWithShortName(varOrChainOrCommandName)) != null) {
				setiChain = chainContainer.retrieveSetiChain(seti, chain,
						setiType);
			} else {
				throw new Exception("No Chain with name "
						+ varOrChainOrCommandName + " found!");
			}
			break;
		case COMMAND:
			Command command = null;
			if ((command = findCommandWithShortName(varOrChainOrCommandName)) != null) {
				setiChain = chainContainer.retrieveSetiChain(seti, command);
			} else {
				throw new Exception("No Command with name "
						+ varOrChainOrCommandName + " found!");
			}
			break;
		}
		return setiChain;
	}

	public VArrayList<Integer> createCBMChain(SymbolString s,
			VArrayList<Integer> cbmChannels, int length, SetiTypeEnum setiType)
			throws Exception {
		SetiChainData setiChainData = createSerialChain(s, setiType);
		ConvertSetiSerial2CBM cbmConverter = new ConvertSetiSerial2CBM(seti);
		return cbmConverter.getCBMContent(cbmChannels, setiChainData, length);
	}

	public VArrayList<Integer> createMRSChain(SymbolString s, int length,
			SetiTypeEnum setiType) throws Exception {
		SetiChainData setiChainData = createSerialChain(s, setiType);
		ConvertSetiSerial2Hex convertSeti2Hex = new ConvertSetiSerial2Hex(seti);
		return convertSeti2Hex.getMRSValues(setiChainData, length);
	}

	/**
	 * @param s
	 * @param setiType
	 * @return
	 * @throws Exception
	 */
	public SetiChainData createSerialChain(SymbolString s, SetiTypeEnum setiType)
			throws Exception {
		String valuestr = s.getValue();
		String[] setiTMArray = valuestr.split(",");
		SetiChainsContainer scC = null;
		if (SettingsSingleton.getInstance().getProperty("mergeseti").equals(
				"true")) {
			scC = new SetiChainsContainerMerged();
		} else {
			scC = new SetiChainsContainerSeparated();
		}
		for (String setiTM : setiTMArray) {
			try {
				constructSetiChain(setiTM.trim(), setiType, scC);
			} catch (Exception e) {
				throw new Exception("Unknown seti-variable " + setiTM.trim(), e);
			}
		}
		LinkedHashMap<Integer, SetiChain> chainList = scC.retrieveAllChains();
		SetiChainData setiChainData = new SetiChainData();
		for (SetiChain setiChain : chainList.values()) {
			setiChainData.append(setiChain.getSetiChainData());
		}
		return setiChainData;
	}

	/**
	 * Find a variable in the seti-xml file
	 * 
	 * @param shortname
	 * @return the found variable or null
	 */
	private Chain findChainWithShortName(String shortname) {
		boolean found = false;
		Chain setiChain = null;
		for (ListIterator<Chain> chainIter = seti.getChains().listIterator(); chainIter
				.hasNext();) {
			setiChain = chainIter.next();
			if (setiChain.getShortname().equalsIgnoreCase(shortname)) {
				found = true;
				break;
			}
		}
		if (found) {
			return setiChain;
		} else {
			return null;
		}
	}

	/**
	 * Find a variable in the seti-xml file
	 * 
	 * @param shortname
	 * @return the found variable or null
	 */
	private Command findCommandWithShortName(String shortname) {
		boolean found = false;
		Command setiVariable = null;
		for (ListIterator<Command> varIter = seti.getCommands().listIterator(); varIter
				.hasNext();) {
			setiVariable = varIter.next();
			if (setiVariable.getShortname().equalsIgnoreCase(shortname)) {
				found = true;
				break;
			}
		}
		if (found) {
			return setiVariable;
		} else {
			return null;
		}
	}

	/**
	 * Find a variable in the seti-xml file
	 * 
	 * @param shortname
	 * @return the found variable or null
	 */
	private Variable findVariableWithShortName(String shortname) {
		boolean found = false;
		Variable setiVariable = null;
		for (ListIterator<Variable> varIter = seti.getVariables()
				.listIterator(); varIter.hasNext();) {
			setiVariable = varIter.next();
			if (setiVariable.getShortname().equalsIgnoreCase(shortname)) {
				found = true;
				break;
			}
		}
		if (found) {
			return setiVariable;
		} else {
			return null;
		}
	}

}
