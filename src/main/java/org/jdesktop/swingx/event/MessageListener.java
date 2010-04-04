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

package org.jdesktop.swingx.event;

/**
 * The listener interface for recieving message events. The class interested in
 * handling {@link MessageEvent}s should implement this interface. The
 * complementary interface would be {@link MessageSource}
 * 
 * @see MessageEvent
 * @see MessageSource
 * @author Mark Davidson
 */
public interface MessageListener extends java.util.EventListener {

	/**
	 * Invoked to send a message to a listener. The {@link MessageEvent} is
	 * qualified depending on context. It may represent a simple transient
	 * messages to be passed to the ui or it could represent a serious exception
	 * which has occured during processing.
	 * <p>
	 * The implementation of this interface should come up with a consistent
	 * policy to reflect the business logic of the application.
	 * 
	 * @param evt
	 *            an object which describes the message
	 */
	void message(MessageEvent evt);
}
