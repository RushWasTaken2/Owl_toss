package owl_toss;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Owl {
    float[] position;
    float[] velocity;
    float roll;
    int[] size;
    float coeficient_of_restitution = 0.333f;
    boolean isTossing = false;
    boolean MouseHeld;
    float tosing_multiplyer = 0.005f;

    BufferedImage playerSprite;

    long lastTossTime = 0; // Tracks the last time the owl was tossed

    public Owl(float[] position, float[] velocity, int[] size, float roll) {
        this.position = position;
        this.velocity = velocity;
        this.roll = roll;
        this.size = size;

        try {
            playerSprite = ImageIO.read(getClass().getResource("owl-player.png"));
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
            playerSprite = null;
        }
    }

    public void update(double deltaTime) {
        velocity[1] = (float) (velocity[1] + Owl_toss.gravity * deltaTime);

        position[1] += velocity[1];
        position[0] += velocity[0];

        float floor = Owl_toss.HEIGHT - size[1] - 35f;
        if (position[1] > floor) {
            position[1] = floor;
            velocity[1] = -velocity[1] * coeficient_of_restitution;
            velocity[0] = velocity[0] * coeficient_of_restitution;
            if (Math.abs(velocity[1]) < 0.01f) {
                velocity[1] = 0;
            }
        }

        if (position[1] < 0) {
            position[1] = 0;
            velocity[1] = -velocity[1] * coeficient_of_restitution;
            velocity[0] = velocity[0] * coeficient_of_restitution;
            if (Math.abs(velocity[1]) < 0.01f) {
                velocity[1] = 0;
            }
        }
        if (position[0] < 0) {
            position[0] = 0;
            velocity[0] = -velocity[0] * coeficient_of_restitution;
            if (Math.abs(velocity[0]) < 0.01f) {
                velocity[0] = 0;
            }
        }
        if (position[0] > Owl_toss.WIDTH - size[0]) {
            position[0] = Owl_toss.WIDTH - size[0];
            velocity[0] = -velocity[0] * coeficient_of_restitution;
            if (Math.abs(velocity[0]) < 0.01f) {
                velocity[0] = 0;
            }
        }

        float[] mouseLocation = {
            (float) MouseInfo.getPointerInfo().getLocation().getX() - (float) Owl_toss.location.getX(),
            (float) MouseInfo.getPointerInfo().getLocation().getY() - (float) Owl_toss.location.getY()
        };

        // Check if the owl can be tossed
        long currentTime = System.currentTimeMillis();
        if (isTossing && !MouseHeld && (currentTime - lastTossTime >= 500)) { // 0.5 second cooldown
            isTossing = false;
            lastTossTime = currentTime; // Update the last toss time
            velocity[0] = -((mouseLocation[0]) - position[0]) * tosing_multiplyer;
            velocity[1] = -(mouseLocation[1] - position[1]) * tosing_multiplyer;
        }
    }
}