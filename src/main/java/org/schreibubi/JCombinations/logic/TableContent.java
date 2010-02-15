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

import javax.swing.event.EventListenerList;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import org.schreibubi.symbol.Symbol;

/**
 * A class which contains all relevant information for displaying the tables
 * 
 * @author Jörg Werner
 */
public class TableContent implements TableModel {

	private ArrayList<ArrayList<Double>> table = null;

	private ArrayList<Symbol> xlabel = null;

	private ArrayList<Symbol> ylabel = null;

	private DataModel dm = null;

	private String name;

	/**
	 * Event listener list
	 */
	protected EventListenerList listenerList = new EventListenerList();

	/**
	 * Constructor
	 * 
	 * @param name
	 *            Name of the table
	 * @param table
	 *            Table content
	 * @param xlabel
	 *            x-labels
	 * @param ylabel
	 *            y-labels
	 */
	public TableContent(String name, ArrayList<ArrayList<Double>> table,
			ArrayList<Symbol> xlabel, ArrayList<Symbol> ylabel) {
		super();
		setName(name);
		setTable(table);
		setXlabel(xlabel);
		setYlabel(ylabel);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.table.TableModel#addTableModelListener(javax.swing.event.
	 * TableModelListener)
	 */
	public void addTableModelListener(TableModelListener l) {
		this.listenerList.add(TableModelListener.class, l);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getColumnClass(int)
	 */
	@SuppressWarnings("unchecked")
	public Class getColumnClass(int column) {
		return String.class;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	public int getColumnCount() {
		if (this.table != null) {
			return this.table.get(0).size() + 1;
		} else {
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getColumnName(int)
	 */
	public String getColumnName(int column) {
		return null;
		// return xlabel.get( column ).getValueUnitString();
	}

	/**
	 * @return Returns the DataModel.
	 */
	public DataModel getDataModel() {
		return this.dm;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return this.name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	public int getRowCount() {
		if (this.table != null) {
			return this.table.size() + 1;
		} else {
			return 0;
		}
	}

	/**
	 * @return Returns the table.
	 */
	public ArrayList<ArrayList<Double>> getTable() {
		return this.table;
	}

	/**
	 * @return List of model listeners
	 */
	public TableModelListener[] getTableModelListeners() {
		return this.listenerList.getListeners(TableModelListener.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	public Object getValueAt(int row, int col) {
		if (this.table != null) {
			if (row == 0) {
				if (col == 0) {
					return ""; // left upper corner
				} else {
					return this.xlabel.get(col - 1).getValueUnitString();
				}
			} else if (col == 0) {
				return this.ylabel.get(row - 1).getValueUnitString();
			} else {
				return this.table.get(row - 1).get(col - 1);
			}
		} else {
			return "";
		}
	}

	/**
	 * @return Returns the xlabel.
	 */
	public ArrayList<Symbol> getXlabel() {
		return this.xlabel;
	}

	/**
	 * @return Returns the ylabel.
	 */
	public ArrayList<Symbol> getYlabel() {
		return this.ylabel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#isCellEditable(int, int)
	 */
	public boolean isCellEditable(int arg0, int arg1) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.table.TableModel#removeTableModelListener(javax.swing.event
	 * .TableModelListener)
	 */
	public void removeTableModelListener(TableModelListener l) {
		this.listenerList.remove(TableModelListener.class, l);
	}

	/**
	 * @param dm
	 *            The DataModel to set.
	 */
	public void setDataModel(DataModel dm) {
		this.dm = dm;
		dm.addDataEventListener(new DataEventListenerAdapter() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.schreibubi.JCombinations.logic.DataEventListener#selectionUpdated
			 * (org.schreibubi.JCombinations.logic.SelectionEvent)
			 */
			@Override
			public void selectionUpdated(SelectionEvent e) {
				// fire events onward to View...
				fireTableChanged(TableContent.this);
			}
		});
	}

	/**
	 * @param name
	 *            The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param table
	 *            The table to set.
	 */
	public void setTable(ArrayList<ArrayList<Double>> table) {
		this.table = table;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#setValueAt(java.lang.Object, int, int)
	 */
	public void setValueAt(Object arg0, int arg1, int arg2) {
	}

	/**
	 * @param xlabel
	 *            The xlabel to set.
	 */
	public void setXlabel(ArrayList<Symbol> xlabel) {
		this.xlabel = xlabel;
	}

	/**
	 * @param ylabel
	 *            The ylabel to set.
	 */
	public void setYlabel(ArrayList<Symbol> ylabel) {
		this.ylabel = ylabel;
	}

	protected void fireTableChanged(TableContent obj) {
		// Guaranteed to return a non-null array
		Object[] listeners = this.listenerList.getListenerList();
		TableModelEvent e = null;
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == TableModelListener.class) {
				// Lazily create the event:
				if (e == null) {
					e = new TableModelEvent(obj);
				}
				((TableModelListener) listeners[i + 1]).tableChanged(e);
			}
		}
	}

}
