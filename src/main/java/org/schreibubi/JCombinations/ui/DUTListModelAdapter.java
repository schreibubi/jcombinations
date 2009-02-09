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

import javax.swing.ListModel;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.TreeModelEvent;

import org.schreibubi.JCombinations.logic.DataEvent;
import org.schreibubi.JCombinations.logic.DataEventListener;
import org.schreibubi.JCombinations.logic.DataModel;
import org.schreibubi.JCombinations.logic.SelectionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Jörg Werner
 * 
 */
public class DUTListModelAdapter implements ListModel, DataEventListener {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -8143801778039582153L;

	private static Logger		logger				= LoggerFactory.getLogger(DUTListModelAdapter.class);

	private DataModel			dm					= null;

	private String[]			dutList				= null;

	/**
	 * Event listener list
	 */
	protected EventListenerList	listenerList		= new EventListenerList();

	/**
	 * Constructor
	 * 
	 * @param dm
	 */
	public DUTListModelAdapter(DataModel dm) {
		super();
		this.dm = dm;
		dm.addDataEventListener(this);
		this.dutList = dm.getPossibleDataList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.ListModel#addListDataListener(javax.swing.event.ListDataListener)
	 */
	public void addListDataListener(ListDataListener l) {
		this.listenerList.add(ListDataListener.class, l);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.ListModel#getElementAt(int)
	 */
	public Object getElementAt(int arg0) {
		return this.dutList[arg0];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.ListModel#getSize()
	 */
	public int getSize() {
		return this.dutList.length;
	}

	public void limitMessage(DataEvent e) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.ListModel#removeListDataListener(javax.swing.event.ListDataListener)
	 */
	public void removeListDataListener(ListDataListener l) {
		this.listenerList.remove(ListDataListener.class, l);
	}

	public void selectionUpdated(SelectionEvent e) {
	}

	public void treeNodesChanged(TreeModelEvent e) {
	}

	public void treeNodesInserted(TreeModelEvent e) {
		DUTListModelAdapter.logger.info("received treeNodesInserted");
		this.dutList = this.dm.getPossibleDataList();
		fireListDataEvent(new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, getSize() - 1));
	}

	public void treeNodesRemoved(TreeModelEvent e) {
		DUTListModelAdapter.logger.info("received treeNodesRemoved");
		this.dutList = this.dm.getPossibleDataList();
		fireListDataEvent(new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, getSize() - 1));
	}

	public void treeStructureChanged(TreeModelEvent e) {
		DUTListModelAdapter.logger.info("received treeStructureChanged");
		this.dutList = this.dm.getPossibleDataList();
		fireListDataEvent(new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, getSize() - 1));
	}

	/**
	 * @param e
	 */
	protected void fireListDataEvent(ListDataEvent e) {
		// Guaranteed to return a non-null array
		Object[] listeners = this.listenerList.getListenerList();
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2)
			if (listeners[i] == ListDataListener.class)
				((ListDataListener) listeners[i + 1]).contentsChanged(e);
	}

}
