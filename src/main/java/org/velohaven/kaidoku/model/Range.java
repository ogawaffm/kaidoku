package org.velohaven.kaidoku.model;

import java.util.Objects;

public class Range {

    private final Range parent;
    private final int row;
    private final int column;
    private final int rowCount;
    private final int columnCount;

    Range(Range parent, int row, int column, int rowCount, int columnCount) {
        this.parent = parent;
        this.row = row;
        this.column = column;
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        if (rowCount <= 0) {
            throw new IllegalArgumentException("rowCount must be positive, got: " + rowCount);
        }
        if (columnCount <= 0) {
            throw new IllegalArgumentException("columnCount must be positive, got: " + columnCount);
        }
        if (row < 0) {
            throw new IllegalArgumentException("row must be non-negative, got: " + row);
        }
        if (column < 0) {
            throw new IllegalArgumentException("row must be non-negative, got: " + column);
        }
//        if (getParent() != null && position >= getParent().getCellCount()) {
//            throw new IllegalArgumentException("Position must be less than the number of cells ("
//                    + getCellCount() + "), got: " + position);
//        }
    }

    /**
     * Returns the parent range of this range.
     * <ul>
     * <li>For a cell, the parent is the box, column or row that contains it.</li>
     * <li>For a box, the parent is the grid that contains it.</li>
     * <li>For a grid, there is no parent and this method returns null.</li>
     * </ul>
     *
     * @return the parent range of this range, or null if this range is a grid
     */
    public Range getParent() {
        return parent;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public int getGridRow() {
        return getParent() == null ? getRow() : getParent().getGridRow() + getRow();
    }

    public int getGridColumn() {
        return getParent() == null ? getColumn() : getParent().getGridColumn() + getColumn();
    }

    /**
     * Returns the number of rows in this range.
     * <ul>
     * <li>For a cell and for a row, this is 1.</li>
     * <li>For a grid and for a Column, this is the height of the grid.</li>
     * <li>For a box, this is the height of the box.</li>
     * </ul>
     *
     * @return number of rows
     */
    public int getRowCount() {
        return rowCount;
    }

    /**
     * Returns the number of columns in this range.
     * <ul>
     * <li>For a cell and for a column, this is 1.</li>
     * <li>For a grid and for a row, this is the width of the grid.</li>
     * <li>For a box, this is the width of the box.</li>
     * </ul>
     *
     * @return number of columns
     */
    public int getColumnCount() {
        return columnCount;
    }

    /**
     * Returns the grid that this range belongs to.
     * The grid is the top-level range that contains this range. If this range is a grid, then it returns itself.
     *
     * @return the grid that this range belongs to
     */
    public Grid getGrid() {
        return getParent() == null
                ? (Grid) this
                : getParent().getGrid();
    }

    public int getCellCount() {
        return getRowCount() * getColumnCount();
    }

    /**
     * Returns the Cell with the given index. The index starts from 0 to getCellCount()-1.
     *
     * @param index index of the cell to return
     * @return the Cell with the given index
     */
    public Range getCell(int index) {
        Objects.checkIndex(index, getCellCount());
        return getCell(index / getColumnCount(), index % getColumnCount());
    }

    public Range getCell(int row, int column) {
        Objects.checkIndex(row, getRowCount());
        Objects.checkIndex(column, getColumnCount());
        return new Range(this, getGridRow() + row, getGridColumn() + column, 1, 1);
    }

    public Range getRow(int index) {
        Objects.checkIndex(index, getRowCount());
        return new Range(this, index, 0, 1, getColumnCount());
    }

    public Range getColumn(int index) {
        Objects.checkIndex(index, getColumnCount());
        return new Range(this, 0, index, getRowCount(), 1);
    }

    public RangeIterator<Range> getCells() {
        return new RangeIterator<>(this::getCell, getCellCount());
    }

    public RangeIterator<Range> getRows() {
        return new RangeIterator<>(this::getRow, getRowCount());
    }

    public RangeIterator<Range> getColumns() {
        return new RangeIterator<>(this::getColumn, getColumnCount());
    }

    /**
     * Returns the DigitSet of this range.
     * This method only works for ranges of type CELL.
     *
     * @return the DigitSet of the cell
     * @throws IllegalStateException if this range is not a cell
     */
    public DigitSet getDigitSet() {
        if (getType() != RANGE_TYPE.CELL) {
            throw new IllegalStateException("getDigitSet() can only be called on CELL ranges, not on " + getType());
        }
        return getGrid().getDigitSet(getGridRow(), getGridColumn());
    }

    /**
     * Sets the DigitSet of this range.
     * This method only works for ranges of type CELL.
     *
     * @return the DigitSet ofto set
     * @throws IllegalStateException if this range is not a cell
     */
    public void setDigitSet(DigitSet digitSet) {
        if (getType() != RANGE_TYPE.CELL) {
            throw new IllegalStateException("setDigitSet() can only be called on CELL ranges, not on " + getType());
        }
        getGrid().setDigitSet(getGridRow(), getGridColumn(), digitSet);
    }

    /**
     * Returns the type of this range.
     * The type can be one of the following:
     * <ul>
     *     <li>GRID: the whole grid</li>
     *     <li>ROW: a row of the grid</li>
     *     <li>COLUMN: a column of the grid</li>
     *     <li>BOX: a box of the grid</li>
     *     <li>BOX_ROW: a row of a box</li>
     *     <li>BOX_COLUMN: a column of a box</li>
     *     <li>CELL: a single cell</li>
     * </ul>
     *
     * @return the type of this range
     */
    public RANGE_TYPE getType() {

        if (getParent() == null) {
            return RANGE_TYPE.GRID;
        }

        if (getRowCount() == 1 && getColumnCount() == 1) {
            return RANGE_TYPE.CELL;
        }

        if (getRowCount() == 1) {
            return getColumnCount() == getGrid().getColumnCount()
                    ? RANGE_TYPE.ROW
                    : RANGE_TYPE.BOX_ROW;
        }

        if (getColumnCount() == 1) {
            return getRowCount() == getGrid().getRowCount()
                    ? RANGE_TYPE.COLUMN
                    : RANGE_TYPE.BOX_COLUMN;
        }

        return RANGE_TYPE.BOX;

    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Range otherRange)) {
            return false;
        }

