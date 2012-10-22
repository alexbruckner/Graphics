package com.bru.graphics.display;

import java.io.File;
import java.io.IOException;
import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * User: Alex
 * Date: 07/10/12
 * Time: 12:03
 */
public class VolatileComponentTest {

	@Test
	public void testVolatileComponent() throws IOException, InterruptedException {
		test(new VolatileComponent(800, 600));
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
