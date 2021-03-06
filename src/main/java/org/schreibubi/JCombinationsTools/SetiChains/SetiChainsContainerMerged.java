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

import java.util.LinkedHashMap;

import org.schreibubi.JCombinationsTools.SetiChains.SetiChain.SetiTypeEnum;
import org.schreibubi.JCombinationsTools.SetiFormat.Chain;
import org.schreibubi.JCombinationsTools.SetiFormat.Command;
import org.schreibubi.JCombinationsTools.SetiFormat.Seti;

/**
 * @author Jörg Werner
 * 
 */
public class SetiChainsContainerMerged extends SetiChainsContainer {

	private final LinkedHashMap<Integer, SetiChain> chainHash = new LinkedHashMap<Integer, SetiChain>();

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.schreibubi.JCombinationsTools.SetiChains.SetiChainsContainer#
	 * retrieveAllChains()
	 */
	@Override
	public LinkedHashMap<Integer, SetiChain> retrieveAllChains() {
		return chainHash;
	}

	@Override
	public SetiChain retrieveSetiChain(Seti seti, Chain chain,
			SetiTypeEnum setiType) throws Exception {
		int key = chain.getDecodedAddress() + 10000 * setiType.ordinal();
		if (chainHash.get(key) == null) {
			SetiChain n = new SetiChain(seti, chain, setiType);
			chainHash.put(key, n);
			return n;
		} else {
			return chainHash.get(key);
		}
	}

	@Override
	public SetiChain retrieveSetiChain(Seti seti, Command command)
			throws Exception {
		int key = command.getDecodedCode() + 100000;
		if (chainHash.get(key) == null) {
			SetiChain n = new SetiChain(seti, command);
			chainHash.put(key, n);
			return n;
		} else {
			return chainHash.get(key);
		}
	}

}
