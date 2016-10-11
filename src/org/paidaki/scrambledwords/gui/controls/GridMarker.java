package org.paidaki.scrambledwords.gui.controls;

import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import org.paidaki.scrambledwords.util.Util;

import java.util.List;

import static org.paidaki.scrambledwords.util.Preferences.*;

public class GridMarker extends Rectangle {

    private GridCell start, end;
    private String word;
    private List<GridCell> cells;
    private int length;
    private Direction direction;
    private Glow glow;
    private DropShadow dropShadow;

    private enum Direction {
        SPOT(0, 0, 0),
        L2R_HOR(1, 0, 0),
        T2B_VER(0, 1, 0),
        L2R_T2B(0, DIAGONAL_FIX, -45),
        L2R_B2T(0, DIAGONAL_FIX, -135);

        private double modX, modY, angle;

        Direction(double modX, double modY, double angle) {
            this.modX = modX;
            this.modY = modY;
            this.angle = angle;
        }

        public double getModX() {
            return modX;
        }

        public double getModY() {
            return modY;
        }

        public double getAngle() {
            return angle;
        }
    }

    public GridMarker(List<GridCell> cells, String word) {
        super();
        this.cells = cells;
        this.word = word;
        start = cells.get(0);
        end = cells.get(cells.size() - 1);
        length = cells.size();

        initialize();

        glow = new Glow(0.8);
        dropShadow = new DropShadow(20, (Color) this.getFill());
    }

    private void initialize() {
        int x = end.getRow() - start.getRow();
        int y = end.getCol() - start.getCol();

        if (x == 0 && y == 0) {
            direction = Direction.SPOT;
        } else if (x == 0 && y > 0) {
            direction = Direction.L2R_HOR;
        } else if (x == 0 && y < 0) {
            start = end;
            end = cells.get(0);
            direction = Direction.L2R_HOR;
        } else if (x > 0 && y == 0) {
            direction = Direction.T2B_VER;
        } else if (x < 0 && y == 0) {
            start = end;
            end = cells.get(0);
            direction = Direction.T2B_VER;
        } else if (x > 0 && y > 0) {
            direction = Direction.L2R_T2B;
        } else if (x > 0 && y < 0) {
            start = end;
            end = cells.get(0);
            direction = Direction.L2R_B2T;
        } else if (x < 0 && y > 0) {
            direction = Direction.L2R_B2T;
        } else if (x < 0 && y < 0) {
            start = end;
            end = cells.get(0);
            direction = Direction.L2R_T2B;
        }

        this.setWidth(CELL_WIDTH * (length - 1) * direction.getModX() + (CELL_WIDTH * 2) / 3);
        this.setHeight(CELL_HEIGHT * (length - 1) * direction.getModY() + (CELL_HEIGHT * 2) / 3);
        this.setFill(Util.randomColor());
        this.setOpacity(0.4);
        this.setArcWidth(40);
        this.setArcHeight(40);
        this.getTransforms().add(new Rotate(direction.getAngle(), CELL_WIDTH / 3, CELL_HEIGHT / 3));
    }

    public void addGlow() {
        dropShadow.setInput(glow);
        this.setEffect(dropShadow);
    }

    public void removeGlow() {
        dropShadow.setInput(null);
        this.setEffect(null);
    }

    public List<GridCell> getCells() {
        return cells;
    }

    public String getWord() {
        return word;
    }

    public GridCell getStart() {
        return start;
    }

    public GridCell getEnd() {
        return end;
    }

    public int getLength() {
        return length;
    }

    public Direction getDirection() {
        return direction;
    }
}
