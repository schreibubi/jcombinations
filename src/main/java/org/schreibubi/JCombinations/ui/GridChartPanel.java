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

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.TransferHandler;
import javax.swing.event.EventListenerList;
import javax.swing.tree.TreePath;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGeneratorContext;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.svggen.SVGGraphics2DIOException;
import org.schreibubi.JCombinations.logic.DataEventListenerAdapter;
import org.schreibubi.JCombinations.logic.DataModel;
import org.schreibubi.JCombinations.logic.ExtendedJFreeChart;
import org.schreibubi.JCombinations.logic.SelectionEvent;
import org.schreibubi.JCombinations.logic.TableContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.jdesktop.swingx.event.MessageEvent;
import org.jdesktop.swingx.event.MessageListener;
import org.jdesktop.swingx.event.MessageSource;
import org.jdesktop.swingx.event.ProgressEvent;
import org.jdesktop.swingx.event.ProgressListener;
import org.jdesktop.swingx.event.ProgressSource;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.w3c.dom.DOMImplementation;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.FontFactory;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.DefaultFontMapper;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfDestination;
import com.lowagie.text.pdf.PdfOutline;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

/**
 * @author Werner Jörg
 */
public class GridChartPanel extends JPanel implements Printable, MessageSource, ProgressSource {

	static class ImageTransferHandler extends TransferHandler {

		class ImageTransferable implements Transferable {
			private Image	image;

			ImageTransferable(Image pic) {
				this.image = pic;
			}

			public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
				if (!isDataFlavorSupported(flavor))
					throw new UnsupportedFlavorException(flavor);
				return this.image;
			}

			public DataFlavor[] getTransferDataFlavors() {
				return new DataFlavor[] { DataFlavor.imageFlavor };
			}

			public boolean isDataFlavorSupported(DataFlavor flavor) {
				return DataFlavor.imageFlavor.equals(flavor);
			}
		}

		/**
		 * 
		 */
		private static final long	serialVersionUID	= 3617013048540345650L;

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.TransferHandler#getSourceActions(javax.swing.JComponent)
		 */
		@Override
		public int getSourceActions(JComponent c) {
			return TransferHandler.COPY;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.TransferHandler#createTransferable(javax.swing.JComponent)
		 */
		@Override
		protected Transferable createTransferable(JComponent c) {
			BufferedImage b = new BufferedImage(c.getWidth(), c.getHeight(), BufferedImage.TYPE_INT_RGB);
			c.print(b.getGraphics());
			return new ImageTransferable(b);
		}
	}

	private static int			DEFAULT_WIDTH		= 210;

	private static int			DEFAULT_HEIGHT		= 150;

	private static int			EXTRA_MARGIN		= 30;

	private static Logger		logger				= LoggerFactory.getLogger(MainWindow.class);

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -3304134998948470261L;

