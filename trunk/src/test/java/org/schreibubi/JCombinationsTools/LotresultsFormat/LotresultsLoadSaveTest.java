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
package org.schreibubi.JCombinationsTools.LotresultsFormat;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.JiBXException;
import org.schreibubi.JCombinationsTools.LotresultsFormat.DCResults;
import org.testng.annotations.Test;

/**
 * @author Jörg Werner
 * 
 */
public class LotresultsLoadSaveTest {

	/**
	 * @param args
	 * @throws JiBXException
	 * @throws FileNotFoundException
	 */
	@Test
	public static void lotresultsLoadTest() throws JiBXException, FileNotFoundException {
		IBindingFactory bfact = BindingDirectory.getFactory(DCResults.class);
		IUnmarshallingContext con = bfact.createUnmarshallingContext();
		DCResults obj = (DCResults) con.unmarshalDocument(new FileInputStream(
				"target/test-classes/lotresults_real_multi.xml"), null);
		assert obj != null;
	}

}
