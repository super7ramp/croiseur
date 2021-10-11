package com.gitlab.super7ramp.crosswords.solver.api;

/**
 * Coordinates on the grid.
 */
public record Coordinate(int x, int y) {

    /**
     * Constructor.
     *
     * @param x horizontal index, starting at 0
     * @param y vertical index, starting at 0
     */
    public Coordinate {
        if (x < 0 || y < 0) {
            throw new IllegalArgumentException("Invalid coordinates");
        }
    }
}
