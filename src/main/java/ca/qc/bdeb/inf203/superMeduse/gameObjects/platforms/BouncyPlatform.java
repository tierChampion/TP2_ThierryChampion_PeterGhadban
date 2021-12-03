package ca.qc.bdeb.inf203.superMeduse.gameObjects.platforms;

import ca.qc.bdeb.inf203.superMeduse.gameObjects.Jellyfish;
import javafx.scene.paint.Color;

public class BouncyPlatform extends GamePlatform {

    private static final double BOUNCY_STRENGTH = -100;

    public BouncyPlatform(double x, double y,
                          double w){
        super(x, y, w, Color.LIGHTGREEN);
    }

    /**
     * Makes the jellyfish bounce of the platform
     * @param jelly jellyfish to apply the effect on
     */
    public void effect(Jellyfish jelly){
        jelly.setVy(Math.min(jelly.getVy() * -1.5, BOUNCY_STRENGTH));
    }

}
