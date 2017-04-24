package ammo;

import java.awt.geom.Point2D;
import java.awt.geom.Ellipse2D;

public class Ammo {

    private final Point2D acceleration = new Point2D.Double(0, 9.81 * 0.1); //(x, y) y=gravity*0,1
    private final Point2D position = new Point2D.Double(100, 400);
    private final Point2D velocity = new Point2D.Double(10, -15);
    public final int radius = 15;

    public Point2D getPosition() {
        return new Point2D.Double(position.getX(), position.getY());
    }

    public Ellipse2D getCircle() {
        return new Ellipse2D.Double(position.getX(), position.getY(), this.radius*2, this.radius*2);
    }

    public void setPosition(Point2D position) {
        position.setLocation(position);
    }

    public void setVelocity(Point2D position) {
        velocity.setLocation(position);
    }

    public void timeStep(double deltaTime) {
        if (this.position.getY() <= 600 - this.radius*2) {
            scaleAddAssign(velocity, deltaTime / 40, acceleration);
            scaleAddAssign(position, deltaTime / 40, velocity);
        }
    }

    private static void scaleAddAssign(Point2D result, double multiplier, Point2D sum) {
        double x = result.getX() + multiplier * sum.getX();
        double y = result.getY() + multiplier * sum.getY();
        result.setLocation(x, y);
    }
}
