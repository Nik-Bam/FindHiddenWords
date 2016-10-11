package org.paidaki.scrambledwords.gui.controls;

import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import org.paidaki.scrambledwords.app.GridWord;
import org.paidaki.scrambledwords.util.GridKeyEventHandler;
import org.paidaki.scrambledwords.util.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.paidaki.scrambledwords.util.Preferences.CELL_HEIGHT;
import static org.paidaki.scrambledwords.util.Preferences.CELL_WIDTH;

public class Grid extends AnchorPane {

    private int rows, cols;
    private ArrayList<GridMarker> markers;
    private ArrayList<GridCell> cellsList;
    private GridCell[][] cellsArray;

    public Grid() {
        markers = new ArrayList<>();
        cellsList = new ArrayList<>();
        cellsArray = new GridCell[rows][cols];

        this.setDisable(true);
    }

    public boolean initialize(int rows, int cols) {
        if (this.rows == rows && this.cols == cols) {
            return false;
        }
        this.rows = rows;
        this.cols = cols;

        clearMarkers();
        clearCells();
        this.addEventFilter(KeyEvent.KEY_PRESSED, new GridKeyEventHandler());

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                GridCell cell = new GridCell(i, j);
                cellsList.add(cell);
                cellsArray[i][j] = cell;

                this.getChildren().add(cell);
                AnchorPane.setTopAnchor(cell, (double) (i * CELL_WIDTH));
                AnchorPane.setLeftAnchor(cell, (double) (j * CELL_HEIGHT));
            }
        }
        this.setMaxWidth(CELL_WIDTH * cols);
        this.setMaxHeight(CELL_HEIGHT * rows);

        return true;
    }

    public void reset() {
        cellsList.forEach(GridCell::reset);
        clearMarkers();
    }

    public boolean isReady() {
        for (GridCell cell : cellsList) {
            if (!cell.isReady()) {
                return false;
            }
        }
        return true;
    }

    public void highlightWord(List<GridCell> cells, String word) {
        if (cells == null || cells.isEmpty()) {
            return;
        }
        GridMarker marker = new GridMarker(cells, word);

        addMarker(marker);

        AnchorPane.setTopAnchor(marker, (double) (marker.getStart().getRow() * CELL_HEIGHT + CELL_HEIGHT / 6));
        AnchorPane.setLeftAnchor(marker, (double) (marker.getStart().getCol() * CELL_WIDTH + CELL_WIDTH / 6));
    }

    public void setMarkersVisible(boolean state) {
        markers.forEach(marker -> marker.setVisible(state));
    }

    public void clearMarkers() {
        this.getChildren().removeAll(markers);
        markers.clear();
    }

    public void addMarker(GridMarker marker) {
        markers.add(marker);
        this.getChildren().add(marker);
    }

    public void removeMarker(GridMarker marker) {
        markers.remove(marker);
        this.getChildren().remove(marker);
    }

    public void removeMarker(String word) {
        GridMarker marker = getMarker(word);

        if (marker != null) {
            removeMarker(marker);
        }
    }

    public GridMarker getMarker(String word) {
        for (GridMarker m : markers) {
            if (m.getWord().equals(word)) {
                return m;
            }
        }
        return null;
    }

    public void clearCells() {
        this.getChildren().removeAll(cellsList);
        cellsList.clear();
        cellsArray = new GridCell[rows][cols];
    }

    public List<GridWord> getWords(int minLength) {
        List<GridWord> words = new ArrayList<>();

        for (int i = 0; i < rows; i++) {
            extractWordsFromRegion(getRow(i), words, minLength);
        }
        for (int i = 0; i < cols; i++) {
            extractWordsFromRegion(getCol(i), words, minLength);
        }
        for (int i = minLength; i <= rows + cols - minLength; i++) {
            extractWordsFromRegion(getL2RT2BDiag(i), words, minLength);
        }
        for (int i = minLength; i <= rows + cols - minLength; i++) {
            extractWordsFromRegion(getL2RB2TDiag(i), words, minLength);
        }
        return words;
    }

    private void extractWordsFromRegion(List<GridCell> cells, List<GridWord> words, int minLength) {
        List<GridCell> reverseCells = new ArrayList<>(cells);
        Collections.reverse(reverseCells);

        for (int j = 0; j < cells.size(); j++) {
            for (int k = minLength; k <= cells.size() - j; k++) {
                words.add(new GridWord(reverseCells.subList(j, j + k), true));
                words.add(new GridWord(cells.subList(j, j + k), false));
            }
        }
    }

    private List<GridCell> getL2RT2BDiag(int diagIndex) {
        List<GridCell> diag = new ArrayList<>();
        int i = (rows - diagIndex >= 0) ? rows - diagIndex : 0;
        int j = (rows - diagIndex >= 0) ? 0 : diagIndex - rows;

        while (i < rows && j < cols) {
            diag.add(cellsArray[i++][j++]);
        }
        return diag;
    }

    private List<GridCell> getL2RB2TDiag(int diagIndex) {
        List<GridCell> diag = new ArrayList<>();
        int i = (diagIndex - 1 < rows) ? diagIndex - 1 : rows - 1;
        int j = (diagIndex - 1 < rows) ? 0 : diagIndex - rows;

        while (i >= 0 && j < cols) {
            diag.add(cellsArray[i--][j++]);
        }
        return diag;
    }

    private List<GridCell> getRow(int rowIndex) {
        List<GridCell> row = new ArrayList<>();

        row.addAll(Arrays.asList(cellsArray[rowIndex]).subList(0, cols));
        return row;
    }

    private List<GridCell> getCol(int colIndex) {
        List<GridCell> col = new ArrayList<>();

        for (int i = 0; i < rows; i++) {
            col.add(cellsArray[i][colIndex]);
        }
        return col;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public ArrayList<GridCell> getCellsList() {
        return cellsList;
    }

    public ArrayList<GridMarker> getMarkers() {
        return markers;
    }

    public GridCell[][] getCellsArray() {
        return cellsArray;
    }

    @Override
    public String toString() {
        return Util.cellsToString(cellsList);
    }
}
