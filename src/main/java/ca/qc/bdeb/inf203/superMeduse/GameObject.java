package ca.qc.bdeb.inf203.superMeduse;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class GameObject {

    // Physics
    private double x, y;
    private double vx, vy;
    private double ax, ay;
    private double width, height;
    private double minX = 0, maxX;
    // Visual
    private Color color;

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

        x = Math.min(Math.max(x, minX), maxX - width);

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

    public void render(GraphicsContext context) {}

}
