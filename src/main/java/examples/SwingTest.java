package examples;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

public class SwingTest extends JPanel implements
        ActionListener,
        Runnable
{
    private static final long serialVersionUID = 1L;

    private BufferedImage backgroundBuffer;
    private boolean repaintbackground = true;

    private static final int initWidth = 640;
    private static final int initHeight = 480;
    private static final int radius = 25;
    private final Timer t = new Timer(20, this);
    private final Rectangle rect = new Rectangle();

    private long totalTime = 0;
    private int frames = 0;
    private long avgTime = 0;

    public static void main(String[] args) {
        EventQueue.invokeLater(new SwingTest());
    }

    public SwingTest() {
        super(true);
        this.setPreferredSize(new Dimension(initWidth, initHeight));
        this.setLayout(null);
        this.setOpaque(false);
        this.addMouseListener(new MouseHandler());
    }

    @Override
    public void run() {
        JFrame f = new JFrame("SwingTest");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.addComponentListener(new ResizeHandler());

/*      This extra Panel with GridLayout is necessary to make sure
   our content panel is properly resized with the window.*/
        JPanel p = new JPanel(new GridLayout());
        p.add(this);
        f.add(p);
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);

        createBuffer();
        t.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        long start = System.nanoTime();
        super.paintComponent(g);

        if (backgroundBuffer == null) createBuffer();
        if (repaintbackground) {

/*   Repainting the background may require complex rendering operations,
   so we don't want to do this every frame.*/
            repaintBackground(backgroundBuffer);
            repaintbackground = false;
        }

/*  Repainting the pre-rendered background buffer every frame
       seems unavoidable. Previous attempts to keep track of the
       invalidated area and repaint only that part of the background buffer
       image have failed. */
        g.drawImage(backgroundBuffer, 0, 0, null);
        repaintBall(g, backgroundBuffer, this.getWidth(), this.getHeight());
        repaintDrawTime(g, System.nanoTime() - start);
    }

    void repaintBackground(BufferedImage buffer) {
        Graphics2D g = buffer.createGraphics();
        int width = buffer.getWidth();
        int height = buffer.getHeight();

        g.clearRect(0, 0, width, height);
        for (int i = 0; i < 100; i++) {
            g.setColor(new Color(0, 128, 0, 100));
            g.drawLine(width, height, (int)(Math.random() * (width - 1)), (int)(Math.random() * (height - 1)));
        }
    }

    void repaintBall(Graphics g, BufferedImage backBuffer, int width, int height) {
        double time = 2* Math.PI * (System.currentTimeMillis() % 3300) / 3300.;
        rect.setRect((int)(Math.sin(time) * width/3 + width/2 - radius), (int)(Math.cos(time) * height/3 + height/2) - radius, radius * 2, radius * 2);

        g.setColor(Color.BLUE);
        g.fillOval(rect.x, rect.y, rect.width, rect.height);
    }

    void repaintDrawTime(Graphics g, long frameTime) {
        if (frames == 32) {avgTime = totalTime/32; totalTime = 0; frames = 0;}
        else {totalTime += frameTime; ++frames; }
        g.setColor(Color.white);
        String s = String.valueOf(avgTime / 1000000d + " ms");
        g.drawString(s, 5, 16);
    }

    void createBuffer() {
        int width = this.getWidth();
        int height = this.getHeight();

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gs = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gs.getDefaultConfiguration();
        backgroundBuffer = gc.createCompatibleImage(width, height, Transparency.OPAQUE);

        repaintbackground = true;
    }

    private class MouseHandler extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e);
            JTextField field = new JTextField("test");
            Dimension d = field.getPreferredSize();
            field.setBounds(e.getX(), e.getY(), d.width, d.height);
            add(field);
        }
    }

    private class ResizeHandler extends ComponentAdapter {

        @Override
        public void componentResized(ComponentEvent e) {
            super.componentResized(e);
            System.out.println("Resized to " + getWidth() + " x " + getHeight());
            createBuffer();
        }
    }
}
