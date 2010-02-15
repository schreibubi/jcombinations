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
public class ConvertSetiSerial2CBM extends SetiSerial2Converter {

	public ConvertSetiSerial2CBM(Seti seti) {
		super(seti);
	}

	public VArrayList<Integer> getCBMContent(VArrayList<Integer> cbmChannels,
			SetiChainData setiChainData, int cbmLength) throws Exception {
		// cbmChannels command mask data constant_high constant_low

		if (cbmLength * parallelWidth < setiChainData.getSize()) {
			throw new Exception("CBM space too short for the used chain, "
					+ cbmLength * parallelWidth + " available, "
					+ setiChainData.getSize() + " required!");
		}

		boolean[] resulting_data = new boolean[2 * cbmLength * 32];
		// parallelize the serial stream
		for (int i = 0; i < cbmLength; i++) {
			int c = 0;
			for (int j = 0; j < parallelWidth; j++) {
				if (i * parallelWidth + j < setiChainData.getSize()) {
					c = cbmChannels.get(j);
					resulting_data[cbmLength * 32 * (c / 18) + i * 32 + c % 18] = setiChainData
							.getCommandBit(i * parallelWidth + j);
					c = cbmChannels.get(j + parallelWidth);
					resulting_data[cbmLength * 32 * (c / 18) + i * 32 + c % 18] = !setiChainData
							.getDataMaskBit(i * parallelWidth + j);
					c = cbmChannels.get(j + 2 * parallelWidth);
					resulting_data[cbmLength * 32 * (c / 18) + i * 32 + c % 18] = setiChainData
							.getDataBit(i * parallelWidth + j);
				} else {
					// cbm is longer than seti chain so we fill it up with
					// default values
					c = cbmChannels.get(j);
					resulting_data[cbmLength * 32 * (c / 18) + i * 32 + c % 18] = false;
					c = cbmChannels.get(j + parallelWidth);
					resulting_data[cbmLength * 32 * (c / 18) + i * 32 + c % 18] = true;
					c = cbmChannels.get(j + 2 * parallelWidth);
					resulting_data[cbmLength * 32 * (c / 18) + i * 32 + c % 18] = false;
				}
			}
			for (int j = 0; j < fixedHighPins.size(); j++) {
				c = cbmChannels.get(j + 3 * parallelWidth);
				resulting_data[cbmLength * 32 * (c / 18) + i * 32 + c % 18] = true;
			}
			for (int j = 0; j < fixedLowPins.size(); j++) {
				c = cbmChannels.get(j + 3 * parallelWidth
						+ fixedHighPins.size());
				resulting_data[cbmLength * 32 * (c / 18) + i * 32 + c % 18] = false;
			}
		}

		/*
		 * for (int i = 0; i < 2 * cbmLength; i++) { for (int j = 0; j < 32;
		 * j++) { System.out.print(resulting_data[i * 32 + j] ? "1" : "0"); }
		 * System.out.println(); }
		 */

		VArrayList<Integer> cbmData = new VArrayList<Integer>();
		for (int i = 0; i < 2 * cbmLength; i++) {
			int accumulate = 0;
			for (int j = 0; j < 32; j++) {
				if (resulting_data[i * 32 + j]) {
					accumulate += 1 << j;
				}
			}
			cbmData.add(accumulate);
		}
		return cbmData;

	}

}
