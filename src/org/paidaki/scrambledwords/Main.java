package org.paidaki.scrambledwords;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.paidaki.scrambledwords.app.ScrambledWords;
import org.paidaki.scrambledwords.gui.WindowController;

import java.io.IOException;

import static org.paidaki.scrambledwords.util.Preferences.HEIGHT;
import static org.paidaki.scrambledwords.util.Preferences.TITLE;
import static org.paidaki.scrambledwords.util.Preferences.WIDTH;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("gui/window.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        WindowController controller = loader.getController();

        scene.getStylesheets().add("org/paidaki/scrambledwords/gui/css/style.css");
        controller.setScrambledWords(new ScrambledWords());
        primaryStage.setTitle(TITLE);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
