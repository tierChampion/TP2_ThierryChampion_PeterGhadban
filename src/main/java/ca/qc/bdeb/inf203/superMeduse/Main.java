package ca.qc.bdeb.inf203.superMeduse;

import ca.qc.bdeb.inf203.superMeduse.gameObjects.GameObject;
import ca.qc.bdeb.inf203.superMeduse.gameObjects.Jellyfish;
import ca.qc.bdeb.inf203.superMeduse.gameObjects.platforms.BouncyPlatform;
import ca.qc.bdeb.inf203.superMeduse.gameObjects.platforms.MovingPlatform;
import ca.qc.bdeb.inf203.superMeduse.gameObjects.platforms.SimplePlatform;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {

    private static final int WINDOW_WIDTH = 350;
    private static final int WINDOW_HEIGHT = 480;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        var root = new Pane();

        var scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

        var canvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT);
        root.getChildren().add(canvas);

        var context = canvas.getGraphicsContext2D();

        Jellyfish jellyfish = new Jellyfish(0, 0,
                0, 0, 0, 50, 50, WINDOW_WIDTH, Color.BLUE);
        Jellyfish.buildBank();

        var p = new SimplePlatform(200, 200, 0, 0, 0, 0, 200, 10, WINDOW_WIDTH);
        var p2 = new BouncyPlatform(100, 400, 0, 0, 0, 0, 200, 10, WINDOW_WIDTH);

        var timer = new AnimationTimer() {

            private long lastTime = 0;

            @Override
            public void handle(long now) {

                if (lastTime == 0) {
                    lastTime = now;
                    return;
                }

                double deltaTime = (now - lastTime) * 1e-9;
                context.setFill(Color.BLACK);
                context.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

                jellyfish.manageInputs();
                jellyfish.update(deltaTime);
                jellyfish.touchPlatform(p);
                jellyfish.touchPlatform(p2);

                jellyfish.render(context);
                p.render(context);
                p2.render(context);

                lastTime = now;
            }
        };

        scene.setOnKeyPressed((e) -> {
            if (e.getCode() == KeyCode.ESCAPE) Platform.exit();
            else {
                Input.setKeyPressed(e.getCode(), true);
            }
        });

        scene.setOnKeyReleased((e) -> {

            Input.setKeyPressed(e.getCode(), false);
        });

        timer.start();

        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image("meduse4.png"));
        primaryStage.setTitle("Super Méduse Bros");
        primaryStage.setResizable(false);
        primaryStage.show();
    }


}
