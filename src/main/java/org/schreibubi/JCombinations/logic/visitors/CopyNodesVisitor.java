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

/**
 * @author Jörg Werner
 * 
 */
public class CopyNodesVisitor implements TreeVisitor {

	private ArrayList<TreePath> selection = null;

	private OurTreeNode topNode = null;

	private OurTreeNode parent = null;

	/**
	 * @param selection
	 */
	public CopyNodesVisitor(ArrayList<TreePath> selection) {
		this.selection = selection;
	}

	/**
	 * @return Returns the topNode.
	 */
	public OurTreeNode getTopNode() {
		return this.topNode;
	}

	public void visit(Alternative a) throws Exception {
		if (a.componentSelected(this.selection, OurTreeNode.MYSELF
				| OurTreeNode.PARENTS)) {
			Alternative alt = new Alternative(a, true);
			this.parent.addChild(alt);
		} else if (a.componentSelected(this.selection, OurTreeNode.CHILDS)) {
			Alternative alt = new Alternative(a, false);
			this.parent.addChild(alt);
			for (int i = 0; i < a.getChildCount(); i++) {
				this.parent = alt;
				(a.getChildAt(i)).accept(this);
			}
		}
	}

	public void visit(Asdap r) throws Exception {
		if (r.componentSelected(this.selection, OurTreeNode.MYSELF
				| OurTreeNode.PARENTS)) {
			this.topNode = new Asdap(r, true);
		} else if (r.componentSelected(this.selection, OurTreeNode.CHILDS)) {
			this.topNode = new Asdap(r, false);
			for (int i = 0; i < r.getChildCount(); i++) {
				this.parent = this.topNode;
				(r.getChildAt(i)).accept(this);
			}
		}
	}

	public void visit(Shmoo s) throws Exception {
		if (s.componentSelected(this.selection, OurTreeNode.MYSELF
				| OurTreeNode.PARENTS)) {
			Shmoo shm = new Shmoo(s, true);
			this.parent.addChild(shm);
		} else if (s.componentSelected(this.selection, OurTreeNode.CHILDS)) {
			Shmoo shm = new Shmoo(s, false);
			this.parent.addChild(shm);
			for (int i = 0; i < s.getChildCount(); i++) {
				this.parent = shm;
				(s.getChildAt(i)).accept(this);
			}
		}
	}

	public void visit(Ydata d) throws Exception {
		if (d.componentSelected(this.selection, OurTreeNode.MYSELF
				| OurTreeNode.PARENTS)) {
			Ydata dat = new Ydata(d, true);
			this.parent.addChild(dat);
		}
	}

}
