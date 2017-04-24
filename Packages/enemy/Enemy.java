package enemy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.awt.geom.Ellipse2D;
import javafx.scene.image.Image;

public class Enemy {
    private double position;
    private int speed;
    private int HP;
    private int frameDuration;
    public boolean visible;
    private double milliSeconds;
    private HashMap states;
    private String state;
    private Iterator animation;
    private Image image;

    public Enemy(double position) {
        this.position = position;
        this.HP = 100;
        this.speed = 1;
        this.visible = true;
        ArrayList idle = new ArrayList(15);
        ArrayList walking = new ArrayList(10);
        ArrayList dead = new ArrayList(12);
        String fileName;
        String gender = "male";
        for (int frame = 1; frame < 16; frame++) {
            fileName = String.format("images/zombie_animation/%s/Idle (%d).png", gender, frame);
            idle.add(new Image(fileName, 82, 100, true, true));
        }
        for (int frame = 1; frame < 11; frame++) {
            fileName = String.format("images/zombie_animation/%s/Walk (%d).png", gender, frame);
            walking.add(new Image(fileName, 82, 100, true, true));
        }
        for (int frame = 1; frame < 13; frame++) {
            fileName = String.format("images/zombie_animation/%s/Dead (%d).png", gender, frame);
            dead.add(new Image(fileName, 120, 110, true, true));
        }
        this.states = new HashMap(4);
        this.states.put("idle", idle);
        this.states.put("walking", walking);
        this.states.put("dead", dead);
        this.state = "walking";
        this.animation = walking.iterator();
        this.image = (Image) this.animation.next();
        this.milliSeconds = 0;
        this.frameDuration = 100;
    }

    public Image getImage() {
        return this.image;
    }

    public double getPosition() {
        return this.position;
    }

    public void checkHit(Ellipse2D ellipse) {
        if (ellipse.intersects(this.position, 500, image.getWidth(), image.getHeight())) {
            if (ellipse.getY() <= 600 - ellipse.getHeight() && ! this.state.equals("dead")) {
                this.state = "dead";
                this.animation = ((ArrayList) this.states.get(this.state)).iterator();
            }
        }
    }

    private void animate(double deltaTime) {
        this.milliSeconds += deltaTime;
        if (this.milliSeconds >= this.frameDuration) {
            this.image = (Image) this.animation.next();
            this.milliSeconds -= this.frameDuration;
            if (this.state.equals("dead")) {
            }
        }
        if (! this.animation.hasNext() && this.state.equals("dead")) {
            this.visible = false;
        }
        if (! this.animation.hasNext() && this.visible) {
            this.animation = ((ArrayList) this.states.get(this.state)).iterator();
        }
    }

    public void update(double deltaTime) {
        if (this.visible) {
            this.animate(deltaTime);   
        }
        if (this.state.equals("walking")) {
            this.position -= this.speed * deltaTime / 10;
        }
        if (this.position < 300 && this.state.equals("walking")) {
            this.state = "idle";
            this.animation = ((ArrayList) this.states.get(this.state)).iterator();
        }
    }
}
