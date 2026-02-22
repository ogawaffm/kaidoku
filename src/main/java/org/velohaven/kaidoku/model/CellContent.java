package org.velohaven.kaidoku.model;

public class CellContent {

    private final String sample;

    public CellContent(String sample) {
        this.sample = sample;
    }

    @Override
    public String toString() {
        return sample;
    }
}
