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
 * The listener interface for recieving progress events. The class interested in
 * handling {@link ProgressEvent}s should implement this interface. The
 * complementary interface would be {@link MessageSource}
 * 
 * @see ProgressEvent
 * @see MessageSource
 * @author Mark Davidson
 */
public interface ProgressListener extends java.util.EventListener {

	/**
	 * Indicates the start of a long operation. The <code>ProgressEvent</code>
	 * will indicate if this is a determinate or indeterminate operation.
	 * 
	 * @param evt
	 *            an object which describes the event
	 */
	void progressStarted(ProgressEvent evt);

	/**
	 * Indicates that the operation has stopped.
	 */
	void progressEnded(ProgressEvent evt);

	/**
	 * Invoked when an increment of progress is sent. This may not be sent if an
	 * indeterminate progress has started.
	 * 
	 * @param evt
	 *            an object which describes the event
	 */
	void progressIncremented(ProgressEvent evt);
}
