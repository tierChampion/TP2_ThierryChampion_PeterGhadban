package ca.qc.bdeb.inf203.superMeduse.gameObjects;

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

    public void update(double deltaTime) {

        vx += ax * deltaTime;
        vy += ay * deltaTime;
        x += vx * deltaTime;
        y += vy * deltaTime;

        if (x < minX || x + width > maxX) {
            touchWall();
        }
        y = Math.min(Math.max(y, 0), 480 - height);
    }

    public void touchWall() {
        x = Math.min(Math.max(x, 0), maxX - width);
        vx *= -0.9;
    }

    /**
     * Determines if this game object intersects another game object
     * @param o
     * @return if there is an intersection
     */
    public boolean intersects(GameObject o) {

        if (x < o.x + o.width || x + width > o.x) {
            if (y < o.y + o.height || y + height < o.y) {
                return true;
            }
        }

        return false;
    }

    public void render(GraphicsContext context) {

        context.setFill(color);
        context.fillRect(x, y, width, height);
    }

    public double getVy() {
        return vy;
    }
}
