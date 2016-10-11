package org.paidaki.scrambledwords.gui.dialogs;

import javafx.scene.effect.BoxBlur;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class OpenFileDialog {

    private FileChooser fileChooser;

    public OpenFileDialog() {
        fileChooser = new FileChooser();

        fileChooser.setInitialDirectory(new File(System.getProperty("user.home") + File.separator + "Desktop"));
        fileChooser.setTitle("Open");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Dictionary Files (*.dic)", "*.dic"),
                new FileChooser.ExtensionFilter("Wordlist Files (*.lst)", "*.lst"),
                new FileChooser.ExtensionFilter("Text Files (*.txt)", "*.txt")
        );
    }

    public File show(Stage window) {
        File selectedFile;

        window.getScene().getRoot().setEffect(new BoxBlur());
        selectedFile = fileChooser.showOpenDialog(window);
        window.getScene().getRoot().setEffect(null);

        return selectedFile;
    }
}
