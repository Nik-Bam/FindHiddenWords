package org.paidaki.scrambledwords.util;

import com.sun.istack.internal.NotNull;
import javafx.scene.paint.Color;
import org.paidaki.scrambledwords.gui.controls.GridCell;

import java.util.List;
import java.util.Random;

public class Util {

    private static final Random RAND = new Random();

    private Util() {
    }

    @NotNull
    public static String fixWord(String word) {
        return word
                .replaceAll("\\P{L}+", "")
                .replace((char) 0x390, (char) 0x399)
                .replace((char) 0x3B0, (char) 0x399)
                .toUpperCase()
                .replace((char) 0x386, (char) 0x391)
                .replace((char) 0x388, (char) 0x395)
                .replace((char) 0x389, (char) 0x397)
                .replace((char) 0x38A, (char) 0x399)
                .replace((char) 0x38C, (char) 0x39F)
                .replace((char) 0x38E, (char) 0x3A5)
                .replace((char) 0x38F, (char) 0x3A9)
                .replace((char) 0x3AA, (char) 0x399)
                .replace((char) 0x3AB, (char) 0x3A5);
    }

    public static Color randomColor() {
        return Color.color(RAND.nextDouble(), RAND.nextDouble(), RAND.nextDouble());
    }

    public static String cellsToString(List<GridCell> cells) {
        StringBuilder strB = new StringBuilder();

        cells.forEach(gridCell -> strB.append(gridCell.getText()));
        return String.valueOf(strB);
    }
}
