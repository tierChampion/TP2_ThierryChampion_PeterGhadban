package ca.qc.bdeb.inf203.superMeduse.gameObjects.platforms;

import ca.qc.bdeb.inf203.superMeduse.gameObjects.Jellyfish;
import javafx.scene.paint.Color;

public class TemporaryPlatform extends GamePlatform {

    private static final double FALL_SPEED = 200;
    private boolean lastUsed = false; // is stepping on plat
    private boolean used = false; // ever stepped on plat
    private boolean dead = false; // jumped off plat

    public TemporaryPlatform(double x, double y,
                             double w){
        super(x, y, w, Color.BLACK);
    }

    @Override
    public void update(double deltaTime) {

        if (used && !lastUsed) {
            dead = true;
            vy = FALL_SPEED;
        }
        if (dead) {
            super.update(deltaTime);
        }
        lastUsed = false;
    }



    public void effect(Jellyfish jelly){
        lastUsed = true;
        used = true;
        super.effect(jelly);
    }
}
