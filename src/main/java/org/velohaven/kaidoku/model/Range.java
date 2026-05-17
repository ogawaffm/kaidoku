package org.velohaven.kaidoku.model;

import java.util.Objects;

import static org.velohaven.kaidoku.model.Validation.checkValue;

public class Range {

    private final Range parent;
    private final int row;
    private final int column;
    private final int rowCount;
    private final int columnCount;

    Range(Range parent, int row, int column, int rowCount, int columnCount) {
        this.parent = parent;

        // Is Board?
        if (parent == null) {
            this.row = checkValue(row, 0, Integer.MAX_VALUE, "row");
            this.column = checkValue(column, 0, Integer.MAX_VALUE, "column");
            this.columnCount = checkValue(columnCount, 1, Integer.MAX_VALUE, "columnCount");
            this.rowCount = checkValue(rowCount, 1, Integer.MAX_VALUE, "rowCount");
        } else {
            this.row = checkValue(row, 0, parent.getRowCount() - 1, "row");
            this.column = checkValue(column, 0, parent.getColumnCount() - 1, "column");
            this.columnCount = checkValue(columnCount, 1, parent.getColumnCount(), "columnCount");
            this.rowCount = checkValue(rowCount, 1, parent.getRowCount(), "rowCount");
        }
    }

    /**
     * Returns the parent range of this range.
     * <ul>
     * <li>For a cell, the parent is the box, column or row that contains it.</li>
     * <li>For a box, the parent is the board that contains it.</li>
     * <li>For a board, there is no parent and this method returns null.</li>
     * </ul>
     *
     * @return the parent range of this range, or null if this range is a board
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

    public int getBoardRow() {
        return getParent() == null ? getRow() : getParent().getBoardRow() + getRow();
    }

    public int getBoardColumn() {
        return getParent() == null ? getColumn() : getParent().getBoardColumn() + getColumn();
    }

    /**
     * Returns the number of rows in this range.
     * <ul>
     * <li>For a cell and for a row, this is 1.</li>
     * <li>For a board and for a Column, this is the height of the board.</li>
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
     * <li>For a board and for a row, this is the width of the board.</li>
     * <li>For a box, this is the width of the box.</li>
     * </ul>
     *
     * @return number of columns
     */
    public int getColumnCount() {
        return columnCount;
    }

    /**
     * Returns the board that this range belongs to.
     * The board is the top-level range that contains this range. If this range is a board, then it returns itself.
     *
     * @return the board that this range belongs to
     */
    public Board getBoard() {
        return getParent() == null
                ? (Board) this
                : getParent().getBoard();
    }

    public int getCellCount() {
        return getRowCount() * getColumnCount();
    }

    /**
     * Returns the cell-Range for the given cell index.
     *
     * @param cell 0-based index of the cell to return
     * @return cell-Range
     */
    public Range getCell(int cell) {
        checkValue(cell, 0, getCellCount() - 1, "cell");
        return getCell(cell / getColumnCount(), cell % getColumnCount());
    }

    /**
     * Returns the Range for the given row and column index.
     *
     * @param row    0-based row index of the cell to return
     * @param column 0-based column index of the cell to return
     * @return Range (Cell)
     */
    public Range getCell(int row, int column) {
        checkValue(row, 0, getRowCount() - 1, "row");
        checkValue(column, 0, getColumnCount() - 1, "column");
        return new Range(this, getBoardRow() + row, getBoardColumn() + column, 1, 1);
//AI        return new Range(this, row, column, 1, 1);
    }

    /**
     * Returns the Range for the given row index.
     *
     * @param row 0-based row index of the row to return
     * @return Range (Row)
     */
    public Range getRow(int row) {
        checkValue(row, 0, getRowCount() - 1, "row");
        return new Range(this, row, 0, 1, getColumnCount());
    }

    /**
     * Returns the Range for the given column index.
     *
     * @param column 0-based column index of the column to return
     * @return Range (Column)
     */
    public Range getColumn(int column) {
        checkValue(column, 0, getColumnCount() - 1, "column");
        return new Range(this, 0, column, getRowCount(), 1);
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
            throw new IllegalStateException("getDigitSet() can only be called on cell ranges, not on " + getType());
        }
        return getBoard().getDigitSet(getBoardRow(), getBoardColumn());
    }

    /**
     * Sets the DigitSet of this range.
     * This method only works for ranges of type CELL.
     *
     * @throws IllegalStateException if this range is not a cell
     */
    public void setDigitSet(DigitSet digitSet) {
        if (getType() != RANGE_TYPE.CELL) {
            throw new IllegalStateException("setDigitSet() can only be called on CELL ranges, not on " + getType());
        }
        getBoard().setDigitSet(getBoardRow(), getBoardColumn(), digitSet);
    }

    /**
     * Returns the type of this range.
     * The type can be one of the following:
     * <ul>
     *     <li>BOARD: the whole board</li>
     *     <li>ROW: a row of the board</li>
     *     <li>COLUMN: a column of the board</li>
     *     <li>BOX: a box of the board</li>
     *     <li>BOX_ROW: a row of a box</li>
     *     <li>BOX_COLUMN: a column of a box</li>
     *     <li>CELL: a single cell</li>
     * </ul>
     *
     * @return the type of this range
     */
    public RANGE_TYPE getType() {

        if (getParent() == null) {
            return RANGE_TYPE.BOARD;
        }

        if (getRowCount() == 1 && getColumnCount() == 1) {
            return RANGE_TYPE.CELL;
        }

        if (getRowCount() == 1) {
            return getColumnCount() == getBoard().getColumnCount()
                    ? RANGE_TYPE.ROW
                    : RANGE_TYPE.BOX_ROW;
        }

        if (getColumnCount() == 1) {
            return getRowCount() == getBoard().getRowCount()
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
        for (int cell = 0; cell < this.getCellCount(); cell++) {
            DigitSet thisContent = this.getCell(cell).getDigitSet();
            DigitSet otherContent = otherRange.getCell(cell).getDigitSet();
            if (!Objects.equals(thisContent, otherContent)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(row, column, rowCount, columnCount);
        for (int cell = 0; cell < getCellCount(); cell++) {
            result = 31 * result + getCell(cell).getDigitSet().hashCode();
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

        if (getType() == RANGE_TYPE.BOARD) {
            string = "Bord";
        } else if (getType() == RANGE_TYPE.ROW || getType() == RANGE_TYPE.BOX_ROW) {
            string = getParent() + " Row" + getRow();
        } else if (getType() == RANGE_TYPE.COLUMN || getType() == RANGE_TYPE.BOX_COLUMN) {
            string = getParent() + " Column" + getColumn();
        } else if (getType() == RANGE_TYPE.BOX) {
            string = "Box" + (getRow() / getBoard().getBoxColumnCount() * getBoard().getBoxColumnCount() + getColumn() / getBoard().getBoxRowCount());
        } else if (getType() == RANGE_TYPE.CELL) {
            string = getParent() + " Cell";
            if (getParent().getType() == RANGE_TYPE.BOARD || getParent().getType() == RANGE_TYPE.BOX) {
                string += getRow() + "," + getColumn();
            } else if (getParent().getType() == RANGE_TYPE.ROW || getParent().getType() == RANGE_TYPE.BOX_ROW) {
                // row
                string += getColumn();
            } else {
                // column
                string += getRow();
            }
        }
        string = toSubscript(string);
        if (getType() == RANGE_TYPE.CELL) {
            string += ":" + getDigitSet();
        }
        return string;
    }

}
