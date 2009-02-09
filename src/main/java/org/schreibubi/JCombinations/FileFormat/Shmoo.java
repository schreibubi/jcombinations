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

import org.jfree.chart.plot.Marker;
import org.schreibubi.JCombinations.logic.limits.LimitInterface;
import org.schreibubi.symbol.Symbol;
import org.schreibubi.visitor.VArrayList;
import org.schreibubi.visitor.VHashMap;


/**
 * @author Jörg Werner
 * 
 */
public class Shmoo extends OurTreeNode {

	private String					name			= null;

	private String					description		= null;

	private String					subtitle		= null;

	private String					defaultXdata	= null;

	private String					relativeXdata	= null;

	private VArrayList<Xdata>		xdata			= null;

	private VArrayList<OurTreeNode>	ydata			= new VArrayList<OurTreeNode>();

	private OurTreeNode				parent			= null;

	private LimitInterface			limit			= null;

	private VHashMap<Symbol>		constants		= null;

	private Marker					marker			= null;

	private int						passData		= 0;

	private int						failData		= 0;

	private String					trim;

	private String					measure;

	/**
	 * Constructor
	 */
	public Shmoo() {
		super();
	}

	/**
	 * @param parent
	 * @param name
	 * @param subtitle
	 * @param description
	 * @param trim
	 * @param measure
	 * @param xlabels
	 * @param ydata
	 * @param limit
	 */
	public Shmoo(OurTreeNode parent, String name, String subtitle, String description, String trim, String measure,
			VArrayList<Xdata> xlabels, VArrayList<OurTreeNode> ydata, LimitInterface limit) {
		super();
		setParent(parent);
		setName(name);
		setDescription(description);
		setSubtitle(subtitle);
		setXdata(xlabels);
		setYdata(ydata);
		setLimit(limit);
		setTrim(trim);
		setTrim(measure);
	}

