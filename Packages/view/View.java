package view;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import enemy.Enemy;

public class View {

    static public void drawBackground(GraphicsContext gc, double width, double height) {
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, width, height);
    }

    static public void drawAmmo(GraphicsContext gc) {
        gc.setFill(Color.RED);
        gc.fillOval(375, 275, 50, 50);
    }
    
    static public void drawWall(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.fillRect(250, 350, 25, 250); //(x, y, w, h)
    }

    static public void drawEnemy(GraphicsContext gc, Enemy enemy) {
        gc.drawImage(enemy.getImage(), enemy.getPosition(), 500);
    }
}