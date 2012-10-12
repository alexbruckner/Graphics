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

	private BufferedImage image;

	public BufferedCanvas(int width, int height) {
		setSize(width, height);
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics  g = image.getGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0,0,width,height);
		display();
	}

	public void draw(Sprite... sprites) {
		for (Sprite sprite : sprites){
			image.getRaster().setDataElements(sprite.x, sprite.y, sprite.width, sprite.height, sprite.colors);
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
}

