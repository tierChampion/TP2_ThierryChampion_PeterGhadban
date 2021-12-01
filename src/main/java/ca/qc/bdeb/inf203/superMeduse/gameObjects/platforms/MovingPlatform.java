package ca.qc.bdeb.inf203.superMeduse.gameObjects.platforms;

import javafx.scene.paint.Color;

public class MovingPlatform extends GamePlatform {

    private double totalTime;
    private double initialX;
    public MovingPlatform(double x, double y,
                          double w) {
        super(x, y, w, Color.rgb(184, 15, 36));
        this.initialX = x;
    }

    /**
     * Moves from left to right in a sinusoidal way
     * @param deltaTime elapsed time
     */
    public void update(double deltaTime){
        super.update(deltaTime);
        totalTime+=deltaTime;
        x = initialX + Math.sin( 2 * totalTime ) * 100;
    }
}

