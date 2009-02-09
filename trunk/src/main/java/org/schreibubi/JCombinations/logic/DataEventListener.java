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

import java.util.EventListener;

import javax.swing.event.TreeModelEvent;

/**
 * Listener for DataEvents (@see org.schreibubi.JCombinations.logic.DataEvent)
 * 
 * @author Jörg Werner
 */
public interface DataEventListener extends EventListener {

	/**
	 * @param e
	 */
	void limitMessage(DataEvent e);

	/**
	 * @param e
	 */
	void selectionUpdated(SelectionEvent e);

	/**
	 * @param e
	 */
	void treeNodesChanged(TreeModelEvent e);

	/**
	 * @param e
	 */
	void treeNodesInserted(TreeModelEvent e);

	/**
	 * @param e
	 */
	void treeNodesRemoved(TreeModelEvent e);

	/**
	 * @param e
	 */
	void treeStructureChanged(TreeModelEvent e);

}
