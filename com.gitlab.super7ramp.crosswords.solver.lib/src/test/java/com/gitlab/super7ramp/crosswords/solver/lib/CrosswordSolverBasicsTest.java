package com.gitlab.super7ramp.crosswords.solver.lib;

import com.gitlab.super7ramp.crosswords.solver.api.Coordinate;
import com.gitlab.super7ramp.crosswords.solver.api.Dictionary;
import com.gitlab.super7ramp.crosswords.solver.api.PuzzleDefinition;
import com.gitlab.super7ramp.crosswords.solver.api.SolverResult;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Tests for {@link CrosswordSolverImpl}.
 * <p>
 * These tests verify basics:
 * <ul>
 *     <li>Simple grids with no backtracking necessary</li>
 *     <li>Handling of shaded boxes</li>
 *     <li>Handling of prefilled boxes</li>
 * </ul>
 */
final class CrosswordSolverBasicsTest {

    private static Pattern CHARACTER_SEPARATOR = Pattern.compile("(\\|)");

    private static Character SHADED_CHARACTER = '#';

    /**
     * Assert {@link SolverResult} matches the expected solution, given as a multi-line string.
     *
     * @param expected the expected result
     * @param result   the actual result
     */
    private static void assertEquals(final String expected, final SolverResult result) {
        final Map<Coordinate, Character> expectedResult = parsePuzzle(expected).filled();
        final Map<Coordinate, Character> actualResult = result.boxes();
        org.junit.jupiter.api.Assertions.assertEquals(expectedResult, actualResult);
    }

    /**
     * Parse puzzle from multi-line string such as:
     * <pre>
     *  |H|E|Y|
     *  | | | |
     *  | | |#|
     * </pre>
     *
     * @param puzzle the string representation
     * @return the {@link PuzzleDefinition}
     */
    private static PuzzleDefinition parsePuzzle(final String puzzle) {
        final Set<Coordinate> shaded = new HashSet<>();
        final Map<Coordinate, Character> prefilled = new HashMap<>();
        final String[] lines = puzzle.split(System.lineSeparator());
        final int height = lines.length;
        final int width = (int) CHARACTER_SEPARATOR.matcher(lines[0]).results().count() - 1;

        for (int x = 0; x < height; x++) {
            final String[] characters = CHARACTER_SEPARATOR.split(lines[x].substring(1) /* ignore starting "" */);
            for (int y = 0; y < width; y++) {
                final String parsed = characters[y].trim();
                if (!parsed.isEmpty()) {
                    final Character character = parsed.charAt(0);
                    final Coordinate coordinate = new Coordinate(x, y);
                    if (character.equals(SHADED_CHARACTER)) {
                        shaded.add(coordinate);
                    } else {
                        prefilled.put(coordinate, character);
                    }
                } else {
                    // Empty box.
                }
            }
        }

        return new PuzzleDefinition(width, height, shaded, prefilled);
    }

    @Test
    void empty3x3() throws InterruptedException {
        final PuzzleDefinition puzzle = parsePuzzle(
                """
                        | | | |
                        | | | |
                        | | | |
                        """);
        final Dictionary dictionary = new DictionaryMock("AAA", "BBB", "CDE", "ABC", "ABD", "ABE");

        final SolverResult result = new CrosswordSolverImpl().solve(puzzle, dictionary);

        assertEquals(
                """
                        |A|A|A|
                        |B|B|B|
                        |C|D|E|
                        """, result);
    }

    @Test
    void prefilled3x3() {
        // TODO Not implemented yet
        // need a different way to store prefilled value so that they can't be removed by backtracking
    }

    @Test
    void shaded3x3() throws InterruptedException {
        final PuzzleDefinition puzzle = parsePuzzle(
                """
                        | | | |
                        | | | |
                        | | |#|
                        """);
        final Dictionary dictionary = new DictionaryMock("AAA", "BBB", "CD", "ABC", "ABD", "AB");

        final SolverResult result = new CrosswordSolverImpl().solve(puzzle, dictionary);

        assertEquals(
                """
                        |A|A|A|
                        |B|B|B|
                        |C|D|#|
                        """, result);
    }

}
