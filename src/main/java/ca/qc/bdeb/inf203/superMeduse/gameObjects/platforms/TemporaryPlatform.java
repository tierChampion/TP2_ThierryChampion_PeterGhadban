package ca.qc.bdeb.inf203.superMeduse.gameObjects.platforms;

import ca.qc.bdeb.inf203.superMeduse.gameObjects.Jellyfish;
import javafx.scene.paint.Color;

public class TemporaryPlatform extends Platform{

    private boolean lastUsed = false; // is stepping on plat
    private boolean used = false; // ever stepped on plat
    private boolean dead = false; // jumped off plat

    public TemporaryPlatform(double x, double y,
                             double vx, double vy,
                             double ax, double ay,
                             double w, double h,
                             double maxX){
        super(x, y, vx, vy, ay, ax, w, h, maxX, Color.BLACK);
    }

    @Override
    public void update(double deltaTime) {

        if (used && !lastUsed) {
            dead = true;
            vy = 200;
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

