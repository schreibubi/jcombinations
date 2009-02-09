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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Stroke;
import java.util.ArrayList;

import org.jfree.chart.plot.Marker;
import org.jfree.ui.GradientPaintTransformer;
import org.jfree.util.ObjectUtilities;

/**
 * Arbitrary range marker
 * 
 * @author Jörg Werner
 * 
 */
public class ArbitraryMarker extends Marker {

	/**
	 * 
	 */
	private static final long			serialVersionUID	= 2859453922364205910L;

	/** The gradient paint transformer (optional). */
	private GradientPaintTransformer	gradientPaintTransformer;

	private ArrayList<Double>			domainVal			= new ArrayList<Double>();

	private ArrayList<Double>			rangeLow			= new ArrayList<Double>();

	private ArrayList<Double>			rangeHigh			= new ArrayList<Double>();

	/**
	 * Constructor
	 * 
	 * @param domainVal
	 *            x-values of range
	 * @param rangeLow
	 *            lower y-values of range
	 * @param rangeHigh
	 *            upper y-values of range
	 */
	public ArbitraryMarker(ArrayList<Double> domainVal, ArrayList<Double> rangeLow, ArrayList<Double> rangeHigh) {
		this(domainVal, rangeLow, rangeHigh, new Color(222, 222, 255, 240), new BasicStroke(0.5f), new Color(222, 222,
				255, 240), new BasicStroke(0.5f), 0.5f);
	}

	/**
	 * Constructor
	 * 
	 * @param domainVal
	 *            x-values of range
	 * @param rangeLow
	 *            lower y-values of range
	 * @param rangeHigh
	 *            upper y-values of range
	 * @param paint
	 *            Paint
	 * @param stroke
	 *            Stroke
	 * @param outlinePaint
	 *            Outline Paint
	 * @param outlineStroke
	 *            Outline Stroke
	 * @param alpha
	 *            Alpha channel
	 */
	public ArbitraryMarker(ArrayList<Double> domainVal, ArrayList<Double> rangeLow, ArrayList<Double> rangeHigh,
			Paint paint, Stroke stroke, Paint outlinePaint, Stroke outlineStroke, float alpha) {
		super(paint, stroke, outlinePaint, outlineStroke, alpha);
		this.domainVal = domainVal;
		this.rangeLow = rangeLow;
		this.rangeHigh = rangeHigh;
	}

	/**
	 * Returns a clone of the marker.
	 * 
	 * @return A clone.
	 * 
	 * @throws CloneNotSupportedException
	 *             Not thrown by this class, but the exception is declared for the use of subclasses.
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	/**
	 * Tests the marker for equality with an arbitrary object.
	 * 
	 * @param obj
	 *            the object (<code>null</code> permitted).
	 * 
	 * @return A boolean.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (!(obj instanceof ArbitraryMarker))
			return false;
		if (!super.equals(obj))
			return false;
		ArbitraryMarker that = (ArbitraryMarker) obj;
		if (!this.rangeLow.equals(that.rangeLow))
			return false;
		if (!this.rangeHigh.equals(that.rangeHigh))
			return false;
		if (!ObjectUtilities.equal(this.gradientPaintTransformer, that.gradientPaintTransformer))
			return false;
		return true;
	}

	/**
	 * @return x-values
	 */
	public ArrayList<Double> getDomainVal() {
		return this.domainVal;
	}

	/**
	 * Returns the gradient paint transformer.
	 * 
	 * @return The gradient paint transformer (possibly <code>null</code>).
	 */
	public GradientPaintTransformer getGradientPaintTransformer() {
		return this.gradientPaintTransformer;
	}

	/**
	 * @return upper y-values of range
	 */
	public ArrayList<Double> getRangeHighValues() {
		return this.rangeHigh;
	}

	/**
	 * @return lower y-values of range
	 */
	public ArrayList<Double> getRangeLowValues() {
		return this.rangeLow;
	}

	@Override
	public int hashCode() {
		assert false : "hashCode not designed";
		return 42; // any arbitrary constant will do
	}

	/**
	 * Sets the gradient paint transformer.
	 * 
	 * @param transformer
	 *            the transformer (<code>null</code> permitted).
	 */
	public void setGradientPaintTransformer(GradientPaintTransformer transformer) {
		this.gradientPaintTransformer = transformer;
	}

}
