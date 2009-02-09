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

import javax.swing.event.TreeModelEvent;

/**
 * This is just for convinience, so one not needs to implement all the unnecessary routines
 * 
 * @author Jörg Werner
 * 
 */
public class DataEventListenerAdapter implements DataEventListener {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.JCombinations.logic.DataEventListener#limitMessage(org.schreibubi.JCombinations.logic.DataEvent)
	 */
	public void limitMessage(DataEvent e) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.JCombinations.logic.DataEventListener#selectionUpdated(org.schreibubi.JCombinations.logic.SelectionEvent)
	 */
	public void selectionUpdated(SelectionEvent e) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.JCombinations.logic.DataEventListener#treeNodesChanged(javax.swing.event.TreeModelEvent)
	 */
	public void treeNodesChanged(TreeModelEvent e) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.JCombinations.logic.DataEventListener#treeNodesInserted(javax.swing.event.TreeModelEvent)
	 */
	public void treeNodesInserted(TreeModelEvent e) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.JCombinations.logic.DataEventListener#treeNodesRemoved(javax.swing.event.TreeModelEvent)
	 */
	public void treeNodesRemoved(TreeModelEvent e) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.schreibubi.JCombinations.logic.DataEventListener#treeStructureChanged(javax.swing.event.TreeModelEvent)
	 */
	public void treeStructureChanged(TreeModelEvent e) {
	}

}
