package org.paidaki.scrambledwords.app;

import org.paidaki.scrambledwords.gui.controls.GridCell;
import org.paidaki.scrambledwords.util.Util;

import java.util.Comparator;
import java.util.List;

public class GridWord {

    public static final Comparator<GridWord> WORD_COMPARATOR = (o1, o2) -> o1.getWord().compareTo(o2.getWord());

    private String word;
    private String reverseWord = "";
    private List<GridCell> cells;
    private boolean reverse;

    public GridWord(List<GridCell> cells, boolean reverse) {
        this.cells = cells;
        this.reverse = reverse;
        word = Util.cellsToString(cells);
    }

    public String getWord() {
        return word;
    }

    public String getReverseWord() {
        return reverseWord;
    }

    public void setReverseWord(String reverseWord) {
        this.reverseWord = reverseWord;
    }

    public List<GridCell> getCells() {
        return cells;
    }

    public boolean isReverse() {
        return reverse;
    }

    @Override
    public String toString() {
        return reverseWord.isEmpty() ? word : word + "-" + reverseWord;
    }
}
