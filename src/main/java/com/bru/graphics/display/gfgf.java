package com.bru.graphics.display;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Graphics;
import java.awt.image.MemoryImageSource;
import java.awt.image.DirectColorModel;

public class gfgf {
	public static void main(String[] args) {
		Test test = new Test();

		test.run();
	}
};

class Test extends Frame {
	private int mWidth;
	private int mHeight;
	private int[] mPixels;

	private MemoryImageSource mImgSource;
	private Image mImage;

	private Graphics mGraphics;

	public Test() {
		super("Test");

		mWidth = 1280;
		mHeight = 960;

		mPixels = new int[mHeight*mWidth];

		setUndecorated(true);
		setSize(mWidth, mHeight);
		setVisible(true);

		mImgSource = new MemoryImageSource(mWidth, mHeight, new DirectColorModel(24, 0xff0000, 0xff00, 0xff), mPixels, 0, mWidth);
		mImgSource.setAnimated(true);

		mImage = createImage(mImgSource);

		mGraphics = getGraphics();
	}

	void updateImage() {
		for(int i=0;i<mHeight;++i) {
			int c = (int)(Math.random() * 9155351.0);

			for(int j=0;j<mWidth;++j)
				mPixels[i*mWidth+j] = (c += 333);
		}
	}

	public void run() {
		long lastTime = System.currentTimeMillis();
		int currentFrame = 0;
		int lastFrame = 0;

		while(true) {
			updateImage();

			mImgSource.newPixels();
			mGraphics.drawImage(mImage, 0, 0, null);

			++currentFrame;
			long currentTime = System.currentTimeMillis();
			if(currentTime-lastTime > 1000) {
				long fps = (1000 * (currentFrame - lastFrame)) / (currentTime - lastTime);
				System.out.println("FPS: " + fps);
				lastTime = currentTime;
				lastFrame = currentFrame;
			}
		}
	}
};
