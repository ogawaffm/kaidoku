package org.velohaven.kaidoku.model;

import java.util.Objects;

public class Grid extends Range {

    private final DigitSetStorage digitSetStorage;
    private final int boxRowCount;
    private final int boxColumnCount;

    public Grid(DigitSetStorage digitSetStorage, int boxRowCount, int boxColumnCount) {
        super(null, 0, 0, digitSetStorage.getRowCount(), digitSetStorage.getColumnCount());
        int expectedSize = boxRowCount * boxColumnCount * boxRowCount * boxColumnCount;
        if (digitSetStorage.getSize() != expectedSize) {
            throw new IllegalArgumentException("digitSetStorage size (" + digitSetStorage.getSize()
                    + ") does not match grid capacity (" + expectedSize + ")"
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

    public Range getBox(int index) {
        Objects.checkIndex(index, getBoxCount());
        int row = (index / getBoxColumnCount()) * getBoxColumnCount();
        int column = (index % getBoxRowCount()) * getBoxColumnCount();
        return getBox(row, column);
    }

    private Range getBox(int row, int column) {
        Objects.checkIndex(row, getRowCount());
        Objects.checkIndex(column, getColumnCount());
        return new Range(this, row, column, getBoxRowCount(), getBoxColumnCount());
    }


    public RangeIterator<Range> getBoxes() {
        return new RangeIterator<>(this::getBox, getBoxCount());
    }

    DigitSet getDigitSet(int rowIndex, int columnIndex) {
        return digitSetStorage.get(rowIndex, columnIndex);
    }

    void setDigitSet(int rowIndex, int columnIndex, DigitSet digitSet) {
        return digitSetStorage.set(rowIndex, columnIndex, digitSet);
    }

}

