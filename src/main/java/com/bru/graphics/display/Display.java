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

public class Display extends Canvas {

	private int width;
	private int height;

	private BufferedImage image;

	int red = Color.RED.getRGB();

	public Display(int width, int height) {
		this.width = width;
		this.height = height;
		setSize(width, height);
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	}

	public void draw() {
		for (int i = 0; i < width; i++){
			for (int j = 0; j < height; j++){
				image.setRGB(i,j, red);
			}
		}
		repaint();
	}

	public void paint(Graphics g){
		g.drawImage(image, 0, 0, this);
	}

	public void update(Graphics g){
		paint(g);
	}

	private static void makeFrame(Display display){
		JFrame frame = new JFrame();
		frame.getContentPane().add(display);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
		Display display = new Display(300, 200);
		makeFrame(display);
		display.draw();
	}

}
