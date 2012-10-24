package com.bru.graphics.display;

import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.VolatileImage;
import java.util.HashMap;

public class VolatileComponent extends Component implements Display {

    private Sprite[] sprites;

    public VolatileComponent(int width, int height) {
        Frame f = new Frame();
        f.setSize(width, height);
        f.add(this);
        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        f.setVisible(true);
        f.setBackground(Color.BLACK);
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
                    int p_x = sprite.x + x;
                    int p_y = sprite.y + y;
                    gBB.drawLine(p_x, p_y, p_x, p_y);

                    x++;
                    if (x == sprite.width) {
                        x = 0;
                        y++;
                    }
                }
            }
        }
    }

    private final HashMap<Integer, Color> cachedColors = new HashMap<Integer, Color>();
    private Color getColorFromARGB(int argb) {
        Color color = cachedColors.get(argb);
        if (color == null) {
            int r = (argb) & 0xFF;
            int g = (argb >> 8) & 0xFF;
            int b = (argb >> 16) & 0xFF;
            int a = (argb >> 24) & 0xFF;
            color =  new Color(r, g, b, a);
            cachedColors.put(argb, color);
        }
        return color;
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
