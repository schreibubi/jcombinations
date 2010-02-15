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
import java.util.EventObject;

import javax.swing.tree.TreePath;

/**
 * @author Jörg Werner
 */
public class SelectionEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3258135760375918902L;

	ArrayList<TreePath> selection = null;

	/**
	 * Constructor
	 * 
	 * @param arg0
	 * @param selection
	 */
	public SelectionEvent(Object arg0, ArrayList<TreePath> selection) {
		super(arg0);
		this.selection = selection;
	}

	/**
	 * Gets selection
	 * 
	 * @return selection
	 */
	public ArrayList<TreePath> getSelection() {
		return this.selection;
	}

}
