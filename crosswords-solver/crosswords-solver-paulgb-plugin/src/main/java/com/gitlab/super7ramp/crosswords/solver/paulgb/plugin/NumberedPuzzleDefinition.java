package com.gitlab.super7ramp.crosswords.solver.paulgb.plugin;

import com.gitlab.super7ramp.crosswords.common.GridPosition;
import com.gitlab.super7ramp.crosswords.common.PuzzleDefinition;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A wrapper of {@link PuzzleDefinition} adding to each cell a unique integer identifier.
 */
final class NumberedPuzzleDefinition {

    /** The original puzzle definition. */
    private final PuzzleDefinition puzzleDefinition;

    /** A map associating position to a unique integer identifier. */
    private final Map<GridPosition, Long> positionToId;

    /** The reverse map of {@link #positionToId}. */
    private final Map<Long, GridPosition> idToPosition;

    /**
     * Constructs an instance.
     *
     * @param puzzleDefinitionArg the original definition
     */
    NumberedPuzzleDefinition(final PuzzleDefinition puzzleDefinitionArg) {
        puzzleDefinition = puzzleDefinitionArg;
        positionToId = createPositionToIdMap(puzzleDefinitionArg.width(),
                puzzleDefinitionArg.height(),
                puzzleDefinitionArg.shaded());
        idToPosition = createIdToPositionMap(positionToId);
    }

    private static Map<GridPosition, Long> createPositionToIdMap(final int width, final int height,
                                                                 final Set<GridPosition> shaded) {
        final Map<GridPosition, Long> labeledBoxes = new HashMap<>();
        for (int row = 0, id = 0; row < height; row++) {
            for (int column = 0; column < width; column++) {
                final GridPosition position = new GridPosition(column, row);
                if (!shaded.contains(position)) {
                    labeledBoxes.put(position, (long) id++);
                }
            }
        }
        return labeledBoxes;
    }

    private static Map<Long, GridPosition> createIdToPositionMap(Map<GridPosition, Long> positionToLabel) {
        final Map<Long, GridPosition> positions = new HashMap<>();
        for (final Map.Entry<GridPosition, Long> entry : positionToLabel.entrySet()) {
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
        return puzzleDefinition.width();
    }

    /**
     * Returns the height of the grid (i.e. the number of rows).
     *
     * @return the height of the grid (i.e. the number of rows)
     */
    int height() {
        return puzzleDefinition.height();
    }

    /**
     * Returns the filled boxes.
     *
     * @return the filled boxes
     */
    Map<GridPosition, Character> filled() {
        return puzzleDefinition.filled();
    }

    /**
     * Returns the shaded boxes.
     *
     * @return the shaded boxes
     */
    Set<GridPosition> shaded() {
        return puzzleDefinition.shaded();
    }

    /**
     * Returns a map associating position to a unique integer identifier.
     * <p>
     * Returned map is unmodifiable.
     *
     * @return a map associating position to a unique integer identifier
     */
    Map<GridPosition, Long> positionToId() {
        return Collections.unmodifiableMap(positionToId);
    }

    /**
     * Returns the reverse map of {@link #positionToId()}.
     * <p>
     * Returned map is unmodifiable.
     *
     * @return the reverse map of {@link #positionToId()}
     */
    Map<Long, GridPosition> idToPosition() {
        return Collections.unmodifiableMap(idToPosition);
    }
}
