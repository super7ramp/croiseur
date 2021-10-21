package com.gitlab.super7ramp.crosswords.solver.lib;

import com.gitlab.super7ramp.crosswords.solver.api.Coordinate;
import com.gitlab.super7ramp.crosswords.solver.api.SolverResult;

import java.util.Map;

import static com.gitlab.super7ramp.crosswords.solver.lib.PuzzleDefinitionParser.parsePuzzle;

final class Assertions {

    /**
     * Assert {@link SolverResult} matches the expected solution, given as a multi-line string.
     *
     * @param expected the expected result
     * @param result   the actual result
     */
    static void assertEquals(final String expected, final SolverResult result) {
        final Map<Coordinate, Character> expectedResult = parsePuzzle(expected).filled();
        final Map<Coordinate, Character> actualResult = result.boxes();
        org.junit.jupiter.api.Assertions.assertEquals(expectedResult, actualResult);
    }
}
