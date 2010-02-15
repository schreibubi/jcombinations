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
package org.schreibubi.JCombinationsTools.coordinator;

import org.schreibubi.JCombinationsTools.SetiChains.SetiChain;
import org.schreibubi.visitor.VArrayList;

/**
 * @author Jörg Werner
 * 
 */
public class CBMChunk implements Cloneable {
	private String name;
	private SetiChain.SetiTypeEnum setiType;
	private int start;
	private int length;
	private VArrayList<Integer> cbmContent = new VArrayList<Integer>();

	/**
	 * @param setiType
	 *            r/w/c
	 * 
	 */
	public CBMChunk(String name, SetiChain.SetiTypeEnum command, int start,
			int length) {
		this.name = name;
		this.setiType = command;
		this.start = start;
		this.length = length;
	}

	/**
	 * @param nextChunk
	 */
	public void append(CBMChunk nextChunk) {
		this.length += nextChunk.getLength();
		this.cbmContent.addAll(nextChunk.getCbmContent());
	}

	@Override
	public Object clone() {
		CBMChunk that = new CBMChunk(name, setiType, start, length);
		that.setCbmContent(new VArrayList<Integer>(cbmContent));
		return that;
	}

	public VArrayList<Integer> getCbmContent() {
		return cbmContent;
	}

	public SetiChain.SetiTypeEnum getSetiType() {
		return setiType;
	}

	public int getLength() {
		return length;
	}

	public String getName() {
		return name;
	}

	public int getStart() {
		return start;
	}

	public void setCbmContent(VArrayList<Integer> cbmContent) {
		this.cbmContent = cbmContent;
	}

	public void setSetiType(SetiChain.SetiTypeEnum command) {
		this.setiType = command;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setStart(int start) {
		this.start = start;
	}
}
