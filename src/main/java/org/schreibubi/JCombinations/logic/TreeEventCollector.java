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
package org.schreibubi.JCombinations.logic;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelEvent;

import org.schreibubi.JCombinations.FileFormat.OurTreeNode;

/**
 * @author Jörg Werner
 * 
 */
public class TreeEventCollector {

	class ChildPos {
		private OurTreeNode child;

		private int pos;

		/**
		 * @param child
		 * @param pos
		 */
		public ChildPos(OurTreeNode child, int pos) {
			setChild(child);
			setPos(pos);
		}

		/**
		 * @return Returns the child.
		 */
		public OurTreeNode getChild() {
			return this.child;
		}

		/**
		 * @return Returns the pos.
		 */
		public int getPos() {
			return this.pos;
		}

		/**
		 * @param child
		 *            The child to set.
		 */
		public void setChild(OurTreeNode child) {
			this.child = child;
		}

		/**
		 * @param pos
		 *            The pos to set.
		 */
		public void setPos(int pos) {
			this.pos = pos;
		}
	}

	private HashMap<OurTreeNode, ArrayList<ChildPos>> treeNodeList = new HashMap<OurTreeNode, ArrayList<ChildPos>>();

	/**
	 * @param parent
	 * @param child
	 * @param pos
	 */
	public void addEvent(OurTreeNode parent, OurTreeNode child, int pos) {
		ArrayList<ChildPos> eList = this.treeNodeList.get(parent);
		if (eList == null) {
			eList = new ArrayList<ChildPos>();
		}
		ChildPos c = new ChildPos(child, pos);
		eList.add(c);
		this.treeNodeList.put(parent, eList);
	}

	/**
	 * @param listenerList
	 * @param source
	 */
	public void fireTreeNodesChanged(EventListenerList listenerList,
			Object source) {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		TreeModelEvent e = null;
		for (OurTreeNode parent : this.treeNodeList.keySet()) {
			ArrayList<ChildPos> cPositions = this.treeNodeList.get(parent);
			int[] childIndices = new int[cPositions.size()];
			Object[] children = new Object[cPositions.size()];
			for (int i = 0; i < cPositions.size(); i++) {
				ChildPos c = cPositions.get(i);
				childIndices[i] = c.getPos();
				children[i] = c.getChild();
			}
			// Process the listeners last to first, notifying
			// those that are interested in this event
			for (int i = listeners.length - 2; i >= 0; i -= 2) {
				if (listeners[i] == DataEventListener.class) {
					// Lazily create the event:
					if (e == null) {
						e = new TreeModelEvent(source, parent.getTreePath(),
								childIndices, children);
					}
					((DataEventListener) listeners[i + 1]).treeNodesChanged(e);
				}
			}
		}
	}

	/**
	 * @param listenerList
	 * @param source
	 */
	public void fireTreeNodesInserted(EventListenerList listenerList,
			Object source) {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		TreeModelEvent e = null;
		for (OurTreeNode parent : this.treeNodeList.keySet()) {
			ArrayList<ChildPos> cPositions = this.treeNodeList.get(parent);
			int[] childIndices = new int[cPositions.size()];
			Object[] children = new Object[cPositions.size()];
			for (int i = 0; i < cPositions.size(); i++) {
				ChildPos c = cPositions.get(i);
				childIndices[i] = c.getPos();
				children[i] = c.getChild();
			}
			// Process the listeners last to first, notifying
			// those that are interested in this event
			for (int i = listeners.length - 2; i >= 0; i -= 2) {
				if (listeners[i] == DataEventListener.class) {
					// Lazily create the event:
					if (e == null) {
						e = new TreeModelEvent(source, parent.getTreePath(),
								childIndices, children);
					}
					((DataEventListener) listeners[i + 1]).treeNodesInserted(e);
				}
			}
		}
	}

	/**
	 * @param listenerList
	 * @param source
	 */
	public void fireTreeNodesRemoved(EventListenerList listenerList,
			Object source) {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		TreeModelEvent e = null;
		for (OurTreeNode parent : this.treeNodeList.keySet()) {
			ArrayList<ChildPos> cPositions = this.treeNodeList.get(parent);
			int[] childIndices = new int[cPositions.size()];
			Object[] children = new Object[cPositions.size()];
			for (int i = 0; i < cPositions.size(); i++) {
				ChildPos c = cPositions.get(i);
				childIndices[i] = c.getPos();
				children[i] = c.getChild();
			}
			// Process the listeners last to first, notifying
			// those that are interested in this event
			for (int i = listeners.length - 2; i >= 0; i -= 2) {
				if (listeners[i] == DataEventListener.class) {
					// Lazily create the event:
					if (e == null) {
						e = new TreeModelEvent(source, parent.getTreePath(),
								childIndices, children);
					}
					((DataEventListener) listeners[i + 1]).treeNodesRemoved(e);
				}
			}
		}
	}

	/**
	 * removes objects from tree
	 */
	public void removeNodes() {
		for (OurTreeNode parent : this.treeNodeList.keySet()) {
			ArrayList<ChildPos> cPositions = this.treeNodeList.get(parent);
			for (ChildPos pos : cPositions) {
				parent.removeChild(pos.getChild());
			}
		}
	}

}
