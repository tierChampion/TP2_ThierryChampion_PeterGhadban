package ca.qc.bdeb.inf203.superMeduse.gameObjects;

import ca.qc.bdeb.inf203.superMeduse.Camera;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public abstract class GameObject {

    // Physics
    protected double x, y;
    protected double vx, vy;
    protected double ax, ay;
    protected double width, height;
    protected double minX = 0, maxX;
    // Visual
    protected Color color;

    public GameObject(double x, double y,
                      double vx, double vy,
                      double ax, double ay,
                      double w, double h,
                      double maxX, Color color) {

        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.ax = ax;
        this.ay = ay;
        width = w;
        height = h;
        this.maxX = maxX;
        this.color = color;
    }

    /**
     * Calculates how the object moves around
     * @param deltaTime elapsed time
     */
    public void update(double deltaTime) {

        vx += ax * deltaTime;
        vy += ay * deltaTime;
        x += vx * deltaTime;
        y += vy * deltaTime;
    }

    /**
     * Describes how the object is shown on the canvas
     * @param context
     * @param camera
     */
    public void render(GraphicsContext context, Camera camera, boolean debug) {

        context.setFill(color);
        context.fillRect(x, camera.getScreenY(y), width, height);
    }

    // Getters and setters
    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public void setY(double y) {
        this.y = y;
    }

    public double getVx() {
        return vx;
    }
    public double getVy() {
        return vy;
    }
    public void setVy(double vy) {
        this.vy = vy;
    }

    public double getAx() {
        return ax;
    }
    public double getAy() {
        return ay;
    }

    public double getHeight() {
        return height;
    }
}
