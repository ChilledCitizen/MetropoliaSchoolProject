package ammo;

import java.awt.geom.Point2D;
import java.awt.geom.Ellipse2D;

public class Ammo {

    public final Point2D acceleration = new Point2D.Double(0, 0);
    public final Point2D position = new Point2D.Double(-3, 548);
    public final Point2D velocity = new Point2D.Double(0, 0);
    public final int radius = 15;

    public Point2D getPosition() {
        return new Point2D.Double(position.getX(), position.getY());
    }

    public Ellipse2D getCircle() {
        return new Ellipse2D.Double(position.getX(), position.getY(), this.radius*2, this.radius*2);
    }

    public void enableGravity() {
        this.acceleration.setLocation(0, 9.81 * 0.1);
    }

    public void stop() {
        this.acceleration.setLocation(0, 0);
        this.velocity.setLocation(0, 0);
    }

    public void timeStep(double deltaTime) {
        if (this.position.getY() > 600 - this.radius*2) {
            this.stop();
        }
        scaleAddAssign(this.velocity, deltaTime / 40, this.acceleration);
        scaleAddAssign(this.position, deltaTime / 40, this.velocity);
    }

    public static void scaleAddAssign(Point2D result, double multiplier, Point2D sum) {
        double x = result.getX() + multiplier * sum.getX();
        double y = result.getY() + multiplier * sum.getY();
        result.setLocation(x, y);
    }
}
