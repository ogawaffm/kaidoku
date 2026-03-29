package org.velohaven.kaidoku.model;

import java.util.Objects;

public class DigitSetStorage {

    private final int rowCount;
    private final int columnCount;

    private final DigitSet[][] digitSets;

    public DigitSetStorage(int rowCount, int columnCount) {
        this.rowCount = rowCount;
        this.columnCount = columnCount;

        digitSets = new DigitSet[rowCount * columnCount][columnCount * rowCount];
        DigitSetFactory digitSetFactory = DigitSetFactory.forMaxSize(columnCount * rowCount);

        for(int rowIndex = 0; rowIndex < rowCount) {
            for (int columnIndex = 0; columnIndex < columnCount) {
                digitSets[rowIndex, columnIndex] = digitSetFactory.allDigits();
            }
        }

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

    public DigitSet get(int rowIndex, int columnIndex) {
        Objects.checkIndex(rowIndex, rowCount);
        Objects.checkIndex(columnIndex, columnCount);
        return digitSets[rowIndex][columnIndex];
    }

    public void set(int rowIndex, int columnIndex, DigitSet digitSet) {
        Objects.checkIndex(rowIndex, rowCount);
        Objects.checkIndex(columnIndex, columnCount);
        digitSets[rowIndex][columnIndex] = digitSet;
    }

}
