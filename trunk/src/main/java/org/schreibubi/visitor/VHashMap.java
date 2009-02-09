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

import java.util.HashMap;
import java.util.Map;

/**
 * VHash implements a Hash which supports the visitor pattern. Also a custom iterator is used for easier iteration over
 * a hash.
 * 
 * @param <T>
 *            type of class which the HashMap supports
 * 
 */
public class VHashMap<T> extends HashMap<String, T> implements Host<T> {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 6098121288008595079L;

	public VHashMap() {
		super();
	}

	public VHashMap(int initialCapacity) {
		super(initialCapacity);
	}

	public VHashMap(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
	}

	public VHashMap(Map<? extends String, ? extends T> m) {
		super(m);
	}

	public void accept(Visitor<T> v) throws Exception {
		v.visit(this);
	}

}
