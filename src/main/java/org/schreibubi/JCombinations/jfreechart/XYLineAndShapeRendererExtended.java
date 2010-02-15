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
package org.schreibubi.JCombinations.jfreechart;

import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Polygon;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;

import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.Range;
import org.jfree.ui.GradientPaintTransformer;

/**
 * Adds support for ArbitraryMarker (@see
 * org.schreibubi.JCombinations.jfreechart.ArbitraryMarker) in the
 * XYLineAndShapeRenderer
 * 
 * @author Jörg Werner
 * 
 */
public class XYLineAndShapeRendererExtended extends XYLineAndShapeRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4045750507977719516L;

	/**
	 * Constructor
	 */
	public XYLineAndShapeRendererExtended() {
		super();
	}

	/**
	 * Constructor
	 * 
	 * @param lines
	 * @param shapes
	 */
	public XYLineAndShapeRendererExtended(boolean lines, boolean shapes) {
		super(lines, shapes);
	}

	/**
	 * Draws a horizontal line across the chart to represent a 'range marker'.
	 * 
	 * @param g2
	 *            the graphics device.
	 * @param plot
	 *            the plot.
	 * @param rangeAxis
	 *            the range axis.
	 * @param marker
	 *            the marker line.
	 * @param dataArea
	 *            the axis data area.
	 */
	@Override
	public void drawRangeMarker(Graphics2D g2, XYPlot plot,
			ValueAxis rangeAxis, Marker marker, Rectangle2D dataArea) {

		if (marker instanceof ValueMarker) {
			super.drawRangeMarker(g2, plot, rangeAxis, marker, dataArea);
		} else if (marker instanceof IntervalMarker) {
			super.drawRangeMarker(g2, plot, rangeAxis, marker, dataArea);
		} else if (marker instanceof ArbitraryMarker) {

			ArbitraryMarker im = (ArbitraryMarker) marker;
			ArrayList<Double> xvals = im.getDomainVal();
			ArrayList<Double> lows = im.getRangeLowValues();
			ArrayList<Double> highs = im.getRangeHighValues();
			sort(xvals, lows, highs);

			Range range = rangeAxis.getRange();
			ValueAxis domainAxis = plot.getDomainAxis();
			Range domain = domainAxis.getRange();

			int length = xvals.size();
			int[] xpoly = new int[length * 2];
			int[] ypoly = new int[length * 2];

			for (int i = 0; i < xvals.size(); i++) {
				double x = domain.constrain(xvals.get(i));
				double low = range.constrain(lows.get(i));
				double high = range.constrain(highs.get(i));
				int low2d = (int) Math.round(rangeAxis.valueToJava2D(low,
						dataArea, plot.getRangeAxisEdge()));
				int high2d = (int) Math.round(rangeAxis.valueToJava2D(high,
						dataArea, plot.getRangeAxisEdge()));
				int x2d = (int) Math.round(domainAxis.valueToJava2D(x,
						dataArea, plot.getDomainAxisEdge()));
				xpoly[i] = x2d;
				xpoly[2 * length - 1 - i] = x2d;
				ypoly[i] = low2d;
				ypoly[2 * length - 1 - i] = high2d;
			}

			PlotOrientation orientation = plot.getOrientation();
			Polygon poly = null;
			if (orientation == PlotOrientation.HORIZONTAL) {
				poly = new Polygon(ypoly, xpoly, length * 2);
			} else if (orientation == PlotOrientation.VERTICAL) {
				poly = new Polygon(xpoly, ypoly, length * 2);
			}

			Paint p = im.getPaint();
			if (p instanceof GradientPaint) {
				GradientPaint gp = (GradientPaint) p;
				GradientPaintTransformer t = im.getGradientPaintTransformer();
				if (t != null) {
					gp = t.transform(gp, poly);
				}
				g2.setPaint(gp);
			} else {
				g2.setPaint(p);
			}
			g2.fill(poly);
			/*
			 * String label = marker.getLabel(); RectangleAnchor anchor =
			 * marker.getLabelAnchor(); if ( label != null ) { Font labelFont =
			 * marker.getLabelFont(); g2.setFont( labelFont ); g2.setPaint(
			 * marker.getLabelPaint() ); Point2D coordinates =
			 * calculateRangeMarkerTextAnchorPoint( g2, orientation, dataArea,
			 * poly, marker.getLabelOffset(), marker.getLabelOffsetType(),
			 * anchor ); TextUtilities.drawAlignedString( label, g2, ( float )
			 * coordinates.getX(), ( float ) coordinates.getY(),
			 * marker.getLabelTextAnchor() ); }
			 */
		}
	}

	private void sort(ArrayList<Double> x, ArrayList<Double> yl,
			ArrayList<Double> yh) {
		for (int i = x.size(); --i >= 0;) {
			boolean swapped = false;
			for (int j = 0; j < i; j++) {
				if (x.get(j) > x.get(j + 1)) {
					Collections.swap(x, j, j + 1);
					Collections.swap(yl, j, j + 1);
					Collections.swap(yh, j, j + 1);
					swapped = true;
				}
			}
			if (!swapped) {
				return;
			}
		}
	}

}
