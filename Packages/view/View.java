package view;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import java.awt.geom.Point2D;

import enemy.Enemy;
import ammo.Ammo;
import model.Catapult;

public class View {

    static public void drawBackground(GraphicsContext gc, double width, double height) {
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, width, height);
    }

    static public void drawAmmo(GraphicsContext gc, Ammo ammo) {
        gc.setFill(Color.RED);
        gc.fillOval(ammo.getPosition().getX(), ammo.getPosition().getY(), ammo.radius*2, ammo.radius*2);
    }

    static public void drawWall(GraphicsContext gc, int x, int y, double w, double h) {
        gc.setFill(Color.BLACK);
        gc.fillRect(x, y, w, h); //(x, y, w, h)
    }

    static public void drawEnemy(GraphicsContext gc, Enemy enemy) {
        int y = 500;
        Image image = enemy.getImage();
        gc.drawImage(image, 0, 0, image.getWidth(), image.getHeight(),
            image.getWidth() + (int) enemy.getPosition() , y, -image.getWidth(), image.getHeight());
    }

    static public void drawCatapult(GraphicsContext gc, Catapult catapult) {
        gc.setFill(Color.BROWN);
        gc.fillOval(12, 575, 25, 25);
        gc.fillOval(62, 575, 25, 25);
        gc.setStroke(Color.BROWN);
        gc.setLineWidth(5);
        gc.strokeLine(25, 588, 75, 588);
        gc.strokeLine(25, 588, 50, 563);
        gc.strokeLine(50, 563, catapult.arm.getX(), catapult.arm.getY());
        gc.strokeLine(50, 563, 75, 588);
    }
    
   
}
