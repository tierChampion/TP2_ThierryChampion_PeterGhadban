package ca.qc.bdeb.inf203.superMeduse;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;

public class ScoreBoard {

    private static final String SAVE_FILE = "scoreboard.dat";

    private final double WINDOW_WIDTH, WINDOW_HEIGHT;
    // Visual components
    private final Stage STAGE;
    private final Scene HOME;
    private Scene scene;
    private ListView<String> leaderBoard;
    private HBox scoreEntry;
    // Data
    private int currentScore;
    private ArrayList<LeaderBoardElement> data;


    public ScoreBoard(double width, double height, Stage stage, Scene home) {
        WINDOW_WIDTH = width;
        WINDOW_HEIGHT = height;
        this.STAGE = stage;
        this.HOME = home;
        this.data = new ArrayList<>();
        scoreScene();
        updateListView();
    }

    private void scoreScene() {
        var root = new VBox();
        this.scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

        var title = new Text("Meilleurs scores");
        title.setFont(Font.font(40));

        this.leaderBoard = new ListView<>();
        loadFromFile();

        var nameTag = new Text("Nom:");
        var nameEntry = new TextField();
        var save = new Button("Sauvegarder");

        this.scoreEntry = new HBox(nameTag, nameEntry, save);

        var toHome = new Button("Retourner à l'accueil");

        var options = new VBox(scoreEntry, toHome);
        options.setSpacing(20);
        options.setAlignment(Pos.CENTER);

        toHome.setOnAction((e) -> {
            STAGE.setScene(HOME);
        });

        save.setOnAction((e) -> {
            saveRecord(nameEntry);
        });

        root.getChildren().addAll(title, leaderBoard, options);
        root.setAlignment(Pos.TOP_CENTER);
        root.setSpacing(20);
        root.setPadding(new Insets(20));

        scene.setOnKeyPressed((e) -> {
            if (e.getCode() == KeyCode.ESCAPE) Platform.exit();
        });
    }

    // https://stackoverflow.com/questions/51542291/how-to-sort-by-key-in-java
    private void saveRecord(TextField entry) {

        data.add(new LeaderBoardElement(entry.getText(), currentScore));
        data.sort(Comparator.comparing(LeaderBoardElement::getScore));

        updateListView();

        scoreEntry.setVisible(false);
        saveToFile();
    }

    private void updateListView() {
        leaderBoard.getItems().clear();
        for (int i = 0; i < data.size(); i++) {
            leaderBoard.getItems().add("#" + (i + 1) + " -- " + data.get(data.size() - (i + 1)));
        }
    }

    public void accessScoreScene(boolean onDeath) {
        STAGE.setScene(scene);
        scoreEntry.setVisible(onDeath);
    }

    public void setCurrentScore(int newScore) {
        this.currentScore = newScore;
    }

    public void loadFromFile() {

        try (FileInputStream reader = new FileInputStream(SAVE_FILE)) {

            ObjectInputStream ois = new ObjectInputStream(reader);

            this.data = (ArrayList<LeaderBoardElement>) ois.readObject();

        } catch (IOException e) {
            System.err.println("Problems occured while trying to read the data file!");
        } catch (ClassNotFoundException e) {
            System.err.println("The object saved in the data file is not of the right type!");
        }
    }

    private void saveToFile() {

        try (FileOutputStream writer = new FileOutputStream(SAVE_FILE)) {

            ObjectOutputStream oos = new ObjectOutputStream(writer);
            oos.writeObject(data);

        } catch (IOException e) {
            System.err.println("Problems occured while trying to save data to the data file!");
        }
    }

    // deal with files : save the array of leaderboard elements and load it
}
