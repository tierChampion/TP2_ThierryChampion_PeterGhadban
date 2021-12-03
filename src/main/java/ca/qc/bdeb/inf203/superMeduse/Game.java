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
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Random;

public class Game {
    // Constants
    private final double WINDOW_WIDTH, WINDOW_HEIGHT;
    private static final double BUBBLE_CLUSTER_DELTA = 20, BUBBLE_CLUSTER_COUNT = 3, BUBBLE_CLUSTER_SIZE = 5;
    // Visual interface
    private final Stage stage;
    private Scene scene;
    private final ScoreBoard results;
    private GraphicsContext context;
    private Text score;
    private Text deathMessage;
    private Text debugInfo;
    // Game camera and game loop
    private Camera camera;
    private AnimationTimer timer;
    // Game entities
    private Jellyfish player;
    private ArrayList<GamePlatform> platforms;
    private ArrayList<Bubble> bubbles;
    // Game data variables
    private final double[] highestPlatform = new double[]{0};
    private boolean isGameDone;
    private boolean debugMode = false;
    // Utility
    private Random rng;

    public Game(double windowW, double windowH, Stage stage, ScoreBoard results) {

        WINDOW_WIDTH = windowW;
        WINDOW_HEIGHT = windowH;
        rng = new Random();
        this.stage = stage;
        this.results = results;
        gameScene();
    }

    /**
     * Used to begin playing the game in its initial state
     */
    public void startGame() {
        init();
        initGameLoop();
        stage.setScene(scene);
        timer.start();
    }

    /**
     * Used to manage the death of the player
     */
    private void endGame() {
        deathMessage.setVisible(true);
        isGameDone = true;
    }

    /**
     * Used to manage the scene change after death
     */
    private void exitGame() {
        deathMessage.setVisible(false);
        results.accessScoreScene(true);
        results.setCurrentScore(Integer.parseInt(score.getText().split("px")[0]));
        timer.stop();
    }

    /**
     * Initialises the entities and the game logic
     */
    private void init() {
        camera = new Camera(WINDOW_HEIGHT, 0, 0, -2);
        player = new Jellyfish((WINDOW_WIDTH - 50) / 2, WINDOW_HEIGHT - 150,
                0, 0, 0, 50, 50, WINDOW_WIDTH, Color.RED);
        // Platform management
        highestPlatform[0] = WINDOW_HEIGHT - 200;
        platforms = new ArrayList<>();
        platforms.add(new SimplePlatform((WINDOW_WIDTH - 175) / 2, WINDOW_HEIGHT - 100,
                175));
        addPlatform();
        addPlatform();
        addPlatform();

        bubbles = new ArrayList<>();
        isGameDone = false;
    }

    /**
     * Initialises the game loop.
     */
    private void initGameLoop() {
        this.timer = new AnimationTimer() {

            private long lastTime = 0;
            private double deathTime = 0;
            private double bubbleTime = 0;

            @Override
            public void handle(long now) {

                if (lastTime == 0) {
                    lastTime = now;
                    return;
                }

                double deltaTime = (now - lastTime) * 1e-9;

                if (isGameDone) {

                    deathTime += deltaTime;

                    if (deathTime >= 3) {
                        lastTime = 0;
                        deathTime = 0;
                        bubbleTime = 0;
                        exitGame();
                    }
                } else {
                    bubbleTime += deltaTime;
                    // Background
                    context.setFill(Color.DARKBLUE);
                    context.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

                    // Updating
                    player.manageInputs();
                    if (Input.isKeyPressed(KeyCode.T)) debugMode = !debugMode;
                    player.update(deltaTime);
                    for (GamePlatform p : platforms) {
                        p.update(deltaTime);
                        player.touchPlatform(p);
                    }
                    for (Bubble b : bubbles) {
                        b.update(deltaTime);
                    }
                    camera.update(deltaTime);
                    camera.adjustUpwards(player);



                    // Out of bounds
                    int p = 0;
                    while (p < platforms.size()) {
                        if (camera.isNotVisible(platforms.get(p))) {
                            platforms.remove(p);
                        } else {
                            p++;
                        }
                    }

                    int b = 0;
                    while (b < bubbles.size()) {
                        if (camera.isNotVisible(bubbles.get(b))) {
                            bubbles.remove(b);
                        } else {
                            b++;
                        }
                    }

                    if (camera.getY() < highestPlatform[0] + GamePlatform.PLATFORM_THICKNESS) addPlatform();
                    if (bubbleTime >= 3) {
                        addBubbles();
                        bubbleTime = 0;
                    }

                    if (camera.isNotVisible(player)) endGame();
                    updateInformation();

                    // Rendering
                    for (Bubble bubble : bubbles) bubble.render(context, camera, debugMode);

                    for (GamePlatform platform : platforms) platform.render(context, camera, debugMode);
                    player.render(context, camera, debugMode);
                }

                lastTime = now;
            }
        };
    }

