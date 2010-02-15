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

import org.schreibubi.JCombinations.FileFormat.Xdata;
import org.schreibubi.JCombinations.evalVariables.EvalVariablesTreeWalker;
import org.schreibubi.JCombinations.jfreechart.ArbitraryMarker;
import org.schreibubi.symbol.Symbol;
import org.schreibubi.visitor.VHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jfree.chart.plot.Marker;

import antlr.collections.AST;

/**
 * @author Jörg Werner
 */
public class CorridorLimits implements LimitInterface {

	private static Logger logger = LoggerFactory
			.getLogger(CorridorLimits.class);

	private AST ast;

	private String trim;

	private String measure;

	private String measureError;

	/**
	 * Constructor
	 * 
	 * @param ast
	 * @param trim
	 * @param measure
	 * @param measureError
	 */
	public CorridorLimits(AST ast, String trim, String measure,
			String measureError) {
		this.ast = ast;
		this.trim = trim;
		this.measure = measure;
		this.measureError = measureError;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.schreibubi.JCombinations.logic.limits.LimitInterface#calcLimits(org
	 * .schreibubi.JCombinations.FileFormat.Data, java.util.HashMap,
	 * java.util.ArrayList)
	 */
	public Marker calcLimits(ArrayList<Xdata> xlabels, int xAbsValuesIndex,
			int xRelativeValuesIndex, VHashMap<Symbol> constantVariables)
			throws Exception {
		CorridorLimits.logger.debug("CalcLimits for " + this.trim + " measure "
				+ this.measure);
		ArrayList<Double> xvals = new ArrayList<Double>();
		ArrayList<Double> rangeLow = new ArrayList<Double>();
		ArrayList<Double> rangeHigh = new ArrayList<Double>();
		for (int i = 0; i < xlabels.get(0).getLabels().size(); i++) {
			VHashMap<Symbol> symbolTable = new VHashMap<Symbol>();
			// Add all xlabels to symbol table
			for (int j = 0; j < xlabels.size(); j++) {
				Xdata xlabel = xlabels.get(j);
				Symbol sym = xlabel.getLabels().get(i).clone();
				symbolTable.put(sym.getName(), sym);
				if (j == xRelativeValuesIndex) {
					// correct name in symbol table!
					symbolTable.put(this.trim + "TRIM", sym.clone());
				}
			}
			EvalVariablesTreeWalker walker = new EvalVariablesTreeWalker();
			walker.lines(this.ast, symbolTable, constantVariables);
			Symbol m = symbolTable.get(this.measure);
			Symbol me = symbolTable.get(this.measureError);
			if (m == null) {
				throw new Exception("Warning: Variable " + this.measure
						+ " not found\n");
			}
			if (me == null) {
				throw new Exception("Warning: Variable " + this.measureError
						+ " not found\n");
			}
			double dm = m.convertToDouble().getValue();
			double dme = me.convertToDouble().getValue();
			xvals.add(xlabels.get(xAbsValuesIndex).getXPositions().get(i));
			rangeLow.add(new Double(dm - dme));
			rangeHigh.add(new Double(dm + dme));
		}
		CorridorLimits.logger.debug("CalcLimits for " + this.trim + " measure "
				+ this.measure + "succesfully finished");
		return new ArbitraryMarker(xvals, rangeLow, rangeHigh);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.schreibubi.JCombinations.logic.limits.LimitInterface#judgeDataSeries
	 * (java.util.HashMap, java.util.ArrayList, java.util.ArrayList)
	 */
	public boolean judgeDataSeries(ArrayList<Xdata> xlabels,
			int xAbsValuesIndex, int xRelativeValuesIndex,
			VHashMap<Symbol> constantVariables, ArrayList<Symbol> yv)
			throws Exception {
		for (int i = 0; i < xlabels.get(0).getLabels().size(); i++) {
			VHashMap<Symbol> symbolTable = new VHashMap<Symbol>();
			// Add all xlabels to symbol table
			for (int j = 0; j < xlabels.size(); j++) {
				Xdata xlabel = xlabels.get(j);
				Symbol sym = xlabel.getLabels().get(i).clone();
				symbolTable.put(sym.getName(), sym);
				if (j == xRelativeValuesIndex) {
					// correct name in symbol
					// table!
					symbolTable.put(this.trim + "TRIM", sym.clone());
				}
			}

			EvalVariablesTreeWalker walker = new EvalVariablesTreeWalker();
			walker.lines(this.ast, symbolTable, constantVariables);
			Symbol m = symbolTable.get(this.measure);
			Symbol me = symbolTable.get(this.measureError);
			if (m == null) {
				throw new Exception("Limits evaluation for " + this.measure
						+ " failed\n");
			}
			if (me == null) {
				throw new Exception("Limits evaluation for "
						+ this.measureError + " failed\n");
			}
			double dm = m.convertToDouble().getValue();
			double dme = me.convertToDouble().getValue();
			double y = yv.get(i).convertToDouble().getValue();
			if ((y < dm - Math.abs(dme)) | (y > dm + Math.abs(dme))) {
				return false;
			}
		}
		return true;
	}

}
