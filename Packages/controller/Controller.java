package controller;

import java.util.ArrayList;
import java.awt.geom.Point2D;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyEvent;
import javafx.event.EventHandler;

import view.View;
import enemy.Enemy;
import wall.Wall;
import model.Catapult;
import ammo.Ammo;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

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
        Ammo ammo = new Ammo();
        createSliders();
        Catapult cp = new Catapult(0, ammo);

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
                            if (ammo.position.equals(new Point2D.Double(-3.0, 548.0))
                                    && ammo.velocity.equals(new Point2D.Double(0.0, 0.0))) {
                                cp.shoot(130.0);
                            }
                            break;
                        case R:
                            ammo.position.setLocation(-3.0, 548.0);
                            ammo.stop();
                            cp.reset();
                            break;
                            
                        case ENTER: 
                            ammo.position.setLocation(100.0, 400.0);
                            ammo.stop();
                            wall.HP = 100;
                            enemyList.clear();
                            for (int i = 0; i < 4; i++) {
                            enemyList.add(new Enemy(400 + i*100));
                            }
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

                controller.updateModel(deltaTime, enemyList, ammo, cp);
                controller.updateView(gc, canvas, enemyList, wall, ammo, cp);
            }
        }.start();

        primaryStage.show();
    }

    void updateModel(double deltaTime, ArrayList<Enemy> enemyList, Ammo ammo, Catapult cp) {
        cp.update(deltaTime);
        ammo.timeStep(deltaTime);
        for (Enemy enemy : enemyList) {
            enemy.update(deltaTime);
            if (enemy.checkHit(ammo.getCircle())) {
                enemy.kill();
            }   
        }
    }

    void updateView(GraphicsContext gc, Canvas canvas, ArrayList<Enemy> enemyList, Wall wall, Ammo ammo, Catapult cp) {
        View.drawBackground(gc, canvas.getWidth(), canvas.getHeight());
        for (Enemy enemy : enemyList) {
            if (enemy.visible) {
                View.drawEnemy(gc, enemy);   
            }   
        }
        View.drawAmmo(gc, ammo);
        View.drawWall(gc, (int) wall.position, 600 - (int) wall.height, wall.width, wall.height );
        View.drawCatapult(gc, cp);
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
    private void createSliders() {
        JPanel gUI = new JPanel(new GridLayout(1,0));
        
        gUI.add(new JLabel("Angle"));
        JSlider angleSlider = new JSlider(0, 90);
        gUI.add(angleSlider);
        
        gUI.add(new JLabel("Force"));
        JSlider forceSlider = new JSlider(0,100);
        gUI.add(forceSlider);
    }
}
