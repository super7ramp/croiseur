/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.solver.paulgb.plugin;

import com.gitlab.super7ramp.croiseur.common.puzzle.GridPosition;
import com.gitlab.super7ramp.croiseur.common.puzzle.PuzzleGrid;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A wrapper of {@link PuzzleGrid} adding to each cell a unique integer identifier.
 */
final class NumberedPuzzleDefinition {

    /** The original puzzle definition. */
    private final PuzzleGrid puzzleGrid;

    /** A map associating position to a unique integer identifier. */
    private final Map<GridPosition, Integer> positionToId;

    /** The reverse map of {@link #positionToId}. */
    private final Map<Integer, GridPosition> idToPosition;

    /**
     * Constructs an instance.
     *
     * @param puzzleGridArg the original definition
     */
    NumberedPuzzleDefinition(final PuzzleGrid puzzleGridArg) {
        puzzleGrid = puzzleGridArg;
        positionToId = createPositionToIdMap(puzzleGridArg.width(),
                                             puzzleGridArg.height(),
                                             puzzleGridArg.shaded());
        idToPosition = createIdToPositionMap(positionToId);
    }

    private static Map<GridPosition, Integer> createPositionToIdMap(final int width,
                                                                    final int height,
                                                                    final Set<GridPosition> shaded) {
        final Map<GridPosition, Integer> labeledBoxes = new HashMap<>();
        for (int row = 0, id = 0; row < height; row++) {
            for (int column = 0; column < width; column++) {
                final GridPosition position = new GridPosition(column, row);
                if (!shaded.contains(position)) {
                    labeledBoxes.put(position, id++);
                }
            }
        }
        return labeledBoxes;
    }

    private static Map<Integer, GridPosition> createIdToPositionMap(Map<GridPosition, Integer> positionToLabel) {
        final Map<Integer, GridPosition> positions = new HashMap<>();
        for (final Map.Entry<GridPosition, Integer> entry : positionToLabel.entrySet()) {
            positions.put(entry.getValue(), entry.getKey());
        }
        return positions;
    }

    /**
     * Returns the width of the grid (i.e. the number of columns).
     *
     * @return the width of the grid (i.e. the number of columns)
     */
    int width() {
        return puzzleGrid.width();
    }

    /**
     * Returns the height of the grid (i.e. the number of rows).
     *
     * @return the height of the grid (i.e. the number of rows)
     */
    int height() {
        return puzzleGrid.height();
    }

    /**
     * Returns the filled boxes.
     *
     * @return the filled boxes
     */
    Map<GridPosition, Character> filled() {
        return puzzleGrid.filled();
    }

    /**
     * Returns the shaded boxes.
     *
     * @return the shaded boxes
     */
    Set<GridPosition> shaded() {
        return puzzleGrid.shaded();
    }

    /**
     * Returns a map associating position to a unique integer identifier.
     * <p>
     * Returned map is unmodifiable.
     *
     * @return a map associating position to a unique integer identifier
     */
    Map<GridPosition, Integer> positionToId() {
        return Collections.unmodifiableMap(positionToId);
    }

    /**
     * Returns the reverse map of {@link #positionToId()}.
     * <p>
     * Returned map is unmodifiable.
     *
     * @return the reverse map of {@link #positionToId()}
     */
    Map<Integer, GridPosition> idToPosition() {
        return Collections.unmodifiableMap(idToPosition);
    }
}
