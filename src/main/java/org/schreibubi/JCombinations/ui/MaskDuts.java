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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.schreibubi.JCombinations.logic.DataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Jörg Werner
 */
public class MaskDuts extends JInternalFrame {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 3616444596750596152L;

	private static Logger		logger				= LoggerFactory.getLogger(MaskDuts.class);

	private JPanel				jContentPane		= null;

	private JList				jList				= null;

	private JLabel				jLabel				= null;

	private JScrollPane			jScrollPane			= null;

	private DataModel			dm					= null;

	private JButton				deleteButton		= null;

	/**
	 * This is the default constructor
	 */
	public MaskDuts() {
		super();
		initialize();
	}

	/**
	 * Get DataModel
	 * 
	 * @return DataModel
	 */
	public DataModel getDataModel() {
		return this.dm;
	}

	/**
	 * Set associated DataModel
	 * 
	 * @param dm
	 *            DataModel
	 */
	public void setDataModel(DataModel dm) {
		if (this.dm != dm) {
			MaskDuts.logger.info("setDataModel");
			// if ( this.dm != null ) {
			// this.dm.setSeriesMask( selectionToArray(
			// getJList().getSelectedIndices() ) );
			// }
			this.dm = dm;
			if (dm == null) {
				this.setEnabled(false);
				getJList().setEnabled(false);
			} else {
				this.setEnabled(true);
				getJList().setEnabled(true);
				updateSelection(arrayToSelection(dm.getSeriesMask()));
				getJList().setModel(dm.getDUTListModel()); // fires a value
				// changed!!!!
			}
		}
	}

	private int[] arrayToSelection(ArrayList<String> sel) {
		int[] a = new int[sel.size()];
		int count = 0;
		for (String s : sel)
			for (int i = 0; i < getJList().getModel().getSize(); i++)
				if (s.equals(getJList().getModel().getElementAt(i))) {
					a[count++] = i;
					break;
				}
		return a;
	}

	/**
	 * This method initializes deleteButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getDeleteButton() {
		if (this.deleteButton == null) {
			this.deleteButton = new JButton();
			this.deleteButton.setText("Delete selected DUTs");
			this.deleteButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					MaskDuts.this.dm.deleteSeries(selectionToArray(getJList().getSelectedIndices()));
				}
			});
		}
		return this.deleteButton;
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (this.jContentPane == null) {
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints.gridy = 2;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints4.weightx = 1.0;
			gridBagConstraints4.weighty = 1.0;
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.gridy = 1;
			gridBagConstraints4.insets = new java.awt.Insets(5, 5, 5, 5);
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.gridy = 0;
			gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
			this.jLabel = new JLabel();
			this.jLabel.setText("Select DUTs to disable");
			this.jContentPane = new JPanel();
			this.jContentPane.setLayout(new GridBagLayout());
			this.jContentPane.add(getJScrollPane(), gridBagConstraints4);
			this.jContentPane.add(this.jLabel, gridBagConstraints3);
			this.jContentPane.add(getDeleteButton(), gridBagConstraints);
		}
		return this.jContentPane;
	}

	/**
	 * This method initializes jList
	 * 
	 * @return javax.swing.JList
	 */
	private JList getJList() {
		if (this.jList == null) {
			this.jList = new JList();
			this.jList.setLayoutOrientation(JList.VERTICAL);
			this.jList.setVisibleRowCount(-1);
			this.jList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
				public void valueChanged(javax.swing.event.ListSelectionEvent e) {
					// Ignore extra messages.
					if (e.getValueIsAdjusting())
						return;
					MaskDuts.this.dm.setSeriesMask(selectionToArray(getJList().getSelectedIndices()));
				}
			});
		}
		return this.jList;
	}

	/**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (this.jScrollPane == null) {
			this.jScrollPane = new JScrollPane();
			this.jScrollPane.setViewportView(getJList());
		}
		return this.jScrollPane;
	}

	/**
	 * This method initializes this
	 */
	private void initialize() {
		this.setSize(292, 238);
		this.setResizable(true);
		this.setClosable(true);
		this.setIconifiable(true);
		this.setTitle("Mask Duts");
		this.setContentPane(getJContentPane());
		this.setDataModel(null);
		this.setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
	}

	private ArrayList<String> selectionToArray(int[] i) {
		ArrayList<String> sel = new ArrayList<String>();
		for (int element : i)
			sel.add((String) getJList().getModel().getElementAt(element));
		return sel;
	}

	private void updateSelection(int[] a) {
		getJList().setSelectedIndices(a);
	}

} // @jve:decl-index=0:visual-constraint="324,22"