    private void updateInformation() {
        score.setText(-(int) camera.getY() + "px");
        debugInfo.setText("Position = (" + player.getX() + ", " + player.getY() + ")\n" +
                "Vitesse = (" + player.getVx() + ", " + player.getVy() + ")\n" +
                "Accélération = (" + player.getAx() + ", " + player.getAy() + ")\n" +
                "Touche au sol? " + (player.getGrounded() ? "oui" : "non"));
    }

    /**
     * Adds a random platform in the game.
     */
    private void addPlatform() {
        double type = rng.nextDouble();
        double width = rng.nextDouble() * (175 - 85) + 85;
        double x = rng.nextDouble() * (WINDOW_WIDTH - width);

        if (type < 0.5) {
            platforms.add(new SimplePlatform(
                    x,
                    highestPlatform[0],
                    width));
        } else if (type < 0.7) {
            platforms.add(new MovingPlatform(
                    x,
                    highestPlatform[0],
                    width));
        } else if (type < 0.85) {
            platforms.add(new BouncyPlatform(
                    x,
                    highestPlatform[0],
                    width));
        } else {
            platforms.add(new TemporaryPlatform(
                    x,
                    highestPlatform[0],
                    width));
        }

        highestPlatform[0] -= 100;
    }

    /**
     * Adds random clusters of bubbles into the background of the game.
     */
    private void addBubbles() {

        for (int i = 0; i < BUBBLE_CLUSTER_COUNT; i++) {
            double baseX = rng.nextDouble() * (WINDOW_WIDTH);
            for (int j = 0; j < BUBBLE_CLUSTER_SIZE; j++) {
                double bubbleX = (rng.nextDouble() * 2 * BUBBLE_CLUSTER_DELTA) - BUBBLE_CLUSTER_DELTA + baseX;
                double bubbleVY = ((rng.nextDouble() * (Bubble.MAX_SPEED - Bubble.MIN_SPEED)) + Bubble.MIN_SPEED);
                double bubbleDiameter = (rng.nextDouble() * (Bubble.MAX_DIAMETER - Bubble.MIN_DIAMETER)) +
                        Bubble.MIN_DIAMETER;
                bubbles.add(new Bubble(bubbleX, camera.getY() + WINDOW_HEIGHT - bubbleDiameter / 2,
                        bubbleVY, bubbleDiameter));
            }
        }
    }

    /**
     * Creates the scene used to play the game.
     *
     * @return graphics context of the canvas of the scene
     */
    private void gameScene() {
        var root = new StackPane();
        scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        var canvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT);

        this.score = new Text();
        score.setFill(Color.WHITE);
        score.setFont(Font.font(40));

        this.deathMessage = new Text("Partie Terminée");
        deathMessage.setFill(Color.RED);
        deathMessage.setFont(Font.font(40));
        deathMessage.setVisible(false);
        var texts = new VBox(score, deathMessage);
        texts.setAlignment(Pos.TOP_CENTER);
        texts.setSpacing(150);

        debugInfo = new Text();
        debugInfo.setFill(Color.WHITE);

        root.getChildren().addAll(canvas, texts, debugInfo);
        root.setAlignment(Pos.TOP_LEFT);

        scene.setOnKeyPressed((e) -> {
            if (e.getCode() == KeyCode.ESCAPE) Platform.exit();
            else {
                Input.setKeyPressed(e.getCode(), true);
            }
        });

        scene.setOnKeyReleased((e) -> {

            Input.setKeyPressed(e.getCode(), false);
        });

        context = canvas.getGraphicsContext2D();
    }
}
