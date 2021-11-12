package ca.qc.bdeb.inf203.superMeduse.gameObjects.platforms;

import javafx.scene.paint.Color;

public class SimplePlatform extends Platform{
    public SimplePlatform(double x, double y,
                          double vx, double vy,
                          double ax, double ay,
                          double w, double h,
                          double maxX){
        super(x, y, vx, vy, ay, ax, w, h, maxX, Color.ORANGE);
    }

}
