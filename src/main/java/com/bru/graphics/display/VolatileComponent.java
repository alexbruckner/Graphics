package com.bru.graphics.display;

import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.image.VolatileImage;

public class VolatileComponent extends Component implements Display {

    private Sprite[] sprites;

    public VolatileComponent(int width, int height) {
        Frame f = new Frame();
        f.setSize(width, height);
        f.add(this);
        f.setVisible(true);
    }

    @Override
    public void draw(Sprite... sprites) {
        this.sprites = sprites;
        repaint();
    }

    private void draw(Graphics gBB) {
        if (sprites != null) {
            for (Sprite sprite : sprites) {

                int x = 0;
                int y = 0;

                for (int color : sprite.colors) {

                    gBB.setColor(getColorFromARGB(color));
                    gBB.drawLine(sprite.x + x, sprite.y + y, sprite.x + x + 1, sprite.y + y + 1);

                    x++;
                    if (x == sprite.width) {
                        x = 0;
                        y++;
                    }
                }
            }
        }
    }

    private Color getColorFromARGB(int argb) {
        int r = (argb) & 0xFF;
        int g = (argb >> 8) & 0xFF;
        int b = (argb >> 16) & 0xFF;
        int a = (argb >> 24) & 0xFF;
        return new Color(r, g, b, a);
    }

    VolatileImage backBuffer = null;

    void createBackBuffer() {
        if (backBuffer != null) {
            backBuffer.flush();
            backBuffer = null;
        }
        backBuffer = createVolatileImage(getWidth(), getHeight());
    }

    public void paint(Graphics g) {
        if (backBuffer == null) {
            createBackBuffer();
        }
        do {

            // First, we validate the back buffer
            int valCode = backBuffer.validate(getGraphicsConfiguration());
            if (valCode == VolatileImage.IMAGE_RESTORED) {
            } else if (valCode == VolatileImage.IMAGE_INCOMPATIBLE) {
                createBackBuffer();
            }
            // rendering to the back buffer:
            Graphics gBB = backBuffer.getGraphics();

            draw(gBB);

            // copy from the back buffer to the screen
            g.drawImage(backBuffer, 0, 0, this);

            // Now we are done; or are we?  Check contentsLost() and loop as necessary
        } while (backBuffer.contentsLost());
    }

}
