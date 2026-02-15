package org.velohaven.kaidoku.model;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class CellIterator implements Iterator<Cell> {

    private final Range<?> range;
    private int currentIndex;

    CellIterator(Range<?> range) {
        this.range = range;
        this.currentIndex = 0;
    }

    @Override
    public boolean hasNext() {
        return currentIndex < range.getCellNo();
    }

    @Override
    public Cell next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return range.getCell(currentIndex++);
    }
}
