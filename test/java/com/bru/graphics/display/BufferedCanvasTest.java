package com.bru.graphics.display;

import org.junit.Test;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Alex
 * Date: 07/10/12
 * Time: 11:28
 */
public class BufferedCanvasTest {

	@Test
	public void test() throws InterruptedException {
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
				Display display = new BufferedCanvas(300, 200);
				display.updatePixels(changes);
			}
		});

		Thread.currentThread().join();

	}

}
