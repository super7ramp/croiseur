/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.gui.view.model;

/**
 * Coordinates on the grid.
 * <p>
 * The grid starts at (0,0). Row axis is reversed, meaning that (0,1) is below (0,0).
 * <p>
 * Coordinates cannot be negative. Attempting to create a new instance using negative coordinate
 * will result into an {@link IllegalArgumentException}.
 *
 * @param column the column number, i.e. the horizontal index, starting at 0
 * @param row the row number, i.e. the vertical index, starting at 0
 */
public record GridCoord(int column, int row) {

    /**
     * Constructor.
     *
     * @param column the column number, i.e. the horizontal index, starting at 0
     * @param row the row number, i.e. the vertical index, starting at 0
     */
    public GridCoord {
        if (column < 0 || row < 0) {
            throw new IllegalArgumentException("Invalid grid position");
        }
    }

    /**
     * Convenience factory method.
     * <pre>
     * {@code
     * import static re.belv.croiseur.gui.view.model.GridCoord.at;
     *
     * ...
     *
     * final GridCoord myPosition = at(0,0);
     * }
     * </pre>
     *
     *
     * @param x the column number, i.e. the horizontal index, starting at 0
     * @param y the row number, i.e. the vertical index, starting at 0
     * @return a new GridCoord
     */
    public static GridCoord at(final int x, final int y) {
        return new GridCoord(x, y);
    }

    /**
     * Returns a new {@link GridCoord} corresponding to the position below this one.
     *
     * @return a new {@link GridCoord} corresponding to the position below this one.
     */
    public GridCoord down() {
        return plusVerticalOffset(1);
    }

    /**
     * Returns a new {@link GridCoord} corresponding to the position above this one or this
     * position if this position is on the first row.
     *
     * @return a new {@link GridCoord} corresponding to the position above this one  or this
     * position if this position is on the first row.
     */
    public GridCoord up() {
        return plusVerticalOffset(-1);
    }

    /**
     * Returns a new {@link GridCoord} corresponding to the position on the right of this one.
     *
     * @return a new {@link GridCoord} corresponding to the position on the right of this one.
     */
    public GridCoord right() {
        return plusHorizontalOffset(1);
    }

    /**
     * Returns a new {@link GridCoord} corresponding to the position on the left of this one
     * or this position if this position is on the first column.
     *
     * @return a new {@link GridCoord} corresponding to the position on the left of this one
     * this position if this position is on the first column
     */
    public GridCoord left() {
        return plusHorizontalOffset(-1);
    }

    /**
     * Returns a new {@link GridCoord} at specified horizontal offset of this coordinate.
     * <p>
     * If the given offset leads to a negative horizontal coordinate, then the horizontal
     * coordinate of the returned position is set to 0.
     *
     * @param offset the horizontal offset
     * @return a new {@link GridCoord} at specified horizontal offset of this coordinate.
     */
    public GridCoord plusHorizontalOffset(final int offset) {
        return new GridCoord(Math.max(0, column + offset), row);
    }

    /**
     * Returns a new {@link GridCoord} at specified vertical offset of this coordinate.
     * <p>
     * If the given offset leads to a negative vertical coordinate, then the vertical coordinate
     * of the returned position is set to 0.
     *
     * @param offset the vertical offset
     * @return a new {@link GridCoord} at specified vertical offset of this coordinate.
     */
    public GridCoord plusVerticalOffset(final int offset) {
        return new GridCoord(column, Math.max(0, row + offset));
    }

    @Override
    public String toString() {
        return "GridCoord{" + "column=" + column + ", row=" + row + '}';
    }
}
