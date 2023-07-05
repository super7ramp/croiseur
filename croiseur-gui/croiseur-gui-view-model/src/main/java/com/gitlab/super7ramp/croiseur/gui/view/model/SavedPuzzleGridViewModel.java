/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.view.model;

import com.gitlab.super7ramp.croiseur.common.puzzle.GridPosition;

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
    private final Set<GridPosition> shaded;

    /** The filled boxes. */
    private final Map<GridPosition, Character> filled;

    /**
     * Constructs an instance.
     *
     * @param columnCountArg the number of columns
     * @param rowCountArg    the number of rows
     * @param shadedArg      the shaded boxes
     * @param filledArg      the filled boxes
     */
    SavedPuzzleGridViewModel(final int columnCountArg, final int rowCountArg,
                             final Set<GridPosition> shadedArg,
                             final Map<GridPosition, Character> filledArg) {
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
    public Set<GridPosition> shaded() {
        return Collections.unmodifiableSet(shaded);
    }

    /**
     * Returns the filled boxes.
     *
     * @return the filled boxes
     */
    public Map<GridPosition, Character> filled() {
        return Collections.unmodifiableMap(filled);
    }
}
