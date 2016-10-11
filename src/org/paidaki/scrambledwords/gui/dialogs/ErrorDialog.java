package org.paidaki.scrambledwords.gui.dialogs;

import javafx.scene.control.Alert;
import javafx.scene.effect.BoxBlur;
import javafx.stage.Stage;

public class ErrorDialog {

    private Alert error;

    public ErrorDialog() {
        error = new Alert(Alert.AlertType.ERROR);

        error.setTitle("Error");
        error.setHeaderText(null);
    }

    public void show(Stage window, String content) {
        error.setContentText(content);

        window.getScene().getRoot().setEffect(new BoxBlur());
        error.showAndWait();
        window.getScene().getRoot().setEffect(null);
    }
}
