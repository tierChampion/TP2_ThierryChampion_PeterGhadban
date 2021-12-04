package ca.qc.bdeb.inf203.superMeduse;

import ca.qc.bdeb.inf203.superMeduse.gameObjects.GameObject;
import ca.qc.bdeb.inf203.superMeduse.gameObjects.Jellyfish;

public class Camera {

    private static final double ADJUSTMENT_PERCENTAGE = 0.75;
    private static final double AY = -2;

    private final double HEIGHT;
    private double y;
    private double vy;

    public Camera(double h, double y, double vy) {
        this.HEIGHT = h;
        this.y = y;
        this.vy = vy;
    }

    /**
     * Updates all physical properties depending on the elapsed time
     * @param deltaTime represents the elapsed time
     */
    public void update(double deltaTime) {
        vy += AY * deltaTime;
        y += vy * deltaTime;
    }

    /**
     * Adjusts the camera so the jelly is never at the top of the screen
     * @param jelly
     */
    public void adjustUpwards(Jellyfish jelly) {
        double maxJellyHeight = y + HEIGHT * (1 - ADJUSTMENT_PERCENTAGE);
        if (jelly.getY() < maxJellyHeight) {
            y = jelly.getY() - HEIGHT * (1 - ADJUSTMENT_PERCENTAGE);
        }
    }

    /**
     * Determines if the object is currently being shown by the camera,
     * if it is within the visual bounds
     * @param o object to test
     * @return if it isn't visible
     */
    public boolean isNotVisible(GameObject o) {
        return !(o.getY() <= y + HEIGHT) || !(o.getY() + o.getHeight() >= y);
    }

    /**
     * Transforms a given y coordinate into a corresponding screen coordinate
     * @param worldY
     * @return it's y position on the screen
     */
    public double getScreenY(double worldY) {
        return worldY - y;
    }

    // getter

    public double getY() {
        return y;
    }
}
