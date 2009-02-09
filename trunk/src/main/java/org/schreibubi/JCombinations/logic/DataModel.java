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

import java.io.StringReader;
import java.util.ArrayList;

import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelEvent;
import javax.swing.tree.TreePath;

import org.schreibubi.JCombinations.FileFormat.Asdap;
import org.schreibubi.JCombinations.FileFormat.OurTreeNode;
import org.schreibubi.JCombinations.FileFormat.TreeVisitor;
import org.schreibubi.JCombinations.evalVariables.EvalVariablesLexer;
import org.schreibubi.JCombinations.evalVariables.EvalVariablesParser;
import org.schreibubi.JCombinations.logic.visitors.AddNodesVisitor;
import org.schreibubi.JCombinations.logic.visitors.ChartNodesVisitor;
import org.schreibubi.JCombinations.logic.visitors.ChartNodesVisitorOverlay;
import org.schreibubi.JCombinations.logic.visitors.CopyNodesVisitor;
import org.schreibubi.JCombinations.logic.visitors.DataListNodesVisitor;
import org.schreibubi.JCombinations.logic.visitors.RemoveNodesVisitor;
import org.schreibubi.JCombinations.logic.visitors.SeriesToSelectionVisitor;
import org.schreibubi.JCombinations.logic.visitors.SetLimitVisitor;
import org.schreibubi.JCombinations.logic.visitors.TableNodesVisitor;
import org.schreibubi.JCombinations.logic.visitors.XdataListNodesVisitor;
import org.schreibubi.JCombinations.ui.DUTListModelAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import antlr.RecognitionException;
import antlr.TokenStreamException;
import antlr.collections.AST;


/**
 * DataModel
 * 
 * @author Jörg Werner
 */
public class DataModel {

	private static Logger		logger				= LoggerFactory.getLogger(DataModel.class);

	private OurTreeNode			root				= null;

	private ArrayList<String>	seriesMask			= new ArrayList<String>();

	private String				limitsText			= "";

	private DUTListModelAdapter	dutListModelAdapter	= null;

	private boolean				overlay;

	/**
	 * Event listener list
	 */
	protected EventListenerList	listenerList		= new EventListenerList();

	/**
	 * Constructor
	 */
	public DataModel() {
		setRoot(new Asdap(1));
	}

	/**
	 * Constructor
	 * 
	 * @param tree
	 *            initial tree to use
	 */
	public DataModel(OurTreeNode tree) {
		setRoot(tree);
	}

	/**
	 * @param l
	 */
	public void addDataEventListener(DataEventListener l) {
		this.listenerList.add(DataEventListener.class, l);
	}

	/**
	 * @param treeToAdd
	 */
	public void addNodes(OurTreeNode treeToAdd) {
		TreeEventCollector eventCollector = new TreeEventCollector();
		AddNodesVisitor v = new AddNodesVisitor(getRoot(), eventCollector);
		try {
			treeToAdd.accept(v);
		} catch (Exception e) {
			e.printStackTrace();
		}
		eventCollector.fireTreeNodesInserted(this.listenerList, this);
	}

	/**
	 * Apply the limits
	 */
	public void applyLimits() {
		EvalVariablesParser parser = null;
		try {
			EvalVariablesLexer lexer = new EvalVariablesLexer(new StringReader(this.limitsText));
			parser = new EvalVariablesParser(lexer);
			parser.lines();
			fireMessage(this, "Applied limits succesfully\n", false);
		} catch (TokenStreamException f) {
			fireMessage(this, "Lexer error:" + f + "\n", true);
		} catch (RecognitionException f) {
			fireMessage(this, "Syntax error: " + f + "\n", calculateStringPosition(this.limitsText, f.getLine(), f
					.getColumn()), true);
		} catch (Exception f) {
			fireMessage(this, "Exception: " + f + "\n", true);
		}
		AST a = parser.getAST();
		TreeEventCollector eventCollector = new TreeEventCollector();
		TreeVisitor v = new SetLimitVisitor(a, eventCollector);
		try {
			this.root.accept(v);
		} catch (Exception e) {
			fireMessage(this, e.getMessage(), true);
		}
		eventCollector.fireTreeNodesChanged(this.listenerList, this);
	}

