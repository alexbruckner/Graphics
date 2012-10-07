package examples;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.MemoryImageSource;

import javax.swing.JApplet;

/**
 * <p>
 * This applet shows how to use Java standard classes to write fast animations.
 * Instead of using the methods defined in <code>java.awt.Graphics</code> to
 * render the pictures, an integer array will be filled.
 * </p>
 * <p>
 * This source code was originally published at http://jakobvogel.net/
 * and may be freely passed on and used for non-commercial purposes as long as
 * this comment remains as it is.
 * </p>
 * <p>
 * Copyright (c) 2005. Please visit my homepage at <a
 * href="http://jakobvogel.net/">http://jakobvogel.net/</a> for
 * further details!
 * </p>
 *
 * @author Jakob Vogel &lt;<a
 *         href="mailto:jakob[dot]vogel[at]mytum[dot]de">jakob[dot]vogel[at]mytum[dot]de</a>&gt;
 */
public class FastGraphics
		extends JApplet
		implements Runnable
{
	private static final long serialVersionUID = -1060277656462873556L;

	private Thread thread;

	private boolean threadRun = true;

	private Image doubleBuffer;

	private Image tripleBuffer;

	private Image backgroundBuffer;

	/**
	 * <p>
	 * Converted image. Its contents are read from the integer array defined
	 * below.
	 * </p>
	 */
	private Image fastPixelBuffer;

	private Graphics doubleGraphics;

	private Graphics tripleGraphics;

	private boolean screenRepaint = false;

	private int width;

	private int height;

	/**
	 * <p>
	 * Integer array.
	 * </p>
	 */
	private int [ ] screenBuffer;

	/**
	 * <p>
	 * Conversion object.
	 * </p>
	 */
	private MemoryImageSource screenConverter;

	/**
	 * @see java.applet.Applet#init()
	 */
	public void init ( )
	{
		thread = new Thread ( this );
		thread.setPriority ( Thread.MIN_PRIORITY );

		setFont ( new Font ( "Verdana", Font.PLAIN, 10 ) );
		setBackground ( new Color ( 255, 255, 255 ) );

		width = this.getSize ( ).width;
		height = this.getSize ( ).height - 25;

		screenBuffer = new int [ width * height ];

		screenConverter = new MemoryImageSource ( width, height, screenBuffer, 0, width );
		screenConverter.setAnimated ( true );
		screenConverter.setFullBufferUpdates ( false );
		fastPixelBuffer = createImage ( screenConverter );

		backgroundBuffer = createImage ( this.getSize ( ).width, this.getSize ( ).height );
		renderBackground ( backgroundBuffer.getGraphics ( ) );

		doubleBuffer = createImage ( this.getSize ( ).width, this.getSize ( ).height );
		tripleBuffer = createImage ( this.getSize ( ).width, this.getSize ( ).height );

		doubleGraphics = doubleBuffer.getGraphics ( );
		tripleGraphics = tripleBuffer.getGraphics ( );
	}

	/**
	 * @see java.applet.Applet#start()
	 */
	public void start ( )
	{
		threadRun = true;
		thread.start ( );
	}

	/**
	 * @see java.applet.Applet#stop()
	 */
	public void stop ( )
	{
		threadRun = false;
		while ( thread.isAlive ( ) )
		{
			try
			{
				Thread.sleep ( 500 );
			}
			catch ( InterruptedException e )
			{
			}
		}
	}

	/**
	 * @see java.awt.Component#paint(java.awt.Graphics)
	 */
	public void paint ( Graphics g )
	{
		if ( screenRepaint )
		{
			g.drawImage ( doubleBuffer, 0, 0, this );
			screenRepaint = false;
		}
	}

	/**
	 * @see java.applet.Applet#getAppletInfo()
	 */
	public String getAppletInfo ( )
	{
		return "FastGraphics 3.3 - Copyright (c) 2005 by Jakob Vogel <jakob.vogel@mytum.de>";
	}

	/**
	 * @see java.lang.Runnable#run()
	 */
	public void run ( )
	{
		long loopStart;
		int red = 0;
		int green = 0;

		while ( threadRun )
		{
			loopStart = System.currentTimeMillis ( );

			if ( red < 255 && green == 0 )
			{
				red += 5;
			}
			else if ( green < 255 && red == 255 )
			{
				green += 5;
			}
			else if ( red > 0 && green == 255 )
			{
				red -= 5;
			}
			else if ( green > 0 && red == 0 )
			{
				green -= 5;
			}

			for ( int yCnt = 0; yCnt < height; ++yCnt )
			{
				for ( int xCnt = 0; xCnt < width; ++xCnt )
				{
					// 0xAARRGGBB, A = Alpha, R = Red, ...
					screenBuffer [ yCnt * width + xCnt ] =
							( 255 << 24 ) +
									( ( xCnt * ( 255 - red ) / width ) << 16 ) +
									( ( xCnt * ( 255 - green ) / width ) << 8 ) +
									( ( width - xCnt ) * 255 / width );
				}
			}

			screenConverter.newPixels ( );

			// Copy background, the converted image into the main buffer
			tripleGraphics.drawImage ( backgroundBuffer, 0, 0, this );
			tripleGraphics.drawImage ( fastPixelBuffer, 0, 0, this );

			// Show FPS output
			// tripleGraphics.setColor ( Color.white );
			// tripleGraphics.drawString ( String.valueOf ( 1000.0 / (
			// System.currentTimeMillis ( ) - loopStart ) ), 10, 20 );

			doubleGraphics.drawImage ( tripleBuffer, 0, 0, this );

			screenRepaint = true;
			repaint ( );

			// Show FPS rate in the status bar
			showStatus (
					String.valueOf ( 1000.0 / ( System.currentTimeMillis ( ) - loopStart ) ) );

			try
			{
				Thread.sleep ( 15 - System.currentTimeMillis ( ) + loopStart );
			}
			catch ( Exception e )
			{
			}
		}
	}

	/**
	 * <p>
	 * This function is used to create a static background buffer. This image
	 * contains a copyright message.
	 * </p>
	 *
	 * @param g
	 *        The graphics object to use for rendering.
	 */
	private void renderBackground ( Graphics g )
	{
		g.setColor ( new Color ( 255, 255, 255 ) );
		g.fillRect ( 0, 0, getSize ( ).width, getSize ( ).height );

		g.setColor ( new Color ( 85, 85, 85 ) );
		g.fillRect ( 0, getSize ( ).height - 25, getSize ( ).width, 25 );
		g.setColor ( new Color ( 255, 255, 255 ) );
		g.drawString ( "FastGraphics 3.3 - (c) 2005 by Jakob Vogel", 10, getSize ( ).height - 10 );
	}
}
