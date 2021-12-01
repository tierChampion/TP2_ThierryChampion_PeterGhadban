package ca.qc.bdeb.inf203.superMeduse.gameObjects;

import ca.qc.bdeb.inf203.superMeduse.Camera;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Bubble extends GameObject{

    public static final double MAX_DIAMETER = 40, MIN_DIAMETER = 10;
    public static final double MAX_SPEED = -450, MIN_SPEED = -350;

    public Bubble(double x, double y,
                  double vy, double diameter){
        super(x, y, 0.00, vy, 0, 0, diameter, diameter, 0,
                Color.rgb(0, 0, 255, 0.4));
    }

    @Override
    public void render(GraphicsContext context, Camera camera) {
        context.setFill(color);
        context.fillOval(x, camera.getScreenY(y), width, height);
    }
}
