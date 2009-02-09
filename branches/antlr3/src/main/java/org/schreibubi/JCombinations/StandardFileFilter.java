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
package org.schreibubi.JCombinations;

import java.io.File;
import java.util.ArrayList;

import javax.swing.filechooser.FileFilter;

/**
 * File filter class, for filtering after extensions
 * 
 * @author Jörg Werner
 */
public class StandardFileFilter extends FileFilter {

	/**
	 * Get the extension of a file.
	 * 
	 * @param f
	 * @return extension
	 */
	public static String getExtension(File f) {
		String ext = null;
		String s = f.getName();
		int i = s.lastIndexOf('.');

		if ((i > 0) && (i < s.length() - 1))
			ext = s.substring(i + 1).toLowerCase();
		return ext;
	}

	private ArrayList<String>	extensions	= new ArrayList<String>();

	private String				description;

	/**
	 * 
	 */
	public StandardFileFilter() {
		super();
	}

	/**
	 * @param ext
	 */
	public StandardFileFilter(String ext) {
		super();
		addExtension(ext);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
	 */
	@Override
	public boolean accept(File f) {
		if (f.isDirectory())
			return true;

		String extension = getExtension(f);
		if (extension != null)
			for (String e : this.extensions)
				if (extension.equals(e))
					return true;

		return false;
	}

	/**
	 * @param ext
	 */
	public void addExtension(String ext) {
		this.extensions.add(ext);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.filechooser.FileFilter#getDescription()
	 */
	@Override
	public String getDescription() {
		return this.description;
	}

	/**
	 * @param description
	 *            The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

}
