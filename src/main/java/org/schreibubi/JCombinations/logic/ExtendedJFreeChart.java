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

import java.awt.Font;

import javax.swing.tree.TreePath;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.Plot;

/**
 * @author Jörg Werner
 * 
 */
public class ExtendedJFreeChart extends JFreeChart {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4463896210584613767L;

	private TreePath treePath;

	/**
	 * @param plot
	 */
	public ExtendedJFreeChart(Plot plot) {
		super(plot);
	}

	/**
	 * @param title
	 * @param titleFont
	 * @param plot
	 * @param createLegend
	 */
	public ExtendedJFreeChart(String title, Font titleFont, Plot plot,
			boolean createLegend) {
		super(title, titleFont, plot, createLegend);
	}

	/**
	 * @param title
	 * @param plot
	 */
	public ExtendedJFreeChart(String title, Plot plot) {
		super(title, plot);
	}

	/**
	 * @return the treePath
	 */
	public TreePath getTreePath() {
		return treePath;
	}

	/**
	 * @param treePath
	 *            the treePath to set
	 */
	public void setTreePath(TreePath treePath) {
		this.treePath = treePath;
	}

}
