package wall;

import java.awt.geom.Ellipse2D;

import enemy.Enemy;

public class Wall {

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

    public boolean checkHit(Ellipse2D ellipse) {
        return (ellipse.intersects(this.position, 500, this.width, this.height));
    }

    public double takeDamage(double damage) {

         this.HP -= damage;
         return HP;
    }
}
