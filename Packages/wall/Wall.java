package wall;

import enemy.Enemy;

public class Wall {

    Enemy vihu = new Enemy(600);
    int HP;
    double height;
    double width;
    double position;

    public Wall(double position, double width, double height, double HP) {

    }

    public boolean checkHit(double position, double height, double size) {
        if (vihu.getPosition() == position) {
            return true;
        }
        return false;
    }
//?????
    double takeDamage(double damage) {

        return HP - damage;
    }
}
