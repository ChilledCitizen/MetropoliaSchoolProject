package view;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class View {
    static public void drawAmmo(GraphicsContext gc) {
        gc.setFill(Color.RED);
        gc.fillOval(375, 275, 50, 50);
    }
}