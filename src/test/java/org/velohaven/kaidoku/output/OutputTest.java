package org.velohaven.kaidoku.output;

import org.junit.jupiter.api.Test;
import org.velohaven.kaidoku.model.DigitSetFactory;
import org.velohaven.kaidoku.model.DigitSetStorage;
import org.velohaven.kaidoku.model.Board;
import org.velohaven.kaidoku.model.Range;

class OutputTest {

    @Test
    void testOutput_3x3() {
        testOutput(3, 3);
    }

    private DigitSetStorage createContentList(int boxRowCount, int boxColumnCount) {
        DigitSetFactory factory = DigitSetFactory.forMaxSize(boxRowCount * boxColumnCount);
        DigitSetStorage digitSetStorage = new DigitSetStorage(boxRowCount * boxColumnCount);
        System.out.println(digitSetStorage);

        for (int row = 0; row < boxRowCount * boxColumnCount; row++) {
            for (int column = 0; column < boxRowCount * boxColumnCount; column++) {
                digitSetStorage.set(row, column,  factory.allDigits());
            }
        }
        return digitSetStorage;
    }

    public void testOutput(int boxRowCount, int boxColumnCount) {

        DigitSetStorage digitSetStorage = createContentList(boxRowCount, boxColumnCount);

        Board grid = new Board(digitSetStorage, boxRowCount, boxColumnCount);

        System.out.println(grid);
        for (Range cell : grid.getCells()) {
            System.out.println(cell);
            System.out.flush();
        }

        System.out.println(grid);
        for (Range column : grid.getColumns()) {
            System.out.println(column);
            for (Range cell : column.getCells()) {
                System.out.println(cell);
                System.out.flush();
            }
            System.out.println();
            System.out.flush();
        }

        System.out.println();

        System.out.println(grid);
        for (Range row : grid.getRows()) {
            System.out.println(row);
            for (Range cell : row.getCells()) {
                System.out.println(cell);
                System.out.flush();
            }
            System.out.println();
        }

        System.out.println();

        System.out.println(grid);
        for (Range box : grid.getBoxes()) {
            System.out.println(box);
            for (Range row : box.getRows()) {
                for (Range cell : row.getCells()) {
                    System.out.println(cell);
                }
                System.out.println();
            }
            System.out.println();
        }

        System.out.println(grid);
        for (Range box : grid.getBoxes()) {
            System.out.println(box);
            for (Range column : box.getColumns()) {
                for (Range cell : column.getCells()) {
                    System.out.println(cell);
                }
                System.out.println();
            }
            System.out.println();
        }

        System.out.println(grid);
        for (Range box : grid.getBoxes()) {
            System.out.println(box);
            for (Range cell : box.getCells()) {
                System.out.println(cell);
            }
            System.out.println();
            System.out.println();
        }

    }

}
