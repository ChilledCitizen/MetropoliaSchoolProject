package wall;

import enemy.Enemy;

public class Wall {

    Enemy vihu = new Enemy(600);
    public double HP;
    public double height;
    public double width;
    public double position;

    public Wall(double position, double width, double height, double HP) {
        this.position = position;
        this.width = width;
        this.height = height;
        this.HP = HP;
        
        
        
    }

    public boolean checkHit() {
        if (vihu.getPosition() == position) {
            return true;
        }
        return false;
    }
//?????

    public double takeDamage(double damage) {

         HP -= damage;
         return HP;
    }
}
