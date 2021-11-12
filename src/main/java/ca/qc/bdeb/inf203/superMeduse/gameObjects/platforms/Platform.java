package ca.qc.bdeb.inf203.superMeduse.gameObjects.platforms;

import ca.qc.bdeb.inf203.superMeduse.gameObjects.GameObject;
import javafx.scene.paint.Color;

public abstract class Platform extends GameObject {
    public Platform(double x, double y,
                    double vx, double vy,
                    double ax, double ay,
                    double w, double h,
                    double maxX, Color color) {
        super(x, y, vx, vy, ax, ay, w, h, maxX, color);
    }

    public void collision(GameObject o) {

        

    }

}
