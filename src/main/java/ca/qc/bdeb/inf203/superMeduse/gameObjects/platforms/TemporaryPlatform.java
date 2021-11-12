package ca.qc.bdeb.inf203.superMeduse.gameObjects.platforms;

import ca.qc.bdeb.inf203.superMeduse.gameObjects.Jellyfish;
import javafx.scene.paint.Color;

public class TemporaryPlatform extends Platform{
    public TemporaryPlatform(double x, double y,
                             double vx, double vy,
                             double ax, double ay,
                             double w, double h,
                             double maxX){
        super(x, y, vx, vy, ay, ax, w, h, maxX, Color.BLACK);
    }
    public void effect(Jellyfish jelly){
    }
}
