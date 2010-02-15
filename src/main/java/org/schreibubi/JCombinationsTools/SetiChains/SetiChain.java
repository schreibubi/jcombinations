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

import java.math.BigInteger;

import org.schreibubi.JCombinationsTools.SetiFormat.Chain;
import org.schreibubi.JCombinationsTools.SetiFormat.Command;
import org.schreibubi.JCombinationsTools.SetiFormat.Position;
import org.schreibubi.JCombinationsTools.SetiFormat.Seti;
import org.schreibubi.JCombinationsTools.SetiFormat.Variable;

/**
 * @author Jörg Werner
 * 
 */
public class SetiChain {

	public enum SetiTypeEnum {
		READ, WRITE, COMMAND
	}

	private boolean onlyHeader = false;

	private int chainOffset;
	private int bitChainLength;

	private final Chain chain;

	private final SetiChainData setiChainData = new SetiChainData();

	/**
	 * @param seti
	 * @param chain
	 * @param setiChainLen
	 * @throws Exception
	 */
	public SetiChain(Seti seti, Chain chain, SetiTypeEnum setiType)
			throws Exception {
		this.chain = chain;
		initChain(chain.getDecodedAddress(), setiType);
	}

	public SetiChain(Seti seti, Command command) throws Exception {
		this.chain = null;
		initChain(command.getDecodedCode(), SetiTypeEnum.COMMAND);
	}

	/**
	 * @return the chain
	 */
	public Chain getChain() {
		return chain;
	}

	public SetiChainData getSetiChainData() {
		return setiChainData;
	}

	/**
	 * @param valuenum
	 * @throws Exception
	 */
	public void setChain(BigInteger value) throws Exception {
		if (onlyHeader) {
			throw new Exception("Not possible for this kind of chain!");
		}
		// activate the command bits for the whole chain
		for (int i = 0; i < bitChainLength; i++) {
			setiChainData.setBit(chainOffset + i, true, value
					.testBit(bitChainLength - 1 - i), true);
		}
	}

	public void setVariable(Variable var, BigInteger value) throws Exception {
		if (onlyHeader) {
			throw new Exception("Not possible for this kind of chain!");
		}
		Position position = var.getPosition();
		if (position.getChain().getDecodedAddress() != chain
				.getDecodedAddress()) {
			throw new Exception("Variable " + var.getShortname()
					+ " can't be found in Chain " + chain.getShortname() + "!");
		}
		int bitStartOffsetInChain = bitChainLength - 1
				- position.getDecodedPosition();
		int bitStopOffsetInChain = bitChainLength
				- 1
				- (position.getDecodedPosition() - position.getDecodedLength() + 1);
		int bitInChainLength = bitStopOffsetInChain - bitStartOffsetInChain + 1;

		// set mask and data
		for (int i = 0; i < bitInChainLength; i++) {
			setiChainData.setBit(chainOffset + bitStartOffsetInChain + i, true,
					value.testBit(bitInChainLength - 1 - i), true);
		}
	}

	/**
	 * @param chain
	 * @throws Exception
	 */
	private void initChain(int chainOrCommandNumber, SetiTypeEnum setiType)
			throws Exception {
		boolean isWrite = false;
		boolean isCommand = false;
		// offset for the chain start in MRS2Seti compared to the serial
		// interface
		int setiMRS2SetiHeaderOffset = 2;

		// number of bits occupied by the seti chain number in the chain
		int setiChainVariableLength = 6;

		switch (setiType) {
		case READ:
			isWrite = false;
			onlyHeader = true;
			isCommand = false;
			setiMRS2SetiHeaderOffset = 2;
			setiChainVariableLength = 6;
			break;
		case WRITE:
			isWrite = true;
			onlyHeader = false;
			isCommand = false;
			setiMRS2SetiHeaderOffset = 2;
			setiChainVariableLength = 6;
			break;
		case COMMAND:
			isWrite = true;
			onlyHeader = true;
			isCommand = true;
			setiMRS2SetiHeaderOffset = 1;
			setiChainVariableLength = 7;
			break;
		}

		// start bit
		int startBitOffset = setiMRS2SetiHeaderOffset;
		setiChainData.setBit(startBitOffset, true, false, false);

		// command type
		int commandTypeOffset = setiMRS2SetiHeaderOffset + 1;
		setiChainData.setBit(commandTypeOffset, !isCommand, false, false);

		// chain number (MSB to LSB!)
		int chainNumberOffset = commandTypeOffset + 1;
		for (int i = 0; i < setiChainVariableLength; i++) {
			setiChainData.setBit(chainNumberOffset + i,
					((chainOrCommandNumber & (int) Math.pow(2,
							setiChainVariableLength - 1 - i)) > 0), false,
					false);
		}

		// seti read (0)/write (1)
		int setiReadWriteOffset = chainNumberOffset + setiChainVariableLength;
		setiChainData.setBit(setiReadWriteOffset, isWrite, false, false);

		// shift enable
		int shiftEnableOffset = setiReadWriteOffset + 1;
		setiChainData.setBit(shiftEnableOffset, !isCommand, false, false);

		if (!onlyHeader) {
			chainOffset = shiftEnableOffset + 1;
			bitChainLength = chain.getDecodedLength();
			// activate the command bits for the whole chain
			for (int i = 0; i < bitChainLength; i++) {
				setiChainData.setBit(chainOffset + i, true, false, false);
			}

			// stop bit
			int stopBitOffset = chainOffset + bitChainLength;
			setiChainData.setBit(stopBitOffset, false, false, false);
		}
	}

}
