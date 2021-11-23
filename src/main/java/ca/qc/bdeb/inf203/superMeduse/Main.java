package ca.qc.bdeb.inf203.superMeduse;

import ca.qc.bdeb.inf203.superMeduse.gameObjects.Jellyfish;
import ca.qc.bdeb.inf203.superMeduse.gameObjects.platforms.*;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Random;

public class Main extends Application {

    /*
    TODO:
    - Move game controller in class Game. This includes both the animation timer and giving some elements of the
    scene to the class (graphicsContext and score)
    - Add death
    - Add bubbles
    - Make the score scene work with a result file.
    - Make a second result scene with a way to add your score with your name
    - Make the score board editable to add your own record (or add a button)
    ** Probably keep the scores in a simple hashmap so they are sorted and then on load and on close work with the files
     */

    private static final int WINDOW_WIDTH = 350;
    private static final int WINDOW_HEIGHT = 480;

    private static Scene GAME;
    private static Scene HOME;
    private static Scene SCORE;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        // FAIRE RESOURCE MANAGER
        Jellyfish.buildBank();

        var context = gameScene();
        homePageScene(stage);
        scoreScene(stage);

        // Game logic creation
        Random rng = new Random();
        Camera camera = new Camera(WINDOW_HEIGHT, 0, 0, 0, -2);
        Jellyfish jellyfish = new Jellyfish((double)(WINDOW_WIDTH - 50) / 2, WINDOW_HEIGHT - 150,
                0, 0, 0, 50, 50, WINDOW_WIDTH, Color.RED);
        // Platform management
        var platforms = new ArrayList<GamePlatform>();
        platforms.add(new SimplePlatform((double)(WINDOW_WIDTH - 175) / 2, WINDOW_HEIGHT - 100,
                0, 0,
                0, 0,
                175, 10, WINDOW_WIDTH));
        addPlatform(rng, WINDOW_HEIGHT - 200, platforms);
        addPlatform(rng, WINDOW_HEIGHT - 300, platforms);
        addPlatform(rng, WINDOW_HEIGHT - 400, platforms);
        addPlatform(rng, WINDOW_HEIGHT - 500, platforms);
        final double[] nextHeight = {WINDOW_HEIGHT - 600};

        var timer = new AnimationTimer() {

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
                jellyfish.manageInputs();
                jellyfish.update(deltaTime);
                for (GamePlatform p : platforms) {
                    p.update(deltaTime);
                    jellyfish.touchPlatform(p);
                }
                camera.update(deltaTime);
                camera.adjustUpwards(jellyfish);

                // Rendering
                for (GamePlatform p : platforms) {
                    p.render(context, camera);
                }
                jellyfish.render(context, camera);

                // Out of bounds
                int p = 0;
                while (p < platforms.size()) {
                    if (camera.isNotVisible(platforms.get(p))) {
                        platforms.remove(p);
                    } else {
                        p++;
                    }
                }
                if (camera.getY() < nextHeight[0]) {
                    addPlatform(rng, nextHeight[0], platforms);
                    nextHeight[0] -= 100;
                }

                lastTime = now;
            }
        };

        timer.start();

        stage.setScene(HOME);
        stage.getIcons().add(new Image("meduse4.png"));
        stage.setTitle("Super Méduse Bros");
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Creates the scene used to play the game.
     * @return graphics context of the canvas of the scene
     */
    private static GraphicsContext gameScene() {
        var root = new StackPane();
        GAME = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        var canvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT);
        var score = new Text("SCORE GOES HERE (UPDATE IN GAME CLASS)");
        score.setFill(Color.WHITE);
        score.setFont(Font.font(40));
        root.getChildren().addAll(canvas, score);
        root.setAlignment(Pos.TOP_CENTER);

        GAME.setOnKeyPressed((e) -> {
            if (e.getCode() == KeyCode.ESCAPE) Platform.exit();
            else {
                Input.setKeyPressed(e.getCode(), true);
            }
        });

        GAME.setOnKeyReleased((e) -> {

            Input.setKeyPressed(e.getCode(), false);
        });

        return canvas.getGraphicsContext2D();
    }

    /**
     * Creates the scene representing the home page.
     * @param stage
     */
    private static void homePageScene(Stage stage) {

        // Scene and root
        var root = new StackPane();
        HOME = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

        // Canvas for background color
        var canvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT);
        var gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.DARKBLUE);
        gc.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

        // Image and buttons
        var visuals = new VBox();
        var backgroundImg = new ImageView("accueil.png");
        var toGame = new Button("Jouer!");
        toGame.setFont(Font.font(15));
        var toScores = new Button("Meilleurs scores");
        toScores.setFont(Font.font(15));

        // Events
        toGame.setOnAction((e) -> {
            stage.setScene(GAME);
        });
        toScores.setOnAction((e) -> {
            stage.setScene(SCORE);
        });

        visuals.getChildren().addAll(backgroundImg, toGame, toScores);
        visuals.setAlignment(Pos.BASELINE_CENTER);
        visuals.setSpacing(20);
        root.getChildren().addAll(canvas, visuals);
        root.setPadding(new Insets(40));
    }

    /**
     * Creates the scene used to show the high scores.
     * @see <a href="https://devstory.net/11063/javafx-listview">...</a> -> source for ListView
     * @param stage
     */
    private static void scoreScene(Stage stage) {
        var root = new VBox();
        SCORE = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

        var title = new Text("Meilleurs scores");
        title.setFont(Font.font(40));

        var scores = new ListView<>();
        // load elements from file
        //scores.getItems().addAll(new Text("ALLO"), new Text("ALLO"), new Text("ALLO"), new Text("ALLO"));

        var toHome = new Button("Retourner à l'accueil");

        toHome.setOnAction((e) -> {
            stage.setScene(HOME);
        });

        root.getChildren().addAll(title, scores, toHome);
        root.setAlignment(Pos.TOP_CENTER);
        root.setSpacing(20);
        root.setPadding(new Insets(20));
    }

    private void addPlatform(Random rng, double height, ArrayList<GamePlatform> platforms) {
        double type = rng.nextDouble();
        double width = rng.nextDouble() * (175 - 85) + 85;
        double x = rng.nextDouble() * (WINDOW_WIDTH - width);

        if (type < 0.5) {
            platforms.add(new SimplePlatform(
                    x,
                    height,
                    0, 0,
                    0, 0,
                    width, 10, WINDOW_WIDTH));
        } else if (type < 0.7) {
            platforms.add(new MovingPlatform(
                    x,
                    height,
                    0, 0,
                    0, 0,
                    width, 10, WINDOW_WIDTH));
        } else if (type < 0.85) {
            platforms.add(new BouncyPlatform(
                    x,
                    height,
                    0, 0,
                    0, 0,
                    width, 10, WINDOW_WIDTH));
        } else {
            platforms.add(new TemporaryPlatform(
                    x,
                    height,
                    0, 0,
                    0, 0,
                    width, 10, WINDOW_WIDTH));
        }
    }
}
