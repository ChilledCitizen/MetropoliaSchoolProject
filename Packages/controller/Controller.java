package controller;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;

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
        primaryStage.setTitle("Drawing Operations Test");
        Group root = new Group();
        Canvas canvas = new Canvas(800, 600);
        root.getChildren().add(canvas);
        primaryStage.setScene(new Scene(root));
        GraphicsContext gc = canvas.getGraphicsContext2D();
        // Need reference to the controller for the animation timer.
        Controller controller = this;
        Enemy enemy = new Enemy(600);
        Wall wall = new Wall(100,10,50,100);
        Catapult cp = new Catapult(0);
        Ammo ammo = new Ammo();

        new AnimationTimer() {
            private long previousNanoTime;

            public void handle(long currentNanoTime) {
                if (previousNanoTime == 0) {
                    this.previousNanoTime = currentNanoTime;
                }
                // Time since previous loop in milliseconds.
                double deltaTime = (currentNanoTime - this.previousNanoTime) / 1000000.0;
                this.previousNanoTime = currentNanoTime;

                controller.updateModel(deltaTime, enemy, ammo);
                controller.updateView(gc, canvas, enemy, ammo);
            }
        }.start();

        primaryStage.show();
    }

    void updateModel(double deltaTime, Enemy enemy, Ammo ammo) {
        enemy.update(deltaTime);
        ammo.timeStep(deltaTime);
    }

    void updateView(GraphicsContext gc, Canvas canvas, Enemy enemy, Ammo ammo) {
        View.drawBackground(gc, canvas.getWidth(), canvas.getHeight());
        View.drawEnemy(gc, enemy);
        View.drawAmmo(gc, ammo);
        View.drawWall(gc);
    }
    
    void updateWall(Wall wall){
        if (wall.HP <= 0){
            System.exit(0);
        }
        
        if (wall.checkHit(wall.position) == true){
            //wall.HP = wall.takeDamage(25)
            wall.takeDamage(25);
        }
    }
}
