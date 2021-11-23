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
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Random;

public class Main extends Application {

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

    private static GraphicsContext gameScene() {
        var root = new Pane();
        GAME = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        var canvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT);
        root.getChildren().add(canvas);

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
        var play = new Button("Jouer!");
        play.setFont(Font.font(15));
        var scores = new Button("Meilleurs scores");
        scores.setFont(Font.font(15));

        // Events
        play.setOnAction((e) -> {
            stage.setScene(GAME);
        });
        scores.setOnAction((e) -> {
            stage.setScene(SCORE);
        });

        visuals.getChildren().addAll(backgroundImg, play, scores);
        visuals.setAlignment(Pos.BASELINE_CENTER);
        visuals.setSpacing(20);
        root.getChildren().addAll(canvas, visuals);
        root.setPadding(new Insets(40));
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
