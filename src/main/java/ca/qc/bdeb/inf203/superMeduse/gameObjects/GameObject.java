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

    public void update(double deltaTime) {

        vx += ax * deltaTime;
        vy += ay * deltaTime;
        x += vx * deltaTime;
        y += vy * deltaTime;

        if (x < minX || x + width > maxX) {
            touchWall();
        }
    }

    public void touchWall() {
        x = Math.min(Math.max(x, 0), maxX - width);
        vx *= -0.9;
    }

    public void render(GraphicsContext context, Camera camera) {

        context.setFill(color);
        context.fillRect(camera.getScreenX(x), camera.getScreenY(y), width, height);
    }


    //Getters and Setters
    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getVy() {
        return vy;
    }

    public void setVy(double vy) {
        this.vy = vy;
    }

    public double getHeight() {
        return height;
    }
}
