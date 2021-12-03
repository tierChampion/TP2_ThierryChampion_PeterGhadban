package ca.qc.bdeb.inf203.superMeduse.gameObjects.platforms;

import ca.qc.bdeb.inf203.superMeduse.Camera;
import ca.qc.bdeb.inf203.superMeduse.gameObjects.GameObject;
import ca.qc.bdeb.inf203.superMeduse.gameObjects.Jellyfish;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Represents a platform in the game.
 */
public abstract class GamePlatform extends GameObject {
    private boolean jellyOnMe;

    public static final double PLATFORM_THICKNESS = 10;

    public GamePlatform(double x, double y,
                        double w, Color color) {
        super(x, y, 0, 0, 0, 0, w, PLATFORM_THICKNESS, 0, color);
        jellyOnMe = false;
    }

    /**
     * Describes how the platform affects the jellyfish if there is a collision
     * @param jelly jellyfish to apply the effect on
     */
    public  void effect(Jellyfish jelly) {
        jelly.setGrounded(true);
        jelly.setY(this.y - jelly.getHeight());
        jelly.setVy(0);
    }

    @Override
    public void render(GraphicsContext context, Camera camera, boolean debug) {
        context.setFill(debug && jellyOnMe ? Color.YELLOW : color);
        context.fillRect(x, camera.getScreenY(y), width, height);
    }

    public void setJellyOnMe(boolean jellyOnMe) {
        this.jellyOnMe = jellyOnMe;
    }
}
