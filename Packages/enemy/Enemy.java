package enemy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.stream.IntStream;
import java.awt.geom.Ellipse2D;
import javafx.scene.image.Image;

public class Enemy {
    private double position;
    private int speed = 1;
    private int frameDuration = 100;
    public boolean visible = true;
    private double milliSeconds = 0;
    private static class Animations {
        public HashMap<String, ArrayList<Image>> male = new HashMap(4);
        public HashMap<String, ArrayList<Image>> female = new HashMap(4);

        public Animations() {
            ArrayList idle1 = new ArrayList<Image>(15);
            ArrayList walking1 = new ArrayList<Image>(10);
            ArrayList dead1 = new ArrayList<Image>(12);
            ArrayList attack1 = new ArrayList<Image>(8);
            ArrayList idle2 = new ArrayList<Image>(15);
            ArrayList walking2 = new ArrayList<Image>(10);
            ArrayList dead2 = new ArrayList<Image>(12);
            ArrayList attack2 = new ArrayList<Image>(8);

            this.male.put("idle", idle1);
            this.male.put("walking", walking1);
            this.male.put("dead", dead1);
            this.male.put("attack", attack1);
            this.female.put("idle", idle2);
            this.female.put("walking", walking2);
            this.female.put("dead", dead2);
            this.female.put("attack", attack2);

            HashMap<String, HashMap<String, ArrayList<Image>>> animationMap = new HashMap(2);
            animationMap.put("male", male);
            animationMap.put("female", female);
            String fmt1 = "images/zombie_animation/%s/%s (%d).png";
            String fmt2 = "images/zombie_animation/%s/%s (%d).png";

            animationMap.forEach((k, v) -> IntStream.range(1, 16).forEach(
                (i) -> v.get("idle").add(new Image(String.format(fmt1, k, "Idle", i), 82, 100, true, true))));
            animationMap.forEach((k, v) -> IntStream.range(1, 11).forEach(
                (i) -> v.get("walking").add(new Image(String.format(fmt1, k, "Walk", i), 82, 100, true, true))));
            animationMap.forEach((k, v) -> IntStream.range(1, 13).forEach(
                (i) -> v.get("dead").add(new Image(String.format(fmt1, k, "Dead", i), 100, 110, true, true))));
            animationMap.forEach((k, v) -> IntStream.range(1, 9).forEach(
                (i) -> v.get("attack").add(new Image(String.format(fmt1, k, "Attack", i), 82, 100, true, true))));
        }
    }
    private static Animations animations = new Animations();
    private String state = "walking";
    private Iterator<Image> animation;
    private Image image;
    private HashMap<String, ArrayList<Image>> states;

    public Enemy(double position) {
        this.position = position;
        Random random = new Random();
        this.states = random.nextInt() < 0 ? animations.male : animations.female;
        this.animation = this.states.get(this.state).iterator();
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
        if (! this.animation.hasNext() && (this.state.equals("dead") || this.state.equals("attack"))) {
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
        if (this.position < 125 && this.state.equals("walking")) {
            this.state = "attack";
            this.animation = this.states.get(this.state).iterator();
        }
    }
}
