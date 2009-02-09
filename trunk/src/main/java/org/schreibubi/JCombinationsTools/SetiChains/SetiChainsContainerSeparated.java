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
public class SetiChainsContainerSeparated extends SetiChainsContainer {

	private final LinkedHashMap<Integer, SetiChain>	chainHash	= new LinkedHashMap<Integer, SetiChain>();

	private int										count		= 0;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.JCombinationsTools.SetiChains.SetiChainsContainer#retrieveAllChains()
	 */
	@Override
	public LinkedHashMap<Integer, SetiChain> retrieveAllChains() {
		return chainHash;
	}

	@Override
	public SetiChain retrieveSetiChain(Seti seti, Chain chain, SetiTypeEnum setiType) throws Exception {
		SetiChain n = new SetiChain(seti, chain, setiType);
		chainHash.put(count++, n);
		return n;
	}

	@Override
	public SetiChain retrieveSetiChain(Seti seti, Command command) throws Exception {
		SetiChain n = new SetiChain(seti, command);
		chainHash.put(count++, n);
		return n;
	}

}
