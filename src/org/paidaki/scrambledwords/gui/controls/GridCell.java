package org.paidaki.scrambledwords.gui.controls;

import com.sun.javafx.scene.traversal.Direction;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import org.paidaki.scrambledwords.util.Util;

import static org.paidaki.scrambledwords.util.Preferences.*;

public class GridCell extends TextField {

    private int row, col;
    private String change = "";

    public GridCell(int row, int col) {
        this(row, col, CELL_EMPTY);
    }

    public GridCell(int row, int col, String text) {
        super(text);
        this.row = row;
        this.col = col;

        initialize();
    }

    private void initialize() {
        this.getStyleClass().add("cell");
        this.setAlignment(Pos.CENTER);
        this.setPrefWidth(CELL_WIDTH);
        this.setPrefHeight(CELL_HEIGHT);

        this.setTextFormatter(new TextFormatter<String>(change -> {
            this.change = change.getText();
            return change;
        }));

        this.textProperty().addListener((observable, oldValue, newValue) -> {
            String newText = Util.fixWord(change);

            this.setText((newText.isEmpty() || !newText.matches("\\p{Lu}")) ? CELL_EMPTY : newText);
            this.impl_traverse(Direction.NEXT);
        });
    }

    public void reset() {
        this.setText(CELL_EMPTY);
    }

    public boolean isReady() {
        return !this.getText().equals(CELL_EMPTY);
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    @Override
    public String toString() {
        return this.getText();
    }
}
