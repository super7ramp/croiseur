/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.gui.view.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * An immutable model for displaying a fixed view of a saved crossword puzzle grid.
 */
public final class SavedPuzzleGridViewModel {

    /** The number of columns. */
    private final int columnCount;

    /** The number of rows. */
    private final int rowCount;

    /** The shaded boxes. */
    private final Set<GridCoord> shaded;

    /** The filled boxes. */
    private final Map<GridCoord, Character> filled;

    /**
     * Constructs an instance.
     *
     * @param columnCountArg the number of columns
     * @param rowCountArg    the number of rows
     * @param shadedArg      the shaded boxes
     * @param filledArg      the filled boxes
     */
    SavedPuzzleGridViewModel(
            final int columnCountArg,
            final int rowCountArg,
            final Set<GridCoord> shadedArg,
            final Map<GridCoord, Character> filledArg) {
        columnCount = columnCountArg;
        rowCount = rowCountArg;
        shaded = new HashSet<>(shadedArg);
        filled = new HashMap<>(filledArg);
    }

    /**
     * Returns the column count.
     *
     * @return the column count
     */
    public int columnCount() {
        return columnCount;
    }

    /**
     * Returns the row count.
     *
     * @return the row count
     */
    public int rowCount() {
        return rowCount;
    }

    /**
     * Returns the shaded boxes.
     *
     * @return the shaded boxes
     */
    public Set<GridCoord> shaded() {
        return Collections.unmodifiableSet(shaded);
    }

    /**
     * Returns the filled boxes.
     *
     * @return the filled boxes
     */
    public Map<GridCoord, Character> filled() {
        return Collections.unmodifiableMap(filled);
    }
}
