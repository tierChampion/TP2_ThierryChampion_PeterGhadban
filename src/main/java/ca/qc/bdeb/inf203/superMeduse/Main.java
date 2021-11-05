package ca.qc.bdeb.inf203.superMeduse;

import ca.qc.bdeb.inf203.superMeduse.gameObjects.GameObject;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
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

        GameObject jellyfish = new GameObject(0, 0,
                0, 0, 1200, 0, 20, 20, WINDOW_WIDTH, Color.BLUE);

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

                jellyfish.update(deltaTime);
                jellyfish.render(context);

                lastTime = now;
            }
        };

        timer.start();

        primaryStage.setScene(scene);
        primaryStage.setTitle("Super meduse");
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
