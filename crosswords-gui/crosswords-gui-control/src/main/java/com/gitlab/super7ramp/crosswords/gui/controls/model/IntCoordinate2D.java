package com.gitlab.super7ramp.crosswords.gui.controls.model;

/**
 * (x: int, y: int) coordinates.
 */
public record IntCoordinate2D(int x, int y) {

    /**
     * Returns a new {@link IntCoordinate2D} corresponding to the position below this one.
     *
     * @return a new {@link IntCoordinate2D} corresponding to the position below this one.
     */
    public IntCoordinate2D down() {
        return new IntCoordinate2D(x, y + 1);
    }

    /**
     * Returns a new {@link IntCoordinate2D} corresponding to the position above this one.
     *
     * @return a new {@link IntCoordinate2D} corresponding to the position above this one.
     */
    public IntCoordinate2D up() {
        return new IntCoordinate2D(x, y - 1);
    }

    /**
     * Returns a new {@link IntCoordinate2D} corresponding to the position on the right of this one.
     *
     * @return a new {@link IntCoordinate2D} corresponding to the position on the right of this one.
     */
    public IntCoordinate2D right() {
        return new IntCoordinate2D(x + 1, y);
    }

    /**
     * Returns a new {@link IntCoordinate2D} corresponding to the position on the left of this one.
     *
     * @return a new {@link IntCoordinate2D} corresponding to the position on the left of this one.
     */
    public IntCoordinate2D left() {
        return new IntCoordinate2D(Math.max(0, x - 1), y);
    }
}
