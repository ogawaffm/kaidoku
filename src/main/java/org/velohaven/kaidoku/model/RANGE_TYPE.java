package org.velohaven.kaidoku.model;

public enum RANGE_TYPE {
    /**
     * Whole board
     */
    BOARD,

    /**
     * A single row
     */
    ROW,

    /**
     * A single column
     */
    COLUMN,

    /**
     * A single Box
     */
    BOX,

    /**
     * A single row of a box
     */
    BOX_ROW,

    /**
     * A single column of a box
     */
    BOX_COLUMN,

    /**
     * A single cell
     */
    CELL
}