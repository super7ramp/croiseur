package com.gitlab.super7ramp.crosswords.solver.paulgb;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for {@link Solver}.
 */
final class SolverTest {

    @Test
    void emptyResult() {
        final Solver solver = new Solver();
        final Grid emptyGrid = new Grid(new long[0][0]);
        final Dictionary emptyDictionary = new Dictionary(new String[0]);

        final Optional<char[]> result = solver.solve(emptyGrid, emptyDictionary);

        assertTrue(result.isEmpty());
    }

    @Test
    void nonEmptyResult() {
        final Solver solver = new Solver();
        final Grid emptyGrid = new Grid(new long[0][0]);
        final Dictionary emptyDictionary = new Dictionary(new String[]{"hello", "beautiful",
                "world"});

        final Optional<char[]> result = solver.solve(emptyGrid, emptyDictionary);

        // TODO
    }
}
