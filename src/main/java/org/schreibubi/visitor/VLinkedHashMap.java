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

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Jörg Werner
 * 
 * @param <T>
 *            type of the LinkedHashMap
 */
public class VLinkedHashMap<T> extends LinkedHashMap<String, T> implements
		Host<T> {

	private static final long serialVersionUID = -6493849243665071189L;

	public VLinkedHashMap() {
		super();
	}

	public VLinkedHashMap(int initialCapacity) {
		super(initialCapacity);
	}

	public VLinkedHashMap(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
	}

	public VLinkedHashMap(int initialCapacity, float loadFactor,
			boolean accessOrder) {
		super(initialCapacity, loadFactor, accessOrder);
	}

	public VLinkedHashMap(Map<? extends String, ? extends T> m) {
		super(m);
	}

	public void accept(Visitor<T> v) throws Exception {
		v.visit(this);
	}

}
