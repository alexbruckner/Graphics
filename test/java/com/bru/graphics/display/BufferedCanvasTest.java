package com.bru.graphics.display;

import junit.framework.Assert;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by IntelliJ IDEA.
 * User: Alex
 * Date: 07/10/12
 * Time: 11:28
 */
public class BufferedCanvasTest {

	@Test
	public void test() throws InterruptedException, AWTException, InvocationTargetException {
		int width = 300;
		int height = 200;
		int red = Color.RED.getRGB();

		final Change[] changes = new Change[width*height];
		for (int i = 0; i < width; i++){
			for (int j = 0; j < height; j++){
				changes[i*height+j] = new Change(i,j,red);
			}
		}

		SwingUtilities.invokeAndWait(new Runnable() {
			public void run() {
				Display display = new BufferedCanvas(300, 200);
				display.updatePixels(changes);
			}
		});


		Thread.sleep(1000);

		Robot r = new Robot();
		Color color = r.getPixelColor(100, 100);

		Assert.assertEquals(Color.red, color);

	}

}
