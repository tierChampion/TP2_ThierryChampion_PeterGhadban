package ca.qc.bdeb.inf203.superMeduse;

import ca.qc.bdeb.inf203.superMeduse.gameObjects.GameObject;
import ca.qc.bdeb.inf203.superMeduse.gameObjects.Jellyfish;

public class Camera {

    private final double HEIGHT;
    private double y;
    private double vy, ay;

    public Camera(double h, double y, double vy, double ay) {
        this.HEIGHT = h;
        this.y = y;
        this.vy = vy;
        this.ay = ay;
    }

    public void update(double deltaTime) {
        vy += ay * deltaTime;
        y += vy * deltaTime;
    }

    /**
     * Adjusts the camera so the jelly is never at the top of the screen
     * @param jelly
     */
    public void adjustUpwards(Jellyfish jelly) {
        double maxJellyHeight = y + HEIGHT / 4;
        if (jelly.getY() < maxJellyHeight) {
            y = jelly.getY() - HEIGHT / 4;
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
