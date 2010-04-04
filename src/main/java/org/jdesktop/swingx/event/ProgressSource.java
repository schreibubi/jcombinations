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
 * Interface for ProgressListener registrations methods and indicates that the
 * implementation class is a source of ProgressEvents. ProgressListeners which
 * are interested in ProgressEvents from this class can register themselves as
 * listeners.
 * 
 * @see ProgressEvent
 * @see ProgressListener
 * @author Mark Davidson
 */
public interface ProgressSource {

	/**
	 * Register the ProgressListener.
	 * 
	 * @param l
	 *            the listener to register
	 */
	void addProgressListener(ProgressListener l);

	/**
	 * Unregister the ProgressListener from the ProgressSource.
	 * 
	 * @param l
	 *            the listener to unregister
	 */
	void removeProgressListener(ProgressListener l);

	/**
	 * Returns an array of listeners.
	 * 
	 * @return an non null array of ProgressListeners.
	 */
	ProgressListener[] getProgressListeners();
}
