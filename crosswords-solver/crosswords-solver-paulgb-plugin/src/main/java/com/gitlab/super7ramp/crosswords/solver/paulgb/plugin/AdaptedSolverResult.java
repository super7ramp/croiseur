package com.gitlab.super7ramp.crosswords.solver.paulgb.plugin;

import com.gitlab.super7ramp.crosswords.common.GridPosition;
import com.gitlab.super7ramp.crosswords.spi.solver.SolverResult;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Adapts Crossword Composer solver result into {@link SolverResult}.
 */
final class AdaptedSolverResult implements SolverResult {

    /** The result kind. */
    private final Kind kind;

    /** The filled boxes. */
    private final Map<GridPosition, Character> filledBoxes;

    /** The unsolvable boxes. */
    private final Set<GridPosition> unsolvableBoxes;

    /**
     * Constructs an instance.
     *
     * @param idToPosition association between indexes and grid positions
     * @param outputGrid the output grid
     */
    private AdaptedSolverResult(final Map<Long, GridPosition> idToPosition, char[] outputGrid) {
        if (outputGrid == null) {
            kind = Kind.IMPOSSIBLE;
            filledBoxes = Collections.emptyMap();
            // No fine information returned by solver about problematic slots, just mark all
            // cells as problematic
            unsolvableBoxes = new HashSet<>(idToPosition.values());
        } else {
            if (idToPosition.size() != outputGrid.length) {
                throw new IllegalArgumentException("Solver result inconsistent with input grid");
            }
            kind = Kind.SUCCESS;
            filledBoxes = new HashMap<>();
            for (int i = 0; i < outputGrid.length; i++) {
                filledBoxes.put(idToPosition.get((long) i), outputGrid[i]);
            }
            unsolvableBoxes = Collections.emptySet();
        }
    }

    static AdaptedSolverResult impossible(final Map<Long, GridPosition> idToPosition) {
        return new AdaptedSolverResult(idToPosition, null);
    }

    static AdaptedSolverResult success(final Map<Long, GridPosition> idToPosition,
                                       final char[] outputGrid) {
        return new AdaptedSolverResult(idToPosition, outputGrid);
    }

    @Override
    public Kind kind() {
        return kind;
    }

    @Override
    public Map<GridPosition, Character> filledBoxes() {
        return Collections.unmodifiableMap(filledBoxes);
    }

    @Override
    public Set<GridPosition> unsolvableBoxes() {
        return Collections.unmodifiableSet(unsolvableBoxes);
    }
}
