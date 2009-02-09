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
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.prefs.Preferences;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.TransferHandler;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.tree.TreePath;

import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.decorator.Filter;
import org.jdesktop.swingx.decorator.FilterPipeline;
import org.jdesktop.swingx.decorator.PatternFilter;
import org.jdesktop.swingx.event.MessageEvent;
import org.jdesktop.swingx.event.MessageListener;
import org.jdesktop.swingx.event.MessageSource;
import org.jdesktop.swingx.event.ProgressEvent;
import org.jdesktop.swingx.event.ProgressListener;
import org.jdesktop.swingx.event.ProgressSource;
import org.schreibubi.JCombinations.StandardFileFilter;
import org.schreibubi.JCombinations.FileFormat.OurTreeNode;
import org.schreibubi.JCombinations.logic.DataEventListenerAdapter;
import org.schreibubi.JCombinations.logic.DataModel;
import org.schreibubi.JCombinations.logic.SelectionEvent;
import org.schreibubi.JCombinations.logic.io.ExportDataInterface;
import org.schreibubi.JCombinations.logic.io.GeneralDataFormat;
import org.schreibubi.JCombinations.logic.io.ImportDataInterface;
import org.schreibubi.JCombinations.logic.io.RATDataFormat;


/**
 * The central plot window
 * 
 * @author Jörg Werner
 */
public class PlotWindow extends JInternalFrame implements MessageSource, ProgressSource {

	class ShmooTransferHandler extends TransferHandler {

		class ShmooTransferable implements Transferable {

			private OurTreeNode	topNode	= null;

			ShmooTransferable(OurTreeNode topNode) {
				this.topNode = topNode;
			}

			public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
				if (!isDataFlavorSupported(flavor))
					throw new UnsupportedFlavorException(flavor);
				// returns transfer data
				return this.topNode;
			}

			public DataFlavor[] getTransferDataFlavors() {
				return new DataFlavor[] { new DataFlavor(OurTreeNode.class, null) };
			}

			public boolean isDataFlavorSupported(DataFlavor flavor) {
				return flavor.equals(new DataFlavor(OurTreeNode.class, null));
			}

		}

		private static final long	serialVersionUID	= -4832995571767232110L;

		private final DataFlavor	df					= new DataFlavor(OurTreeNode.class, null);

		private ArrayList<TreePath>	selectedNodes		= null;

		@Override
		public boolean canImport(JComponent c, DataFlavor[] flavors) {
			for (DataFlavor element : flavors)
				if (element.equals(this.df))
					return true;
			return false;
		}

		@Override
		public int getSourceActions(JComponent arg0) {
			return TransferHandler.COPY_OR_MOVE;
		}

		@Override
		public Icon getVisualRepresentation(Transferable arg0) {
			return new ImageIcon(getClass().getResource(
					"/org/schreibubi/JCombinations/icons/22x22/mimetypes/vectorgfx.png"));
		}

		@Override
		public boolean importData(JComponent c, Transferable t) {
			if (canImport(c, t.getTransferDataFlavors())) {
				try {
					OurTreeNode topNode = (OurTreeNode) t.getTransferData(this.df);
					getDataModel().addNodes(topNode);
				} catch (UnsupportedFlavorException e) {
				} catch (IOException e) {
				}
				return true;
			} else
				return false;
		}

		@Override
		protected Transferable createTransferable(JComponent c) {
			JXTreeTable t = (JXTreeTable) c;
			ListSelectionModel lsm = t.getSelectionModel();
			if (lsm.isSelectionEmpty()) {
			} else {
				int minIndex = lsm.getMinSelectionIndex();
				int maxIndex = lsm.getMaxSelectionIndex();
				this.selectedNodes = new ArrayList<TreePath>();
				for (int i = minIndex; i <= maxIndex; i++)
					if (lsm.isSelectedIndex(i))
						this.selectedNodes.add(PlotWindow.this.jXTreeTable.getPathForRow(i));
				return new ShmooTransferable(getDataModel().copySelection(this.selectedNodes));
			}
			return null;
		}

