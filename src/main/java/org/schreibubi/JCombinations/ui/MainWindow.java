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

import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.util.logging.Level;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import org.jdesktop.swingx.JXStatusBar;
import org.jdesktop.swingx.event.MessageEvent;
import org.jdesktop.swingx.event.MessageListener;
import org.jdesktop.swingx.event.ProgressEvent;
import org.jdesktop.swingx.event.ProgressListener;
import org.schreibubi.JCombinations.Info;
import org.schreibubi.JCombinations.logic.DataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jgoodies.looks.plastic.PlasticLookAndFeel;
import com.jgoodies.looks.plastic.PlasticXPLookAndFeel;
import com.jgoodies.looks.plastic.theme.ExperienceBlue;

/**
 * Main program window
 * 
 * @author Jörg Werner
 * 
 */
public class MainWindow extends JFrame {

	/**
	 * Listener for Internal Frame events
	 * 
	 * @author Jörg Werner
	 * 
	 */
	public class IntFrameChangeListener implements InternalFrameListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * javax.swing.event.InternalFrameListener#internalFrameActivated(javax
		 * .swing.event.InternalFrameEvent)
		 */
		public void internalFrameActivated(InternalFrameEvent arg0) {
			JInternalFrame i = arg0.getInternalFrame();
			updateMenuItemsActive(i);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * javax.swing.event.InternalFrameListener#internalFrameClosed(javax
		 * .swing.event.InternalFrameEvent)
		 */
		public void internalFrameClosed(InternalFrameEvent arg0) {
			JInternalFrame[] allFrames = getJDesktopPane().getAllFrames();
			boolean plotFrameLeft = false;
			for (JInternalFrame element : allFrames) {
				if (element instanceof PlotWindow) {
					plotFrameLeft = true;
				}
			}
			if (!plotFrameLeft) {
				updateMenuItemsActive(null);
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * javax.swing.event.InternalFrameListener#internalFrameClosing(javax
		 * .swing.event.InternalFrameEvent)
		 */
		public void internalFrameClosing(InternalFrameEvent arg0) {
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * javax.swing.event.InternalFrameListener#internalFrameDeactivated(
		 * javax.swing.event.InternalFrameEvent)
		 */
		public void internalFrameDeactivated(InternalFrameEvent arg0) {
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * javax.swing.event.InternalFrameListener#internalFrameDeiconified(
		 * javax.swing.event.InternalFrameEvent)
		 */
		public void internalFrameDeiconified(InternalFrameEvent arg0) {
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * javax.swing.event.InternalFrameListener#internalFrameIconified(javax
		 * .swing.event.InternalFrameEvent)
		 */
		public void internalFrameIconified(InternalFrameEvent arg0) {
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * javax.swing.event.InternalFrameListener#internalFrameOpened(javax
		 * .swing.event.InternalFrameEvent)
		 */
		public void internalFrameOpened(InternalFrameEvent arg0) {
			JInternalFrame i = arg0.getInternalFrame();
			updateMenuItemsActive(i);
		}

	}

	/**
	 * @author Jörg Werner
	 * 
	 */
	public class MyStatusBar extends JXStatusBar implements MessageListener,
			ProgressListener {

		/**
		 * 
		 */
		private static final long serialVersionUID = 8193727898452849392L;

		private JLabel leadingLabel = null;

		private JLabel trailingLabel = null;

		private JProgressBar progressBar = null;

		/**
		 * 
		 */
		public MyStatusBar() {

			leadingLabel = new JLabel("");
			JXStatusBar.Constraint c1 = new JXStatusBar.Constraint();
			c1.setFixedWidth(100);
			add(leadingLabel, c1);
			progressBar = new JProgressBar();
			add(progressBar, new JXStatusBar.Constraint(
					JXStatusBar.Constraint.ResizeBehavior.FILL));
			progressBar.setVisible(true);
			trailingLabel = new JLabel("Ready");
			JXStatusBar.Constraint c2 = new JXStatusBar.Constraint();
			c2.setFixedWidth(100);
			add(trailingLabel, c2);
		}

		/**
		 * @return leading message
		 */
		public String getLeadingMessage() {
			return leadingLabel.getText();
		}

		/**
		 * @return trailing Message
		 */
		public String getTrailingMessage() {
			return trailingLabel.getText();
		}

		public void message(MessageEvent evt) {
			Level level = evt.getLevel();

			if (level == Level.FINE) {
				// transient messages are sent to the leading label.
				setLeadingMessage(evt.getMessage());
			} else {
				// persisent messages are sent to the trailing label.
				setTrailingMessage(evt.getMessage());
			}
		}

		public void progressEnded(ProgressEvent evt) {
			progressBar.setVisible(false);
		}

		public void progressIncremented(ProgressEvent evt) {
			logger.debug("Progressbar set to" + evt.getProgress());
			progressBar.setValue(evt.getProgress());
		}

		public void progressStarted(ProgressEvent evt) {
			// Set up the progress bar to handle a new progress event.
			boolean indeterminate = evt.isIndeterminate();
			progressBar.setIndeterminate(indeterminate);
			if (indeterminate == false) {
				progressBar.setValue(evt.getMinimum());
				progressBar.setMinimum(evt.getMinimum());
				progressBar.setMaximum(evt.getMaximum());
			}
			progressBar.setVisible(true);
		}

		/**
		 * Places the message in the leading area.
		 * 
		 * @param messageText
		 *            the text to place
		 */
		public void setLeadingMessage(String messageText) {
			leadingLabel.setText(messageText);
		}

		/**
		 * Places the message in the trailing area.
		 * 
		 * @param messageText
		 *            the text to place
		 */
		public void setTrailingMessage(String messageText) {
			trailingLabel.setText(messageText);
		}
	}

	/**
	 * TransferActionListener
	 * 
	 * @author Jörg Werner
	 * 
	 */
	public class TransferActionListener implements ActionListener,
			PropertyChangeListener {
		private JComponent focusOwner = null;

		/**
		 * Constructor
		 */
		public TransferActionListener() {
			KeyboardFocusManager manager = KeyboardFocusManager
					.getCurrentKeyboardFocusManager();
			manager.addPropertyChangeListener("permanentFocusOwner", this);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent
		 * )
		 */
		public void actionPerformed(ActionEvent e) {
			if (this.focusOwner == null) {
				return;
			}
			String action = e.getActionCommand();
			Action a = this.focusOwner.getActionMap().get(action);
			if (a != null) {
				a.actionPerformed(new ActionEvent(this.focusOwner,
						ActionEvent.ACTION_PERFORMED, null));
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @seejava.beans.PropertyChangeListener#propertyChange(java.beans.
		 * PropertyChangeEvent)
		 */
		public void propertyChange(PropertyChangeEvent e) {
			Object o = e.getNewValue();
			if (o instanceof JComponent) {
				this.focusOwner = (JComponent) o;
			} else {
				this.focusOwner = null;
			}
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 3977014033327141169L;

	private static Logger logger = LoggerFactory.getLogger(MainWindow.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PlasticLookAndFeel.setPlasticTheme(new ExperienceBlue());
		try {
			UIManager.setLookAndFeel(new PlasticXPLookAndFeel());
			// UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}

		MainWindow.logger.info("Program started");
		// LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		// StatusPrinter.print(lc);

		MainWindow application = new MainWindow();
		application.setVisible(true);
	}

	private javax.swing.JPanel jContentPane = null;

	private javax.swing.JMenuBar MMenuBar = null;

	private javax.swing.JMenu fileMenu = null;

	private javax.swing.JMenu editMenu = null;

	private javax.swing.JMenu helpMenu = null;

	private javax.swing.JMenuItem exitMenuItem = null;

	private javax.swing.JMenuItem aboutMenuItem = null;

	private javax.swing.JMenuItem cutMenuItem = null;

	private javax.swing.JMenuItem copyMenuItem = null;

	private javax.swing.JMenuItem pasteMenuItem = null;

	private JMenuItem importDataItem = null;

	private JToolBar jToolBar = null;

	private JButton importDataButton = null;

	private JDesktopPane jDesktopPane = null;

	private JMenuItem LoadLimitsMenuItem = null;

	private JMenuItem SaveLimitsMenuItem = null;

	private JButton openLimitsButton = null;

	private JButton openShmooButton = null;

	private JButton openMaskButton = null;

	private JMenuItem limitsMenuItem = null;

	private JMenuItem exportDataItem = null;

	private JMenuItem newDataItem = null;

	private TransferActionListener transferActionListener = null;

	private JMenuItem ratExportMenuItem = null;

	private IntFrameChangeListener intFrameChangeListener = null;

	private JMenuItem closeMenuItem = null;

	private JMenu windowMenu = null;

	private JMenuItem maskMenuItem = null;

	private MaskDuts maskDuts = null;

	private LimitsEditor limitsEditor = null;

	private JMenuItem openDataItem = null;

	private MyStatusBar myStatusBar = null;

	/**
	 * This is the default constructor
	 */
	public MainWindow() {
		super();
		initialize();
	}

	/**
	 * This method initializes jDesktopPane
	 * 
	 * @return javax.swing.JDesktopPane
	 */
	public JDesktopPane getJDesktopPane() {
		if (this.jDesktopPane == null) {
			this.jDesktopPane = new JDesktopPane();
		}
		return this.jDesktopPane;
	}

	/**
	 * 
	 */
	private void closeWindowAction() {
		JInternalFrame j = this.jDesktopPane.getSelectedFrame();
		try {
			j.setClosed(true);
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	private void exportDataAction() {
		JInternalFrame j = this.jDesktopPane.getSelectedFrame();
		if (j instanceof PlotWindow) {
			PlotWindow s = (PlotWindow) j;
			s.exportData();
		}
	}

	/**
	 * Export data to RAT4
	 */
	private void exportRATDataAction() {
		JInternalFrame j = this.jDesktopPane.getSelectedFrame();
		if (j instanceof PlotWindow) {
			PlotWindow s = (PlotWindow) j;
			s.exportRATData();
		}
	}

	/**
	 * This method initializes jMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private javax.swing.JMenuItem getAboutMenuItem() {
		if (this.aboutMenuItem == null) {
			this.aboutMenuItem = new javax.swing.JMenuItem();
			this.aboutMenuItem.setText("About");
			this.aboutMenuItem
					.setIcon(new ImageIcon(
							getClass()
									.getResource(
											"/org/schreibubi/JCombinations/icons/16x16/actions/info.png")));
			this.aboutMenuItem
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							JOptionPane.showMessageDialog(MainWindow.this,
									"JCombinations\nWritten by " + Info.authors
											+ "\n" + Info.copyright + "\n"
											+ Info.version);
						}
					});
		}
		return this.aboutMenuItem;
	}

	/**
	 * This method initializes closeMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getCloseMenuItem() {
		if (this.closeMenuItem == null) {
			this.closeMenuItem = new JMenuItem();
			this.closeMenuItem.setText("Close");
			this.closeMenuItem.setMnemonic(java.awt.event.KeyEvent.VK_C);
			this.closeMenuItem.setAccelerator(javax.swing.KeyStroke
					.getKeyStroke(java.awt.event.KeyEvent.VK_W,
							java.awt.Event.CTRL_MASK, false));
			this.closeMenuItem.setEnabled(false);
			this.closeMenuItem
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							closeWindowAction();
						}
					});
		}
		return this.closeMenuItem;
	}

	/**
	 * This method initializes jMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private javax.swing.JMenuItem getCopyMenuItem() {
		if (this.copyMenuItem == null) {
			this.copyMenuItem = new javax.swing.JMenuItem();
			this.copyMenuItem.setText("Copy");
			this.copyMenuItem.setAccelerator(javax.swing.KeyStroke
					.getKeyStroke(java.awt.event.KeyEvent.VK_C,
							java.awt.Event.CTRL_MASK, true));
			this.copyMenuItem
					.setIcon(new ImageIcon(
							getClass()
									.getResource(
											"/org/schreibubi/JCombinations/icons/16x16/actions/editcopy.png")));
			this.copyMenuItem.addActionListener(getTransferActionListener());
		}
		return this.copyMenuItem;
	}

	/**
	 * This method initializes jMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private javax.swing.JMenuItem getCutMenuItem() {
		if (this.cutMenuItem == null) {
			this.cutMenuItem = new javax.swing.JMenuItem();
			this.cutMenuItem.setText("Cut");
			this.cutMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
					java.awt.event.KeyEvent.VK_X, java.awt.Event.CTRL_MASK,
					true));
			this.cutMenuItem
					.setIcon(new ImageIcon(
							getClass()
									.getResource(
											"/org/schreibubi/JCombinations/icons/16x16/actions/editcut.png")));
			this.cutMenuItem.addActionListener(getTransferActionListener());
		}
		return this.cutMenuItem;
	}

	/**
	 * This method initializes jMenu
	 * 
	 * @return javax.swing.JMenu
	 */
	private javax.swing.JMenu getEditMenu() {
		if (this.editMenu == null) {
			this.editMenu = new javax.swing.JMenu();
			this.editMenu.setText("Edit");
			this.editMenu.add(getCutMenuItem());
			this.editMenu.add(getCopyMenuItem());
			this.editMenu.add(getPasteMenuItem());
		}
		return this.editMenu;
	}

	/**
	 * This method initializes jMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private javax.swing.JMenuItem getExitMenuItem() {
		if (this.exitMenuItem == null) {
			this.exitMenuItem = new javax.swing.JMenuItem();
			this.exitMenuItem.setText("Exit");
			this.exitMenuItem
					.setIcon(new ImageIcon(
							getClass()
									.getResource(
											"/org/schreibubi/JCombinations/icons/16x16/actions/exit.png")));
			this.exitMenuItem
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							System.exit(0);
							// throw new RuntimeException();
						}
					});
		}
		return this.exitMenuItem;
	}

	/**
	 * This method initializes SaveMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getExportDataItem() {
		if (this.exportDataItem == null) {
			this.exportDataItem = new JMenuItem();
			this.exportDataItem.setText("Export data...");
			this.exportDataItem.setAccelerator(javax.swing.KeyStroke
					.getKeyStroke(java.awt.event.KeyEvent.VK_E,
							java.awt.Event.CTRL_MASK, false));
			this.exportDataItem.setEnabled(false);
			this.exportDataItem
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							exportDataAction();
						}
					});
		}
		return this.exportDataItem;
	}

	/**
	 * This method initializes jMenu
	 * 
	 * @return javax.swing.JMenu
	 */
	private javax.swing.JMenu getFileMenu() {
		if (this.fileMenu == null) {
			this.fileMenu = new JMenu();
			this.fileMenu.setText("File");
			this.fileMenu.add(getLoadLimitsMenuItem());
			this.fileMenu.add(getSaveLimitsMenuItem());
			this.fileMenu.addSeparator();
			this.fileMenu.add(getNewDataItem());
			this.fileMenu.add(getOpenDataItem());
			this.fileMenu.add(getImportDataItem());
			this.fileMenu.add(getExportDataItem());
			this.fileMenu.add(getRatExportMenuItem());
			this.fileMenu.addSeparator();
			this.fileMenu.add(getCloseMenuItem());
			this.fileMenu.addSeparator();
			this.fileMenu.add(getExitMenuItem());
		}
		return this.fileMenu;
	}

	/**
	 * This method initializes jMenu
	 * 
	 * @return javax.swing.JMenu
	 */
	private javax.swing.JMenu getHelpMenu() {
		if (this.helpMenu == null) {
			this.helpMenu = new javax.swing.JMenu();
			this.helpMenu.setText("Help");
			this.helpMenu.add(getAboutMenuItem());
		}
		return this.helpMenu;
	}

	/**
	 * This method initializes jButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getImportDataButton() {
		if (this.importDataButton == null) {
			this.importDataButton = new JButton();
			this.importDataButton
					.setIcon(new ImageIcon(
							getClass()
									.getResource(
											"/org/schreibubi/JCombinations/icons/22x22/actions/fileopen.png")));
			this.importDataButton
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							importDataAction();
						}
					});
		}
		return this.importDataButton;
	}

	/**
	 * This method initializes jMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getImportDataItem() {
		if (this.importDataItem == null) {
			this.importDataItem = new JMenuItem();
			this.importDataItem.setText("Import data...");
			this.importDataItem.setAccelerator(javax.swing.KeyStroke
					.getKeyStroke(java.awt.event.KeyEvent.VK_I,
							java.awt.Event.CTRL_MASK, false));
			this.importDataItem.setEnabled(false);
			this.importDataItem
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							importDataAction();
						}
					});
		}
		return this.importDataItem;
	}

	/**
	 * @return the FrameChangeListener
	 */
	private IntFrameChangeListener getIntFrameChangeListener() {
		if (this.intFrameChangeListener == null) {
			this.intFrameChangeListener = new IntFrameChangeListener();
		}
		return this.intFrameChangeListener;
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
			this.jContentPane.add(getJDesktopPane(),
					java.awt.BorderLayout.CENTER);
			this.jContentPane
					.add(getMyStatusBar(), java.awt.BorderLayout.SOUTH);
		}
		return this.jContentPane;
	}

	/**
	 * This method initializes jToolBar
	 * 
	 * @return javax.swing.JToolBar
	 */
	private JToolBar getJToolBar() {
		if (this.jToolBar == null) {
			this.jToolBar = new JToolBar();
			this.jToolBar.add(getOpenMaskButton());
			this.jToolBar.add(getOpenShmooButton());
			this.jToolBar.add(getOpenLimitsButton());
			this.jToolBar.add(getImportDataButton());
		}
		return this.jToolBar;
	}

	/**
	 * This method initializes NewLimits
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getLimitsMenuItem() {
		if (this.limitsMenuItem == null) {
			this.limitsMenuItem = new JMenuItem();
			this.limitsMenuItem.setText("Limit Window...");
			this.limitsMenuItem.setAccelerator(javax.swing.KeyStroke
					.getKeyStroke(java.awt.event.KeyEvent.VK_F,
							java.awt.Event.CTRL_MASK, false));
			this.limitsMenuItem
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							limitsWindowAction();
						}
					});
		}
		return this.limitsMenuItem;
	}

	/**
	 * This method initializes jMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getLoadLimitsMenuItem() {
		if (this.LoadLimitsMenuItem == null) {
			this.LoadLimitsMenuItem = new JMenuItem();
			this.LoadLimitsMenuItem.setText("Load Limits...");
			this.LoadLimitsMenuItem.setAccelerator(javax.swing.KeyStroke
					.getKeyStroke(java.awt.event.KeyEvent.VK_L,
							java.awt.Event.CTRL_MASK, false));
			this.LoadLimitsMenuItem.setEnabled(false);
			this.LoadLimitsMenuItem
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							loadLimitsAction();
						}
					});
		}
		return this.LoadLimitsMenuItem;
	}

	/**
	 * This method initializes maskMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getMaskMenuItem() {
		if (this.maskMenuItem == null) {
			this.maskMenuItem = new JMenuItem();
			this.maskMenuItem.setText("Mask Window...");
			this.maskMenuItem.setAccelerator(javax.swing.KeyStroke
					.getKeyStroke(java.awt.event.KeyEvent.VK_M,
							java.awt.Event.CTRL_MASK, false));
			this.maskMenuItem
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							maskWindowAction();
						}
					});
		}
		return this.maskMenuItem;
	}

	/**
	 * This method initializes jJMenuBar
	 * 
	 * @return javax.swing.JMenuBar
	 */
	private javax.swing.JMenuBar getMMenuBar() {
		if (this.MMenuBar == null) {
			this.MMenuBar = new javax.swing.JMenuBar();
			this.MMenuBar.add(getFileMenu());
			this.MMenuBar.add(getEditMenu());
			this.MMenuBar.add(getHelpMenu());
			this.MMenuBar.add(getWindowMenu());
		}
		return this.MMenuBar;
	}

	private MyStatusBar getMyStatusBar() {
		if (this.myStatusBar == null) {
			this.myStatusBar = new MyStatusBar();
		}
		return this.myStatusBar;
	}

	/**
	 * This method initializes newMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getNewDataItem() {
		if (this.newDataItem == null) {
			this.newDataItem = new JMenuItem();
			this.newDataItem.setText("New Data...");
			this.newDataItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
					java.awt.event.KeyEvent.VK_N, java.awt.Event.CTRL_MASK,
					false));
			this.newDataItem
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							newDataAction();
						}
					});
		}
		return this.newDataItem;
	}

	/**
	 * This method initializes openDataItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getOpenDataItem() {
		if (this.openDataItem == null) {
			this.openDataItem = new JMenuItem();
			this.openDataItem
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							openDataAction();
						}
					});
			this.openDataItem.setText("Open data...");
			this.openDataItem.setAccelerator(javax.swing.KeyStroke
					.getKeyStroke(java.awt.event.KeyEvent.VK_O,
							java.awt.Event.CTRL_MASK, false));
		}
		return this.openDataItem;
	}

	/**
	 * This method initializes jButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getOpenLimitsButton() {
		if (this.openLimitsButton == null) {
			this.openLimitsButton = new JButton();
			this.openLimitsButton
					.setIcon(new ImageIcon(
							getClass()
									.getResource(
											"/org/schreibubi/JCombinations/icons/22x22/actions/text_block.png")));
			this.openLimitsButton
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							limitsWindowAction();
						}
					});
		}
		return this.openLimitsButton;
	}

	/**
	 * This method initializes jButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getOpenMaskButton() {
		if (this.openMaskButton == null) {
			this.openMaskButton = new JButton();
			this.openMaskButton
					.setIcon(new ImageIcon(
							getClass()
									.getResource(
											"/org/schreibubi/JCombinations/icons/22x22/actions/view_detailed.png")));
			this.openMaskButton
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							maskWindowAction();
						}
					});
		}
		return this.openMaskButton;
	}

	/**
	 * This method initializes jButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getOpenShmooButton() {
		if (this.openShmooButton == null) {
			this.openShmooButton = new JButton();
			this.openShmooButton
					.setIcon(new ImageIcon(
							getClass()
									.getResource(
											"/org/schreibubi/JCombinations/icons/22x22/actions/view_sidetree.png")));
			this.openShmooButton
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							newDataAction();
						}
					});
		}
		return this.openShmooButton;
	}

	/**
	 * This method initializes jMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private javax.swing.JMenuItem getPasteMenuItem() {
		if (this.pasteMenuItem == null) {
			this.pasteMenuItem = new javax.swing.JMenuItem();
			this.pasteMenuItem.setText("Paste");
			this.pasteMenuItem.setAccelerator(javax.swing.KeyStroke
					.getKeyStroke(java.awt.event.KeyEvent.VK_V,
							java.awt.Event.CTRL_MASK, true));
			this.pasteMenuItem
					.setIcon(new ImageIcon(
							getClass()
									.getResource(
											"/org/schreibubi/JCombinations/icons/16x16/actions/editpaste.png")));
			this.pasteMenuItem.addActionListener(getTransferActionListener());
		}
		return this.pasteMenuItem;
	}

	/**
	 * This method initializes ratExportMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getRatExportMenuItem() {
		if (this.ratExportMenuItem == null) {
			this.ratExportMenuItem = new JMenuItem();
			this.ratExportMenuItem.setText("Export to RAT4...");
			this.ratExportMenuItem.setAccelerator(javax.swing.KeyStroke
					.getKeyStroke(java.awt.event.KeyEvent.VK_R,
							java.awt.Event.CTRL_MASK, false));
			this.ratExportMenuItem.setEnabled(false);
			this.ratExportMenuItem
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							exportRATDataAction();
						}
					});
		}
		return this.ratExportMenuItem;
	}

	/**
	 * This method initializes jMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getSaveLimitsMenuItem() {
		if (this.SaveLimitsMenuItem == null) {
			this.SaveLimitsMenuItem = new JMenuItem();
			this.SaveLimitsMenuItem.setText("Save Limits...");
			this.SaveLimitsMenuItem.setAccelerator(javax.swing.KeyStroke
					.getKeyStroke(java.awt.event.KeyEvent.VK_S,
							java.awt.Event.CTRL_MASK, false));
			this.SaveLimitsMenuItem.setEnabled(false);
			this.SaveLimitsMenuItem
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							saveLimitsAction();
						}
					});
		}
		return this.SaveLimitsMenuItem;
	}

	private TransferActionListener getTransferActionListener() {
		if (this.transferActionListener == null) {
			this.transferActionListener = new TransferActionListener();
		}
		return this.transferActionListener;
	}

	/**
	 * This method initializes windowMenu
	 * 
	 * @return javax.swing.JMenu
	 */
	private JMenu getWindowMenu() {
		if (this.windowMenu == null) {
			this.windowMenu = new JMenu();
			this.windowMenu.setText("Window");
			this.windowMenu.add(getMaskMenuItem());
			this.windowMenu.add(getLimitsMenuItem());
		}
		return this.windowMenu;
	}

	/**
	 * This method initializes the LimitsEditor
	 * 
	 * @return LimitsEditor
	 */
	private LimitsEditor gLimitsEditor() {
		if (this.limitsEditor == null) {
			this.limitsEditor = new LimitsEditor();
			this.limitsEditor
					.addInternalFrameListener(getIntFrameChangeListener());
			this.jDesktopPane.add(this.limitsEditor);
		}
		return this.limitsEditor;
	}

	/**
	 * This method initializes MaskDuts Dialog
	 * 
	 * @return MaskDuts
	 */
	private MaskDuts gMaskDuts() {
		if (this.maskDuts == null) {
			this.maskDuts = new MaskDuts();
			this.maskDuts.addInternalFrameListener(getIntFrameChangeListener());
			this.jDesktopPane.add(this.maskDuts);
		}
		return this.maskDuts;
	}

	/**
	 * 
	 */
	private void importDataAction() {
		JInternalFrame j = this.jDesktopPane.getSelectedFrame();
		if (j instanceof PlotWindow) {
			PlotWindow s = (PlotWindow) j;
			s.importData();
		}
	}

	/**
	 * This method initializes this
	 */
	private void initialize() {
		this.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
		this.setJMenuBar(getMMenuBar());
		this.setSize(1136, 792);
		this.setContentPane(getJContentPane());
		this.setTitle("Application");
	}

	/**
	 * 
	 */
	private void limitsWindowAction() {
		gLimitsEditor().setVisible(true);
	}

	/**
	 * 
	 */
	private void loadLimitsAction() {
		JInternalFrame j = this.jDesktopPane.getSelectedFrame();
		if (j instanceof LimitsEditor) {
			LimitsEditor s = (LimitsEditor) j;
			s.loadLimits();
		}
	}

	/**
	 * 
	 */
	private void maskWindowAction() {
		gMaskDuts().setVisible(true);
	}

	/**
	 * 
	 */
	private void newDataAction() {
		PlotWindow s = new PlotWindow(new DataModel());
		s.addInternalFrameListener(getIntFrameChangeListener());
		this.jDesktopPane.add(s);
		s.setVisible(true);
	}

	/**
	 * 
	 */
	private void openDataAction() {
		PlotWindow s = new PlotWindow(new DataModel());
		s.addInternalFrameListener(getIntFrameChangeListener());
		s.addMessageListener(getMyStatusBar());
		s.addProgressListener(getMyStatusBar());
		this.jDesktopPane.add(s);
		s.setVisible(true);
		s.importData();
	}

	/**
	 * 
	 */
	private void saveLimitsAction() {
		JInternalFrame j = this.jDesktopPane.getSelectedFrame();
		if (j instanceof LimitsEditor) {
			LimitsEditor s = (LimitsEditor) j;
			s.saveLimits();
		}
	}

	private void updateMenuItemsActive(JInternalFrame i) {
		if (i instanceof PlotWindow) {
			getImportDataItem().setEnabled(true);
			getExportDataItem().setEnabled(true);
			getRatExportMenuItem().setEnabled(true);
			getLoadLimitsMenuItem().setEnabled(false);
			getSaveLimitsMenuItem().setEnabled(false);
			getCloseMenuItem().setEnabled(true);
			gMaskDuts().setDataModel(((PlotWindow) i).getDataModel());
			gLimitsEditor().setDataModel(((PlotWindow) i).getDataModel());
		} else if (i instanceof LimitsEditor) {
			getImportDataItem().setEnabled(false);
			getExportDataItem().setEnabled(false);
			getRatExportMenuItem().setEnabled(false);
			getLoadLimitsMenuItem().setEnabled(true);
			getSaveLimitsMenuItem().setEnabled(true);
			getCloseMenuItem().setEnabled(true);
		} else if (i instanceof MaskDuts) {
			getImportDataItem().setEnabled(false);
			getExportDataItem().setEnabled(false);
			getRatExportMenuItem().setEnabled(false);
			getLoadLimitsMenuItem().setEnabled(false);
			getSaveLimitsMenuItem().setEnabled(false);
			getCloseMenuItem().setEnabled(true);
		} else {
			getImportDataItem().setEnabled(false);
			getExportDataItem().setEnabled(false);
			getRatExportMenuItem().setEnabled(false);
			getLoadLimitsMenuItem().setEnabled(false);
			getSaveLimitsMenuItem().setEnabled(false);
			getCloseMenuItem().setEnabled(false);
			gMaskDuts().setDataModel(null);
			gLimitsEditor().setDataModel(null);
		}
	}
} // @jve:decl-index=0:visual-constraint="52,43"
