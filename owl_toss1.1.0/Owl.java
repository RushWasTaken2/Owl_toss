package owl_toss;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

@SuppressWarnings("unused")
public class Owl {
    float[] position;
    float[] velocity;
    float speed = 0.5f; // speed of movement
    float roll;
    int[] size;
    float coeficientOfRestitution = 0.5f;
    boolean isTossing = false;
    boolean Tossable;
    boolean jumpable = false;
    boolean MouseHeld;
    float tosingMultiplyer = 0.005f;
    float rollingMultiplyer = 0.0006f;
    public float jump = 1.4f;
    public ArrayList<Owl> PlayerOwls = new ArrayList<>(); // for enemy ai


    public int id;// 0 = player-owl, 1 = enemy,

    BufferedImage Sprite;


    public Owl(float[] position, float[] velocity, int[] size, float roll, int id) {
        this.position = position;
        this.velocity = velocity;
        this.roll = roll;
        this.size = size;
        this.id = id;

        try {
            switch (id) {
                case 0:
                    Sprite = ImageIO.read(getClass().getResource("owl-player.png"));
                    break;
                case 1:
                    Sprite = ImageIO.read(getClass().getResource("owlEnemyPlaceholder.png"));
                    break;
            }
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
            Sprite = null;
        }
    }

    public void jump(){
        if (jumpable){
        velocity[1] = -jump;}
    }

    public void update(double deltaTime) { // logic

        if (id != 0){
            Tossable = false; 
        }

        Tossable = false;
        velocity[1] = (float) (velocity[1] + OwlToss.gravity * deltaTime);

        position[1] += velocity[1];
        position[0] += velocity[0];
        roll += velocity[0] * 2 * Math.PI * rollingMultiplyer; // roll the owl based on its horizontal velocity
        
        float floor = OwlToss.HEIGHT - size[1] - 35f;
    if (position[1] > floor) {
        position[1] = floor;
        if (id == 0) {
            Tossable = true;
            jumpable = true; // <-- allow jumping when on ground
    }
        velocity[1] = -velocity[1] * coeficientOfRestitution;
        velocity[0] = velocity[0] * coeficientOfRestitution;
        if (Math.abs(velocity[1]) < 0.01f) {
            velocity[1] = 0;
            }
        } else {
            if (id == 0) {
            jumpable = false; // <-- disable jumping in air
        }
        }

        if (position[1] < 0) {
            position[1] = 0;
            velocity[1] = -velocity[1] * coeficientOfRestitution;
            velocity[0] = velocity[0] * coeficientOfRestitution;
            if (Math.abs(velocity[1]) < 0.01f) {
                velocity[1] = 0;
            }
        }
        if (position[0] < 0) {
            position[0] = 0;
            velocity[0] = -velocity[0] * coeficientOfRestitution;
            if (Math.abs(velocity[0]) < 0.01f) {
                velocity[0] = 0;
            }
        }
        if (position[0] > OwlToss.WIDTH - size[0]) {
            position[0] = OwlToss.WIDTH - size[0];
            velocity[0] = -velocity[0] * coeficientOfRestitution;
            if (Math.abs(velocity[0]) < 0.01f) {
                velocity[0] = 0;
            }
        }

        float[] mouseLocation = {
            (float) MouseInfo.getPointerInfo().getLocation().getX() - (float) OwlToss.location.getX(),
            (float) MouseInfo.getPointerInfo().getLocation().getY() - (float) OwlToss.location.getY()
        };

        if (id == 0 && !MouseHeld && !Tossable) {
            isTossing = false;
        }

    if (id == 0 && isTossing && !MouseHeld && Tossable) {
        isTossing = false;

        float centerX = position[0] + size[0] / 2f;
        float centerY = position[1] + size[1] / 2f;

        velocity[0] = -(mouseLocation[0] - centerX) * tosingMultiplyer;
        velocity[1] = -(mouseLocation[1] - centerY) * tosingMultiplyer;
}
        
        
}
    public void draw(Graphics2D g) {
    if (Sprite != null) {
        
        AffineTransform old = g.getTransform();
        g.rotate(roll, position[0] + size[0] / 2f, position[1] + size[1] / 2f);
        g.drawImage(Sprite, (int) position[0], (int) position[1], size[0], size[1], null);
        g.setTransform(old);
    }
    for (Owl owl : OwlToss.owls) {
        if (owl.id == 0) {
            owl.PlayerOwls.add(owl);
        }
        
    }

}}
    
