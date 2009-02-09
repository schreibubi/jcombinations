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

import javax.swing.tree.TreePath;

import org.schreibubi.JCombinations.FileFormat.Alternative;
import org.schreibubi.JCombinations.FileFormat.Asdap;
import org.schreibubi.JCombinations.FileFormat.OurTreeNode;
import org.schreibubi.JCombinations.FileFormat.Shmoo;
import org.schreibubi.JCombinations.FileFormat.TreeVisitor;
import org.schreibubi.JCombinations.FileFormat.Ydata;
import org.schreibubi.JCombinations.logic.TreeEventCollector;


/**
 * @author Jörg Werner
 * 
 */
public class AddNodesVisitor implements TreeVisitor {

	private OurTreeNode			rootTreeNode	= null;

	private TreeEventCollector	eventCollector	= null;

	/**
	 * @param rootTreeNode
	 *            tree to which visitor adds the nodes
	 * @param eventCollector
	 */
	public AddNodesVisitor(OurTreeNode rootTreeNode, TreeEventCollector eventCollector) {
		this.rootTreeNode = rootTreeNode;
		this.eventCollector = eventCollector;
	}

	public void visit(Alternative a) throws Exception {
		OurTreeNode path = getTreeNodeAtPath(this.rootTreeNode, a.getTreePath());
		if (path != null)
			for (int i = 0; i < a.getChildCount(); i++)
				(a.getChildAt(i)).accept(this);
		else {
			OurTreeNode alt = a.deepClone();
			OurTreeNode parent = getTreeNodeAtPath(this.rootTreeNode, a.getTreePath().getParentPath());
			parent.addChild(alt);
			this.eventCollector.addEvent(parent, alt, parent.getIndex(alt));
		}
	}

	public void visit(Asdap r) throws Exception {
		for (int i = 0; i < r.getChildCount(); i++)
			(r.getChildAt(i)).accept(this);
	}

	public void visit(Shmoo s) throws Exception {
		OurTreeNode path = getTreeNodeAtPath(this.rootTreeNode, s.getTreePath());
		if (path != null)
			for (int i = 0; i < s.getChildCount(); i++)
				(s.getChildAt(i)).accept(this);
		else {
			OurTreeNode shm = s.deepClone();
			OurTreeNode parent = getTreeNodeAtPath(this.rootTreeNode, s.getTreePath().getParentPath());
			parent.addChild(shm);
			this.eventCollector.addEvent(parent, s, parent.getIndex(s));
		}
	}

	public void visit(Ydata d) throws Exception {
		OurTreeNode dat = d.deepClone();
		OurTreeNode parent = getTreeNodeAtPath(this.rootTreeNode, d.getTreePath().getParentPath());
		parent.addChild(dat);
		this.eventCollector.addEvent(parent, d, parent.getIndex(d));
	}

	private OurTreeNode getTreeNodeAtPath(OurTreeNode rootTreeNode, TreePath path) {
		Object[] objectPath = path.getPath();
		if (rootTreeNode.getName().equals(((OurTreeNode) objectPath[0]).getName())) { // the
			// roots are the same
			OurTreeNode node = rootTreeNode;
			for (int i = 1; i < objectPath.length; i++) {
				boolean found = false;
				for (int j = 0; j < node.getChildCount(); j++)
					if (((OurTreeNode) objectPath[i]).getName().equals(((OurTreeNode) node.getChildAt(j)).getName())) {
						node = (OurTreeNode) node.getChildAt(j);
						found = true;
						break;
					}
				if (!found)
					return null;
			}
			return node;
		} else
			return null;
	}
}
