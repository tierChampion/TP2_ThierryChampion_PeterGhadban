package ca.qc.bdeb.inf203.superMeduse.gameObjects.platforms;

import ca.qc.bdeb.inf203.superMeduse.gameObjects.GameObject;
import ca.qc.bdeb.inf203.superMeduse.gameObjects.Jellyfish;
import javafx.scene.paint.Color;

public abstract class GamePlatform extends GameObject {

    private static final double PLATFORM_THICKNESS = 10;

    public GamePlatform(double x, double y,
                        double vx, double vy,
                        double ax, double ay,
                        double w,
                        double maxX, Color color) {
        super(x, y, vx, vy, ax, ay, w, PLATFORM_THICKNESS, maxX, color);
    }

    public  void effect(Jellyfish jelly) {
        jelly.setGrounded(true);
        jelly.setY(this.y - jelly.getHeight());
        jelly.setVy(0);
    }

}
