package org.paidaki.scrambledwords.gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.paidaki.scrambledwords.app.GridWord;
import org.paidaki.scrambledwords.app.ScrambledWords;
import org.paidaki.scrambledwords.gui.controls.Grid;
import org.paidaki.scrambledwords.gui.controls.GridMarker;
import org.paidaki.scrambledwords.gui.dialogs.ErrorDialog;
import org.paidaki.scrambledwords.gui.dialogs.OpenFileDialog;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static org.paidaki.scrambledwords.util.Preferences.*;


public class WindowController {

    private OpenFileDialog openFileDialog;
    private ErrorDialog errorDialog;
    private ScrambledWords scrambledWords;
    private ObservableList<String> listMatches;
    private Grid grid;

    @FXML
    private VBox matchesContainer;
    @FXML
    private VBox mainContainer;
    @FXML
    private Button btnSearch;
    @FXML
    private TextField tfRows;
    @FXML
    private TextField tfCols;
    @FXML
    private TextField tfMinLen;
    @FXML
    public StackPane gridContainer;
    @FXML
    private CheckBox cbEditMode;
    @FXML
    private TextField tfFile;
    @FXML
    private ListView<String> lvMatches;

    private class SizeListener implements ChangeListener<Boolean> {

        private TextField field;
        private int defaultValue;

        private SizeListener(TextField field, int defaultValue) {
            this.field = field;
            this.defaultValue = defaultValue;
        }

        @Override
        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
            if (!newValue) {
                if (field.getText().isEmpty()) {
                    field.setText(String.valueOf(defaultValue));
                }
                if (grid.initialize(Integer.parseInt(tfRows.getText()), Integer.parseInt(tfCols.getText()))) {
                    btnSearch.setDisable(true);
                    listMatches.clear();
                }
            }
        }
    }

    private class LengthListener implements ChangeListener<Boolean> {

        @Override
        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
            if (!newValue) {
                int oldLength = minWordLength;

                minWordLength = tfMinLen.getText().isEmpty() ? DEFAULT_MIN_WORD_LENGTH : Integer.parseInt(tfMinLen.getText());
                tfMinLen.setText(String.valueOf(minWordLength));

                if (oldLength != minWordLength) {
                    listMatches.clear();
                    grid.clearMarkers();
                }
            }
        }
    }

    private class NumberFormatter extends TextFormatter<String> {

        private NumberFormatter() {
            super(change -> {
                String newText = change.getControlNewText();

                return newText.isEmpty() || newText.matches("0*[1-9][0-9]?") ? change : null;
            });
        }
    }

    private class EditModeListener implements ChangeListener<Boolean> {

        private String beforeEditMode = "";

        @Override
        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
            if (newValue) {
                beforeEditMode = String.valueOf(grid);
                grid.setMarkersVisible(false);
                btnSearch.setDisable(true);
            } else {
                if (String.valueOf(grid).equals(beforeEditMode)) {
                    grid.setMarkersVisible(true);
                } else {
                    listMatches.clear();
                    grid.clearMarkers();
                }
                btnSearch.setDisable(!grid.isReady());
            }
        }
    }

    @FXML
    private void initialize() {
        openFileDialog = new OpenFileDialog();
        errorDialog = new ErrorDialog();
        listMatches = FXCollections.observableArrayList();
        grid = new Grid();

        tfRows.setText(String.valueOf(DEFAULT_ROWS));
        tfRows.setTextFormatter(new NumberFormatter());
        tfRows.focusedProperty().addListener(new SizeListener(tfRows, DEFAULT_ROWS));
        tfRows.setOnAction(event -> cbEditMode.requestFocus());

        tfCols.setText(String.valueOf(DEFAULT_COLS));
        tfCols.setTextFormatter(new NumberFormatter());
        tfCols.focusedProperty().addListener(new SizeListener(tfCols, DEFAULT_COLS));
        tfCols.setOnAction(event -> cbEditMode.requestFocus());

        tfMinLen.setText(String.valueOf(DEFAULT_MIN_WORD_LENGTH));
        tfMinLen.setTextFormatter(new NumberFormatter());
        tfMinLen.focusedProperty().addListener(new LengthListener());
        tfMinLen.setOnAction(event -> cbEditMode.requestFocus());

        gridContainer.getChildren().add(grid);
        grid.initialize(Integer.parseInt(tfRows.getText()), Integer.parseInt(tfCols.getText()));
        grid.disableProperty().bind(cbEditMode.selectedProperty().not());

        cbEditMode.selectedProperty().addListener(new EditModeListener());
        cbEditMode.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                cbEditMode.setSelected(!cbEditMode.isSelected());
            }
        });

        lvMatches.setItems(listMatches);
        lvMatches.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                String selected = lvMatches.getSelectionModel().getSelectedItem();
                grid.removeMarker(selected);
                listMatches.remove(selected);
            }
        });
        lvMatches.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            GridMarker oldMarker = grid.getMarker(oldValue);
            GridMarker newMarker = grid.getMarker(newValue);

            if (oldMarker != null) oldMarker.removeGlow();
            if (newMarker != null) {
                newMarker.toFront();
                newMarker.addGlow();
            }
        });
    }

    @FXML
    private void openFile(ActionEvent event) {
        Button source = (Button) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        File file = openFileDialog.show(stage);

        if (file != null) {
            if (scrambledWords.loadDictionary(file)) {
                tfFile.setText(file.getAbsolutePath());
                mainContainer.setDisable(false);
                matchesContainer.setDisable(false);
            } else {
                errorDialog.show(stage, "Invalid Dictionary File.");
            }
        }
    }

    @FXML
    private void resetPuzzle() {
        grid.reset();
        btnSearch.setDisable(true);
        listMatches.clear();
    }

    @FXML
    private void findWords(ActionEvent event) {
        Control source = (Control) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        List<GridWord> foundWords = scrambledWords.searchWords(grid.getWords(minWordLength));

        listMatches.clear();
        grid.clearMarkers();

        if (foundWords.isEmpty()) {
            errorDialog.show(stage, "No words found.");
        } else {
            foundWords.forEach(gridWord -> {
                grid.highlightWord(gridWord.getCells(), String.valueOf(gridWord));
                listMatches.add(String.valueOf(gridWord));
            });
        }
    }

    @FXML
    private void creatorUrl() {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(new URI(GITHUB_URL));
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    public ScrambledWords getScrambledWords() {
        return scrambledWords;
    }

    public void setScrambledWords(ScrambledWords scrambledWords) {
        this.scrambledWords = scrambledWords;
    }
}
