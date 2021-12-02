package ca.qc.bdeb.inf203.superMeduse;

import ca.qc.bdeb.inf203.superMeduse.gameObjects.Jellyfish;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {

    /*
    TODO:
    - Make so only the stepped on platform becomes yellow (add a parameter to platform)
    - Add inputs for escape and t (debug mode) in every scene
     */

    private static final int WINDOW_WIDTH = 350;
    private static final int WINDOW_HEIGHT = 480;

    private static Game game;
    private static ScoreBoard score;
    private static Scene home;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        // FAIRE RESOURCE MANAGER
        Jellyfish.buildBank();

        homePageScene();
        score = new ScoreBoard(WINDOW_WIDTH, WINDOW_HEIGHT, stage, home);
        game = new Game(WINDOW_WIDTH, WINDOW_HEIGHT, stage, score);

        stage.setScene(home);
        stage.getIcons().add(new Image("meduse4.png"));
        stage.setTitle("Super Méduse Bros");
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Creates the scene representing the home page.
     */
    private static void homePageScene() {

        // Scene and root
        var root = new StackPane();
        home = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

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
            game.startGame();
        });
        toScores.setOnAction((e) -> {
            score.accessScoreScene(false);
        });

        visuals.getChildren().addAll(backgroundImg, toGame, toScores);
        visuals.setAlignment(Pos.BASELINE_CENTER);
        visuals.setSpacing(20);
        root.getChildren().addAll(canvas, visuals);
        root.setPadding(new Insets(40));
    }
}
