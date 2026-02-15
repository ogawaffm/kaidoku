package org.velohaven.kaidoku.model;

import java.util.Objects;

public class Grid extends Range<Grid> {

    private final int boxRowNo;
    private final int boxColumnNo;

    private final Cell[] cells;

    public Grid(int boxRowNo, int columns) {
        super(null, 0, boxRowNo * columns, boxRowNo * columns);
        this.boxRowNo = boxRowNo;
        this.boxColumnNo = columns;
        cells = new Cell[boxRowNo * boxColumnNo];
    }

    @Override
    public Grid getParent() {
        return null;
    }

    @Override
    public int getPosition() {
        return 0;
    }

    @Override
    public int getRowNo() {
        return boxRowNo * boxColumnNo;
    }

    @Override
    public int getColumnNo() {
        return boxRowNo * boxColumnNo;
    }

    public int getBoxNo() {
        return getColumnNo();
    }

    public int getBoxRowNo() {
        return boxRowNo;
    }

    public int getBoxColumnNo() {
        return boxColumnNo;
    }

    public Range<Grid> getBox(int index) {
        Objects.checkIndex(index, getBoxNo());
        return new Range<>(getParent(), index, getBoxRowNo(), getBoxColumnNo());
    }

    public RangeIterator<Range<Grid>> getBoxes() {
        return new RangeIterator<>(this::getBox, getRowNo());
    }

    @Override
    public Range<Grid> getCell(int index) {
        Objects.checkIndex(index, getCellNo());
        return cells[index];
    }

}

