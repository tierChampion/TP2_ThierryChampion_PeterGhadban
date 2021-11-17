package ca.qc.bdeb.inf203.superMeduse.gameObjects;

import ca.qc.bdeb.inf203.superMeduse.Camera;
import ca.qc.bdeb.inf203.superMeduse.Input;
import ca.qc.bdeb.inf203.superMeduse.gameObjects.platforms.GamePlatform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

import java.util.HashMap;

public class Jellyfish extends GameObject {

    private static final double GRAVITY = 1200;
    private static final double LOW_SPEED = 50;
    private static final double HIGH_SPEED = 800;
    private static final HashMap<Integer, Image> IMAGE_BANK = new HashMap<>();
    private static final int FRAME_PER_IMAGE = 8;
    private static final int IMAGE_COUNT = 6;

    private int counter = 0;
    private int phase = 1;
    private int direction = 1;
    private boolean isGrounded = true;

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
            phase = (phase + 1) % IMAGE_COUNT + 1;
            counter = 0;
        }

    }

    @Override
    public void touchWall() {
        super.touchWall();
        ax *= -1;
        direction *= -1;
    }

    public void touchPlatform(GamePlatform p) {
        if (vy > 0) {
            if (x < p.x + p.width && x + width > p.x &&
            y + height > p.y && y + (vy > HIGH_SPEED ? 0 : height) < p.y + p.height) {
                p.effect(this);
                isGrounded = true;
            }
        }
    }

    public void manageInputs() {

        if ((Input.isKeyPressed(KeyCode.UP) || Input.isKeyPressed(KeyCode.SPACE)) && isGrounded) { // add isGrounded
            vy = -600; // jump
            isGrounded = false;
        }

        if (Input.isKeyPressed(KeyCode.LEFT)) {
            ax = -1200; // left move
            direction = -1;
        } else if (Input.isKeyPressed(KeyCode.RIGHT)) {
            ax = 1200; // right move
            direction = 1;
        } else if (Math.abs(vx) < LOW_SPEED) {
            ax = 0;
            vx = 0;
        } else {
            ax = (vx > 0) ? -1200 : 1200;
        }
    }

    @Override
    public void render(GraphicsContext context, Camera camera) {

        context.drawImage(IMAGE_BANK.get(phase * direction), camera.getScreenX(x), camera.getScreenY(y),
                width, height);
    }

    public static void buildBank() {

        for (int i = 1; i <= IMAGE_COUNT; i++) {

            IMAGE_BANK.put(i, new Image("meduse" + i + ".png"));
            IMAGE_BANK.put(-i, new Image("meduse" + i + "-g.png"));
        }
    }

    public void setGrounded(boolean grounded) {
        isGrounded = grounded;
    }

}
