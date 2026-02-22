package org.velohaven.kaidoku.model;

import java.util.Objects;

public class CellStorage {

    private final int rowCount;
    private final int columnCount;

    private final CellContent[][] cellContents;

    public CellStorage(int rowCount, int columnCount) {
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        cellContents = new CellContent[rowCount * columnCount][columnCount * rowCount];
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public int getSize() {
        return rowCount * columnCount;
    }

    public CellContent get(int rowIndex, int columnIndex) {
        Objects.checkIndex(rowIndex, rowCount);
        Objects.checkIndex(columnIndex, columnCount);
        return cellContents[rowIndex][columnIndex];
    }

    public void set(int rowIndex, int columnIndex, CellContent content) {
        Objects.checkIndex(rowIndex, rowCount);
        Objects.checkIndex(columnIndex, columnCount);
        cellContents[rowIndex][columnIndex] = content;
    }

}
