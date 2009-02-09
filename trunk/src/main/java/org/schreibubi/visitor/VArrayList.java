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
package org.schreibubi.visitor;

import java.util.ArrayList;
import java.util.Collection;

/**
 * VArray implements an Array which supports the visitor pattern
 * 
 * @param <T>
 *            type of class which the array supports
 */
public class VArrayList<T> extends ArrayList<T> implements Host<T> {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 3696369414526030335L;

	public VArrayList() {
		super();
	}

	public VArrayList(Collection<? extends T> c) {
		super(c);
	}

	public VArrayList(int initialCapacity) {
		super(initialCapacity);
	}

	public void accept(Visitor<T> v) throws Exception {
		v.visit(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.common.base.Supplier#get()
	 */
	public VArrayList<T> get() {
		return null;
		// return new VArrayList<T>();
	}

}
