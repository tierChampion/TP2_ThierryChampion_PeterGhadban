package ca.qc.bdeb.inf203.superMeduse;

import ca.qc.bdeb.inf203.superMeduse.gameObjects.Bubble;
import ca.qc.bdeb.inf203.superMeduse.gameObjects.Jellyfish;
import ca.qc.bdeb.inf203.superMeduse.gameObjects.platforms.*;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Random;

public class Game {

    private final double WINDOW_WIDTH;
    private final double WINDOW_HEIGHT;

    private Scene game;
    private GraphicsContext context;
    private Text score;

    private Camera camera;
    private Jellyfish player;
    // bubbles
    private ArrayList<GamePlatform> platforms;
    private ArrayList<Bubble> bubbles;
    private final double[] highestPlatform = new double[] {0};
    private Random rng;
    private AnimationTimer timer;

    public Game(double windowW, double windowH) {

        WINDOW_WIDTH = windowW;
        WINDOW_HEIGHT = windowH;
        rng = new Random();
        init();

        this.timer = new AnimationTimer() {

            private long lastTime = 0;

            @Override
            public void handle(long now) {

                if (lastTime == 0) {
                    lastTime = now;
                    return;
                }

                double deltaTime = (now - lastTime) * 1e-9;
                // Background
                context.setFill(Color.DARKBLUE);
                context.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

                // Updating
                player.manageInputs();
                player.update(deltaTime);
                for (GamePlatform p : platforms) {
                    p.update(deltaTime);
                    player.touchPlatform(p);
                }
                camera.update(deltaTime);
                camera.adjustUpwards(player);

                // Rendering

                for (GamePlatform p : platforms) {
                    p.render(context, camera);
                }
                player.render(context, camera);

                // Out of bounds
                int p = 0;
                while (p < platforms.size()) {
                    if (camera.isNotVisible(platforms.get(p))) {
                        platforms.remove(p);
                    } else {
                        p++;
                    }
                }
                /*
                int b = 0;
                while (b < bubbles.size()) {
                    if (camera.isNotVisible(bubbles.get(b))) {
                        bubbles.remove(b);
                    } else {
                        b++;
                    }
                }
                */
                if (camera.getY() < highestPlatform[0] + 10) addPlatform(); // platform width

                if (camera.isNotVisible(player)) endGame();
                score.setText(-(int)camera.getY() + "px");
                lastTime = now;
            }
        };
    }

    public void startGame(Stage stage) {
        stage.setScene(game);
        timer.start();
    }

    public void endGame() {
        timer.stop();
    }

    private void init() {
        camera = new Camera(WINDOW_HEIGHT, 0, 0, -2);
        player = new Jellyfish((WINDOW_WIDTH - 50) / 2, WINDOW_HEIGHT - 150,
                0, 0, 0, 50, 50, WINDOW_WIDTH, Color.RED);
        // Platform management
        highestPlatform[0] = WINDOW_HEIGHT - 200;
        platforms = new ArrayList<>();
        platforms.add(new SimplePlatform((WINDOW_WIDTH - 175) / 2, WINDOW_HEIGHT - 100,
                0, 0,
                0, 0,
                175, WINDOW_WIDTH));
        addPlatform();
        addPlatform();
        addPlatform();

        gameScene();
    }

    private void addPlatform() {
        double type = rng.nextDouble();
        double width = rng.nextDouble() * (175 - 85) + 85;
        double x = rng.nextDouble() * (WINDOW_WIDTH - width);

        if (type < 0.5) {
            platforms.add(new SimplePlatform(
                    x,
                    highestPlatform[0],
                    0, 0,
                    0, 0,
                    width, WINDOW_WIDTH));
        } else if (type < 0.7) {
            platforms.add(new MovingPlatform(
                    x,
                    highestPlatform[0],
                    0, 0,
                    0, 0,
                    width, WINDOW_WIDTH));
        } else if (type < 0.85) {
            platforms.add(new BouncyPlatform(
                    x,
                    highestPlatform[0],
                    0, 0,
                    0, 0,
                    width, WINDOW_WIDTH));
        } else {
            platforms.add(new TemporaryPlatform(
                    x,
                    highestPlatform[0],
                    0, 0,
                    0, 0,
                    width, WINDOW_WIDTH));
        }

        highestPlatform[0] -= 100;
    }

    private void addBubbles(){
        double baseX, bubbleX, bubbleVY, bubbleDiameter;

        for (int i =0; i<3; i++){
            baseX = rng.nextDouble()*(WINDOW_WIDTH-40);
            for (int j = 0; j<5; j++){
                bubbleX = (rng.nextDouble()*40)-20+baseX;
                bubbleVY = (rng.nextDouble()*100)+350;
                bubbleDiameter = (rng.nextDouble()*30)+10;
                bubbles.add(new Bubble(bubbleX,0, bubbleVY,bubbleDiameter, WINDOW_WIDTH));
            }
        }

    }
    /**
     * Creates the scene used to play the game.
     * @return graphics context of the canvas of the scene
     */
    private void gameScene() {
        var root = new StackPane();
        game = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        var canvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT);
        this.score = new Text(-(int)camera.getY() + "px");
        score.setFill(Color.WHITE);
        score.setFont(Font.font(40));
        root.getChildren().addAll(canvas, score);
        root.setAlignment(Pos.TOP_CENTER);

        game.setOnKeyPressed((e) -> {
            if (e.getCode() == KeyCode.ESCAPE) Platform.exit();
            else {
                Input.setKeyPressed(e.getCode(), true);
            }
        });

        game.setOnKeyReleased((e) -> {

            Input.setKeyPressed(e.getCode(), false);
        });

        context = canvas.getGraphicsContext2D();
    }
}