	/**
	 * Create PDF
	 * 
	 * @param name
	 *            Filename
	 * @param selection
	 *            Selected Nodes
	 * @param dm
	 *            DataModel
	 * @param pl
	 *            ProgressListener
	 */
	@SuppressWarnings("unchecked")
	public static void generatePDF(File name, DataModel dm, ArrayList<TreePath> selection, ProgressListener pl) {
		com.lowagie.text.Document document = new Document(PageSize.A4.rotate(), 50, 50, 50, 50);
		try {
			ArrayList<ExtendedJFreeChart> charts = dm.getCharts(selection);
			if (charts.size() > 0) {
				PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(name));
				writer.setViewerPreferences(PdfWriter.PageModeUseOutlines);
				document.addAuthor("Jörg Werner");
				document.addSubject("Created by JCombinations");
				document.addKeywords("JCombinations");
				document.addCreator("JCombinations using iText");

				// we define a header and a footer
				HeaderFooter header = new HeaderFooter(new Phrase("JCombinations by Jörg Werner"), false);
				HeaderFooter footer = new HeaderFooter(new Phrase("Page "), new Phrase("."));
				footer.setAlignment(Element.ALIGN_CENTER);
				document.setHeader(header);
				document.setFooter(footer);

				document.open();
				DefaultFontMapper mapper = new DefaultFontMapper();
				FontFactory.registerDirectories();
				mapper.insertDirectory("c:\\WINNT\\fonts");

				PdfContentByte cb = writer.getDirectContent();

				pl.progressStarted(new ProgressEvent(GridChartPanel.class, 0, charts.size()));
				for (int i = 0; i < charts.size(); i++) {
					ExtendedJFreeChart chart = charts.get(i);
					PdfTemplate tp = cb.createTemplate(document.right(EXTRA_MARGIN) - document.left(EXTRA_MARGIN),
							document.top(EXTRA_MARGIN) - document.bottom(EXTRA_MARGIN));
					Graphics2D g2d = tp.createGraphics(document.right(EXTRA_MARGIN) - document.left(EXTRA_MARGIN),
							document.top(EXTRA_MARGIN) - document.bottom(EXTRA_MARGIN), mapper);
					Rectangle2D r2d = new Rectangle2D.Double(0, 0, document.right(EXTRA_MARGIN)
							- document.left(EXTRA_MARGIN), document.top(EXTRA_MARGIN) - document.bottom(EXTRA_MARGIN));
					chart.draw(g2d, r2d);
					g2d.dispose();
					cb.addTemplate(tp, document.left(EXTRA_MARGIN), document.bottom(EXTRA_MARGIN));
					PdfDestination destination = new PdfDestination(PdfDestination.FIT);
					TreePath treePath = chart.getTreePath();
					PdfOutline po = cb.getRootOutline();
					for (int j = 0; j < treePath.getPathCount(); j++) {
						ArrayList<PdfOutline> lpo = po.getKids();
						PdfOutline cpo = null;
						for (PdfOutline outline : lpo)
							if (outline.getTitle().compareTo(treePath.getPathComponent(j).toString()) == 0)
								cpo = outline;
						if (cpo == null)
							cpo = new PdfOutline(po, destination, treePath.getPathComponent(j).toString());
						po = cpo;
					}
					document.newPage();
					pl.progressIncremented(new ProgressEvent(GridChartPanel.class, i));
				}
				document.close();
				pl.progressEnded(new ProgressEvent(GridChartPanel.class));

			}
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}
	}

	private DataModel			dm				= null;

	/**
	 * Event listener list
	 */
	protected EventListenerList	listenerList	= new EventListenerList();

	/**
	 * Constructor
	 */
	public GridChartPanel() {
		super();
	}

	/**
	 * Constructor
	 * 
	 * @param dm
	 *            associated DataModel
	 */
	public GridChartPanel(DataModel dm) {
		super();
		this.dm = dm;
		initialize();
	}

	public void addMessageListener(MessageListener l) {
		this.listenerList.add(MessageListener.class, l);
	}

	public void addProgressListener(ProgressListener l) {
		this.listenerList.add(ProgressListener.class, l);
	}

	/**
	 * Copy to clipboard
	 */
	public void copyToClipboard() {
		TransferHandler t = getTransferHandler();
		if (t != null)
			t.exportToClipboard(this, Toolkit.getDefaultToolkit().getSystemClipboard(), TransferHandler.COPY);
	}

	/**
	 * Create Excel
	 * 
	 * @param name
	 *            Filename
	 * @param selection
	 *            Selected Nodes
	 */
	public void generateExcel(File name, ArrayList<TreePath> selection) {
		ArrayList<TableContent> tables = this.dm.getTable(selection);
		if (tables.size() > 0)
			try {
				HSSFWorkbook wb = new HSSFWorkbook();
				for (int i = 0; i < tables.size(); i++) {
					TableContent table = tables.get(i);
					String sheetName = table.getName().replace('/', ' ');
					HSSFSheet sheet = wb.createSheet(sheetName.substring(0, Math.min(31, sheetName.length())));
					for (int row = 0; row < table.getRowCount(); row++) {
						HSSFRow excelRow = sheet.createRow((short) row);
						for (int col = 0; col < table.getColumnCount(); col++) {
							Object val = table.getValueAt(row, col);
							if (val instanceof String)
								excelRow.createCell((short) col).setCellValue((String) val);
							else if (val instanceof Double)
								excelRow.createCell((short) col).setCellValue((Double) val);
						}

					}
				}
				FileOutputStream fileOut = new FileOutputStream(name);
				wb.write(fileOut);
				fileOut.close();
			} catch (IOException ioe) {
				System.err.println(ioe.getMessage());
			}
	}

	/**
	 * Create SVG
	 * 
	 * @param name
	 *            Filename
	 * @param selection
	 *            Selected Nodes
	 * @param multi
	 *            Multipage (true) or Singlepage (false) mode
	 */
	public void generateSVG(File name, ArrayList<TreePath> selection, boolean multi) {
		DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();

		// Create an instance of org.w3c.dom.Document
		org.w3c.dom.Document document = domImpl.createDocument(null, "svg", null);

		SVGGeneratorContext ctx = SVGGeneratorContext.createDefault(document);
		ctx.setComment("Generated by JCombinations with Batik SVG Generator");
		ctx.setEmbeddedFontsOn(true);
		SVGGraphics2D svgGenerator = new SVGGraphics2D(ctx, true);

		ArrayList<ExtendedJFreeChart> charts = this.dm.getCharts(selection);
		if (charts.size() > 0)
			for (ExtendedJFreeChart chart : charts) {
				Rectangle2D r2d = new Rectangle2D.Double(0, 0, GridChartPanel.DEFAULT_WIDTH,
						GridChartPanel.DEFAULT_HEIGHT);
				chart.draw(svgGenerator, r2d);
			}

		print(svgGenerator);
		try {
			OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(name), "UTF-8");
			svgGenerator.stream(out, false);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (SVGGraphics2DIOException e1) {
			e1.printStackTrace();
		}
	}

	public MessageListener[] getMessageListeners() {
		return this.listenerList.getListeners(MessageListener.class);
	}

	public ProgressListener[] getProgressListeners() {
		return this.listenerList.getListeners(ProgressListener.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.print.Printable#print(java.awt.Graphics, java.awt.print.PageFormat, int)
	 */
	public int print(Graphics g, PageFormat pageFormat, int pageIndex) {
		if (pageIndex > 0)
			return (Printable.NO_SUCH_PAGE     );
		else {
			Graphics2D g2d = (Graphics2D) g;
			g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
			print(g2d);
			return (Printable.PAGE_EXISTS     );
		}
	}

	/**
	 * Print panel
	 * 
	 * @param printJob
	 */
	public void printMe(PrinterJob printJob) {
		try {
			printJob.print();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void removeMessageListener(MessageListener l) {
		this.listenerList.remove(MessageListener.class, l);
	}

	public void removeProgressListener(ProgressListener l) {
		this.listenerList.remove(ProgressListener.class, l);
	}

	/**
	 * Set associated DataModel
	 * 
	 * @param dm
	 *            DataModel
	 */
	public void setGridChartModel(DataModel dm) {
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
				updateDisplay(e.getSelection());
			}
		});
	}

	private void updateDisplay(ArrayList<TreePath> selection) {
		GridChartPanel.logger.info("updateDisplay called");

		removeAll();
		repaint();
		ArrayList<ExtendedJFreeChart> charts = this.dm.getCharts(selection);
		if ((charts.size() > 0) && (charts.size() < 20)) {
			int n = charts.size();
			double aspectratio = Math.sqrt(2);
			double y = Math.sqrt(n / aspectratio);
			double x = y * aspectratio;
			int xi = (int) Math.ceil(x);
			int yi = (int) Math.ceil(y);
			setLayout(new GridLayout(xi, yi));
			setPreferredSize(new Dimension(yi * GridChartPanel.DEFAULT_WIDTH, xi * GridChartPanel.DEFAULT_HEIGHT));
			revalidate();
			setTransferHandler(new ImageTransferHandler());
			fireSetupProgress(this, 0, charts.size() - 1);
			for (int i = 0; i < charts.size(); i++) {
				JFreeChart chart = charts.get(i);
				ChartPanel chartPanel = new ChartPanel(chart, false);
				chartPanel.setPreferredSize(new Dimension(GridChartPanel.DEFAULT_WIDTH, GridChartPanel.DEFAULT_HEIGHT));
				chartPanel.revalidate();
				chartPanel.setMouseZoomable(true);
				add(chartPanel);
				fireProgressIncremented(this, i);
			}
			fireProgressEnded(this);
		}
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

	protected void fireProgressEnded(Object source) {
		// Guaranteed to return a non-null array
		Object[] listeners = this.listenerList.getListenerList();
		ProgressEvent e = null;
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2)
			if (listeners[i] == ProgressListener.class) {
				// Lazily create the event:
				if (e == null)
					e = new ProgressEvent(source);
				((ProgressListener) listeners[i + 1]).progressEnded(e);
			}
	}

	protected void fireProgressIncremented(Object source, int progress) {
		// Guaranteed to return a non-null array
		Object[] listeners = this.listenerList.getListenerList();
		ProgressEvent e = null;
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2)
			if (listeners[i] == ProgressListener.class) {
				// Lazily create the event:
				if (e == null)
					e = new ProgressEvent(source, progress);
				((ProgressListener) listeners[i + 1]).progressIncremented(e);
			}
	}

	protected void fireSetupProgress(Object source, int min, int max) {
		// Guaranteed to return a non-null array
		Object[] listeners = this.listenerList.getListenerList();
		ProgressEvent e = null;
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2)
			if (listeners[i] == ProgressListener.class) {
				// Lazily create the event:
				if (e == null)
					e = new ProgressEvent(source, min, max);
				((ProgressListener) listeners[i + 1]).progressStarted(e);
			}
	}

}
