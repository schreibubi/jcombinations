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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 * Dialog which asks if single or multipage mode for export should be used
 * 
 * @author Jörg Werner
 * 
 */
public class PDFDialog extends JDialog {

	/**
	 * possible dialog results
	 * 
	 */
	public static enum Choice {
		/**
		 * Cancel
		 */
		CANCEL,
		/**
		 * Single page mode
		 */
		SINGLE,
		/**
		 * Multi page mode
		 */
		MULTI
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 394954428313718556L;

	private static PDFDialog dialog;

	private static Choice result = Choice.CANCEL;

	/**
	 * Show the dialog
	 * 
	 * @param frameComp
	 *            containing frame
	 * @return result
	 */
	public static Choice showDialog(Component frameComp) {
		Frame frame = JOptionPane.getFrameForComponent(frameComp);
		PDFDialog.dialog = new PDFDialog(frame);
		PDFDialog.dialog.setVisible(true);
		return PDFDialog.result;
	}

	private JPanel jContentPane = null;

	private JRadioButton singlePageRadioButton = null;

	private JRadioButton multiplePagesRadioButton = null;

	private JButton exportButton = null;

	private JButton cancelButton = null;

	private ButtonGroup buttonGroup = null;

	private JLabel jLabel = null;;

	private JPanel radioPanel = null;

	private JPanel buttonPanel = null;

	/**
	 * This is the default constructor
	 * 
	 * @param frame
	 *            containing frame
	 */
	public PDFDialog(Frame frame) {
		super(frame, "PDF Export...", true);
		initialize();
	}

	/**
	 * This method initializes buttonPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getButtonPanel() {
		if (this.buttonPanel == null) {
			this.buttonPanel = new JPanel();
			this.buttonPanel.setLayout(new BoxLayout(getButtonPanel(),
					BoxLayout.X_AXIS));
			this.buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		}
		return this.buttonPanel;
	}

	/**
	 * This method initializes jButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getCancelButton() {
		if (this.cancelButton == null) {
			this.cancelButton = new JButton();
			this.cancelButton.setText("Cancel");
			this.cancelButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					PDFDialog.result = Choice.CANCEL;
					setVisible(false);
				}
			});
		}
		return this.cancelButton;
	}

	/**
	 * This method initializes jButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getExportButton() {
		if (this.exportButton == null) {
			this.exportButton = new JButton();
			this.exportButton.setText("Export...");
			this.exportButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (getMultiplePagesRadioButton().isSelected()) {
						PDFDialog.result = Choice.MULTI;
					} else {
						PDFDialog.result = Choice.SINGLE;
					}

					setVisible(false);
				}
			});
		}
		return this.exportButton;
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (this.jContentPane == null) {
			this.jContentPane = new JPanel();
			this.jContentPane.setLayout(new BoxLayout(getJContentPane(),
					BoxLayout.Y_AXIS));

			JPanel m_radioPanel = getRadioPanel();

			this.jLabel = new JLabel();
			this.jLabel.setText("Select format:");
			m_radioPanel.add(this.jLabel, null);
			m_radioPanel.add(getSinglePageRadioButton(), null);
			m_radioPanel.add(getMultiplePagesRadioButton(), null);
			getSinglePageRadioButton().setSelected(true);

			this.buttonGroup = new ButtonGroup();
			this.buttonGroup.add(getSinglePageRadioButton());
			this.buttonGroup.add(getMultiplePagesRadioButton());

			this.jContentPane.add(m_radioPanel, null);
			this.jContentPane.add(Box.createVerticalGlue());

			JPanel m_buttonPanel = getButtonPanel();
			m_buttonPanel.add(Box.createHorizontalGlue());
			m_buttonPanel.add(getExportButton(), null);
			m_buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
			m_buttonPanel.add(getCancelButton(), null);
			this.jContentPane.add(m_buttonPanel, null);

		}
		return this.jContentPane;
	}

	/**
	 * This method initializes jRadioButton1
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getMultiplePagesRadioButton() {
		if (this.multiplePagesRadioButton == null) {
			this.multiplePagesRadioButton = new JRadioButton();
			this.multiplePagesRadioButton.setText("Multiple pages");
		}
		return this.multiplePagesRadioButton;
	}

	/**
	 * This method initializes radioPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getRadioPanel() {
		if (this.radioPanel == null) {
			this.radioPanel = new JPanel();
			this.radioPanel.setLayout(new BoxLayout(getRadioPanel(),
					BoxLayout.Y_AXIS));
			this.radioPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		}
		return this.radioPanel;
	}

	/**
	 * This method initializes jRadioButton
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getSinglePageRadioButton() {
		if (this.singlePageRadioButton == null) {
			this.singlePageRadioButton = new JRadioButton();
			this.singlePageRadioButton.setText("Single page");
		}
		return this.singlePageRadioButton;
	}

	/**
	 * This method initializes this
	 */
	private void initialize() {
		this.setSize(200, 150);
		this.setContentPane(getJContentPane());
	}

} // @jve:decl-index=0:visual-constraint="10,10"
