package view;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import java.awt.geom.Point2D;

import enemy.Enemy;
import ammo.Ammo;

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
}
