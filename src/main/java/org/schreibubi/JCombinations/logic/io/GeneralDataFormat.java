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
package org.schreibubi.JCombinations.logic.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.swing.tree.TreePath;

import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IMarshallingContext;
import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.JiBXException;
import org.schreibubi.JCombinations.FileFormat.Asdap;
import org.schreibubi.JCombinations.FileFormat.OurTreeNode;
import org.schreibubi.JCombinations.logic.DataModel;

/**
 * @author Werner Jörg
 */
public class GeneralDataFormat implements ExportDataInterface,
		ImportDataInterface {

	// private static Logger logger = LoggerFactory.getLogger(
	// GeneralDataFormat.class
	// );

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.schreibubi.JCombinations.logic.io.ImportDataInterface#readData(org
	 * .schreibubi.JCombinations.logic.DataModel, java.io.InputStream)
	 */
	public void readData(DataModel dm, InputStream in) throws IOException {

		IBindingFactory bfact;
		try {
			bfact = BindingDirectory.getFactory(Asdap.class);
			IUnmarshallingContext uctx = bfact.createUnmarshallingContext();
			dm.setRoot((OurTreeNode) uctx.unmarshalDocument(in, null));
		} catch (JiBXException e) {
			throw new IOException("XML parsing exception");
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.schreibubi.JCombinations.logic.io.ExportDataInterface#writeData(org
	 * .schreibubi.JCombinations.logic.DataModel, java.io.OutputStream,
	 * java.util.ArrayList)
	 */
	public void writeData(DataModel dm, OutputStream out,
			ArrayList<TreePath> nodes) throws IOException {

		IBindingFactory bfact;
		try {
			bfact = BindingDirectory.getFactory(Asdap.class);

			IMarshallingContext mctx = bfact.createMarshallingContext();
			mctx.setIndent(4);
			mctx.marshalDocument(dm.getRoot(), "UTF-8", null, out);

		} catch (JiBXException e) {
			throw new IOException("XML parsing exception");
		}

	}

}
