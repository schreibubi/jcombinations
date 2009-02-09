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
package org.schreibubi.JCombinations.logic.limits;

import java.util.ArrayList;

import org.jfree.chart.plot.Marker;
import org.schreibubi.JCombinations.FileFormat.Xdata;
import org.schreibubi.symbol.Symbol;
import org.schreibubi.visitor.VHashMap;


/**
 * Interface which all Limit classes need to implement
 * 
 * @author Jörg Werner
 * 
 */
public interface LimitInterface {

	/**
	 * Calculates the limit values
	 * 
	 * @param xlabels
	 *            x-labels supplying the possible additional x-values
	 * @param xAbsValuesIndex
	 *            which label to use for the x-values
	 * @param xRelativeValuesIndex
	 * @param constantVariables
	 *            constant values needed for the calculation
	 * @return Marker
	 * @throws Exception
	 */
	public abstract Marker calcLimits(ArrayList<Xdata> xlabels, int xAbsValuesIndex, int xRelativeValuesIndex,
			VHashMap<Symbol> constantVariables) throws Exception;

	/**
	 * @param xlabels
	 *            x-labels supplying the possible additional x-values
	 * @param xAbsValuesIndex
	 *            which label to use for the x-values
	 * @param xRelativeValuesIndex
	 * @param constantVariables
	 *            constant values needed for the calculation
	 * @param yv
	 *            y-values to judge according to the calculated limits
	 * @return pass or fail
	 * @throws Exception
	 */
	public abstract boolean judgeDataSeries(ArrayList<Xdata> xlabels, int xAbsValuesIndex, int xRelativeValuesIndex,
			VHashMap<Symbol> constantVariables, ArrayList<Symbol> yv) throws Exception;

}