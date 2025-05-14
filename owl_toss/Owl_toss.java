package owl_toss;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Owl_toss extends JPanel {
    static float gravity = 0.7f;
    static final int WIDTH = 700;
    static final int HEIGHT = 600;
    final static float[] TERMINALVELOCITY = {1000, 1000};

    Owl owl;
    float fps = 0f;
    JFrame frame;
    static Point location;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Owl Toss");
        Owl_toss game = new Owl_toss(frame);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WIDTH, HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setContentPane(game);
        frame.setVisible(true);

        game.startGameLoop();
    }

    public Owl_toss(JFrame frame) {
        this.frame = frame;
        owl = new Owl(new float[]{(WIDTH / 2f) - 50, 0}, new float[]{0, 0}, new int[]{100, 100}, 0);

        // Set up mouse input handling
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point p = e.getPoint();
                if (p.x > owl.position[0] && p.x < owl.position[0] + owl.size[0]
                        && p.y > owl.position[1] && p.y < owl.position[1] + owl.size[1]) {
                    owl.isTossing = true;
                    owl.MouseHeld = true;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                owl.MouseHeld = false;
            }
        });
    }

    public void startGameLoop() {
        new Thread(() -> {
            long lastTime = System.nanoTime();

            while (true) {
                long now = System.nanoTime();
                double deltaTime = (now - lastTime) / 1_000_000_000.0;
                lastTime = now;

                fps = 1.0f / (float) deltaTime;
                location = frame.getLocationOnScreen();
                owl.update(deltaTime);
                repaint();

                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(owl.playerSprite, (int) owl.position[0], (int) owl.position[1], owl.size[0], owl.size[1], null);

        g.setColor(Color.black);
        g.setFont(new Font("Monospaced", Font.PLAIN, 16));
        g.drawString("FPS: " + (int) fps, 0, 10);
        g.drawString("ver: 1.0.0", 0, HEIGHT-45); //the Owl toss version convention is the following: major update.minor update.patch
    }
}
