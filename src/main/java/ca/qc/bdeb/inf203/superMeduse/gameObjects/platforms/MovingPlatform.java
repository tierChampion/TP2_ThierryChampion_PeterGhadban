package ca.qc.bdeb.inf203.superMeduse.gameObjects.platforms;

import javafx.scene.paint.Color;

public class MovingPlatform extends GamePlatform {
    private double totalTime;
    private double initialX;
    public MovingPlatform(double x, double y,
                          double vx, double vy,
                          double ax, double ay,
                          double w,
                          double maxX) {
        super(x, y, vx, vy, ay, ax, w, maxX, Color.rgb(184, 15, 36));
        this.initialX = x;
    }

    public void update(double deltaTime){
        super.update(deltaTime);
        totalTime+=deltaTime;
        x = initialX + Math.sin( 2 * totalTime ) * 100;
    }
}

