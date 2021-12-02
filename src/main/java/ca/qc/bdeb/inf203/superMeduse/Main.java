package ca.qc.bdeb.inf203.superMeduse;

import ca.qc.bdeb.inf203.superMeduse.gameObjects.Jellyfish;
import javafx.application.Application;
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
    - Make the death change scene after 3 seconds in game (give game access to SCORE)
    - ** Create a score scene class that will take care of playing with the files and managing the score scene **
    (To see if needed)
    - Make a second score scene with an option to add your score (or make the edit bar visible in existing scene?)
    - Deal with the files
    - Add debug mode (print bunch of stuff, show the jellyfish hitbox and change the color of the platforms)
    Need to add these feature to the rendering of jellyfish and platforms
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

        homePageScene(stage);
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
     * @param stage
     */
    private static void homePageScene(Stage stage) {

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
