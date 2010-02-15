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

import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 * @author Jörg Werner
 * 
 */
public abstract class OurTreeNode implements TreeNode {

	/**
	 * The node itself is selected
	 */
	public static final int MYSELF = 1;

	/**
	 * A parent of the node is selected
	 */
	public static final int PARENTS = 2;

	/**
	 * A child of the node is selected
	 */
	public static final int CHILDS = 4;

	/**
	 * Visit function
	 * 
	 * @param v
	 *            visitor
	 * @throws Exception
	 */
	public abstract void accept(TreeVisitor v) throws Exception;

	/**
	 * Add a child to the node
	 * 
	 * @param child
	 */
	public abstract void addChild(OurTreeNode child);

	@Override
	public abstract OurTreeNode clone();

	/**
	 * Determines if the node, a parent or one of its childs is selected
	 * 
	 * @param treePaths
	 *            List of selected nodes
	 * @param sel
	 *            Determines the condition if the node itself has to be
	 *            selected, or if it is enough that a parent or a child is
	 *            selected. Or MYSELF, CHILD, PARENT to describe what you want
	 * @return true if selected
	 */
	public boolean componentSelected(ArrayList<TreePath> treePaths, int sel) {
		if (treePaths != null) {
			if (((sel & OurTreeNode.MYSELF) > 0)) {
				for (TreePath treePath : treePaths) {
					if (treePath.equals(this.getTreePath())) {
						return true;
					}
				}
			}
			if ((sel & OurTreeNode.PARENTS) > 0) {
				TreePath childPath = getTreePath();
				for (TreePath treePath : treePaths) {
					if (treePath.isDescendant(childPath)) {
						return true;
					}
				}
			}
			if ((sel & OurTreeNode.CHILDS) > 0) {
				if (treePaths != null) {
					TreePath childPath = getTreePath();
					for (TreePath treePath : treePaths) {
						if (childPath.isDescendant(treePath)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * same as clone but also copies all childs
	 * 
	 * @return cloned OurTreeNode
	 */
	public abstract OurTreeNode deepClone();

	/**
	 * Get name of Node
	 * 
	 * @return name
	 */
	public abstract String getName();

	/**
	 * Get TreePath of the node
	 * 
	 * @return tree path of the node
	 */
	public TreePath getTreePath() {
		ArrayList<Object> nodelist = new ArrayList<Object>();
		OurTreeNode node = this;
		do {
			nodelist.add(0, node);
			node = (OurTreeNode) node.getParent();
		} while (node != null);
		TreePath tp = new TreePath(nodelist.toArray());
		return tp;
	}

	/**
	 * Get value of column (for TreeTable)
	 * 
	 * @param column
	 * @return object for column
	 */
	public abstract Object getValueAt(int column);

	/**
	 * Remove child from the node
	 * 
	 * @param child
	 *            to remove
	 */
	public abstract void removeChild(OurTreeNode child);

	/**
	 * Remove child at a certain position
	 * 
	 * @param pos
	 *            position of the child to remove
	 */
	public abstract void removeChildAt(int pos);

	/**
	 * @param parent
	 *            The parent to set.
	 */
	public abstract void setParent(OurTreeNode parent);

}
