package com.bru.graphics.display;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Alex
 * Date: 07/10/12
 * Time: 11:43
 */
public class Sprite {

	public int x;
	public int y;
	public int width;
	public int height;
	public int[] colors;

	public Sprite(File file) throws IOException {
		BufferedImage image = ImageIO.read(file);
		width = image.getWidth();
		height = image.getHeight();
		colors = new int[width * height];
		image.getRGB(0, 0, width, height, colors, 0, width);
		assert (width > 0);
		assert (height > 0);
	}

	//test only:
	public Sprite(){
		width = 300;
		height = 200;
		int red = Color.RED.getRGB();

		colors = new int[width*height];
		for (int i = 0; i < width; i++){
			for (int j = 0; j < height; j++){
				colors[i*height+j] = red;
			}
		}
	}

}
