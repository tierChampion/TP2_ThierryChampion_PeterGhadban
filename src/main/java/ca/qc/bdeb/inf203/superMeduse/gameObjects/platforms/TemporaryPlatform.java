package ca.qc.bdeb.inf203.superMeduse.gameObjects.platforms;

import ca.qc.bdeb.inf203.superMeduse.gameObjects.Jellyfish;
import javafx.scene.paint.Color;

public class TemporaryPlatform extends GamePlatform {

    private static final Color TEMPORARY_COLOR = Color.BLACK;

    private static final double FALL_SPEED = 200;
    private boolean lastUsed = false; // is stepping on plat
    private boolean used = false; // ever stepped on plat
    private boolean dead = false; // jumped off plat

    public TemporaryPlatform(double x, double y,
                             double w){
        super(x, y, w, TEMPORARY_COLOR);
    }

    /**
     * Updates like a normal platform except that if it was previously stepped on but is not being stepped on,
     * the platform is considered dead and will start falling
     * @param deltaTime elapsed time
     */
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

    /**
     * Acts like a regular platform but keeps in mind that the jelly fish stepped on it
     * @param jelly jellyfish to apply the effect on
     */
    public void effect(Jellyfish jelly){
        lastUsed = true;
        used = true;
        super.effect(jelly);
    }
}
