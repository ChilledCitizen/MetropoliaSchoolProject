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
    private ArrayList<Enemy> enemyList;
    private Wall wall = new Wall(100,25,120,100);
    private Ammo ammo = new Ammo();
    private Catapult cp = new Catapult(0, ammo);
    private Canvas canvas = new Canvas(800, 600);
    private GraphicsContext gc = this.canvas.getGraphicsContext2D();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.enemyList = new ArrayList<Enemy>(10);
        for (int i = 1; i < 4; i++) {
            enemyList.add(new Enemy(400 + i*100));
        }
        createSliders();

        primaryStage.setTitle("Drawing Operations Test");
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
                    switch(event.getCode()) {
                        case SPACE:
                            if (controller.ammo.position.equals(Ammo.startingPos)
                                    && controller.ammo.velocity.equals(new Point2D.Double(0.0, 0.0))) {
                                cp.shoot(130.0);
                            }
                            break;
                        case R:
                            controller.ammo.position.setLocation(Ammo.startingPos);
                            controller.ammo.stop();
                            controller.cp.reset();
                            break;
                            
                        case ENTER: 
                            controller.ammo.position.setLocation(Ammo.startingPos);
                            controller.ammo.stop();
                            controller.wall.HP = 100;
                            controller.enemyList.clear();
                            for (int i = 0; i < 4; i++) {
                            controller.enemyList.add(new Enemy(400 + i*100));
                            }
                            controller.cp.reset();
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
        for (Enemy enemy : this.enemyList) {
            enemy.update(deltaTime);
            if (enemy.checkHit(this.ammo.getCircle())) {
                enemy.kill();
            }   
        }
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
        View.drawCatapult(this.gc, this.cp);
    }
    
    void updateWall(){
        
        
        if (this.wall.checkHit()){
            this.wall.takeDamage(100);
            //this.wall.takeDamage(100);
        }
        if (this.wall.HP <= 0){
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
