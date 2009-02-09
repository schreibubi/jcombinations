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
package org.schreibubi.JCombinations.FileFormat;

import java.util.Iterator;

import org.jibx.runtime.IAliasable;
import org.jibx.runtime.IMarshaller;
import org.jibx.runtime.IMarshallingContext;
import org.jibx.runtime.IUnmarshaller;
import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.JiBXException;
import org.jibx.runtime.impl.MarshallingContext;
import org.jibx.runtime.impl.UnmarshallingContext;
import org.schreibubi.symbol.Symbol;
import org.schreibubi.symbol.SymbolDouble;
import org.schreibubi.symbol.SymbolInteger;
import org.schreibubi.symbol.SymbolString;
import org.schreibubi.visitor.VArrayList;


/**
 * @author Jörg Werner
 * 
 */
public class VArrayListSymbolSerializer implements IMarshaller, IUnmarshaller, IAliasable {

	private static final String	ENTRY_ELEMENT	= "const";

	private static final String	KEY_ATTRIBUTE	= "name";

	private static final String	TYPE_ATTRIBUTE	= "type";

	private String				m_uri;

	private int					m_index;

	private String				m_name;

	/**
	 * Default constructor. This uses a pre-defined name for the top-level element. It'll be used by JiBX when no name
	 * information is supplied by the mapping which references this custom marshaller/unmarshaller.
	 */
	public VArrayListSymbolSerializer() {
		this.m_uri = null;
		this.m_index = 0;
		this.m_name = "constants";
	}

	/**
	 * Aliased constructor. This takes a name definition for the top-level element. It'll be used by JiBX when a name is
	 * supplied by the mapping which references this custom marshaller/unmarshaller.
	 * 
	 * @param uri
	 *            namespace URI for the top-level element (also used for all other names within the binding)
	 * @param index
	 *            namespace index corresponding to the defined URI within the marshalling context definitions
	 * @param name
	 *            local name for the top-level element
	 */
	public VArrayListSymbolSerializer(String uri, int index, String name) {
		this.m_uri = uri;
		this.m_index = index;
		this.m_name = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jibx.runtime.IMarshaller#isExtension(int)
	 */
	public boolean isExtension(int index) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jibx.runtime.IUnmarshaller#isPresent(org.jibx.runtime.IUnmarshallingContext)
	 */
	public boolean isPresent(IUnmarshallingContext ctx) throws JiBXException {
		return ctx.isAt(this.m_uri, this.m_name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jibx.runtime.IMarshaller#marshal(java.lang.Object, org.jibx.runtime.IMarshallingContext)
	 */
	public void marshal(Object obj, IMarshallingContext ictx) throws JiBXException {

		// make sure the parameters are as expected
		if (!(obj instanceof VArrayList))
			throw new JiBXException("Invalid object type for marshaller");
		else if (!(ictx instanceof MarshallingContext))
			throw new JiBXException("Invalid object type for marshaller");
		else {

			// start by generating start tag for container
			MarshallingContext ctx = (MarshallingContext) ictx;
			VArrayList map = (VArrayList) obj;
			ctx.startTagAttributes(this.m_index, this.m_name).closeStartContent();

			// loop through all entries in map
			Iterator iter = map.iterator();
			while (iter.hasNext()) {
				Symbol value = (Symbol) iter.next();
				if (value != null) {
					ctx.startTagAttributes(this.m_index, VArrayListSymbolSerializer.ENTRY_ELEMENT);
					ctx.attribute(this.m_index, VArrayListSymbolSerializer.KEY_ATTRIBUTE, value.getName());
					ctx.attribute(this.m_index, VArrayListSymbolSerializer.TYPE_ATTRIBUTE, value.getTypeString());
					ctx.closeStartContent();
					ctx.content(value.getValueString());
					ctx.endTag(this.m_index, VArrayListSymbolSerializer.ENTRY_ELEMENT);
				}
			}

			// finish with end tag for container element
			ctx.endTag(this.m_index, this.m_name);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jibx.runtime.IUnmarshaller#unmarshal(java.lang.Object, org.jibx.runtime.IUnmarshallingContext)
	 */
	@SuppressWarnings("unchecked")
	public Object unmarshal(Object obj, IUnmarshallingContext ictx) throws JiBXException {

		// make sure we're at the appropriate start tag
		UnmarshallingContext ctx = (UnmarshallingContext) ictx;
		if (!ctx.isAt(this.m_uri, this.m_name))
			ctx.throwStartTagNameError(this.m_uri, this.m_name);

		// create new arraylist if needed
		VArrayList list = (VArrayList) obj;
		if (list == null)
			list = new VArrayList();

		// process all entries present in document
		ctx.parsePastStartTag(this.m_uri, this.m_name);
		while (ctx.isAt(this.m_uri, VArrayListSymbolSerializer.ENTRY_ELEMENT)) {
			String key = ctx.attributeText(null, VArrayListSymbolSerializer.KEY_ATTRIBUTE, null);
			Symbol.SymType type = Symbol.getTypeFromString(ctx.attributeText(null,
					VArrayListSymbolSerializer.TYPE_ATTRIBUTE, null).toUpperCase());
			String text = ctx.parseElementText(this.m_uri, VArrayListSymbolSerializer.ENTRY_ELEMENT);
			Symbol sym = null;
			switch (type) {
			case INTEGER:
				sym = new SymbolInteger(key, text);
				break;
			case DOUBLE:
				sym = new SymbolDouble(key, text);
				break;
			case STRING:
				sym = new SymbolString(key, text);
				break;
			default:
				throw new JiBXException("Mapped value is not unmarshallable");
			}
			list.add(sym);
		}
		ctx.parsePastEndTag(this.m_uri, this.m_name);
		return list;
	}
}
