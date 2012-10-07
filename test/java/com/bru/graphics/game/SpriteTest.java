package com.bru.graphics.game;

import com.bru.graphics.display.BufferedCanvas;
import com.bru.graphics.display.Display;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Alex
 * Date: 07/10/12
 * Time: 12:03
 */
public class SpriteTest {

	@Test
	public void test() throws IOException, InterruptedException {
		Sprite test = new Sprite(new File("test/resources/Clear4j_trans.png"));
		Display display = new BufferedCanvas(800,600);
		Thread.sleep(1000);
		for (int i = 0; i < 400; i++){
			test.draw(display,i,i);
			Thread.sleep(2);
		}
		Thread.sleep(5000);
	}
}
