package controller;

import java.util.ArrayList;
import java.util.Iterator;
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
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.util.Duration;

import view.View;
import enemy.Enemy;
import wall.Wall;
import model.Catapult;
import ammo.Ammo;
import java.io.File;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;

public class Controller extends Application {

    private static MediaPlayer wmp = new MediaPlayer(new Media(new File("sounds/tch.wav").toURI().toString()));
    private static MediaPlayer mp = new MediaPlayer(new Media(new File("sounds/scr.wav").toURI().toString()));
    private ArrayList<Enemy> enemyList = new ArrayList<Enemy>(10);
    private Wall wall = new Wall(150,25,120,100);
    private Ammo ammo = new Ammo();
    private Catapult cp = new Catapult(0, ammo);
    private Canvas canvas = new Canvas(800, 600);
    private GraphicsContext gc = this.canvas.getGraphicsContext2D();
    private int wave = 1;

    public static void main(String[] args) {
        launch(args);
    }

    private void createEnemies() {
        this.enemyList.clear();
        int enemyNum = 4 + (int) Math.floor(this.wave / 3.0);
        this.enemyList.ensureCapacity(enemyNum);
        for (int i = 0; i < enemyNum; i++) {
            this.enemyList.add(new Enemy(400 + i*(400 / enemyNum)));
        }
        for (Enemy enemy : this.enemyList) {
            enemy.setSpeed(1+ this.wave / 10.0);
        }
    }

    @Override
    public void start(Stage primaryStage) {
        createSliders();
        this.createEnemies();
        primaryStage.setTitle("Catapult Simulation");
        Group root = new Group();
        root.getChildren().add(this.canvas);
        Scene scene = new Scene(root);
        // Need reference to the controller for the animation timer and event handler.
        Controller controller = this;
        scene.setOnKeyPressed(
            new EventHandler<KeyEvent>()
            {
                public void handle(KeyEvent event)
                {
                    double angle;
                    switch(event.getCode()) {
                        case SPACE:
                            if (controller.ammo.position.equals(Ammo.startingPos)
                                    && controller.ammo.velocity.equals(new Point2D.Double(0.0, 0.0))) {
                                cp.shoot();
                                for (Enemy enemy : enemyList) {
                                    if (enemy.getState().equals("idle")) {
                                        enemy.setState("walking");
                                    }
                                }
                            }
                            break;
                        case R:
                            controller.ammo.position.setLocation(Ammo.startingPos);
                            controller.ammo.stop();
                            angle = controller.cp.stopAngle;
                            controller.cp.reset();
                            controller.cp.stopAngle = angle;
                            break;
                            
                        case ENTER:
                            boolean noneVisible = true;
                            for (Enemy enemy : controller.enemyList) {
                                if (enemy.visible) {
                                    noneVisible = false;
                                    break;
                                }
                            }
                            if (noneVisible) {
                                controller.wave++;
                                controller.ammo.position.setLocation(Ammo.startingPos);
                                controller.ammo.stop();
                                angle = controller.cp.stopAngle;
                                controller.cp.reset();
                                controller.cp.stopAngle = angle;
                                controller.createEnemies();
                            }
                            break;
                            
                        case LEFT:
                            controller.cp.stopAngle = Math.min(controller.cp.stopAngle + 5, 180);
                            break;

                        case RIGHT:
                            controller.cp.stopAngle = Math.max(controller.cp.stopAngle - 5, 90);
                            break;
                    }
                }
            });
        primaryStage.setScene(scene);

        new AnimationTimer() {
            private long previousNanoTime;

            public void handle(long currentNanoTime) {
                if (previousNanoTime == 0) {
                    this.previousNanoTime = currentNanoTime;
                }
                // Time since previous loop in milliseconds.
                double deltaTime = (currentNanoTime - this.previousNanoTime) / 1000000.0;
                this.previousNanoTime = currentNanoTime;

                controller.updateModel(deltaTime);
                controller.updateView();
            }
        }.start();

        primaryStage.show();
    }

    void updateModel(double deltaTime) {
        this.cp.update(deltaTime);
        this.ammo.timeStep(deltaTime);
        Iterator<Enemy> enemyIter = this.enemyList.iterator();
        Enemy enemy;
        while (enemyIter.hasNext()) {
            enemy =  enemyIter.next();
            enemy.update(deltaTime);
            if (enemy.checkHit(this.ammo.getCircle())) {
                enemy.kill();
                if (mp.getStatus() != Status.PLAYING || mp.getCurrentTime().greaterThanOrEqualTo(mp.getStopTime())) {
                    mp.seek(new Duration(0));
                    mp.play(); 
                } 
            }
            if (! enemy.visible && enemy.getState().equals("attack")) {
                if (wmp.getStatus() != Status.PLAYING || wmp.getCurrentTime().greaterThanOrEqualTo(wmp.getStopTime())) {
                    wmp.seek(new Duration(0));
                    wmp.play(); 
                } 
                this.wall.takeDamage(25);
                enemyIter.remove();
            }
        }
        this.updateWall();
    }

    void updateView() {
        View.drawBackground(this.gc, this.canvas.getWidth(), this.canvas.getHeight());
        for (Enemy enemy : this.enemyList) {
            if (enemy.visible) {
                View.drawEnemy(this.gc, enemy);   
            }   
        }
        View.drawAmmo(this.gc, this.ammo);
        View.drawWall(this.gc, (int) this.wall.position, 600 - (int) this.wall.height, this.wall.width, this.wall.height );
        View.drawHealthBar(this.gc, this.wall);
        View.drawCatapult(this.gc, this.cp);
        View.drawAngle(this.gc, this.cp.stopAngle);
        View.drawWave(this.gc, this.wave);
    }
    
    void updateWall(){
        
        
        if (this.wall.checkHit(this.ammo.getCircle())){
            this.ammo.velocity.setLocation(-this.ammo.velocity.getX(), this.ammo.velocity.getY());
            this.wall.takeDamage(5);
        }
        if (this.wall.HP <= 0){
            System.exit(1);
        }
    }
    void createSliders() {

        GridPane grid = new GridPane();
        
        Label angleLabel = new Label("Angle");
        Slider angleSlider = new Slider();
        angleSlider.setMin(0);
        angleSlider.setMax(90);
        angleLabel.setVisible(true);
        
        Label forceLabel = new Label("Force");
        Slider forceSlider = new Slider();
        forceSlider.setMin(0);
        forceSlider.setMax(100);
        forceSlider.setVisible(true);
                       
    }
}
