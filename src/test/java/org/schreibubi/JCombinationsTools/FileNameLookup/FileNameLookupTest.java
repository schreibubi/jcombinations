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
package org.schreibubi.JCombinationsTools.FileNameLookup;

import java.io.File;
import java.util.Collections;

import org.schreibubi.JCombinationsTools.FileNameLookup.FileNameLookupSingleton;
import org.schreibubi.visitor.VArrayList;
import org.testng.annotations.Test;


/**
 * @author Jörg Werner
 * 
 */
public class FileNameLookupTest {

	@Test()
	public void LookupTest() throws Exception {
		VArrayList<String> pre = new VArrayList<String>(), post = new VArrayList<String>();
		Collections.addAll(pre, "001G", "H70");
		Collections.addAll(post, "T5571");

		FileNameLookupSingleton.initialize(pre, post);
		File foundFile = FileNameLookupSingleton.getInstance().lookup(new File("target/test-classes/"),
				"channels.const");
		assert foundFile.getName().compareTo("channels__H70.const_T5571") == 0;
	}

}
