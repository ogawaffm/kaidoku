package org.velohaven.kaidoku.output;

import org.junit.jupiter.api.Test;
import org.velohaven.kaidoku.model.CellContent;
import org.velohaven.kaidoku.model.CellStorage;
import org.velohaven.kaidoku.model.Grid;
import org.velohaven.kaidoku.model.Range;

class OutputTest {

    @Test
    void testOutput_3x3() {
        testOutput(3, 3);
    }

    private CellStorage createContentList(int boxRowCount, int boxColumnCount) {
        CellStorage cellStorage = new CellStorage(boxRowCount * boxColumnCount, boxColumnCount * boxRowCount);

        for (int rowIndex = 0; rowIndex < boxRowCount * boxColumnCount; rowIndex++) {
            for (int columnIndex = 0; columnIndex < boxRowCount * boxColumnCount; columnIndex++) {
                cellStorage.set(rowIndex, columnIndex, new CellContent("R" + rowIndex + "C" + columnIndex));
            }
        }
        return cellStorage;
    }

    public void testOutput(int boxRowCount, int boxColumnCount) {

        CellStorage cellStorage = createContentList(boxRowCount, boxColumnCount);

        Grid grid = new Grid(cellStorage, boxRowCount, boxColumnCount);

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
