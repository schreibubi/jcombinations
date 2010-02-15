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

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multiset;

/**
 * VArray implements an Array which supports the visitor pattern
 * 
 * @param <T>
 *            type of class which the array supports
 */
public class VArrayListMultimap<T> implements Host<T> {

	private final ListMultimap<String, T> myArrayListMultimap = null;

	/**
	 * 
	 */
	private static final long serialVersionUID = 3696369414526030335L;

	public VArrayListMultimap() {
		// myArrayListMultimap = Multimaps.newListMultimap(new HashMap<String,
		// Collection<T>>(), new VArrayList<T>());
	}

	public void accept(Visitor<T> v) throws Exception {
		v.visit(this);
	}

	/**
	 * @return
	 * @see com.google.common.collect.StandardMultimap#asMap()
	 */
	public Map<String, Collection<T>> asMap() {
		return myArrayListMultimap.asMap();
	}

	/**
	 * 
	 * @see com.google.common.collect.StandardMultimap#clear()
	 */
	public void clear() {
		myArrayListMultimap.clear();
	}

	/**
	 * @param key
	 * @param value
	 * @return
	 * @see com.google.common.collect.StandardMultimap#containsEntry(java.lang.Object,
	 *      java.lang.Object)
	 */
	public boolean containsEntry(Object key, Object value) {
		return myArrayListMultimap.containsEntry(key, value);
	}

	/**
	 * @param key
	 * @return
	 * @see com.google.common.collect.StandardMultimap#containsKey(java.lang.Object)
	 */
	public boolean containsKey(Object key) {
		return myArrayListMultimap.containsKey(key);
	}

	/**
	 * @param arg0
	 * @return
	 * @see com.google.common.collect.StandardMultimap#containsValue(java.lang.Object)
	 */
	public boolean containsValue(Object arg0) {
		return myArrayListMultimap.containsValue(arg0);
	}

	/**
	 * @return
	 * @see com.google.common.collect.StandardMultimap#entries()
	 */
	public Collection<Entry<String, T>> entries() {
		return myArrayListMultimap.entries();
	}

	/**
	 * @param obj
	 * @return
	 * @see com.google.common.collect.StandardListMultimap#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		return myArrayListMultimap.equals(obj);
	}

	/**
	 * @param key
	 * @return
	 * @see com.google.common.collect.StandardListMultimap#get(java.lang.Object)
	 */
	public List<T> get(String key) {
		return myArrayListMultimap.get(key);
	}

	/**
	 * @return
	 * @see com.google.common.collect.StandardMultimap#hashCode()
	 */
	@Override
	public int hashCode() {
		return myArrayListMultimap.hashCode();
	}

	/**
	 * @return
	 * @see com.google.common.collect.StandardMultimap#isEmpty()
	 */
	public boolean isEmpty() {
		return myArrayListMultimap.isEmpty();
	}

	/**
	 * @return
	 * @see com.google.common.collect.StandardMultimap#keys()
	 */
	public Multiset<String> keys() {
		return myArrayListMultimap.keys();
	}

	/**
	 * @return
	 * @see com.google.common.collect.StandardMultimap#keySet()
	 */
	public Set<String> keySet() {
		return myArrayListMultimap.keySet();
	}

	/**
	 * @param key
	 * @param value
	 * @return
	 * @see com.google.common.collect.StandardListMultimap#put(java.lang.Object,
	 *      java.lang.Object)
	 */
	public boolean put(String key, T value) {
		return myArrayListMultimap.put(key, value);
	}

	/**
	 * @param arg0
	 * @see com.google.common.collect.StandardMultimap#putAll(com.google.common.collect.Multimap)
	 */
	public void putAll(Multimap<? extends String, ? extends T> arg0) {
		myArrayListMultimap.putAll(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @see com.google.common.collect.StandardMultimap#putAll(java.lang.Object,
	 *      java.lang.Iterable)
	 */
	public void putAll(String arg0, Iterable<? extends T> arg1) {
		myArrayListMultimap.putAll(arg0, arg1);
	}

	/**
	 * @param key
	 * @param value
	 * @return
	 * @see com.google.common.collect.StandardMultimap#remove(java.lang.Object,
	 *      java.lang.Object)
	 */
	public boolean remove(Object key, Object value) {
		return myArrayListMultimap.remove(key, value);
	}

	/**
	 * @param key
	 * @return
	 * @see com.google.common.collect.StandardListMultimap#removeAll(java.lang.Object)
	 */
	public List<T> removeAll(Object key) {
		return myArrayListMultimap.removeAll(key);
	}

	/**
	 * @param key
	 * @param values
	 * @return
	 * @see com.google.common.collect.StandardListMultimap#replaceValues(java.lang.Object,
	 *      java.lang.Iterable)
	 */
	public List<T> replaceValues(String key, Iterable<? extends T> values) {
		return myArrayListMultimap.replaceValues(key, values);
	}

	/**
	 * @return
	 * @see com.google.common.collect.StandardMultimap#size()
	 */
	public int size() {
		return myArrayListMultimap.size();
	}

	/**
	 * @return
	 * @see com.google.common.collect.StandardMultimap#toString()
	 */
	@Override
	public String toString() {
		return myArrayListMultimap.toString();
	}

	/**
	 * @return
	 * @see com.google.common.collect.StandardMultimap#values()
	 */
	public Collection<T> values() {
		return myArrayListMultimap.values();
	}

}
