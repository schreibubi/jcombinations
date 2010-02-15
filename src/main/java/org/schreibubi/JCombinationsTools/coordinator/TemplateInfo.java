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

import org.schreibubi.visitor.VArrayList;

/**
 * @author Jörg Werner
 * 
 */
public class TemplateInfo {

	VArrayList<CBMChunk> cbms = null;
	VArrayList<Integer> channels = null;

	/**
	 * 
	 */
	public TemplateInfo(VArrayList<CBMChunk> cbms, VArrayList<Integer> channels) {
		this.cbms = cbms;
		this.channels = channels;
	}

	public VArrayList<String> getCbmNames() {
		VArrayList<String> names = new VArrayList<String>();
		for (CBMChunk c : cbms) {
			names.add(c.getName());
		}
		return names;
	}

	public VArrayList<CBMChunk> getCbms() {
		return cbms;
	}

	public VArrayList<Integer> getChannels() {
		return channels;
	}

	public void setCbms(VArrayList<CBMChunk> cbms) {
		this.cbms = cbms;
	}

	public void setChannels(VArrayList<Integer> channels) {
		this.channels = channels;
	}

}
