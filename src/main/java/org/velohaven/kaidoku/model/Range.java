package org.velohaven.kaidoku.model;

import java.util.Objects;

public class Range<P extends Range<P>> {

    private final P parent;
    private final int position;
    private final int rowNo;
    private final int columnNo;

    Range(P parent, int position, int rowNo, int columnNo) {
        this.parent = parent;
        this.position = position;
        this.rowNo = rowNo;
        this.columnNo = columnNo;
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
    public P getParent() {
        return parent;
    }

    public int getPosition() {
        return position;
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
    public int getRowNo() {
        return rowNo;
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
    public int getColumnNo() {
        return columnNo;
    }

    /**
     * Returns the grid that this range belongs to.
     * The grid is the top-level range that contains this range. If this range is a grid, then it returns itself.
     *
     * @return the grid that this range belongs to
     */
    public Grid getGrid() {
        if (getParent() == null) {
            return (Grid) this;
        } else {
            return getParent().getGrid();
        }
    }


    public int getCellNo() {
        return getRowNo() * getColumnNo();
    }

    /**
     * Returns the Cell with the given index. The index starts from 0 to getCellNo()-1.
     *
     * @param index index of the cell to return
     * @return the Cell with the given index
     */
    public Range<P> getCell(int index) {
        Objects.checkIndex(index, getCellNo());
        int xOffset = getPosition() % getGrid().getColumnNo();
        int yOffset = getPosition() / getGrid().getColumnNo();
        int xAddOn = index % getColumnNo();
        int yAddOn = index / getColumnNo();
        return getGrid().getCell((xOffset + xAddOn) + (yOffset + yAddOn) * getGrid().getColumnNo());
    }

    public Range<P> getRow(int index) {
        Objects.checkIndex(index, getRowNo());
        return new Range<>(getParent(), index * getColumnNo(), 1, getColumnNo());
    }

    public Range<P> getColumn(int index) {
        Objects.checkIndex(index, getColumnNo());
        return new Range<>(getParent(), index, getRowNo(), 1);
    }

    public RangeIterator<Range<P>> getCells() {
        return new RangeIterator<>(this::getCell, getCellNo());
    }

    public RangeIterator<Range<P>> getRows() {
        return new RangeIterator<>(this::getRow, getRowNo());
    }

    public RangeIterator<Range<P>> getColumns() {
        return new RangeIterator<>(this::getColumn, getColumnNo());
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

        if (getRowNo() == 1 && getColumnNo() == 1) {
            return RANGE_TYPE.CELL;
        }

        if (getRowNo() == 1) {
            return getColumnNo() == getGrid().getColumnNo()
                    ? RANGE_TYPE.ROW
                    : RANGE_TYPE.BOX_ROW;
        }

        if (getColumnNo() == 1) {
            return getRowNo() == getGrid().getRowNo()
                    ? RANGE_TYPE.COLUMN
                    : RANGE_TYPE.BOX_COLUMN;
        }

        return RANGE_TYPE.BOX;

    }

    @Override
    public String toString() {
        // TODO add column and row information for cells, rows and columns
        return getType().toString().toLowerCase() + " " + getPosition();
    }

}
