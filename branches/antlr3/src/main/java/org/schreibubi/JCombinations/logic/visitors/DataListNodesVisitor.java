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

import java.util.TreeSet;

import org.schreibubi.JCombinations.FileFormat.Alternative;
import org.schreibubi.JCombinations.FileFormat.Asdap;
import org.schreibubi.JCombinations.FileFormat.Shmoo;
import org.schreibubi.JCombinations.FileFormat.TreeVisitor;
import org.schreibubi.JCombinations.FileFormat.Ydata;


/**
 * Returns a set of all used activeDuts
 * 
 * @author Jörg Werner
 */
public class DataListNodesVisitor implements TreeVisitor {

	TreeSet<String>	dataList	= new TreeSet<String>();

	/**
	 * Constructor
	 */
	public DataListNodesVisitor() {
	}

	/**
	 * @return Returns the dataList.
	 */
	public TreeSet<String> getDataList() {
		return this.dataList;
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
		for (int i = 0; i < s.getChildCount(); i++)
			(s.getChildAt(i)).accept(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.JCombinations.FileFormat.TreeVisitor#visit(org.schreibubi.JCombinations.FileFormat.Data)
	 */
	public void visit(Ydata d) throws Exception {
		this.dataList.add(d.getDescription());
	}

}
