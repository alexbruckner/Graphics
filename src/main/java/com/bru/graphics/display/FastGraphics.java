package com.bru.graphics.display;

import javax.swing.*;
import java.awt.image.MemoryImageSource;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

public class FastGraphics
        extends JPanel
        implements Runnable
{
    private static final long serialVersionUID = -1060277656462873556L;

    private Thread thread;

    private boolean threadRun = true;

    private Image doubleBuffer;

    private Image tripleBuffer;

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
     * @see java.lang.Runnable#run()
     */
    public void run ( )
    {
        long loopStart;

        while ( threadRun )
        {
            loopStart = System.currentTimeMillis ( );

            effects();

            screenConverter.newPixels ( );

            // Copy background, the converted image into the main buffer
            tripleGraphics.drawImage ( fastPixelBuffer, 0, 0, this );

            doubleGraphics.drawImage ( tripleBuffer, 0, 0, this );

            screenRepaint = true;
            repaint ( );

            // Show FPS rate in the status bar
//            showStatus (
//                    String.valueOf ( 1000.0 / ( System.currentTimeMillis ( ) - loopStart ) ) );

            try
            {
                Thread.sleep ( 15 - System.currentTimeMillis ( ) + loopStart );
            }
            catch ( Exception e )
            {
            }
        }
    }

    //TODO for now paint everything red
    private void effects() {
//        for ( int yCnt = 0; yCnt < height; ++yCnt )
//        {
//            for ( int xCnt = 0; xCnt < width; ++xCnt )
//            {
//                setPixel(xCnt, yCnt, 255, 255, 0, 0);
//            }
//        }
    }

    public void setPixel(int x, int y, int alpha, int red, int green, int blue){
        screenBuffer [ y * width + x ] =
                ( alpha << 24 ) +
                        ( red  << 16 ) +
                        ( green << 8 ) +
                        ( blue );

    }

    public FastGraphics(){
        JFrame frame = new JFrame();
        frame.setSize(600,400);
        frame.getContentPane().add(this);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        FastGraphics fg = new FastGraphics();
        fg.init();
        fg.start();
        for (int i = 0; i <  fg.getWidth(); i++){
            fg.setPixel(i,10,255,0,255,0);
        }
        for (int j = 0; j <  fg.getHeight(); j++){
            fg.setPixel(10,j,255,255,0,0);
        }
    }
}
