package enemy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.awt.geom.Ellipse2D;
import javafx.scene.image.Image;

public class Enemy {
    private double position;
    private int speed = 1;
    private int frameDuration = 100;
    public boolean visible = true;
    private double milliSeconds = 0;
    private HashMap<String, ArrayList<Image>> states = new HashMap(4);
    private String state = "walking";
    private Iterator<Image> animation;
    private Image image;

    public Enemy(double position) {
        this.position = position;
        ArrayList idle = new ArrayList<Image>(15);
        ArrayList walking = new ArrayList<Image>(10);
        ArrayList dead = new ArrayList<Image>(12);
        String fileName;
        Random random = new Random();
        String gender = random.nextInt() < 0 ? "male" : "female";
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
        this.states.put("idle", idle);
        this.states.put("walking", walking);
        this.states.put("dead", dead);
        this.animation = walking.iterator();
        this.image = this.animation.next();
    }

    public Image getImage() {
        return this.image;
    }

    public double getPosition() {
        return this.position;
    }

    public boolean checkHit(Ellipse2D ellipse) {
        return (ellipse.intersects(this.position, 500, image.getWidth(), image.getHeight())
            && ellipse.getY() <= 600 - ellipse.getHeight());
    }

    public void kill() {
        if (! this.state.equals("dead")) {
            this.state = "dead";
            this.animation = this.states.get(this.state).iterator();
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
            this.animation = this.states.get(this.state).iterator();
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
            this.animation = this.states.get(this.state).iterator();
        }
    }
}
