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

/**
 * @author Jörg Werner
 * 
 */
public class Asdap extends OurTreeNode {

	private ArrayList<OurTreeNode> childs = new ArrayList<OurTreeNode>();

	private int version;

	/**
	 * Constructor
	 */
	public Asdap() {
		super();
	}

	/**
	 * Copy constructor
	 * 
	 * @param a
	 * @param deepCopy
	 */
	public Asdap(Asdap a, boolean deepCopy) {
		super();
		setVersion(a.getVersion());
		if (deepCopy) {
			ArrayList<OurTreeNode> childs_copy = new ArrayList<OurTreeNode>();
			for (OurTreeNode node : a.getChilds()) {
				childs_copy.add(node.deepClone());
			}
			setChilds(childs_copy);
		} else {
			setChilds(null);
		}
	}

	/**
	 * @param version
	 */
	public Asdap(int version) {
		super();
		setVersion(version);
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
		child.setParent(this);
		if (this.childs == null) {
			this.childs = new ArrayList<OurTreeNode>();
		}
		this.childs.add(child);
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
		return new Asdap(this, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.JCombinations.FileFormat.OurTreeNode#deepClone()
	 */
	@Override
	public OurTreeNode deepClone() {
		return new Asdap(this, true);
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
		return this.childs.get(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.tree.TreeNode#getChildCount()
	 */
	public int getChildCount() {
		return this.childs.size();
	}

	/**
	 * @return Returns the childs.
	 */
	public ArrayList<OurTreeNode> getChilds() {
		return this.childs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.tree.TreeNode#getIndex(javax.swing.tree.TreeNode)
	 */
	public int getIndex(TreeNode arg0) {
		return this.childs.indexOf(arg0);
	}

	@Override
	public String getName() {
		return "Root";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.tree.TreeNode#getParent()
	 */
	public OurTreeNode getParent() {
		return null;
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
			return "Root";
		default:
			return "";
		}
	}

	/**
	 * @return Returns the version.
	 */
	public int getVersion() {
		return this.version;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.tree.TreeNode#isLeaf()
	 */
	public boolean isLeaf() {
		return (this.childs.size() == 0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.schreibubi.JCombinations.FileFormat.OurTreeNode#removeChild(org.
	 * schreibubi.JCombinations.FileFormat.OurTreeNode)
	 */
	@Override
	public void removeChild(OurTreeNode child) {
		this.childs.remove(child);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.schreibubi.JCombinations.FileFormat.OurTreeNode#removeChildAt(int)
	 */
	@Override
	public void removeChildAt(int pos) {
		this.childs.remove(pos);
	}

	/**
	 * @param childs
	 *            The childs to set.
	 */
	public void setChilds(ArrayList<OurTreeNode> childs) {
		if (childs != null) {
			for (OurTreeNode node : childs) {
				node.setParent(this);
			}
		}
		this.childs = childs;
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
	}

	/**
	 * @param version
	 *            The version to set.
	 */
	public void setVersion(int version) {
		this.version = version;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getName();
	}

}
