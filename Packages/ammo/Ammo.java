package ammo;

import java.awt.geom.Point2D;

public class Ammo {

    private final Point2D acceleration = new Point2D.Double(0, -9.81 * 0.1); //(x, y) y=gravity*0,1
    private final Point2D position = new Point2D.Double(100, 400);
    private final Point2D velocity = new Point2D.Double(2, 1);
    public final int radius = 10;

    public Point2D getPosition() {
        return new Point2D.Double(position.getX(), position.getY());
    }

    public void setPosition(Point2D position) {
        position.setLocation(position);
    }

    public void setVelocity(Point2D position) {
        velocity.setLocation(position);
    }

    public void timeStep(double deltaTime) {
        if (this.position.getY() > this.radius) {
            scaleAddAssign(velocity, deltaTime, acceleration);
            scaleAddAssign(position, deltaTime, velocity);
        }
    }

    private static void scaleAddAssign(Point2D result, double multiplier, Point2D sum) {
        double x = result.getX() + multiplier * sum.getX();
        double y = result.getY() + multiplier * sum.getY();
        result.setLocation(x, y);
    }
}
