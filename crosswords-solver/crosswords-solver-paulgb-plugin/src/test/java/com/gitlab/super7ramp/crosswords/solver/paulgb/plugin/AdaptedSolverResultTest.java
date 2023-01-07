package com.gitlab.super7ramp.crosswords.solver.paulgb.plugin;

import com.gitlab.super7ramp.crosswords.common.GridPosition;
import com.gitlab.super7ramp.crosswords.solver.paulgb.Solution;
import com.gitlab.super7ramp.crosswords.spi.solver.SolverResult;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for {@link AdaptedSolverResult}.
 */
final class AdaptedSolverResultTest {

    @Test
    void impossible() {
        final GridPosition position0 = new GridPosition(0, 0);
        final GridPosition position1 = new GridPosition(0, 1);
        final GridPosition position2 = new GridPosition(0, 2);
        final Set<GridPosition> positions = Set.of(position0, position1, position2);

        final SolverResult result = AdaptedSolverResult.impossible(positions);

        assertEquals(SolverResult.Kind.IMPOSSIBLE, result.kind());
        assertEquals(Collections.emptyMap(), result.filledBoxes());
        assertEquals(positions, result.unsolvableBoxes());
    }

    @Test
    void success() {
        final Map<Integer, GridPosition> idToPosition = new HashMap<>();
        final GridPosition position0 = new GridPosition(0, 0);
        idToPosition.put(0, position0);
        final GridPosition position1 = new GridPosition(0, 1);
        idToPosition.put(1, position1);
        final GridPosition position2 = new GridPosition(0, 2);
        idToPosition.put(2, position2);
        final Solution solution = new Solution(new char[]{'A', 'B', 'C'});

        final SolverResult result = AdaptedSolverResult.success(idToPosition, solution);

        assertEquals(SolverResult.Kind.SUCCESS, result.kind());
        assertEquals(Map.of(position0, 'A', position1, 'B', position2, 'C'), result.filledBoxes());
        assertEquals(Collections.emptySet(), result.unsolvableBoxes());
    }
}
