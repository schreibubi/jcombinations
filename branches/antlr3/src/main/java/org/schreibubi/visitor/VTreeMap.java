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

import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author Jörg Werner
 * 
 * @param <T>
 *            type of the TreeMap
 */
public class VTreeMap<T> extends TreeMap<String, T> implements Host<T> {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -2043676214317085712L;

	public VTreeMap() {
		super();
	}

	public VTreeMap(Comparator<? super String> comparator) {
		super(comparator);
	}

	public VTreeMap(Map<? extends String, ? extends T> m) {
		super(m);
	}

	public VTreeMap(SortedMap<String, ? extends T> m) {
		super(m);
	}

	public void accept(Visitor<T> v) throws Exception {
		v.visit(this);
	}

}
