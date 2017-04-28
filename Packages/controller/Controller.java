package controller;

import java.util.ArrayList;
import java.awt.geom.Point2D;
import java.awt.geom.Ellipse2D;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.event.EventHandler;

import view.View;
import enemy.Enemy;
import wall.Wall;
import model.Catapult;
import ammo.Ammo;

public class Controller extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        ArrayList<Enemy> enemyList = new ArrayList<Enemy>(10);
        for (int i = 1; i < 4; i++) {
            enemyList.add(new Enemy(400 + i*100));
        }
        Wall wall = new Wall(100,25,120,100);
        Catapult cp = new Catapult(0);
        Ammo ammo = new Ammo();

        primaryStage.setTitle("Drawing Operations Test");
        Group root = new Group();
        Canvas canvas = new Canvas(800, 600);
        root.getChildren().add(canvas);
        Scene scene = new Scene(root);
        scene.setOnKeyPressed(
            new EventHandler<KeyEvent>()
            {
                public void handle(KeyEvent event)
                {
                    switch(event.getCode()) {
                        case SPACE:
                            if (ammo.position.equals(new Point2D.Double(100.0, 400.0))
                                    && ammo.velocity.equals(new Point2D.Double(0.0, 0.0))) {
                                ammo.enableGravity();
                                ammo.velocity.setLocation(10.0, -15.0);
                                }
                            break;
                        case R:
                            ammo.position.setLocation(100.0, 400.0);
                            ammo.stop();
                            break;
                    }
                }
            });
        primaryStage.setScene(scene);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        // Need reference to the controller for the animation timer.
        Controller controller = this;

        new AnimationTimer() {
            private long previousNanoTime;

            public void handle(long currentNanoTime) {
                if (previousNanoTime == 0) {
                    this.previousNanoTime = currentNanoTime;
                }
                // Time since previous loop in milliseconds.
                double deltaTime = (currentNanoTime - this.previousNanoTime) / 1000000.0;
                this.previousNanoTime = currentNanoTime;

                controller.updateModel(deltaTime, enemyList, ammo);
                controller.updateView(gc, canvas, enemyList, wall, ammo);
            }
        }.start();

        primaryStage.show();
    }

    void updateModel(double deltaTime, ArrayList<Enemy> enemyList, Ammo ammo) {
        ammo.timeStep(deltaTime);
        for (Enemy enemy : enemyList) {
            enemy.update(deltaTime);
            if (enemy.checkHit(ammo.getCircle())) {
                enemy.kill();
            }   
        }
    }

    void updateView(GraphicsContext gc, Canvas canvas, ArrayList<Enemy> enemyList, Wall wall, Ammo ammo) {
        View.drawBackground(gc, canvas.getWidth(), canvas.getHeight());
        for (Enemy enemy : enemyList) {
            if (enemy.visible) {
                View.drawEnemy(gc, enemy);   
            }   
        }
        View.drawAmmo(gc, ammo);
        View.drawWall(gc, (int) wall.position, 600 - (int) wall.height, wall.width, wall.height );
    }
    
    void updateWall(Wall wall){
        
        
        if (wall.checkHit() == true){
            wall.HP = wall.takeDamage(100);
            //wall.takeDamage(100);
        }
        if (wall.HP <= 0){
            System.exit(1);
        }
    }
}
