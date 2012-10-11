package examples;

/**
 * Created by IntelliJ IDEA.
 * User: Alex
 * Date: 11/10/12
 * Time: 21:07
 */
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;

// Use MemoryImageSource, set int[] buffer pixels, newPixels to BI, then drawImage to BB
// Very fast WINDOWS. Fast LINUX. Very slow remotely.
public class TestGraph22 extends JPanel {
	public Point myMousePos;
	public int myObjectSize;
	public int myObjectStep;
	public int myWidth;
	public int myHeight;

	private int[] myScreenBuffer;
	MemoryImageSource myMISource;
	private Image myBitmap;

	public TestGraph22() {
		myMousePos = new Point(-1,-1);
		myObjectSize = 80;
		myObjectStep = 10;
		myHeight = 0;
		myWidth = 0;
		myScreenBuffer = null;
		myBitmap = null;

		setSize(myWidth,myHeight);
		validate();
		addMouseMotionListener( new MouseMotionAdapter() {
			public void mouseMoved( MouseEvent e ) {
				myMousePos = e.getPoint();
				TestGraph22.this.repaint();
			}
		});
	}
	private void resetBitmap() {
		myScreenBuffer = new int[myWidth * myHeight];
		myMISource = new MemoryImageSource(myWidth,myHeight,myScreenBuffer,0,myWidth);
		myMISource.setAnimated(true);
		myMISource.setFullBufferUpdates(true);
		myBitmap = createImage( myMISource );
	}
	public void paintComponent( Graphics g ) {
		Rectangle rect = getVisibleRect();
		if( myBitmap == null || myHeight != rect.height || myWidth != rect.width ) {
			myHeight = rect.height;
			myWidth = rect.width;
			resetBitmap();
		}
		paintObject();
		g.drawImage( myBitmap, 0, 0, this );
	}
	public void paintObject() {
		int color = Color.lightGray.getRGB();
		for( int iy = 0; iy < myHeight; iy++ ) {
			int indexRow = iy*myWidth;
			for( int ix = 0; ix < myWidth; ix++ ) {
				myScreenBuffer[ix+indexRow] = color;
			}
		}

		if( myMousePos.x >= 0 && myMousePos.y >= 0 ) {
			int color1 = Color.black.getRGB();
			int color2 = Color.white.getRGB();
			int xpos1 = Math.max( myMousePos.x-myObjectSize/2, 0 );
			int xpos2 = Math.min( myMousePos.x+myObjectSize/2, myWidth );
			int ypos1 = Math.max( myMousePos.y-myObjectSize/2, 0 );
			int ypos2 = Math.min( myMousePos.y+myObjectSize/2, myHeight );
			for( int iy = ypos1; iy < ypos2; iy++ ) {
				int indexRow = iy*myWidth;
				int yZeroOne = (int)( iy / myObjectStep ) % 2;
				for( int ix = xpos1; ix < xpos2; ix++ ) {
					int xZeroOne = (int)( ix / myObjectStep ) % 2;
					if( xZeroOne == yZeroOne )
						myScreenBuffer[ix+indexRow] = color1;
					else
						myScreenBuffer[ix+indexRow] = color2;
				}
			}
		}
		myMISource.newPixels();
	}
	public static void main(String[] argv){
		JFrame frame = new JFrame("Test graphics 22: screenB -> BI -> BB");
		JPanel p = new TestGraph22();
		frame.getContentPane().add(p);
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frame.pack();
		frame.setSize(800,600) ;
		frame.setVisible(true);
	}
}
