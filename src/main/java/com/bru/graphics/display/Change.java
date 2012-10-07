package com.bru.graphics.display;

/**
 * Created by IntelliJ IDEA.
 * User: Alex
 * Date: 07/10/12
 * Time: 10:46
 */
public class Change {
	public int x, y, w, h;
	public int[] colors;

	public Change(int x, int y, int w, int h, int[] colors) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.colors = colors;
	}
}
