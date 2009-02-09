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
package org.schreibubi.JCombinations.ui;

import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreePath;

import org.jdesktop.swingx.treetable.TreeTableModel;
import org.schreibubi.JCombinations.FileFormat.OurTreeNode;
import org.schreibubi.JCombinations.logic.DataEventListenerAdapter;
import org.schreibubi.JCombinations.logic.DataModel;


/**
 * Adapts the DataModel to the TreeTableModel
 * 
 * @author Jörg Werner
 */
public class TreeTableModelAdapter implements TreeTableModel {

	/**
	 * Event listener list
	 */
	protected EventListenerList	listenerList	= new EventListenerList();

	// private static Logger logger = LoggerFactory.getLogger(
	// TreeTableModelAdapter.class );

	DataModel					dm				= null;

	/**
	 * Constructor
	 * 
	 * @param dm
	 * 		associated DataModel
	 */
	public TreeTableModelAdapter(DataModel dm) {
		this.dm = dm;
		dm.addDataEventListener(new DataEventListenerAdapter() {

			/**
			 * @param e
			 */
			@Override
			public void treeNodesChanged(TreeModelEvent e) {
				fireTreeNodesChanged(e);
			}

			/**
			 * @param e
			 */
			@Override
			public void treeNodesInserted(TreeModelEvent e) {
				fireTreeNodesInserted(e);
			}

			/**
			 * @param e
			 */
			@Override
			public void treeNodesRemoved(TreeModelEvent e) {
				fireTreeNodesRemoved(e);
			}

			/**
			 * @param e
			 */
			@Override
			public void treeStructureChanged(TreeModelEvent e) {
				fireTreeStructureChanged(e);
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.tree.TreeModel#addTreeModelListener(javax.swing.event.TreeModelListener)
	 */
	public void addTreeModelListener(TreeModelListener l) {
		this.listenerList.add(TreeModelListener.class, l);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.tree.TreeModel#getChild(java.lang.Object, int)
	 */
	public Object getChild(Object node, int child) {
		return ((OurTreeNode) node).getChildAt(child);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.tree.TreeModel#getChildCount(java.lang.Object)
	 */
	public int getChildCount(Object node) {
		return ((OurTreeNode) node).getChildCount();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jdesktop.swingx.treetable.TreeTableModel#getColumnClass(int)
	 */
	public Class getColumnClass(int column) {
		return column == 0 ? TreeTableModel.class : String.class;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jdesktop.swingx.treetable.TreeTableModel#getColumnCount()
	 */
	public int getColumnCount() {
		return 6;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jdesktop.swingx.treetable.TreeTableModel#getColumnName(int)
	 */
	public String getColumnName(int column) {
		switch (column) {
		case 0:
			return "Shmoo";
		case 1:
			return "Trim";
		case 2:
			return "Measure";
		case 3:
			return "Pass";
		case 4:
			return "Fail";
		case 5:
			return "not judged";
		default:
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jdesktop.swingx.treetable.TreeTableModel#getHierarchicalColumn()
	 */
	public int getHierarchicalColumn() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.tree.TreeModel#getIndexOfChild(java.lang.Object, java.lang.Object)
	 */
	public int getIndexOfChild(Object parent, Object child) {
		return ((OurTreeNode) parent).getIndex((OurTreeNode) child);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.tree.TreeModel#getRoot()
	 */
	public Object getRoot() {
		return this.dm.getRoot();
	}

	/**
	 * @return List of registered TreeModelListeners
	 */
	public TreeModelListener[] getTreeModelListeners() {
		return this.listenerList.getListeners(TreeModelListener.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jdesktop.swingx.treetable.TreeTableModel#getValueAt(java.lang.Object, int)
	 */
	public Object getValueAt(Object node, int column) {
		OurTreeNode s = (OurTreeNode) node;
		// logger.info( "treetable name col:" + column + " " + s.getValueAt(
		// column ) );
		return s.getValueAt(column);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jdesktop.swingx.treetable.TreeTableModel#isCellEditable(java.lang.Object, int)
	 */
	public boolean isCellEditable(Object node, int column) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.tree.TreeModel#isLeaf(java.lang.Object)
	 */
	public boolean isLeaf(Object node) {
		return ((OurTreeNode) node).isLeaf();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.tree.TreeModel#removeTreeModelListener(javax.swing.event.TreeModelListener)
	 */
	public void removeTreeModelListener(TreeModelListener l) {
		this.listenerList.remove(TreeModelListener.class, l);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jdesktop.swingx.treetable.TreeTableModel#setValueAt(java.lang.Object, java.lang.Object, int)
	 */
	public void setValueAt(Object value, Object node, int column) {
		// not applicable
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.tree.TreeModel#valueForPathChanged(javax.swing.tree.TreePath, java.lang.Object)
	 */
	public void valueForPathChanged(TreePath arg0, Object arg1) {
	}

	/*
	 * Notify all listeners that have registered interest for notification on this event type. The event instance is
	 * lazily created using the parameters passed into the fire method.
	 * 
	 * @see EventListenerList
	 */
	protected void fireTreeNodesChanged(TreeModelEvent e) {
		// Guaranteed to return a non-null array
		Object[] listeners = this.listenerList.getListenerList();
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2)
			if (listeners[i] == TreeModelListener.class) {
				((TreeModelListener) listeners[i + 1]).treeNodesChanged(e);
			}
	}

	/*
	 * Notify all listeners that have registered interest for notification on this event type. The event instance is
	 * lazily created using the parameters passed into the fire method.
	 * 
	 * @see EventListenerList
	 */
	protected void fireTreeNodesInserted(TreeModelEvent e) {
		// Guaranteed to return a non-null array
		Object[] listeners = this.listenerList.getListenerList();
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2)
			if (listeners[i] == TreeModelListener.class) {
				((TreeModelListener) listeners[i + 1]).treeNodesInserted(e);
			}
	}

	/*
	 * Notify all listeners that have registered interest for notification on this event type. The event instance is
	 * lazily created using the parameters passed into the fire method.
	 * 
	 * @see EventListenerList
	 */
	protected void fireTreeNodesRemoved(TreeModelEvent e) {
		// Guaranteed to return a non-null array
		Object[] listeners = this.listenerList.getListenerList();
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2)
			if (listeners[i] == TreeModelListener.class) {
				((TreeModelListener) listeners[i + 1]).treeNodesRemoved(e);
			}
	}

	/*
	 * Notify all listeners that have registered interest for notification on this event type. The event instance is
	 * lazily created using the parameters passed into the fire method.
	 * 
	 * @see EventListenerList
	 */
	protected void fireTreeStructureChanged(TreeModelEvent e) {
		// Guaranteed to return a non-null array
		Object[] listeners = this.listenerList.getListenerList();
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2)
			if (listeners[i] == TreeModelListener.class) {
				((TreeModelListener) listeners[i + 1]).treeStructureChanged(e);
			}
	}

}
