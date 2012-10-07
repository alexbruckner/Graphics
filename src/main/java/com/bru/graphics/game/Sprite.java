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
		final Change[] changes = new Change[width*height];
		int red = Color.RED.getRGB();
		int cnt = 0;
		for (int i = 0; i < width; i++){
			for (int j = 0; j < height; j++){
				changes[cnt++] = new Change(i+x,j+y, data[j*width+i]);
			}
		}
		display.updatePixels(changes);
	}

//	private int convertToInt(byte r, byte g, byte b){
//		return ((255 & 0xFF) << 24) | //alpha
//				(((int)r & 0xFF) << 16) | //red
//				(((int)g & 0xFF) << 8)  | //green
//				(((int)b & 0xFF) << 0); //blue
//	}

}
