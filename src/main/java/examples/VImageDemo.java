package examples;


import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.image.VolatileImage;

public class VImageDemo extends Component {

	VolatileImage backBuffer = null;

	void createBackBuffer() {
		if (backBuffer != null) {
			backBuffer.flush();
			backBuffer = null;
		}
		backBuffer = createVolatileImage(getWidth(), getHeight());
	}

	public void paint(Graphics g) {
		if (backBuffer == null) {
			createBackBuffer();
		}
		do {
			// First, we validate the back buffer
			int valCode = backBuffer.validate(getGraphicsConfiguration());
			if (valCode == VolatileImage.IMAGE_RESTORED) {
				// This case is just here for illustration
				// purposes.  Since we are
				// recreating the contents of the back buffer
				// every time through this loop, we actually
				// do not need to do anything here to recreate
				// the contents.  If our VImage was an image that
				// we were going to be copying _from_, then we
				// would need to restore the contents at this point
			} else if (valCode == VolatileImage.IMAGE_INCOMPATIBLE) {
				createBackBuffer();
			}
			// Now we've handled validation, get on with the rendering

			//
			// rendering to the back buffer:
			Graphics gBB = backBuffer.getGraphics();


			gBB.setColor(Color.white);
			gBB.fillRect(0, 0, getWidth(), getHeight());
			gBB.setColor(Color.red);
			gBB.drawLine(0, 0, getWidth(), getHeight());

			// copy from the back buffer to the screen
			g.drawImage(backBuffer, 0, 0, this);

			// Now we are done; or are we?  Check contentsLost() and loop as necessary
		} while (backBuffer.contentsLost());
	}

	public static void main(String args[]) {
		Frame f = new Frame();
		f.setSize(500, 500);
		f.add(new VImageDemo());
		f.setVisible(true);
	}
}