		@Override
		protected void exportDone(JComponent source, Transferable data, int action) {
			if (action == TransferHandler.MOVE)
				getDataModel().removeSelection(this.selectedNodes);
		}

	}

	private static final String	DATA_DIR				= "dataDir";

	private static final String	PDF_DIR					= "pdfDir";

	private static final String	EXCEL_DIR				= "excelDir";

	private static final String	SVG_DIR					= "svgDir";

	/**
	 * 
	 */
	private static final long	serialVersionUID		= 3256721771343395128L;

	private javax.swing.JPanel	jContentPane			= null;

	private JScrollPane			leftScrollPane			= null;

	private JSplitPane			jSplitPane				= null;

	private JScrollPane			rightGraphScrollPane	= null;

	private JToolBar			jToolBar				= null;

	private JButton				PrintButton				= null;

	private JButton				ExportPDFButton			= null;

	private JButton				ExportExcelButton		= null;

	private JButton				CopyButton				= null;

	private JCheckBox			OverlayButton			= null;

	private JButton				ExportSVGButton			= null;

	private JXTreeTable			jXTreeTable				= null;

	private DataModel			dm						= null;

	private JTabbedPane			jTabbedPane				= null;

	private JPanel				jPanel					= null;

	private JTextField			searchField				= null;

	private JButton				clearSearchButton		= null;

	private GridChartPanel		gridChartPanel			= null;

	private MultiTabTablePanel	multiTabTablePanel		= null;

	private JComboBox			jComboBox				= null;

	/**
	 * Event listener list
	 */
	protected EventListenerList	listenerList			= new EventListenerList();

	/**
	 * This is the default constructor
	 */
	public PlotWindow() {
		super();
		initialize();
	}

	/**
	 * Constructor
	 * 
	 * @param dm
	 *            associated DataModel
	 */
	public PlotWindow(DataModel dm) {
		super();
		setDataModel(dm);
		initialize();
	}

	public void addMessageListener(MessageListener l) {
		this.listenerList.add(MessageListener.class, l);
		getGridChartPanel().addMessageListener(l);
	}

	public void addProgressListener(ProgressListener l) {
		this.listenerList.add(ProgressListener.class, l);
		getGridChartPanel().addProgressListener(l);
	}

	/**
	 * Export Data
	 */
	public void exportData() {
		final JFileChooser fc = new JFileChooser();
		final Preferences prefs = Preferences.userNodeForPackage(PlotWindow.class);
		String dirString = prefs.get(PlotWindow.DATA_DIR, null);
		if (dirString == null)
			fc.setCurrentDirectory(null);
		else
			fc.setCurrentDirectory(new File(dirString));

		StandardFileFilter filter = new StandardFileFilter("jcombinations");
		filter.setDescription("JCombinations-Files");
		fc.setFileFilter(filter);
		JCheckBox checkBox = new JCheckBox("Selection only");
		fc.setAccessory(checkBox);

		int returnVal = fc.showSaveDialog(this);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			setTitle(fc.getSelectedFile().toString());
			prefs.put(PlotWindow.DATA_DIR, fc.getCurrentDirectory().getAbsolutePath());
			try {
				ExportDataInterface out = new GeneralDataFormat();
				FileOutputStream fileOutputStream = new FileOutputStream(fc.getSelectedFile());

				ArrayList<TreePath> nodes;
				if (!checkBox.isSelected())
					nodes = selectionToNodes(null);
				else
					nodes = selectionToNodes(getJXTreeTable().getSelectedRows());

				if (fc.getSelectedFile().getName().endsWith(".zip")) {
					ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);
					zipOutputStream.putNextEntry(new ZipEntry("default.xml"));
					out.writeData(getDataModel(), zipOutputStream, nodes);
					zipOutputStream.close();
					fileOutputStream.close();
				} else
					out.writeData(getDataModel(), fileOutputStream, nodes);

			} catch (IOException e) {
				JOptionPane.showMessageDialog(this, "Error saving document", "I/O error", JOptionPane.ERROR_MESSAGE);
			}

		}
	}

	/**
	 * Export to RAT4
	 */
	public void exportRATData() {
		final JFileChooser fc = new JFileChooser();
		StandardFileFilter filter = new StandardFileFilter("csv");
		filter.setDescription("CSV-Files");
		fc.setFileFilter(filter);
		JCheckBox checkBox = new JCheckBox("Selection only");
		fc.setAccessory(checkBox);

		int returnVal = fc.showSaveDialog(this);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			ExportDataInterface out = new RATDataFormat();
			try {
				FileOutputStream fileOutputStream = new FileOutputStream(fc.getSelectedFile());
				ArrayList<TreePath> nodes;
				if (!checkBox.isSelected())
					nodes = selectionToNodes(null);
				else
					nodes = selectionToNodes(getJXTreeTable().getSelectedRows());
				out.writeData(getDataModel(), fileOutputStream, nodes);
				fileOutputStream.close();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(this, "Error saving document", "I/O error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * @return Returns the dm.
	 */
	public DataModel getDataModel() {
		return this.dm;
	}

	public MessageListener[] getMessageListeners() {
		return this.listenerList.getListeners(MessageListener.class);
	}

	public ProgressListener[] getProgressListeners() {
		return this.listenerList.getListeners(ProgressListener.class);
	}

	/**
	 * Import Data
	 */
	public void importData() {
		final JFileChooser fc = new JFileChooser();
		final Preferences prefs = Preferences.userNodeForPackage(PlotWindow.class);
		String dirString = prefs.get(PlotWindow.DATA_DIR, null);
		if (dirString == null)
			fc.setCurrentDirectory(null);
		else
			fc.setCurrentDirectory(new File(dirString));
		StandardFileFilter filter = new StandardFileFilter("zip");
		filter.addExtension("xml");
		filter.setDescription("JCombinations-Files");
		fc.setFileFilter(filter);
		fc.setMultiSelectionEnabled(true);

		int returnVal = fc.showOpenDialog(this);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			setTitle(fc.getSelectedFile().toString());
			prefs.put(PlotWindow.DATA_DIR, fc.getCurrentDirectory().getAbsolutePath());
			try {
				File[] files = fc.getSelectedFiles();
				for (File element : files) {
					ImportDataInterface in = new GeneralDataFormat();

					FileInputStream fileInputStream = new FileInputStream(element);
					if (element.getName().endsWith(".zip")) {
						ZipInputStream zipInputStream = new ZipInputStream(fileInputStream);
						zipInputStream.getNextEntry();
						in.readData(getDataModel(), zipInputStream);
						zipInputStream.close();
					} else
						in.readData(getDataModel(), fileInputStream);
					fileInputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(this, "Error loading document", "I/O error", JOptionPane.ERROR_MESSAGE);
			}
		}

	}

	public void removeMessageListener(MessageListener l) {
		this.listenerList.remove(MessageListener.class, l);
		getGridChartPanel().removeMessageListener(l);
	}

	public void removeProgressListener(ProgressListener l) {
		this.listenerList.remove(ProgressListener.class, l);
		getGridChartPanel().removeProgressListener(l);
	}

	/**
	 * @param dm
	 *            The dm to set.
	 */
	public void setDataModel(DataModel dm) {
		this.dm = dm;
	}

	/**
	 * This method initializes jButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getClearSearchButton() {
		if (this.clearSearchButton == null) {
			this.clearSearchButton = new JButton();
			this.clearSearchButton.setIcon(new ImageIcon(getClass().getResource(
					"/org/schreibubi/JCombinations/icons/16x16/actions/clear_left.png")));
			this.clearSearchButton.setToolTipText("Clear search");
			this.clearSearchButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					getSearchField().setText("");
				}
			});
			this.clearSearchButton.setPreferredSize(new java.awt.Dimension(30, 20));
		}
		return this.clearSearchButton;
	}

	/**
	 * This method initializes jButton3
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getCopyButton() {
		if (this.CopyButton == null) {
			this.CopyButton = new JButton();
			this.CopyButton.setIcon(new ImageIcon(getClass().getResource(
					"/org/schreibubi/JCombinations/icons/22x22/actions/editcopy.png")));
			this.CopyButton.setToolTipText("Copy to clipboard");
			this.CopyButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					getGridChartPanel().copyToClipboard();
				}
			});
		}
		return this.CopyButton;
	}

	/**
	 * This method initializes ExportExcelButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getExportExcelButton() {
		if (this.ExportExcelButton == null) {
			this.ExportExcelButton = new JButton();
			this.ExportExcelButton.setToolTipText("Export as Excel");
			this.ExportExcelButton.setIcon(new ImageIcon(getClass().getResource(
					"/org/schreibubi/JCombinations/icons/22x22/mimetypes/spreadsheet.png")));

			this.ExportExcelButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					final SwingWorker<Object, Object> worker = new SwingWorker<Object, Object>() {

						@Override
						public Object doInBackground() {

							final JFileChooser fc = new JFileChooser();
							final Preferences prefs = Preferences.userNodeForPackage(PlotWindow.class);
							String dirString = prefs.get(PlotWindow.EXCEL_DIR, null);
							if (dirString == null)
								fc.setCurrentDirectory(null);
							else
								fc.setCurrentDirectory(new File(dirString));

							StandardFileFilter filter = new StandardFileFilter("xls");
							filter.setDescription("Excel-Files");
							fc.setFileFilter(filter);
							int returnVal = fc.showSaveDialog(PlotWindow.this);

							if (returnVal == JFileChooser.APPROVE_OPTION) {
								prefs.put(PlotWindow.EXCEL_DIR, fc.getCurrentDirectory().getAbsolutePath());
								getGridChartPanel().generateExcel(fc.getSelectedFile(),
										selectionToNodes(getJXTreeTable().getSelectedRows()));
							}
							return null;
						}
					};
					worker.execute();
				}
			});
		}
		return this.ExportExcelButton;
	}

	/**
	 * This method initializes ExportPDFButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getExportPDFButton() {
		if (this.ExportPDFButton == null) {
			this.ExportPDFButton = new JButton();
			this.ExportPDFButton.setToolTipText("Export as PDF");
			this.ExportPDFButton.setIcon(new ImageIcon(getClass().getResource(
					"/org/schreibubi/JCombinations/icons/22x22/mimetypes/pdf.png")));

			this.ExportPDFButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					final SwingWorker<Object, Object> worker = new SwingWorker<Object, Object>() {

						@Override
						public Object doInBackground() {
							fireMessage(this, "Exporting PDF...");
							final JFileChooser fc = new JFileChooser();
							final Preferences prefs = Preferences.userNodeForPackage(PlotWindow.class);
							String dirString = prefs.get(PlotWindow.PDF_DIR, null);
							if (dirString == null)
								fc.setCurrentDirectory(null);
							else
								fc.setCurrentDirectory(new File(dirString));

							StandardFileFilter filter = new StandardFileFilter("pdf");
							filter.setDescription("PDF-Files");
							fc.setFileFilter(filter);
							int returnVal = fc.showSaveDialog(PlotWindow.this);

							if (returnVal == JFileChooser.APPROVE_OPTION) {
								prefs.put(PlotWindow.PDF_DIR, fc.getCurrentDirectory().getAbsolutePath());
								GridChartPanel.generatePDF(fc.getSelectedFile(), getDataModel(),
										selectionToNodes(getJXTreeTable().getSelectedRows()), new ProgressListener() {

											public void progressEnded(ProgressEvent evt) {
												fireProgressEnded(this, evt);
											}

											public void progressIncremented(ProgressEvent evt) {
												fireProgressIncremented(this, evt);
											}

											public void progressStarted(ProgressEvent evt) {
												fireProgressStarted(this, evt);
											}
										});
							}
							fireMessage(this, "Done");
							return null;
						}

					};
					worker.execute();
				}
			});
		}
		return this.ExportPDFButton;
	}

	/**
	 * This method initializes jButton4
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getExportSVGButton() {
		if (this.ExportSVGButton == null) {
			this.ExportSVGButton = new JButton();
			this.ExportSVGButton.setIcon(new ImageIcon(getClass().getResource(
					"/org/schreibubi/JCombinations/icons/22x22/mimetypes/vectorgfx.png")));
			this.ExportSVGButton.setToolTipText("Export as SVG");
			this.ExportSVGButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					final SwingWorker<Object, Object> worker = new SwingWorker<Object, Object>() {

						@Override
						public Object doInBackground() {
							final JFileChooser fc = new JFileChooser();
							final Preferences prefs = Preferences.userNodeForPackage(PlotWindow.class);
							String dirString = prefs.get(PlotWindow.SVG_DIR, null);
							if (dirString == null)
								fc.setCurrentDirectory(null);
							else
								fc.setCurrentDirectory(new File(dirString));

							StandardFileFilter filter = new StandardFileFilter("svg");
							filter.setDescription("SVG-Files");
							fc.setFileFilter(filter);

							int returnVal = fc.showSaveDialog(PlotWindow.this);

							if (returnVal == JFileChooser.APPROVE_OPTION) {
								prefs.put(PlotWindow.SVG_DIR, fc.getCurrentDirectory().getAbsolutePath());
								getGridChartPanel().generateSVG(fc.getSelectedFile(),
										selectionToNodes(getJXTreeTable().getSelectedRows()), true);
							}
							return null;
						}
					};
					worker.execute();
				}
			});
		}
		return this.ExportSVGButton;
	}

	/**
	 * This method initializes gridChartPanel
	 * 
	 * @return org.schreibubi.JCombinations.ui.GridChartPanel
	 */
	private GridChartPanel getGridChartPanel() {
		if (this.gridChartPanel == null) {
			this.gridChartPanel = new GridChartPanel();
			this.gridChartPanel.setGridChartModel(getDataModel());
		}
		return this.gridChartPanel;
	}

	/**
	 * This method initializes jComboBox
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getJComboBox() {
		if (this.jComboBox == null) {
			this.jComboBox = new JComboBox();
			getDataModel().addDataEventListener(new DataEventListenerAdapter() {

				/*
				 * (non-Javadoc)
				 * 
				 * @see org.schreibubi.JCombinations.logic.DataEventListener#selectionUpdated(org.schreibubi.JCombinations.logic.SelectionEvent)
				 */
				@Override
				public void selectionUpdated(SelectionEvent e) {
					ArrayList<String> possibleList = getDataModel().getPossibleXdataList(e.getSelection());
					PlotWindow.this.jComboBox.removeAllItems();
					if (possibleList != null)
						for (String s : possibleList)
							PlotWindow.this.jComboBox.addItem(s);
				}
			});
			this.jComboBox.addActionListener(new java.awt.event.ActionListener() {

				public void actionPerformed(ActionEvent arg0) {
				}
			});
		}
		return this.jComboBox;
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
			this.jContentPane.add(getJToolBar(), java.awt.BorderLayout.NORTH);
			this.jContentPane.add(getJSplitPane(), java.awt.BorderLayout.CENTER);
		}
		return this.jContentPane;
	}

	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel() {
		if (this.jPanel == null) {
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints4.gridy = 0;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints3.gridx = 1;
			gridBagConstraints3.gridy = 0;
			gridBagConstraints3.ipadx = 0;
			gridBagConstraints3.ipady = 0;
			gridBagConstraints3.weightx = 1.0;
			gridBagConstraints3.insets = new java.awt.Insets(5, 5, 5, 5);
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints2.gridy = 1;
			gridBagConstraints2.ipadx = 0;
			gridBagConstraints2.ipady = 0;
			gridBagConstraints2.weightx = 1.0;
			gridBagConstraints2.weighty = 1.0;
			gridBagConstraints2.gridwidth = 2;
			gridBagConstraints2.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints2.gridx = 0;
			this.jPanel = new JPanel();
			this.jPanel.setLayout(new GridBagLayout());
			this.jPanel.add(getLeftScrollPane(), gridBagConstraints2);
			this.jPanel.add(getSearchField(), gridBagConstraints3);
			this.jPanel.add(getClearSearchButton(), gridBagConstraints4);
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
			this.jSplitPane.setOneTouchExpandable(true);
			this.jSplitPane.setDividerLocation(300);
			this.jSplitPane.setOrientation(javax.swing.JSplitPane.HORIZONTAL_SPLIT);
			// jSplitPane.setPreferredSize( new java.awt.Dimension( 400, 400 )
			// );
			this.jSplitPane.setLeftComponent(getJPanel());
			this.jSplitPane.setRightComponent(getJTabbedPane());
		}
		return this.jSplitPane;
	}

	/**
	 * This method initializes jTabbedPane
	 * 
	 * @return javax.swing.JTabbedPane
	 */
	private JTabbedPane getJTabbedPane() {
		if (this.jTabbedPane == null) {
			this.jTabbedPane = new JTabbedPane();
			this.jTabbedPane.setTabPlacement(SwingConstants.BOTTOM);
			this.jTabbedPane.addTab("Graphs", new ImageIcon(getClass().getResource(
					"/org/schreibubi/JCombinations/icons/16x16/mimetypes/log.png")), getRightGraphScrollPane(),
					"Displays data as graphs");
			this.jTabbedPane.addTab("Tables", new ImageIcon(getClass().getResource(
					"/org/schreibubi/JCombinations/icons/16x16/mimetypes/spreadsheet.png")),
					getMultiTabTablePanel(), null);
		}
		return this.jTabbedPane;
	}

	/**
	 * This method initializes jToolBar
	 * 
	 * @return javax.swing.JToolBar
	 */
	private JToolBar getJToolBar() {
		if (this.jToolBar == null) {
			this.jToolBar = new JToolBar();
			this.jToolBar.add(getCopyButton());
			this.jToolBar.addSeparator();
			this.jToolBar.add(getExportSVGButton());
			this.jToolBar.add(getExportPDFButton());
			this.jToolBar.add(getExportExcelButton());
			this.jToolBar.addSeparator();
			this.jToolBar.add(getPrintButton());
			this.jToolBar.add(getJComboBox());
			this.jToolBar.add(getOverlayButton());
		}
		return this.jToolBar;
	}

	/**
	 * This method initializes JXTreeTable1
	 * 
	 * @return org.jdesktop.swing.JXTreeTable
	 */
	private JXTreeTable getJXTreeTable() {
		if (this.jXTreeTable == null) {
			this.jXTreeTable = new JXTreeTable();
			this.jXTreeTable.setTreeTableModel(new TreeTableModelAdapter(getDataModel()));
			this.jXTreeTable.getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			this.jXTreeTable.setShowVerticalLines(true);
			this.jXTreeTable.setShowsRootHandles(true);
			this.jXTreeTable.setSize(100, 100);
			this.jXTreeTable.setRowHeight(20);
			this.jXTreeTable.setTransferHandler(new ShmooTransferHandler());
			this.jXTreeTable.setDragEnabled(true);
			this.jXTreeTable.setRootVisible(true);
			ActionMap map = this.jXTreeTable.getActionMap();
			map.put(TransferHandler.getCutAction().getValue(Action.NAME), TransferHandler.getCutAction());
			map.put(TransferHandler.getCopyAction().getValue(Action.NAME), TransferHandler.getCopyAction());
			map.put(TransferHandler.getPasteAction().getValue(Action.NAME), TransferHandler.getPasteAction());
			this.jXTreeTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent e) {
					// Ignore extra messages.
					if (e.getValueIsAdjusting())
						return;

					ListSelectionModel lsm = (ListSelectionModel) e.getSource();
					if (lsm.isSelectionEmpty()) {
					} else {
						ArrayList<TreePath> nodes = selectionToNodes(getJXTreeTable().getSelectedRows());

						// getDataModel().setSelection( nodes );
						getDataModel().fireSelectionChanged(this, nodes);
					}
				}
			});
		}
		return this.jXTreeTable;
	}

	/**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getLeftScrollPane() {
		if (this.leftScrollPane == null) {
			this.leftScrollPane = new JScrollPane();
			this.leftScrollPane.setViewportView(getJXTreeTable());
		}
		return this.leftScrollPane;
	}

	/**
	 * This method initializes multiTabTablePanel
	 * 
	 * @return org.schreibubi.JCombinations.logic.MultiTabTablePanel
	 */
	private MultiTabTablePanel getMultiTabTablePanel() {
		if (this.multiTabTablePanel == null) {
			this.multiTabTablePanel = new MultiTabTablePanel();
			this.multiTabTablePanel.setMultiTabTableModel(getDataModel());
		}
		return this.multiTabTablePanel;
	}

	/**
	 * This method initializes jButton3
	 * 
	 * @return javax.swing.JButton
	 */
	private JCheckBox getOverlayButton() {
		if (this.OverlayButton == null) {
			this.OverlayButton = new JCheckBox("Overlay");
			this.OverlayButton.setToolTipText("Overlay on/off");
			this.OverlayButton.addItemListener(new java.awt.event.ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					dm.setOverlay(e.getStateChange() == ItemEvent.SELECTED);
				}
			});
		}
		return this.OverlayButton;
	}

	/**
	 * This method initializes jButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getPrintButton() {
		if (this.PrintButton == null) {
			this.PrintButton = new JButton();
			this.PrintButton.setIcon(new ImageIcon(getClass().getResource(
					"/org/schreibubi/JCombinations/icons/22x22/actions/printer1.png")));
			this.PrintButton.setToolTipText("Print");
			this.PrintButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {

					PrinterJob printJob = PrinterJob.getPrinterJob();
					printJob.setPrintable(getGridChartPanel());
					if (printJob.printDialog())
						getGridChartPanel().printMe(printJob);
				}
			});
		}
		return this.PrintButton;
	}

	/**
	 * This method initializes jScrollPane2
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getRightGraphScrollPane() {
		if (this.rightGraphScrollPane == null) {
			this.rightGraphScrollPane = new JScrollPane();
			this.rightGraphScrollPane.setViewportView(getGridChartPanel());
		}
		return this.rightGraphScrollPane;
	}

	/**
	 * This method initializes jTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getSearchField() {
		if (this.searchField == null) {
			this.searchField = new JTextField();
			this.searchField.setPreferredSize(new java.awt.Dimension(100, 20));
			this.searchField.addCaretListener(new javax.swing.event.CaretListener() {
				public void caretUpdate(javax.swing.event.CaretEvent e) {
					System.out.println(PlotWindow.this.searchField.getText());
					Filter[] filters = new Filter[] { new PatternFilter(PlotWindow.this.searchField.getText(),
							Pattern.MULTILINE, 0), };
					FilterPipeline pipeline = new FilterPipeline(filters);
					PlotWindow.this.jXTreeTable.setFilters(pipeline);
				}
			});
		}
		return this.searchField;
	}

	/**
	 * This method initializes this
	 */
	private void initialize() {
		this.setSize(926, 630);
		this.setMinimumSize(new java.awt.Dimension(200, 200));
		this.setContentPane(getJContentPane());
		this.setIconifiable(true);
		this.setMaximizable(true);
		this.setResizable(true);
		this.setTitle("Available Shmoos");
		this.setClosable(true);
	}

	private ArrayList<TreePath> selectionToNodes(int[] selection) {
		ArrayList<TreePath> nodes = new ArrayList<TreePath>();
		if (selection == null) {
			int rows = getJXTreeTable().getRowCount();
			for (int i = 0; i < rows; i++)
				nodes.add(getJXTreeTable().getPathForRow(i));
		} else
			for (int element : selection)
				nodes.add(getJXTreeTable().getPathForRow(element));
		return nodes;
	}

	protected void fireMessage(Object source, String message) {
		// Guaranteed to return a non-null array
		Object[] listeners = this.listenerList.getListenerList();
		MessageEvent e = null;
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2)
			if (listeners[i] == MessageListener.class) {
				// Lazily create the event:
				if (e == null)
					e = new MessageEvent(source, message);
				((MessageListener) listeners[i + 1]).message(e);
			}
	}

	protected void fireProgressEnded(Object source, ProgressEvent pe) {
		// Guaranteed to return a non-null array
		Object[] listeners = this.listenerList.getListenerList();
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2)
			if (listeners[i] == ProgressListener.class)
				// Lazily create the event:
				((ProgressListener) listeners[i + 1]).progressEnded(pe);
	}

	protected void fireProgressIncremented(Object source, ProgressEvent pe) {
		// Guaranteed to return a non-null array
		Object[] listeners = this.listenerList.getListenerList();
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2)
			if (listeners[i] == ProgressListener.class)
				// Lazily create the event:
				((ProgressListener) listeners[i + 1]).progressIncremented(pe);
	}

	protected void fireProgressStarted(Object source, ProgressEvent pe) {
		// Guaranteed to return a non-null array
		Object[] listeners = this.listenerList.getListenerList();
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2)
			if (listeners[i] == ProgressListener.class)
				// Lazily create the event:
				((ProgressListener) listeners[i + 1]).progressStarted(pe);
	}

} // @jve:decl-index=0:visual-constraint="11,17"
