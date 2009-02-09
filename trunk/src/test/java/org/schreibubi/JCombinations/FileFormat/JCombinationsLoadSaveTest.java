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
package org.schreibubi.JCombinations.FileFormat;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IMarshallingContext;
import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.JiBXException;
import org.schreibubi.JCombinations.FileFormat.OurTreeNode;
import org.schreibubi.JCombinationsTools.mergeResults.MatlabWriteTreeVisitor;
import org.testng.annotations.Test;

import com.jmatio.io.MatFileWriter;

/**
 * @author Jörg Werner
 * 
 */
public class JCombinationsLoadSaveTest {

	/**
	 * @param args
	 * @throws JiBXException
	 * @throws FileNotFoundException
	 */
	@Test
	public static void jCombinationsLoadSave() throws JiBXException, FileNotFoundException {
		IBindingFactory bfact;
		bfact = BindingDirectory.getFactory(org.schreibubi.JCombinations.FileFormat.Asdap.class);
		IUnmarshallingContext uctx = bfact.createUnmarshallingContext();
		OurTreeNode obj = (OurTreeNode) uctx.unmarshalDocument(new FileInputStream("target/test-classes/trim_srf.xml"),
				null);
		assert obj != null;
		IMarshallingContext mctx = bfact.createMarshallingContext();
		mctx.setIndent(4);
		mctx.marshalDocument(obj, "UTF-8", null, new FileOutputStream("target/test-classes/output_test.xml"));
	}

	@Test
	public static void jCombinationsMatSave() throws Exception {
		IBindingFactory bfact;
		bfact = BindingDirectory.getFactory(org.schreibubi.JCombinations.FileFormat.Asdap.class);
		IUnmarshallingContext uctx = bfact.createUnmarshallingContext();
		OurTreeNode obj = (OurTreeNode) uctx.unmarshalDocument(new FileInputStream("target/test-classes/trim_srf.xml"),
				null);
		assert obj != null;
		MatlabWriteTreeVisitor mw = new MatlabWriteTreeVisitor();
		obj.accept(mw);
		ArrayList mllist = new ArrayList();
		mllist.add(mw.getMLArray());
		new MatFileWriter("target/test-classes/output_test.mat", mllist);

	}

}
