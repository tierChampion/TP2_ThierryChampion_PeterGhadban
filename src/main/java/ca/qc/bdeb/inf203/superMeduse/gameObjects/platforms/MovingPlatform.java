package ca.qc.bdeb.inf203.superMeduse.gameObjects.platforms;

import javafx.scene.paint.Color;

public class MovingPlatform extends GamePlatform {

    private static final Color MOVING_COLOR = Color.rgb(184, 15, 36);
    private static final double PERIOD = 2;
    private static final double AMPLITUDE = 100;

    private double totalTime;
    private double initialX;

    public MovingPlatform(double x, double y,
                          double w) {
        super(x, y, w, MOVING_COLOR);
        this.initialX = x;
    }

    /**
     * Moves from left to right in a sinusoidal way
     * @param deltaTime elapsed time
     */
    public void update(double deltaTime){
        super.update(deltaTime);
        totalTime+=deltaTime;
        x = initialX + Math.sin( PERIOD * totalTime ) * AMPLITUDE;
    }
}

