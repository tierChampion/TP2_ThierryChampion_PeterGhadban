package ca.qc.bdeb.inf203.superMeduse.gameObjects;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.HashMap;

public class Jellyfish extends GameObject {

    private static final double GRAVITY = 1200;

    private static final HashMap<Integer, Image> IMAGE_BANK = new HashMap<>();
    private static final int FRAME_PER_IMAGE = 8;
    private static final int IMAGE_COUNT = 6;

    private int counter = 0;
    private int phase = 0;
    private int direction = 0;

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
        if (counter % FRAME_PER_IMAGE == 0) {
            phase = (phase + 1) % IMAGE_COUNT;
        }
    }

    @Override
    public void render(GraphicsContext context) {
        context.drawImage(IMAGE_BANK.get(phase + direction * IMAGE_COUNT), x, y, width, height);
    }

    public static void buildBank() {

        for (int i = 0; i < 6; i++) {

            IMAGE_BANK.put(i, new Image("meduse" + (i + 1) + ".png"));
            IMAGE_BANK.put(i + 6, new Image("meduse" + (i + 1) + "-g.png"));
        }
    }
}
