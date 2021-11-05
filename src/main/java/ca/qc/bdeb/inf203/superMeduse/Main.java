package ca.qc.bdeb.inf203.superMeduse;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        var root = new VBox();

        var scene = new Scene(root, 500, 500);

        root.getChildren().add(new Button("WOW"));

        primaryStage.setScene(scene);
        primaryStage.setTitle("Super meduse");
        primaryStage.show();
    }
}
