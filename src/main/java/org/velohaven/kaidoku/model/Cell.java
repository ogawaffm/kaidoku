package org.velohaven.kaidoku.model;

public class Cell<P extends Range<P>> extends Range<P> implements GridPosition {

    private final int position;

    Cell(P parent, int position) {
        super(parent, position, 1, 1);
        this.position = position;
    }

    @Override
    public int getPosition() {
        return position;
    }

}
