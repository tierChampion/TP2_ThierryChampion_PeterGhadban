package ca.qc.bdeb.inf203.superMeduse.gameObjects.platforms;

import ca.qc.bdeb.inf203.superMeduse.gameObjects.GameObject;
import ca.qc.bdeb.inf203.superMeduse.gameObjects.Jellyfish;
import javafx.scene.paint.Color;

public abstract class GamePlatform extends GameObject {

    private static final double PLATFORM_THICKNESS = 10;

    public GamePlatform(double x, double y,
                        double w, Color color) {
        super(x, y, 0, 0, 0, 0, w, PLATFORM_THICKNESS, 0, color);
    }

    @Override
    public void touchWall() {}

    public  void effect(Jellyfish jelly) {
        jelly.setGrounded(true);
        jelly.setY(this.y - jelly.getHeight());
        jelly.setVy(0);
    }

}
