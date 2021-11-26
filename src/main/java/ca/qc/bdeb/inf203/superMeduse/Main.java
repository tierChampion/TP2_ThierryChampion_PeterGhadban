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

    private static Game game;
    private static Scene HOME;
    private static Scene SCORE;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        // FAIRE RESOURCE MANAGER
        Jellyfish.buildBank();

        game = new Game(WINDOW_WIDTH, WINDOW_HEIGHT);
        homePageScene(stage);
        scoreScene(stage);

        stage.setScene(HOME);
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
            game.startGame(stage);
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
}
