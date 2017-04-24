package enemy;

// HEAD




//=======
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javafx.scene.image.Image;

public class Enemy {
    private int position;
    private int HP;
    private int frameDuration;
    private double milliSeconds;
    private ArrayList idle;
    private ArrayList walking;
    private HashMap states;
    private String state;
    private Iterator animation;
    private Image image;

    public Enemy(int position) {
        this.position = position;
        this.HP = 100;
        this.idle = new ArrayList(15);
        this.walking = new ArrayList(10);
        String fileName;
        String gender = "male";
        for (int frame = 1; frame < 16; frame++) {
            fileName = String.format("images/zombie_animation/%s/Idle (%d).png", gender, frame);
            this.idle.add(new Image(fileName, 82, 100, true, true));
        }
        for (frame = 1; frame < 11; frame++) {
            fileName = String.format("images/zombie_animation/%s/Walk (%d).png", gender, frame);
            this.walking.add(new Image(fileName, 82, 100, true, true));
        }
        this.states = new HashMap(4);
        this.states.put("idle", this.idle);
        this.states.put("walking", this.walking);
        this.state = "walking";
        this.animation = this.walking.iterator();
        this.image = (Image) this.animation.next();
        this.milliSeconds = 0;
        this.frameDuration = 100;
    }

    public Image getImage() {
        return this.image;
    }

    public int getPosition() {
        return this.position;
    }

    private void animate(double deltaTime) {
        this.milliSeconds += deltaTime;
        if (this.milliSeconds >= this.frameDuration) {
            this.image = (Image) this.animation.next();
            this.milliSeconds -= this.frameDuration;
        }
        if (! this.animation.hasNext()) {
            this.animation = ((ArrayList) this.states.get(this.state)).iterator();
        }
    }

    public void update(double deltaTime) {
        this.animate(deltaTime);
    }
}
//>>>>>>> e76c009b935cde5c6333fb8d69526936eaa272c7
