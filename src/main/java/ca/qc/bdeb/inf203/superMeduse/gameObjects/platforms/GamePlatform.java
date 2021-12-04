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
    public static final double MAX_LENGTH = 175, MIN_LENGTH = 85;

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

    /**
     * Renders platforms
     * @param context is the context used to render the platform
     * @param camera is used to render the platform correctly in the visible area
     * @param debug is used to change the color of a platform when needed in the debug mode
     */
    @Override
    public void render(GraphicsContext context, Camera camera, boolean debug) {
        context.setFill(debug && jellyOnMe ? Color.YELLOW : color);
        context.fillRect(x, camera.getScreenY(y), width, height);
    }

    /**
     * Sets whether or not the player is on the platform
     * @param jellyOnMe is the value that jellyOnMe will become
     */
    public void setJellyOnMe(boolean jellyOnMe) {
        this.jellyOnMe = jellyOnMe;
    }
}
