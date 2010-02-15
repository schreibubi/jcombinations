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
import java.util.Enumeration;

import javax.swing.tree.TreeNode;

import org.schreibubi.symbol.Symbol;

/**
 * @author Jörg Werner
 * 
 */
public class Ydata extends OurTreeNode {

	private String name = null;

	private String description = null;

	private ArrayList<Symbol> values = null;

	private OurTreeNode parent;

	private String unit = null;

	/**
	 * Constructor
	 */
	public Ydata() {
		super();

	}

	/**
	 * @param parent
	 * @param name
	 * @param description
	 * @param unit
	 * @param values
	 */
	public Ydata(OurTreeNode parent, String name, String description,
			String unit, ArrayList<Symbol> values) {
		super();
		setParent(parent);
		setName(name);
		setDescription(description);
		setUnit(unit);
		setValues(values);
	}

	/**
	 * Copy constructor
	 * 
	 * @param d
	 * @param deepCopy
	 */
	public Ydata(Ydata d, boolean deepCopy) {
		super();
		setName(d.getName());
		setDescription(d.getDescription());
		setUnit(d.getUnit());
		if (deepCopy) {
			ArrayList<Symbol> values_copy = new ArrayList<Symbol>();
			for (Symbol b : d.getValues()) {
				values_copy.add(b);
			}
			setValues(values_copy);
		} else {
			setValues(d.getValues());
		}
		setParent(d.getParent());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.schreibubi.JCombinations.FileFormat.OurTreeNode#accept(org.schreibubi
	 * .JCombinations.FileFormat.TreeVisitor)
	 */
	@Override
	public void accept(TreeVisitor v) throws Exception {
		v.visit(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.schreibubi.JCombinations.FileFormat.OurTreeNode#addChild(org.schreibubi
	 * .JCombinations.FileFormat.OurTreeNode)
	 */
	@Override
	public void addChild(OurTreeNode child) {
	}

	public Enumeration children() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public OurTreeNode clone() {
		return new Ydata(this, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.JCombinations.FileFormat.OurTreeNode#deepClone()
	 */
	@Override
	public OurTreeNode deepClone() {
		return new Ydata(this, true);
	}

	public boolean getAllowsChildren() {
		return false;
	}

	public TreeNode getChildAt(int arg0) {
		return null;
	}

	public int getChildCount() {
		return 0;
	}

	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * @return Returns the values.
	 */
	public ArrayList<Double> getDoubleValues() {
		ArrayList<Double> d = new ArrayList<Double>();
		for (Symbol sym : this.values) {
			try {
				d.add(sym.convertToDouble().getValue());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return d;
	}

	public int getIndex(TreeNode arg0) {
		return 0;
	}

	/**
	 * @return Returns the name.
	 */
	@Override
	public String getName() {
		return this.name;
	}

	public OurTreeNode getParent() {
		return this.parent;
	}

	/**
	 * @return Returns the unit.
	 */
	public String getUnit() {
		return this.unit;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.JCombinations.FileFormat.OurTreeNode#getValueAt(int)
	 */
	@Override
	public Object getValueAt(int column) {
		switch (column) {
		case 0:
			return getName();
		default:
			return "";
		}
	}

	/**
	 * @return Returns the values.
	 */
	public ArrayList<Symbol> getValues() {
		return this.values;
	}

	public boolean isLeaf() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.schreibubi.JCombinations.FileFormat.OurTreeNode#removeChild(org.
	 * schreibubi.JCombinations.FileFormat.OurTreeNode)
	 */
	@Override
	public void removeChild(OurTreeNode child) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.schreibubi.JCombinations.FileFormat.OurTreeNode#removeChildAt(int)
	 */
	@Override
	public void removeChildAt(int pos) {
	}

	/**
	 * @param description
	 *            The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @param name
	 *            The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.schreibubi.JCombinations.FileFormat.OurTreeNode#setParent(org.schreibubi
	 * .JCombinations.FileFormat.OurTreeNode)
	 */
	@Override
	public void setParent(OurTreeNode parent) {
		this.parent = parent;
	}

	/**
	 * @param unit
	 *            The unit to set.
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}

	/**
	 * @param values
	 *            The values to set.
	 */
	public void setValues(ArrayList<Symbol> values) {
		this.values = values;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getDescription();
	}

}
