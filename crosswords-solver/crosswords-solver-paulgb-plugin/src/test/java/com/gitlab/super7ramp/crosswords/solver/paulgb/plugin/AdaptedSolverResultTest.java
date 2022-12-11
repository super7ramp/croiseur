package com.gitlab.super7ramp.crosswords.solver.paulgb.plugin;

import com.gitlab.super7ramp.crosswords.common.GridPosition;
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
        final Map<Long, GridPosition> idToPosition = new HashMap<>();
        final GridPosition position0 = new GridPosition(0, 0);
        idToPosition.put(0L, position0);
        final GridPosition position1 = new GridPosition(0, 1);
        idToPosition.put(1L, position1);
        final GridPosition position2 = new GridPosition(0, 2);
        idToPosition.put(2L, position2);

        final SolverResult result = AdaptedSolverResult.impossible(idToPosition);

        assertEquals(SolverResult.Kind.IMPOSSIBLE, result.kind());
        assertEquals(Collections.emptyMap(), result.filledBoxes());
        assertEquals(Set.of(position0, position1, position2), result.unsolvableBoxes());
    }

    @Test
    void success() {
        final Map<Long, GridPosition> idToPosition = new HashMap<>();
        final GridPosition position0 = new GridPosition(0, 0);
        idToPosition.put(0L, position0);
        final GridPosition position1 = new GridPosition(0, 1);
        idToPosition.put(1L, position1);
        final GridPosition position2 = new GridPosition(0, 2);
        idToPosition.put(2L, position2);

        final SolverResult result = AdaptedSolverResult.success(idToPosition, new char[]{'A',
                'B', 'C'});

        assertEquals(SolverResult.Kind.SUCCESS, result.kind());
        assertEquals(Map.of(position0, 'A', position1, 'B', position2, 'C'), result.filledBoxes());
        assertEquals(Collections.emptySet(), result.unsolvableBoxes());
    }
}
