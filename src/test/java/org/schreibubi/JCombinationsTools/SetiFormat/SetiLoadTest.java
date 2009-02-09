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
package org.schreibubi.JCombinationsTools.SetiFormat;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.jibx.extras.BindingSelector;
import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.JiBXException;
import org.schreibubi.JCombinationsTools.SetiChains.SetiChainBuilder;
import org.schreibubi.JCombinationsTools.SetiFormat.Seti;
import org.testng.annotations.Test;


/**
 * @author Jörg Werner
 * 
 */
public class SetiLoadTest {

	/**
	 * @param args
	 * @throws JiBXException
	 * @throws FileNotFoundException
	 */
	@Test
	public static void SetiLoad() throws FileNotFoundException, JiBXException {
		BindingSelector sel = new BindingSelector(SetiChainBuilder.VERSION_URI, SetiChainBuilder.VERSION_NAME,
				SetiChainBuilder.VERSION_TEXTS, SetiChainBuilder.VERSION_BINDINGS);
		IUnmarshallingContext con = sel.getContext();
		con.setDocument(new FileInputStream("target/test-classes/setichains_001G_H70.xml"), null);
		Seti obj = (Seti) sel.unmarshalVersioned(Seti.class);
		assert obj != null;
	}

}
