package org.velohaven.kaidoku.model;

import java.util.Objects;

public class Grid extends Range {

    private final CellStorage cellStorage;
    private final int boxRowCount;
    private final int boxColumnCount;


    public Grid(CellStorage cellStorage, int boxRowCount, int boxColumnCount) {
        super(null, 0, 0, cellStorage.getRowCount(), cellStorage.getColumnCount());
        int expectedSize = boxRowCount * boxColumnCount * boxRowCount * boxColumnCount;
        if (cellStorage.getSize() != expectedSize) {
            throw new IllegalArgumentException("CellStorage size (" + cellStorage.getSize()
                    + ") does not match grid capacity (" + expectedSize + ")"
            );
        }
        this.cellStorage = cellStorage;
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

    CellContent getContent(int rowIndex, int columnIndex) {
        return cellStorage.get(rowIndex, columnIndex);
    }

}