        // Compare structural properties
        if (this.getRow() != otherRange.getRow()
                || this.getColumn() != otherRange.getColumn()
                || this.getRowCount() != otherRange.getRowCount()
                || this.getColumnCount() != otherRange.getColumnCount()) {
            return false;
        }

        // Compare content of all cells
        for (int index = 0; index < this.getCellCount(); index++) {
            CellContent thisContent = this.getCell(index).getContent();
            CellContent otherContent = otherRange.getCell(index).getContent();
            if (!Objects.equals(thisContent, otherContent)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(row, column, rowCount, columnCount);
        for (int index = 0; index < getCellCount(); index++) {
            result = 31 * result + Objects.hashCode(getCell(index).getContent());
        }
        return result;
    }

    public static String toSubscript(String input) {
        if (input == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder(input.length());

        for (char c : input.toCharArray()) {
            if (c >= '0' && c <= '9') {
                sb.append((char) ('₀' + (c - '0')));
            } else {
                sb.append(c);
            }
        }

        return sb.toString();
    }

    @Override
    public String toString() {
        String string = null;

        if (getType() == RANGE_TYPE.GRID) {
            string = "Grid";
        } else if (getType() == RANGE_TYPE.ROW || getType() == RANGE_TYPE.BOX_ROW) {
            string = getParent() + " Row" + getRow();
        } else if (getType() == RANGE_TYPE.COLUMN || getType() == RANGE_TYPE.BOX_COLUMN) {
            string = getParent() + " Column" + getColumn();
        } else if (getType() == RANGE_TYPE.BOX) {
            string = "Box" + (getRow() / getGrid().getBoxColumnCount() * getGrid().getBoxColumnCount() + getColumn() / getGrid().getBoxRowCount());
        } else if (getType() == RANGE_TYPE.CELL) {
            string = getParent() + " Cell";
            if (getParent().getType() == RANGE_TYPE.GRID || getParent().getType() == RANGE_TYPE.BOX) {
                string += getRow() + "," + getColumn();
            } else if (getParent().getType() == RANGE_TYPE.ROW || getParent().getType() == RANGE_TYPE.BOX_ROW) {
                // row
                string += getColumn();
            } else {
                // column
                string += getRow();
            }
        }
        return toSubscript(string);
    }

}
