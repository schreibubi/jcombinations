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

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.prefs.Preferences;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import javax.swing.undo.UndoManager;

import org.schreibubi.JCombinations.StandardFileFilter;
import org.schreibubi.JCombinations.logic.DataEvent;
import org.schreibubi.JCombinations.logic.DataEventListener;
import org.schreibubi.JCombinations.logic.DataEventListenerAdapter;
import org.schreibubi.JCombinations.logic.DataModel;


/**
 * @author Jörg Werner
 */
public class LimitsEditor extends JInternalFrame {

	private static final String	LIMIT_DIR			= "limitDir";

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 3256719576615170100L;

	private javax.swing.JPanel	jContentPane		= null;

	private JToolBar			jToolBar			= null;

	private JButton				ApplyButton			= null;

	private JEditorPane			jEditorPane			= null;

	private JTextPane			jTextPane			= null;

	private JSplitPane			jSplitPane			= null;

	private JButton				ClearButton			= null;

	private JButton				UndoButton			= null;

	private JButton				RedoButton			= null;

	private JPanel				jPanel				= null;

	private DataModel			dm					= null;

	private JScrollPane			bottomScrollPane	= null;

	private JScrollPane			topScrollPane		= null;

	private DataEventListener	dataEventListener;

	protected UndoManager		undo				= null;