	/**
	 * Copy constructor
	 * 
	 * @param c
	 * @param deepCopy
	 */
	public Shmoo(Shmoo c, boolean deepCopy) {
		super();
		setName(c.getName());
		setDescription(c.getDescription());
		setSubtitle(c.getSubtitle());
		VArrayList<Xdata> xlabels_copy = new VArrayList<Xdata>();
		for (Xdata b : c.getXdata())
			xlabels_copy.add(new Xdata(b));
		setXdata(xlabels_copy);

		if (deepCopy) {

			VArrayList<OurTreeNode> ydata_copy = new VArrayList<OurTreeNode>();
			for (OurTreeNode n : c.getYdata())
				ydata_copy.add(n.deepClone());
			setYdata(ydata_copy);
		} else
			setYdata(null);
		setTrim(c.getTrim());
		setMeasure(c.getMeasure());
		setParent(c.getParent());
		setLimit(c.getLimit());
		setDefaultXdata(c.getDefaultXdata());
		setRelativeXdata(c.getRelativeXdata());
		setConstants(c.getConstants());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.JCombinations.FileFormat.OurTreeNode#accept(org.schreibubi.JCombinations.FileFormat.TreeVisitor)
	 */
	@Override
	public void accept(TreeVisitor v) throws Exception {
		v.visit(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.JCombinations.FileFormat.OurTreeNode#addChild(org.schreibubi.JCombinations.FileFormat.OurTreeNode)
	 */
	@Override
	public void addChild(OurTreeNode child) {
		if (this.ydata == null)
			this.ydata = new VArrayList<OurTreeNode>();
		this.ydata.add(child);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.tree.TreeNode#children()
	 */
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
		return new Shmoo(this, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.JCombinations.FileFormat.OurTreeNode#deepClone()
	 */
	@Override
	public OurTreeNode deepClone() {
		return new Shmoo(this, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.tree.TreeNode#getAllowsChildren()
	 */
	public boolean getAllowsChildren() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.tree.TreeNode#getChildAt(int)
	 */
	public OurTreeNode getChildAt(int arg0) {
		return this.ydata.get(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.tree.TreeNode#getChildCount()
	 */
	public int getChildCount() {
		return this.ydata.size();
	}

	/**
	 * @return Returns the constants.
	 */
	public VHashMap<Symbol> getConstants() {
		return this.constants;
	}

	/**
	 * @return Returns the defaultXdata.
	 */
	public String getDefaultXdata() {
		return this.defaultXdata;
	}

	/**
	 * @return Returns the xlabels.
	 */
	public int getDefaultXdataPos() {
		for (int i = 0; i < this.xdata.size(); i++)
			if (this.xdata.get(i).getName().equals(this.defaultXdata))
				return i;
		return -1;
	}

	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return this.description;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.tree.TreeNode#getIndex(javax.swing.tree.TreeNode)
	 */
	public int getIndex(TreeNode arg0) {
		return this.ydata.indexOf(arg0);
	}

	/**
	 * @return Returns the limit.
	 */
	public LimitInterface getLimit() {
		return this.limit;
	}

	/**
	 * @return Returns the marker.
	 */
	public Marker getMarker() {
		return this.marker;
	}

	/**
	 * @return Returns the measure.
	 */
	public String getMeasure() {
		return this.measure;
	}

	/**
	 * @return Returns the name.
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/**
	 * @return Returns the parent.
	 */
	public OurTreeNode getParent() {
		return this.parent;
	}

	/**
	 * @return Returns the relativeXdata.
	 */
	public String getRelativeXdata() {
		return this.relativeXdata;
	}

	/**
	 * @return Returns the xlabels.
	 */
	public int getRelativeXdataPos() {
		for (int i = 0; i < this.xdata.size(); i++)
			if (this.xdata.get(i).getName().equals(this.relativeXdata))
				return i;
		return -1;
	}

	/**
	 * @return Returns the subtitle.
	 */
	public String getSubtitle() {
		return this.subtitle;
	}

	/**
	 * @return Returns the trim.
	 */
	public String getTrim() {
		return this.trim;
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
		case 1:
			return getTrim();
		case 2:
			return getMeasure();
		case 3:
			return this.passData;
		case 4:
			return this.failData;
		case 5:
			return getChildCount() - this.passData - this.failData;
		default:
			return "";
		}

	}

	/**
	 * @return Returns the xlabels.
	 */
	public ArrayList<Xdata> getXdata() {
		return this.xdata;
	}

	/**
	 * @return Returns the xlabels.
	 */
	public Xdata getXdataDefault() {
		for (Xdata xd : this.xdata)
			if (xd.getName().equals(this.defaultXdata))
				return xd;
		return null;
	}

	/**
	 * @return Returns the ydata.
	 */
	public ArrayList<OurTreeNode> getYdata() {
		return this.ydata;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.tree.TreeNode#isLeaf()
	 */
	public boolean isLeaf() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.JCombinations.FileFormat.OurTreeNode#removeChild(org.schreibubi.JCombinations.FileFormat.OurTreeNode)
	 */
	@Override
	public void removeChild(OurTreeNode child) {
		this.ydata.remove(child);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.JCombinations.FileFormat.OurTreeNode#removeChildAt(int)
	 */
	@Override
	public void removeChildAt(int pos) {
		this.ydata.remove(pos);
	}

	/**
	 * @param constants
	 *            The constants to set.
	 */
	public void setConstants(VHashMap<Symbol> constants) {
		this.constants = constants;
	}

	/**
	 * @param defaultXdata
	 *            The defaultXdata to set.
	 */
	public void setDefaultXdata(String defaultXdata) {
		this.defaultXdata = defaultXdata;
	}

	/**
	 * @param description
	 *            The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @param limit
	 *            The limit to set.
	 */
	public void setLimit(LimitInterface limit) {
		this.limit = limit;
		try {
			setMarker(limit.calcLimits(getXdata(), getDefaultXdataPos(), getRelativeXdataPos(), getConstants()));
			this.passData = 0;
			this.failData = 0;
			for (int i = 0; i < getChildCount(); i++) {
				Ydata d = (Ydata) getChildAt(i);
				if (limit.judgeDataSeries(getXdata(), getDefaultXdataPos(), getRelativeXdataPos(), getConstants(), d
						.getValues()))
					this.passData++;
				else
					this.failData++;
			}
		} catch (Exception e) {
		}
	}

	/**
	 * @param marker
	 *            The marker to set.
	 */
	public void setMarker(Marker marker) {
		this.marker = marker;
	}

	/**
	 * @param measure
	 *            The measure to set.
	 */
	public void setMeasure(String measure) {
		this.measure = measure;
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
	 * @see org.schreibubi.JCombinations.FileFormat.OurTreeNode#setParent(org.schreibubi.JCombinations.FileFormat.OurTreeNode)
	 */
	@Override
	public void setParent(OurTreeNode parent) {
		this.parent = parent;
	}

	/**
	 * @param relativeXdata
	 *            The relativeXdata to set.
	 */
	public void setRelativeXdata(String relativeXdata) {
		this.relativeXdata = relativeXdata;
	}

	/**
	 * @param subtitle
	 *            The subtitle to set.
	 */
	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	/**
	 * @param trim
	 *            The trim to set.
	 */
	public void setTrim(String trim) {
		this.trim = trim;
	}

	/**
	 * @param xdata
	 *            The xlabels to set.
	 */
	public void setXdata(VArrayList<Xdata> xdata) {
		this.xdata = xdata;
	}

	/**
	 * @param ydata
	 *            The ydata to set.
	 */
	public void setYdata(VArrayList<OurTreeNode> ydata) {
		if (ydata != null)
			for (OurTreeNode node : ydata)
				node.setParent(this);
		this.ydata = ydata;
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
