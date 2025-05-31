package owl_toss;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

@SuppressWarnings("unused")
public class OwlToss extends JPanel {
    static float gravity = 1.1f;
    static final int WIDTH = 900;
    static final int HEIGHT = 600;
    final static float[] TERMINALVELOCITY = {1000, 1000};

    public Owl createOwl(float[] position, float[] velocity, int[] size, float roll, int id) {
        Owl owl = new Owl(position, velocity, size, roll, id);
        owls.add(owl);
        return owl;
    }

    Owl owlPlayer;
    Owl owlEnemy;
    static public ArrayList<Owl> owls = new ArrayList<>();

    Point mousePos = new Point(0, 0);
    float fps = 0f;
    JFrame frame;
    static Point location;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Owl Toss");
        OwlToss game = new OwlToss(frame);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WIDTH, HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setContentPane(game);
        frame.setVisible(true);

        game.startGameLoop();
    }

    public OwlToss(JFrame frame) {
        this.frame = frame;

        owlEnemy = createOwl(new float[]{500, HEIGHT - 80 - 35f}, new float[]{0, 0}, new int[]{70, 70}, 0, 1);
        owlPlayer = createOwl(new float[]{100, HEIGHT - 100 - 35f}, new float[]{0, 0}, new int[]{80, 80}, 0, 0);

        setFocusable(true);
        requestFocusInWindow();

        // WASD controls for player owl
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (owlPlayer != null) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_W:
                                owlPlayer.jump();
                            break;
                        case KeyEvent.VK_A:
                            owlPlayer.velocity[0] -= owlPlayer.speed;
                            break;
                        case KeyEvent.VK_D:
                            owlPlayer.velocity[0] += owlPlayer.speed;
                            break;
                    }
                }
            }
        });

        // Set up mouse input handling
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) { // only works for player owls
                Point p = e.getPoint();
                owlPlayer.MouseHeld = true;
                if (p.x > owlPlayer.position[0] && p.x < owlPlayer.position[0] + owlPlayer.size[0]
                        && p.y > owlPlayer.position[1] && p.y < owlPlayer.position[1] + owlPlayer.size[1]) {
                    owlPlayer.isTossing = true;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                owlPlayer.MouseHeld = false;
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                mousePos = e.getPoint();
            }
            @Override
            public void mouseDragged(MouseEvent e) {
                mousePos = e.getPoint();
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

                for (Owl owl : owls) {
                    owl.update(deltaTime);
                }

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

        Graphics2D g2d = (Graphics2D) g.create();

        for (Owl owl : owls) {
            owl.draw(g2d);
        }

        g.setColor(Color.black);
        g.setFont(new Font("Monospaced", Font.PLAIN, 16));

        g.drawString("FPS: " + (int) fps, 10, 20);
        g.drawString("ver: 1.1.0", 10, HEIGHT - 50);
    }
}