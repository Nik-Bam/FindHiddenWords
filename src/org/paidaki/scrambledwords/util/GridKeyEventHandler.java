package org.paidaki.scrambledwords.util;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.paidaki.scrambledwords.gui.controls.Grid;
import org.paidaki.scrambledwords.gui.controls.GridCell;

import java.util.List;

public class GridKeyEventHandler implements EventHandler<KeyEvent> {

    @Override
    public void handle(KeyEvent event) {
        KeyCode code = event.getCode();
        Grid grid = (Grid) event.getSource();
        GridCell cell = (GridCell) event.getTarget();
        List<GridCell> cellsList = grid.getCellsList();
        GridCell[][] cellsArray = grid.getCellsArray();

        switch (code) {
            case ENTER:
                cellsList.get(Math.floorMod((cellsList.indexOf(cell) + 1), cellsList.size())).requestFocus();
                break;
            case BACK_SPACE:
                cellsList.get(Math.floorMod((cellsList.indexOf(cell) - 1), cellsList.size())).requestFocus();
                break;
            case KP_UP:
            case UP:
                cellsList.get(Math.floorMod((cellsList.indexOf(cell) - grid.getCols()), cellsList.size())).requestFocus();
                break;
            case KP_DOWN:
            case DOWN:
                cellsList.get(Math.floorMod((cellsList.indexOf(cell) + grid.getCols()), cellsList.size())).requestFocus();
                break;
            case KP_LEFT:
            case LEFT:
                cellsArray[cell.getRow()][Math.floorMod(cell.getCol() - 1, grid.getCols())].requestFocus();
                break;
            case KP_RIGHT:
            case RIGHT:
                cellsArray[cell.getRow()][Math.floorMod(cell.getCol() + 1, grid.getCols())].requestFocus();
                break;
            default:
                return;
        }
        event.consume();
    }
}
