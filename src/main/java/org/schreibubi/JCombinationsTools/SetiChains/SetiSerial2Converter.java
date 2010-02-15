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

import org.schreibubi.JCombinationsTools.SetiFormat.PinAssignment;
import org.schreibubi.JCombinationsTools.SetiFormat.Seti;
import org.schreibubi.visitor.VArrayList;

/**
 * @author Jörg Werner
 * 
 */
public class SetiSerial2Converter {

	protected int parallelWidth = 4;
	protected VArrayList<Integer> fixedHighPins = new VArrayList<Integer>();
	protected VArrayList<Integer> fixedLowPins = new VArrayList<Integer>();
	protected VArrayList<Integer> commandPins = new VArrayList<Integer>();
	protected VArrayList<Integer> maskPins = new VArrayList<Integer>();
	protected VArrayList<Integer> dataPins = new VArrayList<Integer>();

	/**
	 * 
	 */
	public SetiSerial2Converter(Seti seti) {
		// set defaults for H90, since it is not listed in xml-file
		Collections.addAll(commandPins, 3, 2, 1, 0);
		Collections.addAll(maskPins, 8, 6, 5, 4);
		Collections.addAll(dataPins, 12, 11, 10, 9);
		Collections.addAll(fixedHighPins, 7);
		parallelWidth = 4;

		PinAssignment p = seti.getPinassignment("MRS2SETI");
		if (p != null) {
			commandPins = p.getBitgroup("command").getPinsInOrder();
			maskPins = p.getBitgroup("mask").getPinsInOrder();
			dataPins = p.getBitgroup("data").getPinsInOrder();
			if (p.getBitgroup("constant_high") != null) {
				fixedHighPins = p.getBitgroup("constant_high").getPins();
			} else {
				fixedHighPins = new VArrayList<Integer>();
			}
			if (p.getBitgroup("constant_low") != null) {
				fixedLowPins = p.getBitgroup("constant_low").getPins();
			} else {
				fixedLowPins = new VArrayList<Integer>();
			}

			parallelWidth = commandPins.size();
		}
	}

}