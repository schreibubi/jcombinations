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

import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.tree.TreePath;

import org.schreibubi.JCombinations.logic.DataEventListenerAdapter;
import org.schreibubi.JCombinations.logic.DataModel;
import org.schreibubi.JCombinations.logic.SelectionEvent;
import org.schreibubi.JCombinations.logic.TableContent;


/**
 * Panel which host multiple Tables as tabs
 * 
 * @author Jörg Werner
 * 
 */
public class MultiTabTablePanel extends JTabbedPane {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -6951602937272000952L;

	DataModel					dm					= null;

	/**
	 * Constructor
	 */
	public MultiTabTablePanel() {
		super();
	}

	/**
	 * Constructor
	 * 
	 * @param dm
	 *            associated DataModel
	 */
	public MultiTabTablePanel(DataModel dm) {
		super();
		this.dm = dm;
		initialize();
	}

	/**
	 * Set associated DataModel
	 * 
	 * @param dm
	 */
	public void setMultiTabTableModel(DataModel dm) {
		this.dm = dm;
		initialize();
	}

	private void initialize() {
		this.dm.addDataEventListener(new DataEventListenerAdapter() {
			/**
			 * @param e
			 */
			@Override
			public void selectionUpdated(SelectionEvent e) {
				update(e.getSelection());
			}

		});
	}

	private void update(ArrayList<TreePath> selection) {
		removeAll();
		ArrayList<TableContent> tables = this.dm.getTable(selection);
		for (int i = 0; i < tables.size(); i++) {
			JTable table = new JTable(tables.get(i));
			table.setDragEnabled(true);
			table.setAutoCreateColumnsFromModel(false);
			table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			table.setColumnSelectionAllowed(true);
			table.setRowSelectionAllowed(true);
			table.setCellSelectionEnabled(true);
			table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			JScrollPane scrollpane = new JScrollPane(table);
			addTab(tables.get(i).getName(), new ImageIcon(getClass().getResource(
					"/org/schreibubi/JCombinations/icons/16x16/mimetypes/spreadsheet.png")), scrollpane);
		}
	}

}
