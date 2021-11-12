package ca.qc.bdeb.inf203.superMeduse;

public class Camera {

    private double x, y;
    private double vy, ay;

    public Camera(double x, double y, double vy, double ay) {
        this.x = x;
        this.y = y;
        this.vy = vy;
        this.ay = ay;
    }

    public void update(double deltaTime) {
        vy += ay * deltaTime;
        y += vy * deltaTime;
    }

    public double getScreenX(double worldX) {
        return worldX - x;
    }

    public double getScreenY(double worldY) {
        return worldY - y;
    }
}
