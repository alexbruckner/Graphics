package com.bru.graphics.game;

import com.bru.graphics.display.BufferedCanvas;
import com.bru.graphics.display.Display;
import com.bru.graphics.display.Sprite;
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
	public void testBufferedCanvas() throws IOException, InterruptedException {
		test(new BufferedCanvas(800, 600));
	}

	public void test(Display display) throws IOException, InterruptedException {
		Sprite test = new Sprite(new File("src/test/resources/Clear4j_trans.png"));
		Sprite test2 = new Sprite(new File("src/test/resources/Clear4j_trans.png"));
		Thread.sleep(1000);
		for (int i = 0; i < 400; i++){
			test.x = test.y = i;
			display.draw(test, test2);
			Thread.sleep(2);
		}
		Thread.sleep(5000);
	}
}
