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
package org.schreibubi.JCombinations.logic.visitors;

import java.util.ArrayList;

import javax.swing.tree.TreePath;

import org.schreibubi.JCombinations.FileFormat.Alternative;
import org.schreibubi.JCombinations.FileFormat.Asdap;
import org.schreibubi.JCombinations.FileFormat.OurTreeNode;
import org.schreibubi.JCombinations.FileFormat.Shmoo;
import org.schreibubi.JCombinations.FileFormat.TreeVisitor;
import org.schreibubi.JCombinations.FileFormat.Xdata;
import org.schreibubi.JCombinations.FileFormat.Ydata;


/**
 * Creates the charts from all selected nodes
 * 
 * @author Jörg Werner
 * 
 */
public class XdataListNodesVisitor implements TreeVisitor {

	ArrayList<TreePath>	treePaths	= null;

	ArrayList<String>	xdataList	= null;

	/**
	 * Constructor
	 * 
	 * @param treePaths
	 *            selected paths
	 */
	public XdataListNodesVisitor(ArrayList<TreePath> treePaths) {
		this.treePaths = treePaths;
	}

	/**
	 * @return Returns the dataList.
	 */
	public ArrayList<String> getXdataList() {
		return this.xdataList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.JCombinations.FileFormat.TreeVisitor#visit(org.schreibubi.JCombinations.FileFormat.Alternative)
	 */
	public void visit(Alternative a) throws Exception {
		for (int i = 0; i < a.getChildCount(); i++)
			(a.getChildAt(i)).accept(this);
	}

	public void visit(Asdap r) throws Exception {
		for (int i = 0; i < r.getChildCount(); i++)
			(r.getChildAt(i)).accept(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.JCombinations.FileFormat.TreeVisitor#visit(org.schreibubi.JCombinations.FileFormat.Shmoo)
	 */
	public void visit(Shmoo s) throws Exception {
		if (s.componentSelected(this.treePaths, OurTreeNode.MYSELF | OurTreeNode.PARENTS | OurTreeNode.CHILDS)) {
			ArrayList<Xdata> xdata = s.getXdata();
			ArrayList<String> xdataNames = new ArrayList<String>();
			for (Xdata x : xdata)
				xdataNames.add(x.getDescription());
			if (this.xdataList == null)
				this.xdataList = xdataNames;
			else
				this.xdataList.retainAll(xdataNames);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.JCombinations.FileFormat.TreeVisitor#visit(org.schreibubi.JCombinations.FileFormat.Data)
	 */
	public void visit(Ydata d) throws Exception {
	}

}