	/**
	 * @param selection
	 * @return root node
	 */
	public OurTreeNode copySelection(ArrayList<TreePath> selection) {
		CopyNodesVisitor v = new CopyNodesVisitor(selection);
		try {
			this.root.accept(v);
		} catch (Exception e) {
			e.printStackTrace();
		}
		OurTreeNode topNode = v.getTopNode();
		return topNode;
	}

	/**
	 * @param series
	 */
	public void deleteSeries(ArrayList<String> series) {
		ArrayList<TreePath> selection = new ArrayList<TreePath>();
		SeriesToSelectionVisitor v = new SeriesToSelectionVisitor(series, selection);
		try {
			this.root.accept(v);
		} catch (Exception e) {
			e.printStackTrace();
		}
		removeSelection(selection);
	}

	/**
	 * @param obj
	 * @param selection
	 */
	public void fireSelectionChanged(Object obj, ArrayList<TreePath> selection) {
		// Guaranteed to return a non-null array
		Object[] listeners = this.listenerList.getListenerList();
		SelectionEvent e = null;
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2)
			if (listeners[i] == DataEventListener.class) {
				// Lazily create the event:
				if (e == null) {
					e = new SelectionEvent(obj, selection);
				}
				((DataEventListener) listeners[i + 1]).selectionUpdated(e);
			}
	}

	/**
	 * @param selection
	 * @return charts
	 */
	public ArrayList<ExtendedJFreeChart> getCharts(ArrayList<TreePath> selection) {
		if (selection == null)
			return null;
		ArrayList<ExtendedJFreeChart> charts = new ArrayList<ExtendedJFreeChart>();
		if (overlay) {
			ChartNodesVisitorOverlay v = new ChartNodesVisitorOverlay(selection, this.seriesMask, charts);
			try {
				this.root.accept(v);
			} catch (Exception e) {
				e.printStackTrace();
				fireMessage(this, e.getMessage(), false);
			}
		} else {
			ChartNodesVisitor v = new ChartNodesVisitor(selection, this.seriesMask, charts);
			try {
				this.root.accept(v);
			} catch (Exception e) {
				e.printStackTrace();
				fireMessage(this, e.getMessage(), false);
			}
		}
		return charts;
	}

	/**
	 * @return list of registered listeners
	 */
	public DataEventListener[] getDataEventListeners() {
		return this.listenerList.getListeners(DataEventListener.class);
	}

	/**
	 * getDUTListModel
	 * 
	 * @return DUTListModelAdapter
	 */
	public DUTListModelAdapter getDUTListModel() {
		if (this.dutListModelAdapter == null) {
			DataModel.logger.info("getDUTListModel");
			this.dutListModelAdapter = new DUTListModelAdapter(this);
		}
		return this.dutListModelAdapter;
	}

	/**
	 * @return limits text
	 */
	public String getLimitsText() {
		return this.limitsText;
	}

	/**
	 * @return possible list of xdata
	 */
	public String[] getPossibleDataList() {
		DataListNodesVisitor v = new DataListNodesVisitor();
		try {
			this.root.accept(v);
		} catch (Exception e) {
			fireMessage(this, e.getMessage(), false);
		}
		return v.getDataList().toArray(new String[0]);
	}

	/**
	 * @param selection
	 * @return possible list of xdata
	 */
	public ArrayList<String> getPossibleXdataList(ArrayList<TreePath> selection) {
		XdataListNodesVisitor v = new XdataListNodesVisitor(selection);
		try {
			this.root.accept(v);
		} catch (Exception e) {
			fireMessage(this, e.getMessage(), false);
		}
		ArrayList<String> xdataList = v.getXdataList();
		return xdataList;

	}

	// ////////////////////////////////////////////////////////////////////////////////////
	// Tree Node Interface
	// ////////////////////////////////////////////////////////////////////////////////////
	/**
	 * @return root node
	 */
	public OurTreeNode getRoot() {
		return this.root;
	}

	/**
	 * @return series mask
	 */
	public ArrayList<String> getSeriesMask() {
		return this.seriesMask;
	}

	// ////////////////////////////////////////////////////////////////////////////////////
	// Event interface & helpers
	// ////////////////////////////////////////////////////////////////////////////////////

	/**
	 * @param selection
	 * @return table
	 */
	public ArrayList<TableContent> getTable(ArrayList<TreePath> selection) {
		if (selection == null)
			return null;
		ArrayList<TableContent> table = new ArrayList<TableContent>();
		TableNodesVisitor v = new TableNodesVisitor(selection, this.seriesMask, table);
		try {
			this.root.accept(v);
		} catch (Exception e) {
			fireMessage(this, e.getMessage(), false);
		}
		// set the data model, so events are propagated correctly...
		for (TableContent content : table) {
			content.setDataModel(this);
		}
		return table;
	}

	/**
	 * @return the overlay
	 */
	public boolean isOverlay() {
		return overlay;
	}

	/**
	 * @param l
	 */
	public void removeDataEventListener(DataEventListener l) {
		this.listenerList.remove(DataEventListener.class, l);
	}

	/**
	 * @param selection
	 */
	public void removeSelection(ArrayList<TreePath> selection) {

		TreeEventCollector eventCollector = new TreeEventCollector();
		RemoveNodesVisitor v = new RemoveNodesVisitor(selection, eventCollector);
		try {
			this.root.accept(v);
		} catch (Exception e) {
			e.printStackTrace();
		}
		eventCollector.removeNodes();
		eventCollector.fireTreeNodesRemoved(this.listenerList, this);
	}

	/**
	 * @param limitsText
	 *            set limits text
	 */
	public void setLimitsText(String limitsText) {
		this.limitsText = limitsText;
	}

	/**
	 * @param overlay
	 *            the overlay to set
	 */
	public void setOverlay(boolean overlay) {
		this.overlay = overlay;
	}

	/**
	 * set root node
	 * 
	 * @param root
	 */
	public void setRoot(OurTreeNode root) {
		this.root = root;
		fireTreeStructureChanged(this, new TreePath(root), null, null);
	}

	/**
	 * set series mask
	 * 
	 * @param dutMask
	 */
	public void setSeriesMask(ArrayList<String> dutMask) {
		DataModel.logger.info("seriesMask updated");
		this.seriesMask = dutMask;
	}

	private int calculateStringPosition(String str, int line, int column) {
		int pos = -1;
		for (int i = 1; i < line; i++) {
			pos = str.indexOf('\n', pos + 1);
		}
		pos = pos + column;
		pos = Math.max(pos, str.length());
		return pos;
	}

	protected void fireMessage(Object source, String str, boolean error) {
		// Guaranteed to return a non-null array
		Object[] listeners = this.listenerList.getListenerList();
		DataEvent e = null;
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2)
			if (listeners[i] == DataEventListener.class) {
				// Lazily create the event:
				if (e == null) {
					e = new DataEvent(source, str, error);
				}
				((DataEventListener) listeners[i + 1]).limitMessage(e);
			}
	}

	protected void fireMessage(Object source, String str, int pos, boolean error) {
		// Guaranteed to return a non-null array
		Object[] listeners = this.listenerList.getListenerList();
		DataEvent e = null;
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2)
			if (listeners[i] == DataEventListener.class) {
				// Lazily create the event:
				if (e == null) {
					e = new DataEvent(source, str, pos, error);
				}
				((DataEventListener) listeners[i + 1]).limitMessage(e);
			}
	}

	protected void fireTreeStructureChanged(Object source, TreePath path, int[] childIndices, Object[] children) {
		DataModel.logger.info("fired TreeStructureChanged");
		// Guaranteed to return a non-null array
		Object[] listeners = this.listenerList.getListenerList();
		TreeModelEvent e = null;
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2)
			if (listeners[i] == DataEventListener.class) {
				// Lazily create the event:
				if (e == null) {
					e = new TreeModelEvent(source, path, childIndices, children);
				}
				((DataEventListener) listeners[i + 1]).treeStructureChanged(e);
			}
	}

}
