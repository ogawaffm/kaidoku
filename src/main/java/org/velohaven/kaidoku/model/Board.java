package org.velohaven.kaidoku.model;

import static org.velohaven.kaidoku.model.Validation.checkValue;

public class Board extends Range {

    private final DigitSetStorage digitSetStorage;
    private final int boxRowCount;
    private final int boxColumnCount;

    public Board(DigitSetStorage digitSetStorage, int boxRowCount, int boxColumnCount) {
        super(null, 0, 0, digitSetStorage.getBoardSideLength(), digitSetStorage.getBoardSideLength());
        int expectedSideLength = boxRowCount * boxColumnCount;
        if (digitSetStorage.getBoardSideLength() != boxRowCount * boxColumnCount) {
            throw new IllegalArgumentException("digitSetStorage boardSideLength (" + digitSetStorage.getBoardSideLength()
                    + ") does not match board row/column side length (" + expectedSideLength + ")"
            );
        }
        this.digitSetStorage = digitSetStorage;
        this.boxRowCount = boxRowCount;
        this.boxColumnCount = boxColumnCount;
    }

    public int getBoxCount() {
        return getColumnCount();
    }

    public int getBoxRowCount() {
        return boxRowCount;
    }

    public int getBoxColumnCount() {
        return boxColumnCount;
    }

    public Range getBox(int box) {
        checkValue(box, 0, getBoxCount() - 1, "box");
        /*
        int row = (box / getBoxColumnCount()) * getBoxColumnCount();
        int column = (box % getBoxRowCount()) * getBoxColumnCount();
        return getBox(row, column);
        */
        int boxesPerRow = getBoxColumnCount();
        int boxRow = box / boxesPerRow;
        int boxCol = box % boxesPerRow;

        int row = boxRow * getBoxRowCount();
        int column = boxCol * getBoxColumnCount();

        return getBox(row, column);
    }

    private Range getBox(int row, int column) {
/*
        checkValue(row, 0, getBoxRowCount() - 1, "row");
        checkValue(column, 0, getBoxColumnCount() - 1, "column");
        return new Range(this, row, column, getBoxRowCount(), getBoxColumnCount());
*/
        checkValue(row, 0, getRowCount() - 1, "row");
        checkValue(column, 0, getColumnCount() - 1, "column");
        return new Range(this, row, column, getBoxRowCount(), getBoxColumnCount());
    }


    public RangeIterator<Range> getBoxes() {
        return new RangeIterator<>(this::getBox, getBoxCount());
    }

    DigitSet getDigitSet(int row, int column) {
        return digitSetStorage.get(row, column);
    }

    void setDigitSet(int row, int column, DigitSet digitSet) {
        digitSetStorage.set(row, column, digitSet);
    }

    private String toString(Range cell, int level) {
        DigitSet digitSet = digitSetStorage.get(cell.getRow(), cell.getColumn());
        int width = getBoxColumnCount();
        return digitSet.toString().substring(width * level, width * (level + 1));
    }

    @Override
    public String toString() {
        if (1 == 1) {
            return "NIX";
        }
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < getRowCount(); row++) {
            for (int level = 0; level < getBoxRowCount(); level++) {
                for (int column = 0; column < getColumnCount(); column++) {
                    if (column > 0) {
                        sb.append('|');
                    }
                    Range cell = getCell(row, column);
                    sb.append(cell);
                }
                sb.append('\n');
            }
        }
        return sb.toString();
    }

}

