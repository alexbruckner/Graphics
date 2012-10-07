/**
 * Created by IntelliJ IDEA.
 * User: alex
 * Date: 06/10/12
 * Time: 12:37
 */

package com.bru.graphics.display;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class BufferedCanvas extends Canvas implements Display {

	private int width;
	private int height;

	private BufferedImage image;

	private BufferedCanvas(int width, int height) {
		this.width = width;
		this.height = height;
		setSize(width, height);
		image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		display();
	}

	public void updatePixels(Change[] changes) {
		for (Change change : changes) {
			image.setRGB(change.x, change.y, change.color); //TODO check performance of setRGB
		}
		repaint();
	}

	public void paint(Graphics g) {
		g.drawImage(image, 0, 0, this);
	}

	public void update(Graphics g) {
		paint(g);
	}

	private void display() {
		JFrame frame = new JFrame();
		frame.getContentPane().add(this);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {

		int width = 300;
		int height = 200;
		int red = Color.RED.getRGB();

		final Change[] changes = new Change[width*height];
		for (int i = 0; i < width; i++){
			for (int j = 0; j < height; j++){
				changes[i*height+j] = new Change(i,j,red);
			}
		}

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				BufferedCanvas display = new BufferedCanvas(300, 200);
				display.updatePixels(changes);
			}
		});

	}

}


//	//TODO
//	public void processFrame(byte[] frame, int width, int height) {
//		byte[] imgData = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
//		System.arraycopy(frame, 0, imgData, 0, frame.length);
//	}

//	//TODO
//	private BufferedImage toCompatibleImage(BufferedImage image) {
//		// obtain the current system graphical settings
//		GraphicsConfiguration gfx_config = GraphicsEnvironment.
//				getLocalGraphicsEnvironment().getDefaultScreenDevice().
//				getDefaultConfiguration();
//
//		/*
//				 * if image is already compatible and optimized for current system
//				 * settings, simply return it
//				 */
//		if (image.getColorModel().equals(gfx_config.getColorModel()))
//			return image;
//
//		// image is not optimized, so create a new image that is
//		BufferedImage new_image = gfx_config.createCompatibleImage(
//				image.getWidth(), image.getHeight(), image.getTransparency());
//
//		// get the graphics context of the new image to draw the old image on
//		Graphics2D g2d = (Graphics2D) new_image.getGraphics();
//
//		// actually draw the image and dispose of context no longer needed
//		g2d.drawImage(image, 0, 0, null);
//		g2d.dispose();
//
//		// return the new optimized image
//		return new_image;
//	}
