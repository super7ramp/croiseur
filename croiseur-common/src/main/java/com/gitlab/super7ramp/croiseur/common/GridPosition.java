/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.common;

/**
 * Coordinates on the grid.
 * <p>
 * The grid starts at (0,0). Row axis is reversed, meaning that (0,1) is below (0,0).
 * <p>
 * Coordinates cannot be negative. Attempting to create a new instance using negative coordinate
 * will result into an {@link IllegalArgumentException}.
 *
 * @param x the column number
 * @param y the row number
 */
public record GridPosition(int x, int y) {

    /**
     * Constructor.
     *
     * @param x horizontal index, starting at 0
     * @param y vertical index, starting at 0
     */
    public GridPosition {
        if (x < 0 || y < 0) {
            throw new IllegalArgumentException("Invalid grid position");
        }
    }

    /**
     * Returns a new {@link GridPosition} corresponding to the position below this one.
     *
     * @return a new {@link GridPosition} corresponding to the position below this one.
     */
    public GridPosition down() {
        return plusVerticalOffset(1);
    }

    /**
     * Returns a new {@link GridPosition} corresponding to the position above this one or this
     * position if this position is on the first y.
     *
     * @return a new {@link GridPosition} corresponding to the position above this one  or this
     * position if this position is on the first y.
     */
    public GridPosition up() {
        return plusVerticalOffset(-1);
    }

    /**
     * Returns a new {@link GridPosition} corresponding to the position on the right of this one.
     *
     * @return a new {@link GridPosition} corresponding to the position on the right of this one.
     */
    public GridPosition right() {
        return plusHorizontalOffset(1);
    }

    /**
     * Returns a new {@link GridPosition} corresponding to the position on the left of this one
     * or this position if this position is on the first x.
     *
     * @return a new {@link GridPosition} corresponding to the position on the left of this one
     * this position if this position is on the first x
     */
    public GridPosition left() {
        return plusHorizontalOffset(-1);
    }

    /**
     * Returns a new {@link GridPosition} at specified horizontal offset of this coordinate.
     * <p>
     * If the given offset leads to a negative horizontal coordinate, then the horizontal
     * coordinate of the returned position is set to 0.
     *
     * @param offset the horizontal offset
     * @return a new {@link GridPosition} at specified horizontal offset of this coordinate.
     */
    public GridPosition plusHorizontalOffset(final int offset) {
        return new GridPosition(Math.max(0, x + offset), y);
    }

    /**
     * Returns a new {@link GridPosition} at specified vertical offset of this coordinate.
     * <p>
     * If the given offset leads to a negative vertical coordinate, then the vertical coordinate
     * of the returned position is set to 0.
     *
     * @param offset the vertical offset
     * @return a new {@link GridPosition} at specified vertical offset of this coordinate.
     */
    public GridPosition plusVerticalOffset(final int offset) {
        return new GridPosition(x, Math.max(0, y + offset));
    }

    @Override
    public String toString() {
        return "GridPosition{" + "x=" + x + ", y=" + y + '}';
    }

}
