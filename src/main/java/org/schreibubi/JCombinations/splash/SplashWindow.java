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
package org.schreibubi.JCombinations.splash;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

/**
 * A Splash window.
 * <p>
 * Usage: MyApplication is your application class. Create a Splasher class which opens the splash window, invokes the
 * main method of your Application class, and disposes the splash window afterwards. Please note that we want to keep
 * the Splasher class and the SplashWindow class as small as possible. The less code and the less classes must be loaded
 * into the JVM to open the splash screen, the faster it will appear.
 * 
 * <pre>
 * class Splasher {
 * 	public static void main(String[] args) {
 * 		SplashWindow.splash(Startup.class.getResource(&quot;splash.gif&quot;));
 * 		MyApplication.main(args);
 * 		SplashWindow.disposeSplash();
 * 	}
 * }
 * </pre>
 * 
 * @author Werner Randelshofer
 * @version 2.1 2005-04-03 Revised.
 */
public class SplashWindow extends Window {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -4293157705278246780L;

	/**
	 * The current instance of the splash window. (Singleton design pattern).
	 */
	private static SplashWindow	instance;

	/**
	 * Closes the splash window.
	 */
	public static void disposeSplash() {
		if (SplashWindow.instance != null) {
			SplashWindow.instance.getOwner().dispose();
			SplashWindow.instance = null;
		}
	}

	/**
	 * Invokes the main method of the provided class name.
	 * 
	 * @param className
	 *            class to invoke
	 * 
	 * @param args
	 *            the command line arguments
	 */
	public static void invokeMain(String className, String[] args) {
		try {
			Class.forName(className).getMethod("main", new Class[] { String[].class }).invoke(null,
					new Object[] { args });
		} catch (Exception e) {
			InternalError error = new InternalError("Failed to invoke main method");
			error.initCause(e);
			throw error;
		}
	}

	/**
	 * Open's a splash window using the specified image.
	 * 
	 * @param image
	 *            The splash image.
	 */
	public static void splash(Image image) {
		if ((SplashWindow.instance == null) && (image != null)) {
			Frame f = new Frame();

			// Create the splash image
			SplashWindow.instance = new SplashWindow(f, image);

			// Show the window.
			SplashWindow.instance.setVisible(true);

			// Note: To make sure the user gets a chance to see the
			// splash window we wait until its paint method has been
			// called at least once by the AWT event dispatcher thread.
			// If more than one processor is available, we don't wait,
			// and maximize CPU throughput instead.
			if (!EventQueue.isDispatchThread() && (Runtime.getRuntime().availableProcessors() == 1))
				synchronized (SplashWindow.instance) {
					while (!SplashWindow.instance.paintCalled)
						try {
							SplashWindow.instance.wait();
						} catch (InterruptedException e) {
						}
				}
		}
	}

	/**
	 * Open's a splash window using the specified image.
	 * 
	 * @param imageURL
	 *            The url of the splash image.
	 */
	public static void splash(URL imageURL) {
		if (imageURL != null)
			splash(Toolkit.getDefaultToolkit().createImage(imageURL));
	}

	/**
	 * The splash image which is displayed on the splash window.
	 */
	private Image	image;

	/**
	 * This attribute indicates whether the method paint(Graphics) has been called at least once since the construction
	 * of this window.<br>
	 * This attribute is used to notify method splash(Image) that the window has been drawn at least once by the AWT
	 * event dispatcher thread.<br>
	 * This attribute acts like a latch. Once set to true, it will never be changed back to false again.
	 * 
	 * @see #paint
	 */
	private boolean	paintCalled	= false;

	/**
	 * Creates a new instance.
	 * 
	 * @param parent
	 *            the parent of the window.
	 * @param image
	 *            the splash image.
	 */
	private SplashWindow(Frame parent, Image image) {
		super(parent);
		this.image = image;

		// Load the image
		MediaTracker mt = new MediaTracker(this);
		mt.addImage(image, 0);
		try {
			mt.waitForID(0);
		} catch (InterruptedException ie) {
		}

		// Center the window on the screen
		int imgWidth = image.getWidth(this);
		int imgHeight = image.getHeight(this);
		setSize(imgWidth, imgHeight);
		Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screenDim.width - imgWidth) / 2, (screenDim.height - imgHeight) / 2);

		// Users shall be able to close the splash window by
		// clicking on its display area. This mouse listener
		// listens for mouse clicks and disposes the splash window.
		MouseAdapter disposeOnClick = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent evt) {
				// Note: To avoid that method splash hangs, we
				// must set paintCalled to true and call notifyAll.
				// This is necessary because the mouse click may
				// occur before the contents of the window
				// has been painted.
				synchronized (SplashWindow.this) {
					SplashWindow.this.paintCalled = true;
					SplashWindow.this.notifyAll();
				}
				dispose();
			}
		};
		addMouseListener(disposeOnClick);
	}

	/**
	 * Paints the image on the window.
	 */
	@Override
	public void paint(Graphics g) {
		g.drawImage(this.image, 0, 0, this);

		// Notify method splash that the window
		// has been painted.
		// Note: To improve performance we do not enter
		// the synchronized block unless we have to.
		if (!this.paintCalled) {
			this.paintCalled = true;
			synchronized (this) {
				notifyAll();
			}
		}
	}

	/**
	 * Updates the display area of the window.
	 */
	@Override
	public void update(Graphics g) {
		// Note: Since the paint method is going to draw an
		// image that covers the complete area of the component we
		// do not fill the component with its background color
		// here. This avoids flickering.
		paint(g);
	}
}
