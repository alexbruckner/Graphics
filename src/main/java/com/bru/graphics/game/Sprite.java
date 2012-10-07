package com.bru.graphics.game;

import com.bru.graphics.display.BufferedCanvas;
import com.bru.graphics.display.Change;
import com.bru.graphics.display.Display;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Alex
 * Date: 07/10/12
 * Time: 11:43
 */
public class Sprite {

	private int width;
	private int height;
	private int[] data;

	public Sprite(File file) throws IOException {
		BufferedImage image = ImageIO.read(file);
		width = image.getWidth();
		height = image.getHeight();
		data = new int[width*height];
		image.getRGB(0, 0, width, height, data, 0, width);
		assert (width > 0);
		assert (height > 0);
	}

	public void draw(Display display, int x, int y){
		Change change = new Change(x,y,width,height,data);
		display.updatePixels(change);
	}

}
