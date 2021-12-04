package ca.qc.bdeb.inf203.superMeduse;

import ca.qc.bdeb.inf203.superMeduse.gameObjects.Bubble;
import ca.qc.bdeb.inf203.superMeduse.gameObjects.Jellyfish;
import ca.qc.bdeb.inf203.superMeduse.gameObjects.platforms.*;
import javafx.animation.AnimationTimer;
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
    private static final Font GAME_FONT = Font.font(40);
    private static final double GAME_SPACING = 150;
    private final double WINDOW_WIDTH, WINDOW_HEIGHT;
    private static final double BUBBLE_CLUSTER_DELTA = 20, BUBBLE_CLUSTER_COUNT = 3, BUBBLE_CLUSTER_SIZE = 5;
    private static final double BUBBLE_SPAWN_INTERVAL = 3, DEATH_TOTAL_TIME = 3;
    private static final double PLATFORM_OFFSET = 100;
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
    public Jellyfish player;
    private ArrayList<GamePlatform> platforms;
    private ArrayList<Bubble> bubbles;
    // Game data variables
    private final double[] highestPlatform = new double[]{0};
    private boolean isGameDone;
    private double deathTime = 0;
    private double bubbleTime = 0;
    private boolean debugMode = false;
    private Scene home;
    // Utility
    private Random rng;

    /**
     * Constructs the Game
     * @param windowW reprensents the Window Width
     * @param windowH represents the Window Height
     * @param stage is the stage that will be used to display the game
     * @param results represents the current scoreboard and will be used to add new scores
     * @param home is used to return to the homepage when escape is pressed
     */
    public Game(double windowW, double windowH, Stage stage, ScoreBoard results, Scene home) {

        WINDOW_WIDTH = windowW;
        WINDOW_HEIGHT = windowH;
        this.home = home;
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
     * Used to manage the scene change
     * @param onEscape if this method was accessed by the press of escape
     */
    private void exitGame(boolean onEscape) {
        deathTime = 0;
        bubbleTime = 0;
        deathMessage.setVisible(false);
        if (onEscape) stage.setScene(home);
        else results.accessScoreScene(true);
        results.setCurrentScore(Integer.parseInt(score.getText().split("px")[0]));
        timer.stop();
    }

    /**
     * Initialises the entities and the game logic
     */
    private void init() {
        camera = new Camera(WINDOW_HEIGHT, 0, 0);
        player = new Jellyfish((WINDOW_WIDTH - Jellyfish.JELLY_WIDTH) / 2,
                WINDOW_HEIGHT - PLATFORM_OFFSET - Jellyfish.JELLY_HEIGHT,
                0, 0, 0, WINDOW_WIDTH, Color.RED);
        // Platform management
        highestPlatform[0] = WINDOW_HEIGHT - 2 * PLATFORM_OFFSET;
        platforms = new ArrayList<>();
        platforms.add(new SimplePlatform((WINDOW_WIDTH - GamePlatform.MAX_LENGTH) / 2,
                WINDOW_HEIGHT - PLATFORM_OFFSET, GamePlatform.MAX_LENGTH));
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

            @Override
            public void handle(long now) {

                if (lastTime == 0) {
                    lastTime = now;
                    return;
                }
                double deltaTime = (now - lastTime) * 1e-9;

                if (isGameDone) {
                    deathTime += deltaTime;
                    if (deathTime >= DEATH_TOTAL_TIME) {
                        exitGame(false);
                    }
                } else {
                    update(deltaTime);
                    if (camera.isNotVisible(player)) endGame();
                    // Rendering
                    render();
                }
                lastTime = now;
            }
        };
    }

    /**
     * renders all visual elements in the game
     */
    private void render(){

        // Background
        context.setFill(Color.DARKBLUE);
        context.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

        //Bubbles
        for (Bubble bubble : bubbles) bubble.render(context, camera, debugMode);
        //Platforms
        for (GamePlatform platform : platforms) platform.render(context, camera, debugMode);
        player.render(context, camera, debugMode);
    }

    /**
     * Updates all moving elements in the game an removes any object that isn't visible anymore
     * @param deltaTime needed to correctly update the elements' movement properties
     */
    private void update(double deltaTime){

        bubbleTime += deltaTime;

        // Updating objects
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
        if (!debugMode)
        camera.update(deltaTime);
        camera.adjustUpwards(player);

        // Removing objects if needed
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

        // Adding objects if needed
        if (camera.getY() < highestPlatform[0] + GamePlatform.PLATFORM_THICKNESS) addPlatform();
        if (bubbleTime >= BUBBLE_SPAWN_INTERVAL) {
            addBubbles();
            bubbleTime = 0;
        }
        updateInformation();
    }

    /**
     * Updates all the informations that are displayed when in debug mode
     */
    private void updateInformation() {
        score.setText(-(int) camera.getY() + "px");
        debugInfo.setVisible(debugMode);
        debugInfo.setText("Position = (" + (int)player.getX() + ", " + (int)player.getY() + ")\n" +
                "Vitesse = (" + (int)player.getVx() + ", " + (int)player.getVy() + ")\n" +
                "Accélération = (" + (int)player.getAx() + ", " + (int)player.getAy() + ")\n" +
                "Touche au sol? " + (player.getGrounded() ? "oui" : "non"));
    }

    /**
     * Adds a random platform in the game.
     */
    private void addPlatform() {
        double type = rng.nextDouble();
        double width = rng.nextDouble() * (GamePlatform.MAX_LENGTH - GamePlatform.MIN_LENGTH) + GamePlatform.MIN_LENGTH;
        double x = rng.nextDouble() * (WINDOW_WIDTH - width);

        if (type < 0.5) { // 50% simple
            platforms.add(new SimplePlatform(
                    x,
                    highestPlatform[0],
                    width));
        } else if (type < 0.7) { // 20% moving
            platforms.add(new MovingPlatform(
                    x,
                    highestPlatform[0],
                    width));
        } else if (type < 0.85) { // 15% bouncy
            platforms.add(new BouncyPlatform(
                    x,
                    highestPlatform[0],
                    width));
        } else { // 15% temporary
            platforms.add(new TemporaryPlatform(
                    x,
                    highestPlatform[0],
                    width));
        }

        highestPlatform[0] -= PLATFORM_OFFSET;
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
        score.setFont(GAME_FONT);

        this.deathMessage = new Text("Partie Terminée");
        deathMessage.setFill(Color.RED);
        deathMessage.setFont(GAME_FONT);
        deathMessage.setVisible(false);
        var texts = new VBox(score, deathMessage);
        texts.setAlignment(Pos.TOP_CENTER);
        texts.setSpacing(GAME_SPACING);

        debugInfo = new Text();
        debugInfo.setFill(Color.WHITE);

        root.getChildren().addAll(canvas, texts, debugInfo);
        root.setAlignment(Pos.TOP_LEFT);

        scene.setOnKeyPressed((e) -> {
            if (e.getCode() == KeyCode.ESCAPE){
                exitGame(true);
            }
            else {
                Input.setKeyPressed(e.getCode(), true);
            }
        });

        scene.setOnKeyReleased((e) -> Input.setKeyPressed(e.getCode(), false));

        context = canvas.getGraphicsContext2D();
    }
}
