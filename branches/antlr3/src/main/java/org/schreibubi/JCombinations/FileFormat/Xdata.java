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
package org.schreibubi.JCombinations.FileFormat;

import java.util.ArrayList;

import org.schreibubi.symbol.Symbol;
import org.schreibubi.visitor.VArrayList;


/**
 * @author Jörg Werner
 * 
 */
public class Xdata {

	private String				name		= null;

	private String				description	= null;

	private String				unit		= null;

	private VArrayList<Symbol>	labels		= new VArrayList<Symbol>();

	private VArrayList<Double>	xPositions	= new VArrayList<Double>();

	/**
	 * Constructor
	 */
	public Xdata() {
		super();
	}

	/**
	 * @param name
	 * @param description
	 * @param unit
	 * @param labels
	 * @param positions
	 */
	public Xdata(String name, String description, String unit, VArrayList<Symbol> labels, VArrayList<Double> positions) {
		super();
		setName(name);
		setDescription(description);
		setUnit(unit);
		setLabels(labels);
		setXPositions(positions);
	}

	/**
	 * Copy constructor
	 * 
	 * @param l
	 */
	public Xdata(Xdata l) {
		super();
		setName(l.getName());
		setDescription(l.getDescription());
		setUnit(l.getUnit());
		VArrayList<Symbol> labels_copy = new VArrayList<Symbol>();
		for (Symbol s : l.getLabels())
			labels_copy.add(s);
		setLabels(labels_copy);
		setXPositions(l.getXPositions());
	}

	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * @return Returns the labels.
	 */
	public ArrayList<Symbol> getLabels() {
		return this.labels;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @return Returns the unit.
	 */
	public String getUnit() {
		return this.unit;
	}

	/**
	 * @return Returns the xPositions.
	 */
	public VArrayList<Double> getXPositions() {
		return this.xPositions;
	}

	/**
	 * @param description
	 *            The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @param labels
	 *            The labels to set.
	 */
	public void setLabels(VArrayList<Symbol> labels) {
		this.labels = labels;
	}

	/**
	 * @param name
	 *            The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param unit
	 *            The unit to set.
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}

	/**
	 * @param positions
	 *            The xPositions to set.
	 */
	public void setXPositions(VArrayList<Double> positions) {
		this.xPositions = positions;
	}

}