	/**
	 * This is the default constructor
	 */
	public LimitsEditor() {
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
	 * Load limits from file
	 */
	public void loadLimits() {
		final JFileChooser fc = new JFileChooser();
		final Preferences prefs = Preferences.userNodeForPackage(LimitsEditor.class);
		String dirString = prefs.get(LimitsEditor.LIMIT_DIR, null);
		if (dirString == null)
			fc.setCurrentDirectory(null);
		else
			fc.setCurrentDirectory(new File(dirString));

		StandardFileFilter filter = new StandardFileFilter("limits");
		filter.setDescription("Limit-Files");
		fc.setFileFilter(filter);
		int returnVal = fc.showOpenDialog(this);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			prefs.put(LimitsEditor.LIMIT_DIR, fc.getCurrentDirectory().getAbsolutePath());
			setTitle("Limits Editor: " + fc.getSelectedFile().toString());
			try {
				BufferedReader in = new BufferedReader(new FileReader(fc.getSelectedFile()));
				String loadedText = "";
				String s;
				while ((s = in.readLine()) != null)
					loadedText = loadedText + s + "\n";
				getJEditorPane().setText(loadedText);
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(this, "Error reading document", "I/O error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * Save limits to file
	 */
	public void saveLimits() {
		final JFileChooser fc = new JFileChooser();
		final Preferences prefs = Preferences.userNodeForPackage(LimitsEditor.class);
		String dirString = prefs.get(LimitsEditor.LIMIT_DIR, null);
		if (dirString == null)
			fc.setCurrentDirectory(null);
		else
			fc.setCurrentDirectory(new File(dirString));

		StandardFileFilter filter = new StandardFileFilter("limits");
		filter.setDescription("Limit-Files");
		fc.setFileFilter(filter);

		int returnVal = fc.showSaveDialog(this);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			prefs.put(LimitsEditor.LIMIT_DIR, fc.getCurrentDirectory().getAbsolutePath());
			setTitle("Limits Editor: " + fc.getSelectedFile().toString());
			try {
				BufferedWriter out = new BufferedWriter(new FileWriter(fc.getSelectedFile()));
				out.write(getJEditorPane().getText());
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(this, "Error saving document", "I/O error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * Set DataModel
	 * 
	 * @param dm
	 *            DataModel
	 */
	public void setDataModel(DataModel dm) {
		if (this.dm != null) {
			this.dm.setLimitsText(getJEditorPane().getText());
			this.dm.removeDataEventListener(getDataEventListener());
		}
		this.dm = dm;
		if (dm == null) {
			this.setEnabled(false);
			getJEditorPane().setEnabled(false);
			getApplyButton().setEnabled(false);
		} else {
			this.setEnabled(true);
			getJEditorPane().setEnabled(true);
			getApplyButton().setEnabled(true);
			getJEditorPane().setText(dm.getLimitsText());
			dm.addDataEventListener(getDataEventListener());
		}
	}

	/**
	 * This method initializes jButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getApplyButton() {
		if (this.ApplyButton == null) {
			this.ApplyButton = new JButton();
			this.ApplyButton.setText("");
			this.ApplyButton.setIcon(new ImageIcon(getClass().getResource(
					"/org/schreibubi/JCombinations/icons/22x22/actions/button_ok.png")));
			this.ApplyButton.setToolTipText("Apply");
			this.ApplyButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					LimitsEditor.this.dm.setLimitsText(LimitsEditor.this.jEditorPane.getText());
					LimitsEditor.this.dm.applyLimits();
				}
			});
		}
		return this.ApplyButton;
	}

	/**
	 * This method initializes bottomScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getBottomScrollPane() {
		if (this.bottomScrollPane == null) {
			this.bottomScrollPane = new JScrollPane();
			this.bottomScrollPane.setPreferredSize(new java.awt.Dimension(200, 50));
			this.bottomScrollPane.setViewportView(getJTextPane());
		}
		return this.bottomScrollPane;
	}

	/**
	 * This method initializes jButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getClearButton() {
		if (this.ClearButton == null) {
			this.ClearButton = new JButton();
			this.ClearButton.setIcon(new ImageIcon(getClass().getResource(
					"/org/schreibubi/JCombinations/icons/22x22/actions/clear_left.png")));
			this.ClearButton.setText("");
			this.ClearButton.setToolTipText("Clear messages");
			this.ClearButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					LimitsEditor.this.jTextPane.setText("");
				}
			});
		}
		return this.ClearButton;
	}

	private DataEventListener getDataEventListener() {
		if (this.dataEventListener == null)
			this.dataEventListener = new DataEventListenerAdapter() {

				/**
				 * @param e
				 */
				@Override
				public void limitMessage(DataEvent e) {
					// jTextPane.setText( jTextPane.getText() + e.getMessage()
					// );
					StyledDocument doc = LimitsEditor.this.jTextPane.getStyledDocument();
					Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
					Style regular = doc.addStyle("regular", def);

					Style s = doc.addStyle("red", regular);
					StyleConstants.setForeground(s, Color.RED);

					try {
						if (e.getError())
							doc.insertString(doc.getLength(), e.getMessage(), doc.getStyle("red"));
						else
							doc.insertString(doc.getLength(), e.getMessage(), doc.getStyle("regular"));
					} catch (BadLocationException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if (e.getPos() > -1) {
						LimitsEditor.this.jEditorPane.setCaretPosition(e.getPos());
						LimitsEditor.this.jEditorPane.requestFocusInWindow();
					}
				}

			};
		return this.dataEventListener;
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJContentPane() {
		if (this.jContentPane == null) {
			this.jContentPane = new javax.swing.JPanel();
			this.jContentPane.setLayout(new java.awt.BorderLayout());
			this.jContentPane.setPreferredSize(null);
			this.jContentPane.add(getJToolBar(), java.awt.BorderLayout.NORTH);
			this.jContentPane.add(getJSplitPane(), java.awt.BorderLayout.CENTER);
		}
		return this.jContentPane;
	}

	/**
	 * This method initializes jEditorPane
	 * 
	 * @return javax.swing.JEditorPane
	 */
	private JEditorPane getJEditorPane() {
		if (this.jEditorPane == null) {
			this.jEditorPane = new JEditorPane();
			this.undo = new UndoManager();
			this.jEditorPane.setForeground(null);
			this.jEditorPane.setPreferredSize(new java.awt.Dimension(200, 100));
			this.jEditorPane.getDocument().addUndoableEditListener(new UndoableEditListener() {
				public void undoableEditHappened(UndoableEditEvent e) {
					LimitsEditor.this.undo.addEdit(e.getEdit());
					getUndoButton().setEnabled(LimitsEditor.this.undo.canUndo());
					getRedoButton().setEnabled(LimitsEditor.this.undo.canRedo());
					// undoAction.updateUndoState();
					// redoAction.updateRedoState();
				}
			});
		}
		return this.jEditorPane;
	}

	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel() {
		if (this.jPanel == null) {
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints2.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.gridy = 1;
			gridBagConstraints2.weightx = 1.0;
			gridBagConstraints2.weighty = 1.0;
			gridBagConstraints2.gridwidth = 2;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 0;
			this.jPanel = new JPanel();
			this.jPanel.setLayout(new GridBagLayout());
			this.jPanel.setPreferredSize(new java.awt.Dimension(200, 200));
			this.jPanel.add(getClearButton(), gridBagConstraints1);
			this.jPanel.add(getBottomScrollPane(), gridBagConstraints2);
		}
		return this.jPanel;
	}

	/**
	 * This method initializes jSplitPane
	 * 
	 * @return javax.swing.JSplitPane
	 */
	private JSplitPane getJSplitPane() {
		if (this.jSplitPane == null) {
			this.jSplitPane = new JSplitPane();
			this.jSplitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
			this.jSplitPane.setOneTouchExpandable(true);
			this.jSplitPane.setDividerLocation(150);
			this.jSplitPane.setTopComponent(getTopScrollPane());
			this.jSplitPane.setBottomComponent(getJPanel());
		}
		return this.jSplitPane;
	}

	/**
	 * This method initializes jTextPane
	 * 
	 * @return javax.swing.JTextPane
	 */
	private JTextPane getJTextPane() {
		if (this.jTextPane == null) {
			this.jTextPane = new JTextPane();
			// jTextPane.setPreferredSize( new java.awt.Dimension( 50, 100 ) );
			this.jTextPane.setEditable(false);
			this.jTextPane.setPreferredSize(new java.awt.Dimension(500, 50));
		}
		return this.jTextPane;
	}

	/**
	 * This method initializes jToolBar
	 * 
	 * @return javax.swing.JToolBar
	 */
	private JToolBar getJToolBar() {
		if (this.jToolBar == null) {
			this.jToolBar = new JToolBar();
			this.jToolBar.add(getApplyButton());
			this.jToolBar.addSeparator();
			this.jToolBar.add(getRedoButton());
			this.jToolBar.add(getUndoButton());
		}
		return this.jToolBar;
	}

	/**
	 * This method initializes jButton1
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getRedoButton() {
		if (this.RedoButton == null) {
			this.RedoButton = new JButton();
			this.RedoButton.setIcon(new ImageIcon(getClass().getResource(
					"/org/schreibubi/JCombinations/icons/22x22/actions/redo.png")));
			this.RedoButton.setText("");
			this.RedoButton.setEnabled(false);
			this.RedoButton.setToolTipText("Redo");
			this.RedoButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					LimitsEditor.this.undo.redo();
					getUndoButton().setEnabled(LimitsEditor.this.undo.canUndo());
					getRedoButton().setEnabled(LimitsEditor.this.undo.canRedo());
				}
			});
		}
		return this.RedoButton;
	}

	/**
	 * This method initializes topScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getTopScrollPane() {
		if (this.topScrollPane == null) {
			this.topScrollPane = new JScrollPane();
			this.topScrollPane.setPreferredSize(new java.awt.Dimension(200, 100));
			this.topScrollPane.setViewportView(getJEditorPane());
		}
		return this.topScrollPane;
	}

	/**
	 * This method initializes jButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getUndoButton() {
		if (this.UndoButton == null) {
			this.UndoButton = new JButton();
			this.UndoButton.setIcon(new ImageIcon(getClass().getResource(
					"/org/schreibubi/JCombinations/icons/22x22/actions/undo.png")));
			this.UndoButton.setText("");
			this.UndoButton.setEnabled(false);
			this.UndoButton.setToolTipText("Undo");
			this.UndoButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					LimitsEditor.this.undo.undo();
					getUndoButton().setEnabled(LimitsEditor.this.undo.canUndo());
					getRedoButton().setEnabled(LimitsEditor.this.undo.canRedo());
				}
			});
		}
		return this.UndoButton;
	}

	/**
	 * This method initializes this
	 */
	private void initialize() {
		this.setSize(591, 383);
		this.setResizable(true);
		this.setTitle("Limits Editor");
		this.setMaximizable(true);
		this.setIconifiable(true);
		this.setClosable(true);
		this.setContentPane(getJContentPane());
		this.setDataModel(null);
		this.setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
	}

} // @jve:decl-index=0:visual-constraint="110,88"
