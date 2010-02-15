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
package org.schreibubi.JCombinationsTools.mergeResults;

import java.util.ArrayList;
import java.util.Stack;

import org.schreibubi.JCombinations.FileFormat.Alternative;
import org.schreibubi.JCombinations.FileFormat.Asdap;
import org.schreibubi.JCombinations.FileFormat.Shmoo;
import org.schreibubi.JCombinations.FileFormat.TreeVisitor;
import org.schreibubi.JCombinations.FileFormat.Xdata;
import org.schreibubi.JCombinations.FileFormat.Ydata;
import org.schreibubi.symbol.Symbol;

import com.jmatio.types.MLArray;
import com.jmatio.types.MLChar;
import com.jmatio.types.MLDouble;
import com.jmatio.types.MLStructure;

/**
 * Creates the charts from all selected nodes
 * 
 * @author Jörg Werner
 * 
 */
public class MatlabWriteTreeVisitor implements TreeVisitor {

	private MLStructure rootStructure = null;
	private final Stack<MLStructure> arrayStack = new Stack<MLStructure>();
	private final Stack<Integer> positionStack = new Stack<Integer>();

	/**
	 * Constructor
	 * 
	 */
	public MatlabWriteTreeVisitor() {
	}

	public MLArray getMLArray() {
		return rootStructure;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.schreibubi.JCombinations.FileFormat.TreeVisitor#visit(org.schreibubi
	 * .JCombinations.FileFormat. Alternative)
	 */
	public void visit(Alternative a) throws Exception {
		MLStructure altStructure = arrayStack.peek();
		Integer position = positionStack.peek();
		altStructure.setField("name", new MLChar(null, a.getName()), position);
		altStructure.setField("description", new MLChar(null, a
				.getDescription()), position);

		int countShmoos = 0;
		int countAlts = 0;
		for (int i = 0; i < a.getChildCount(); i++) {
			if (a.getChildAt(i) instanceof Shmoo) {
				countShmoos++;
			} else if (a.getChildAt(i) instanceof Alternative) {
				countAlts++;
			}
		}
		MLStructure collectShmoos = new MLStructure("shmoos", new int[] { 1,
				countShmoos });
		arrayStack.push(collectShmoos);
		positionStack.push(0);
		for (int i = 0; i < a.getChildCount(); i++) {
			if (a.getChildAt(i) instanceof Shmoo) {
				(a.getChildAt(i)).accept(this);
			}
		}
		arrayStack.pop();
		positionStack.pop();
		altStructure.setField("shmoos", collectShmoos);

		MLStructure collectAlts = new MLStructure("alternatives", new int[] {
				1, countAlts });
		arrayStack.push(collectAlts);
		positionStack.push(0);
		for (int i = 0; i < a.getChildCount(); i++) {
			if (a.getChildAt(i) instanceof Alternative) {
				(a.getChildAt(i)).accept(this);
			}
		}
		arrayStack.pop();
		positionStack.pop();
		altStructure.setField("alternatives", collectAlts);

		positionStack.push(positionStack.pop() + 1);
	}

	public void visit(Asdap r) throws Exception {
		rootStructure = new MLStructure("root", new int[] { 1, 1 });
		rootStructure.setField("name", new MLChar(null, r.getName()));

		int countShmoos = 0;
		int countAlts = 0;
		for (int i = 0; i < r.getChildCount(); i++) {
			if (r.getChildAt(i) instanceof Shmoo) {
				countShmoos++;
			} else if (r.getChildAt(i) instanceof Alternative) {
				countAlts++;
			}
		}
		MLStructure collectShmoos = new MLStructure("shmoos", new int[] { 1,
				countShmoos });
		arrayStack.push(collectShmoos);
		positionStack.push(0);
		for (int i = 0; i < r.getChildCount(); i++) {
			if (r.getChildAt(i) instanceof Shmoo) {
				(r.getChildAt(i)).accept(this);
			}
		}
		arrayStack.pop();
		positionStack.pop();
		rootStructure.setField("shmoos", collectShmoos);

		MLStructure collectAlts = new MLStructure("alternatives", new int[] {
				1, countAlts });
		arrayStack.push(collectAlts);
		positionStack.push(0);
		for (int i = 0; i < r.getChildCount(); i++) {
			if (r.getChildAt(i) instanceof Alternative) {
				(r.getChildAt(i)).accept(this);
			}
		}
		arrayStack.pop();
		positionStack.pop();
		rootStructure.setField("alternatives", collectAlts);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.schreibubi.JCombinations.FileFormat.TreeVisitor#visit(org.schreibubi
	 * .JCombinations.FileFormat.Shmoo )
	 */
	public void visit(Shmoo s) throws Exception {
		MLStructure mlStructure = arrayStack.peek();
		Integer position = positionStack.peek();
		mlStructure.setField("name", new MLChar(null, s.getName()), position);
		mlStructure.setField("description",
				new MLChar(null, s.getDescription()), position);
		mlStructure.setField("subtitle", new MLChar(null, s.getSubtitle()),
				position);
		mlStructure.setField("trim", new MLChar(null, s.getTrim()), position);
		mlStructure.setField("measure", new MLChar(null, s.getMeasure()),
				position);
		mlStructure.setField("defaultXdata", new MLChar(null, s
				.getDefaultXdata()), position);
		mlStructure.setField("relativeXdata", new MLChar(null, s
				.getRelativeXdata()), position);

		ArrayList<Xdata> xdatasets = s.getXdata();
		MLStructure mlXdataSets = new MLStructure("xdataset", new int[] { 1,
				xdatasets.size() });
		for (int i = 0; i < xdatasets.size(); i++) {
			Xdata xdata = xdatasets.get(i);
			mlXdataSets.setField("name", new MLChar(null, xdata.getName()), i);
			mlXdataSets.setField("description", new MLChar(null, xdata
					.getDescription()), i);
			mlXdataSets.setField("unit", new MLChar(null, xdata.getUnit()), i);
			Double[] d = new Double[] { 1.0 };
			mlXdataSets.setField("xpos", new MLDouble(null, xdata
					.getXPositions().toArray(d), 1), i);
		}
		mlStructure.setField("xdataset", mlXdataSets, position);

		MLStructure collectYdata = new MLStructure("ydata", new int[] { 1,
				s.getChildCount() });
		arrayStack.push(collectYdata);
		positionStack.push(0);
		for (int i = 0; i < s.getChildCount(); i++) {
			(s.getChildAt(i)).accept(this);
		}
		arrayStack.pop();
		positionStack.pop();

		mlStructure.setField("ydata", collectYdata, position);

		positionStack.push(positionStack.pop() + 1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.schreibubi.JCombinations.FileFormat.TreeVisitor#visit(org.schreibubi
	 * .JCombinations.FileFormat.Data)
	 */
	public void visit(Ydata d) throws Exception {
		MLStructure mlStructure = arrayStack.peek();
		Integer position = positionStack.peek();

		mlStructure.setField("name", new MLChar(null, d.getName()), position);
		mlStructure.setField("description",
				new MLChar(null, d.getDescription()), position);
		mlStructure.setField("unit", new MLChar(null, d.getUnit()), position);

		ArrayList<Symbol> v = d.getValues();
		Double[] dd = new Double[v.size()];
		for (int i = 0; i < v.size(); i++) {
			dd[i] = v.get(i).convertToDouble().getValue();
		}
		mlStructure.setField("dut", new MLDouble("dut", dd, 1));

		positionStack.push(positionStack.pop() + 1);

	}

}
