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
package org.schreibubi.JCombinationsTools.SetiFormat;

import java.util.Collections;
import java.util.Comparator;

import org.schreibubi.visitor.VArrayList;


/**
 * @author Jörg Werner
 * 
 */
public class Bitgroup {

	private String			type;
	private VArrayList<Pin>	pins	= new VArrayList<Pin>();

	public VArrayList<Integer> getPins() {
		VArrayList<Integer> l = new VArrayList<Integer>();
		for (Pin p : pins) {
			String pname = p.getPinname();
			if (pname.toUpperCase().substring(0, 3).equals("ADD"))
				l.add(Integer.parseInt(pname.substring(3)));
		}
		return l;
	}

	public VArrayList<Integer> getPinsInOrder() {
		Collections.sort(pins, new Comparator<Pin>() {
			public int compare(Pin o1, Pin o2) {
				return o2.getBit() - o1.getBit();
			}
		});
		VArrayList<Integer> l = new VArrayList<Integer>();
		for (Pin p : pins)
			l.add(Integer.parseInt(p.getPinname().substring(3)));
		return l;
	}

	public String getType() {
		return type;
	}

	public void setPins(VArrayList<Pin> pins) {
		this.pins = pins;
	}

	public void setType(String type) {
		this.type = type;
	}

}
