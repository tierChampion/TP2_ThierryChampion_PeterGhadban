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

    public boolean isNotVisible(GameObject o) {
        return o.getY() > y + HEIGHT;
    }

    public double getScreenY(double worldY) {
        return worldY - y;
    }

    public double getY() {
        return y;
    }
}
