package model;

import java.awt.geom.Point2D;

import ammo.Ammo;

//KAAVA:  range=lähtönopeus^2/painovoima*sin(2*kulma)

public class Catapult {

    public int position;
    private Ammo ammo;
    private boolean shooting;
    private double angle;
    private int armLength;
    private Point2D middle;
    public Point2D arm;
    private double stopAngle;

    public Catapult(int position, Ammo ammo) {
        this.position = position;
        this.ammo = ammo;
        this.reset();
    }

    public void reset() {
        this.shooting = false;
        this.angle = 180.0;
        this.armLength = 38;
        this.middle = new Point2D.Double(50, 563);
        this.arm = new Point2D.Double(
            this.armLength*Math.cos(Math.toRadians(this.angle)) + 50, -this.armLength*Math.sin(Math.toRadians(this.angle)) + 563);
        this.stopAngle = 90;
    }

    public void shoot(double angle) {
        this.shooting = true;
        this.stopAngle = angle;
    }
    
    public void update(double time){
        Point2D ammoCenter = new Point2D.Double(this.ammo.getCircle().getCenterX(), this.ammo.getCircle().getCenterY());
        if (this.shooting && this.arm.distance(ammoCenter) <= this.ammo.radius) {
            this.angle -= (1 + this.arm.distance(this.middle)/20.0)*time/8.0;
            this.arm = new Point2D.Double(
                this.armLength*Math.cos(Math.toRadians(this.angle)) + 50, -this.armLength*Math.sin(Math.toRadians(this.angle)) + 563);
            this.ammo.position.setLocation(this.arm.getX() - this.ammo.radius, this.arm.getY() - this.ammo.radius);
            this.ammo.velocity.setLocation(0, 0);
            if (this.angle < this.stopAngle) {
                this.shooting = false;
                this.ammo.enableGravity();
                Point2D startingPos = new Point2D.Double(
                    this.armLength*Math.cos(Math.toRadians(180)) + 50, -this.armLength*Math.sin(Math.toRadians(180)) + 563);
                Point2D velocity = new Point2D.Double(startingPos.distance(this.arm)*Math.cos(Math.toRadians(this.angle - 90)),
                    -2*startingPos.distance(this.arm)*Math.sin(Math.toRadians(this.angle - 90)));
                this.ammo.scaleAddAssign(this.ammo.velocity, 0.4, velocity);
            }
        }
    }
}
