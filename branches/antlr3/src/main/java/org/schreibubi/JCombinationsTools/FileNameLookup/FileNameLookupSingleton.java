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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.schreibubi.visitor.VArrayList;


/**
 * @author Jörg Werner
 * 
 */
public class FileNameLookupSingleton {

	private static FileNameLookupSingleton	myInstance	= null;

	public static FileNameLookupSingleton getInstance() throws Exception {
		if (myInstance == null)
			throw new Exception("FileNameLookup has not yet been initialized");
		return myInstance;
	}

	public static void initialize(VArrayList<String> pre, VArrayList<String> post) throws Exception {
		if (myInstance != null)
			throw new Exception("FileNameLookupSingleton was already initialized!");
		myInstance = new FileNameLookupSingleton(pre, post);
	}

	private final VArrayList<String>	preList, postList;

	private FileNameLookupSingleton(VArrayList<String> pre, VArrayList<String> post) {
		this.preList = pre;
		this.postList = post;
	}

	/**
	 * @param name
	 * @param directory
	 * @return
	 * @throws Exception
	 */
	public File lookup(File dir, String name) throws Exception {
		int pos = name.indexOf('.');
		if (pos == -1)
			throw new Exception("No . found in filename: " + name + "!");
		String type = name.substring(pos + 1);
		String basename = name.substring(0, pos);
		String acc = "";
		for (int k = postList.size(); k >= 0; k--) {
			for (int i = preList.size(); i >= 0; i--) {
				acc += basename;
				for (int j = 0; j < i; j++) {
					acc += "_(|([A-Z0-9@]*\\+)*" + preList.get(j) + "(\\+[A-Z0-9@]*)*)";
				}
				acc += "." + type;
				for (int l = 0; l < k; l++) {
					acc += "_(|([A-Z0-9@]*\\+)*" + postList.get(l) + "(\\+[A-Z0-9@]*)*)";
				}
				if (i > 0) {
					acc += "|";
				}
			}
			if (k > 0) {
				acc += "|";
			}
		}
		final Pattern p = Pattern.compile(acc);

		if (dir.isDirectory()) {
			List<String> list = Arrays.asList(dir.list());

			// Put all matches into results together with the number of tag matches
			ArrayList<String> results = new ArrayList<String>();
			int max = 0;
			for (String n : list) {
				Matcher m = p.matcher(n);
				boolean b = m.matches();
				if (b) {
					int ng = m.groupCount();
					int count = 0;
					for (int i = 0; i < ng; i++) {
						if (m.group(i) != null) {
							count++;
						}
					}
					if (count > max) {
						results.clear();
						results.add(n);
						max = count;
					} else if (count == max) {
						results.add(n);
					}
				}
			}

			if (results.size() > 1)
				throw new Exception("No unique filename match for " + name + "! Possible candidates are: "
						+ results.toString());
			else if (results.size() == 0)
				throw new Exception("No matching file for " + name + "!");
			return new File(dir, results.get(0));
		} else
			return null;
	}
}
