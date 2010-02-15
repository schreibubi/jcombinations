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

import org.schreibubi.JCombinations.FileFormat.Alternative;
import org.schreibubi.JCombinations.FileFormat.Asdap;
import org.schreibubi.JCombinations.FileFormat.OurTreeNode;
import org.schreibubi.JCombinations.FileFormat.Shmoo;
import org.schreibubi.JCombinations.FileFormat.TreeVisitor;
import org.schreibubi.JCombinations.FileFormat.Ydata;

/**
 * Creates the charts from all selected nodes
 * 
 * @author Jörg Werner
 * 
 */
public class OptimizeTreeVisitor implements TreeVisitor {

	/**
	 * Constructor
	 * 
	 */
	public OptimizeTreeVisitor() {
	}

	public void visit(Alternative a) throws Exception {

		int countAlternativeChilds = 0;
		OurTreeNode child = null;
		for (int i = 0; i < a.getChildCount(); i++) {
			if (a.getChildAt(i) instanceof Alternative) {
				countAlternativeChilds++;
				child = a.getChildAt(i);
			}
		}

		if (countAlternativeChilds > 1) {
			for (int i = 0; i < a.getChildCount(); i++) {
				if (a.getChildAt(i) instanceof Alternative) {
					(a.getChildAt(i)).accept(this);
				}
			}
		} else if (countAlternativeChilds == 1) {
			a.removeChild(child);
			for (int j = 0; j < child.getChildCount(); j++) {
				a.addChild((OurTreeNode) child.getChildAt(j));
			}
			visit(a);
		}

	}

	public void visit(Asdap r) throws Exception {
		int countAlternativeChilds = 0;
		OurTreeNode child = null;
		for (int i = 0; i < r.getChildCount(); i++) {
			if (r.getChildAt(i) instanceof Alternative) {
				countAlternativeChilds++;
				child = r.getChildAt(i);
			}
		}

		if (countAlternativeChilds > 1) {
			for (int i = 0; i < r.getChildCount(); i++) {
				if (r.getChildAt(i) instanceof Alternative) {
					(r.getChildAt(i)).accept(this);
				}
			}
		} else if (countAlternativeChilds == 1) {
			r.removeChild(child);
			for (int j = 0; j < child.getChildCount(); j++) {
				r.addChild((OurTreeNode) child.getChildAt(j));
			}
			visit(r);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.schreibubi.JCombinations.FileFormat.TreeVisitor#visit(org.schreibubi
	 * .JCombinations.FileFormat.Shmoo )
	 */
	public void visit(Shmoo s) throws Exception {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.schreibubi.JCombinations.FileFormat.TreeVisitor#visit(org.schreibubi
	 * .JCombinations.FileFormat.Data)
	 */
	public void visit(Ydata d) throws Exception {
	}

}
