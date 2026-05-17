package org.velohaven.kaidoku.model;

import static org.velohaven.kaidoku.model.Validation.checkValue;

public class DigitSetStorage {

    private final int boardSideLength;

    private final DigitSet[][] digitSets;

    public DigitSetStorage(int boardSideLength) {
        this.boardSideLength = boardSideLength;

        digitSets = new DigitSet[boardSideLength][boardSideLength];
        DigitSetFactory digitSetFactory = DigitSetFactory.forMaxSize(boardSideLength);

        for(int row = 0; row < this.boardSideLength; row++) {
            for (int column = 0; column < boardSideLength; column++) {
                digitSets[row][column] = digitSetFactory.allDigits();
            }
        }
    }

    public int getBoardSideLength() {
        return boardSideLength;
    }

    public DigitSet get(int row, int column) {
        checkValue(column, 0, boardSideLength - 1, "column");
        checkValue(row, 0, boardSideLength - 1, "row");
        return digitSets[row][column];
    }

    public void set(int row, int column, DigitSet digitSet) {
        checkValue(column, 0, boardSideLength - 1, "column");
        checkValue(row, 0, boardSideLength - 1, "row");
        digitSets[row][column] = digitSet;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < boardSideLength; row++) {
            for (int column = 0; column < boardSideLength; column++) {
                sb.append(digitSets[row][column]);
                if (column < boardSideLength - 1) {
                    sb.append(" | ");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

}
