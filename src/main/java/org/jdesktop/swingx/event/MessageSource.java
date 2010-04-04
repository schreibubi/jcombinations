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
 * Interface for MessageListener registrations methods and indicates that the
 * implementation class is a source of MessageEvents. MessageListeners which are
 * interested in MessageEvents from this class can register themselves as
 * listeners.
 * 
 * @see MessageEvent
 * @see MessageListener
 * @author Mark Davidson
 */
public interface MessageSource {

	/**
	 * Register the MessageListener.
	 * 
	 * @param l
	 *            the listener to register
	 */
	void addMessageListener(MessageListener l);

	/**
	 * Unregister the MessageListener from the MessageSource.
	 * 
	 * @param l
	 *            the listener to unregister
	 */
	void removeMessageListener(MessageListener l);

	/**
	 * Returns an array of listeners.
	 * 
	 * @return an non null array of MessageListeners.
	 */
	MessageListener[] getMessageListeners();
}
