package ca.qc.bdeb.inf203.superMeduse.gameObjects;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.HashMap;

public class Jellyfish extends GameObject {

    private static final double GRAVITY = 1200;

    private static final HashMap<Integer, Image> RIGHT_BANK = new HashMap<>();
    private static final HashMap<Integer, Image> LEFT_BANK = new HashMap<>();

    private int counter = 0;
    private int phase = 0;

    public Jellyfish(double x, double y,
                     double vx, double vy,
                     double ax,
                     double w, double h,
                     double maxX, Color color) {
        super(x, y, vx, vy, ax, GRAVITY, w, h, maxX, color);
    }

    @Override
    public void update(double deltaTime) {
        super.update(deltaTime);
        counter++;
        if (counter % 8 == 0) {
            phase = (phase + 1) % 6;
        }
    }
}
