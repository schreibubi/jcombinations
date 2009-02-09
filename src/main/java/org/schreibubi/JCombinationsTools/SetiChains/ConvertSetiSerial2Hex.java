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

import org.schreibubi.JCombinationsTools.SetiFormat.Seti;
import org.schreibubi.visitor.VArrayList;

/**
 * @author Jörg Werner
 * 
 */
public class ConvertSetiSerial2Hex extends SetiSerial2Converter {

	public ConvertSetiSerial2Hex(Seti seti) {
		super(seti);
	}

	public String getHexString(SetiChainData setiChainData, int hexLength) throws Exception {
		if (hexLength * parallelWidth < setiChainData.getSize())
			throw new Exception("HEX space too short for the used chain");

		boolean[] resulting_data = multiplexChain(setiChainData, hexLength);

		// convert to a hex result string
		String resstr = "";
		for (int i = 0; i < hexLength; i++) {
			String lstr = "";
			for (int k = 0; k < 32 / 8; k++) {
				int r = 0;
				for (int j = 0; j < 8; j++) {
					r = r + (int) Math.pow(2, j) * (resulting_data[i * 32 + k * 8 + j] ? 1 : 0);
				}
				lstr = ((char) r) + lstr;
			}
			resstr += lstr;
		}
		return resstr;

	}

	public VArrayList<Integer> getMRSValues(SetiChainData setiChainData, int hexLength) throws Exception {

		// if hex length is negative we automatically determine the required length.
		if (hexLength < 0) {
			hexLength = (setiChainData.getSize() - 1) / parallelWidth + 1;
		}

		if (hexLength * parallelWidth < setiChainData.getSize())
			throw new Exception("HEX space too short for the used chain");

		boolean[] resulting_data = multiplexChain(setiChainData, hexLength);

		VArrayList<Integer> iList = new VArrayList<Integer>();
		// convert to a hex result string
		for (int i = 0; i < hexLength; i++) {
			int r = 0;
			for (int k = 0; k < 32; k++) {
				r = r + (int) Math.pow(2, k) * (resulting_data[i * 32 + k] ? 1 : 0);
			}
			iList.add(r);
		}
		return iList;

	}

	/**
	 * @param setiChainData
	 * @param hexLength
	 * @return
	 */
	private boolean[] multiplexChain(SetiChainData setiChainData, int hexLength) {
		boolean[] resulting_data = new boolean[hexLength * parallelWidth * 32];

		// parallelize the serial stream
		for (int i = 0; i < hexLength; i++) {
			for (int j = 0; j < parallelWidth; j++) {
				if (i * parallelWidth + j < setiChainData.getSize()) {
					resulting_data[i * 32 + commandPins.get(j)] = setiChainData.getCommandBit(i * parallelWidth + j);
					resulting_data[i * 32 + dataPins.get(j)] = setiChainData.getDataBit(i * parallelWidth + j);
					resulting_data[i * 32 + maskPins.get(j)] = !setiChainData.getDataMaskBit(i * parallelWidth + j);
				} else {
					// hex is longer than seti chain so we fill it up with default values
					resulting_data[i * 32 + commandPins.get(j)] = false;
					resulting_data[i * 32 + dataPins.get(j)] = false;
					resulting_data[i * 32 + maskPins.get(j)] = true;
				}
			}
			for (int j = 0; j < fixedHighPins.size(); j++) {
				resulting_data[i * 32 + fixedHighPins.get(j)] = true;
			}
			for (int j = 0; j < fixedLowPins.size(); j++) {
				resulting_data[i * 32 + fixedLowPins.get(j)] = false;
			}
		}
		return resulting_data;
	}
}
