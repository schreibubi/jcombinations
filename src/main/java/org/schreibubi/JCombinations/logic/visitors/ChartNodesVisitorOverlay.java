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
package org.schreibubi.JCombinations.logic.visitors;

import java.util.ArrayList;

import javax.swing.tree.TreePath;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeriesCollection;
import org.schreibubi.JCombinations.FileFormat.Alternative;
import org.schreibubi.JCombinations.FileFormat.Asdap;
import org.schreibubi.JCombinations.FileFormat.OurTreeNode;
import org.schreibubi.JCombinations.FileFormat.Shmoo;
import org.schreibubi.JCombinations.FileFormat.TreeVisitor;
import org.schreibubi.JCombinations.FileFormat.Ydata;
import org.schreibubi.JCombinations.jfreechart.XYLineAndShapeRendererExtended;
import org.schreibubi.JCombinations.logic.ExtendedJFreeChart;

/**
 * Creates the charts from all selected nodes
 * 
 * @author Jörg Werner
 * 
 */
public class ChartNodesVisitorOverlay implements TreeVisitor {

	ArrayList<TreePath> treePaths = null;

	ArrayList<ExtendedJFreeChart> charts = null;

	ArrayList<ArrayList<Double>> dutYData = null;

	ArrayList<String> dutName = null;

	ArrayList<String> seriesMask = null;

	XYSeriesCollection xyseries = null;

	ArrayList<ArrayList<Double>> shmooXData = null;

	ArrayList<ArrayList<ArrayList<Double>>> shmooYData = null;

	/**
	 * Constructor
	 * 
	 * @param treePaths
	 *            selected paths
	 * @param seriesMask
	 *            series Mask
	 * @param charts
	 *            generated charts
	 */
	public ChartNodesVisitorOverlay(ArrayList<TreePath> treePaths,
			ArrayList<String> seriesMask, ArrayList<ExtendedJFreeChart> charts) {
		this.charts = charts;
		this.seriesMask = seriesMask;
		this.treePaths = treePaths;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.schreibubi.JCombinations.FileFormat.TreeVisitor#visit(org.schreibubi
	 * .JCombinations.FileFormat.Alternative)
	 */
	public void visit(Alternative a) throws Exception {
		accumulateVisit(a);
	}

	public void visit(Asdap r) throws Exception {
		accumulateVisit(r);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.schreibubi.JCombinations.FileFormat.TreeVisitor#visit(org.schreibubi
	 * .JCombinations.FileFormat.Shmoo)
	 */
	public void visit(Shmoo s) throws Exception {
		if (s.componentSelected(this.treePaths, OurTreeNode.MYSELF
				| OurTreeNode.PARENTS | OurTreeNode.CHILDS)) {

			this.shmooXData.add(s.getXdataDefault().getXPositions());
			for (int i = 0; i < s.getChildCount(); i++) {
				(s.getChildAt(i)).accept(this);
			}

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.schreibubi.JCombinations.FileFormat.TreeVisitor#visit(org.schreibubi
	 * .JCombinations.FileFormat.Data)
	 */
	public void visit(Ydata d) throws Exception {
		if (d.componentSelected(this.treePaths, OurTreeNode.MYSELF
				| OurTreeNode.PARENTS)) {
			if (!this.seriesMask.contains(d.getDescription())
					| d.componentSelected(this.treePaths, OurTreeNode.MYSELF)) {
				this.dutYData.add(d.getDoubleValues());
				this.dutName.add(d.getDescription());
			}
		}
	}

	private void accumulateVisit(OurTreeNode o) throws Exception {
		this.dutYData = new ArrayList<ArrayList<Double>>();
		this.dutName = new ArrayList<String>();

		for (int i = 0; i < o.getChildCount(); i++) {
			((OurTreeNode) o.getChildAt(i)).accept(this);
		}

		/*
		 * ArrayList< Double > x = s.getXdataDefault().getXPositions(); for (
		 * int j = 0; j < this.dutYData.size(); j++ ) { ArrayList< Double > y =
		 * this.dutYData.get( j ); XYSeries xy = new XYSeries( this.dutName.get(
		 * j ) ); for ( int i = 0; i < y.size(); i++ ) { xy.add( x.get( i ),
		 * y.get( i ) ); } this.xyseries.addSeries( xy ); }
		 */
		NumberAxis xAxis = new NumberAxis(" X ");
		xAxis.setAutoRangeIncludesZero(false);
		NumberAxis yAxis = new NumberAxis(" Y ");
		yAxis.setAutoRangeIncludesZero(false);
		XYLineAndShapeRenderer renderer = new XYLineAndShapeRendererExtended(
				true, true);
		renderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator());
		xyseries = new XYSeriesCollection();

		XYPlot plot = new XYPlot(xyseries, xAxis, yAxis, renderer);
		plot.setOrientation(PlotOrientation.VERTICAL);
		plot.setDomainCrosshairVisible(true);
		plot.setRangeCrosshairVisible(true);
		ExtendedJFreeChart chart = new ExtendedJFreeChart("Overlay",
				JFreeChart.DEFAULT_TITLE_FONT, plot, false);
		this.charts.add(chart);

	}

}
