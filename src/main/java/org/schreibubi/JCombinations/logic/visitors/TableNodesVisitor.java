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
import org.schreibubi.JCombinations.FileFormat.Ydata;
import org.schreibubi.JCombinations.logic.TableContent;
import org.schreibubi.symbol.Symbol;
import org.schreibubi.symbol.SymbolString;

/**
 * Collects the data needed for displaying the selected data
 * 
 * @author Jörg Werner
 */
public class TableNodesVisitor implements TreeVisitor {

	ArrayList<TreePath> selection = null;

	ArrayList<TableContent> tableContent = null;

	ArrayList<ArrayList<Double>> table = null;

	ArrayList<Symbol> dutName = null;

	ArrayList<String> seriesMask = null;

	/**
	 * Constructor
	 * 
	 * @param selection
	 *            selection
	 * @param seriesMask
	 *            series Mask
	 * @param tableContent
	 *            Table contents
	 */
	public TableNodesVisitor(ArrayList<TreePath> selection,
			ArrayList<String> seriesMask, ArrayList<TableContent> tableContent) {
		this.tableContent = tableContent;
		this.seriesMask = seriesMask;
		this.selection = selection;
	}

	/**
	 * @return Returns the selection.
	 */
	public ArrayList<TreePath> getSelection() {
		return this.selection;
	}

	/**
	 * @return Returns the tableContent.
	 */
	public ArrayList<TableContent> getTableContent() {
		return this.tableContent;
	}

	/**
	 * @param selection
	 *            The selection to set.
	 */
	public void setSelection(ArrayList<TreePath> selection) {
		this.selection = selection;
	}

	/**
	 * @param tableContent
	 *            The tableContent to set.
	 */
	public void setTableContent(ArrayList<TableContent> tableContent) {
		this.tableContent = tableContent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.schreibubi.JCombinations.FileFormat.TreeVisitor#visit(org.schreibubi
	 * .JCombinations.FileFormat.Alternative)
	 */
	public void visit(Alternative a) throws Exception {
		for (int i = 0; i < a.getChildCount(); i++) {
			(a.getChildAt(i)).accept(this);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.schreibubi.JCombinations.FileFormat.TreeVisitor#visit(org.schreibubi
	 * .JCombinations.FileFormat.Asdap)
	 */
	public void visit(Asdap r) throws Exception {
		for (int i = 0; i < r.getChildCount(); i++) {
			(r.getChildAt(i)).accept(this);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.schreibubi.JCombinations.FileFormat.TreeVisitor#visit(org.schreibubi
	 * .JCombinations.FileFormat.Shmoo)
	 */
	public void visit(Shmoo s) throws Exception {
		if (s.componentSelected(this.selection, OurTreeNode.MYSELF
				| OurTreeNode.PARENTS | OurTreeNode.CHILDS)) {

			this.table = new ArrayList<ArrayList<Double>>();
			this.dutName = new ArrayList<Symbol>();

			for (int i = 0; i < s.getChildCount(); i++) {
				(s.getChildAt(i)).accept(this);
			}
			this.tableContent.add(new TableContent(s.getDescription(),
					this.table, s.getXdataDefault().getLabels(), this.dutName));
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.schreibubi.JCombinations.FileFormat.TreeVisitor#visit(org.schreibubi
	 * .JCombinations.FileFormat.Data)
	 */
	public void visit(Ydata d) throws Exception {
		if (d.componentSelected(this.selection, OurTreeNode.MYSELF
				| OurTreeNode.PARENTS)) {
			if (!this.seriesMask.contains(d.getDescription())
					| d.componentSelected(this.selection, OurTreeNode.MYSELF)) {
				ArrayList<Double> x = d.getDoubleValues();
				// ArrayList< String > t = new ArrayList< String >();
				// for ( Double xp : x ) {
				// t.add( xp.toString() );
				// }
				this.table.add(x);
				this.dutName.add(new SymbolString(d.getName()));
			}
		}
	}

}
