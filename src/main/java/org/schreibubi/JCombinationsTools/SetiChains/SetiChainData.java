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
import java.util.Vector;

/**
 * @author Jörg Werner
 * 
 */
public class SetiChainData {
	private final Vector<Boolean> commandsChain = new Vector<Boolean>();
	private final Vector<Boolean> dataMaskChain = new Vector<Boolean>();
	private final Vector<Boolean> dataChain = new Vector<Boolean>();

	public void append(SetiChainData other) {
		commandsChain.addAll(other.getCommands());
		dataMaskChain.addAll(other.getDataMask());
		dataChain.addAll(other.getData());
	}

	public boolean getCommandBit(int bit) {
		return commandsChain.get(bit);
	}

	public boolean getDataBit(int bit) {
		return dataChain.get(bit);
	}

	public boolean getDataMaskBit(int bit) {
		return dataMaskChain.get(bit);
	}

	public int getSize() {
		return commandsChain.size();
	}

	public void setBit(int bit, boolean command, boolean data, boolean mask) {
		int size = getSize();
		if (size <= bit) {
			commandsChain.addAll(Collections.nCopies(bit - size + 1, false));
			dataChain.addAll(Collections.nCopies(bit - size + 1, false));
			dataMaskChain.addAll(Collections.nCopies(bit - size + 1, false));
		}
		commandsChain.set(bit, command);
		dataChain.set(bit, data);
		dataMaskChain.set(bit, mask);
	}

	private Vector<Boolean> getCommands() {
		return commandsChain;
	}

	private Vector<Boolean> getData() {
		return dataChain;
	}

	private Vector<Boolean> getDataMask() {
		return dataMaskChain;
	}
}
