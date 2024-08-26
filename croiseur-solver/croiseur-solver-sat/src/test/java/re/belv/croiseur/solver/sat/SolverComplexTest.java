/*
 * SPDX-FileCopyrightText: 2024 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.solver.sat;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import re.belv.croiseur.dictionary.common.StringFilters;
import re.belv.croiseur.dictionary.common.StringTransformers;

/** Tests for {@link Solver} with large grids and/or large word lists. */
final class SolverComplexTest {

    /** The test word list. */
    private static String[] words;

    /**
     * Sets up the word list for all tests in this class.
     *
     * @throws NullPointerException if test dictionary is not found
     * @throws URISyntaxException if test dictionary location fails to be converted to URI
     * @throws IOException if test dictionary fails to be read
     */
    @BeforeAll
    static void setUp() throws URISyntaxException, IOException {
        final var dictionaryUrl = Objects.requireNonNull(
                SolverComplexTest.class.getResource("/UKACD18plus.txt"),
                "Test dictionary not found, verify the test resources.");
        final var dictionaryPath = Path.of(dictionaryUrl.toURI());
        try (final Stream<String> lines = Files.lines(dictionaryPath)) {
            words = lines.map(StringTransformers.toAcceptableCrosswordEntry())
                    .filter(StringFilters.hasOnlyCharactersInRangeAtoZ())
                    .toArray(String[]::new);
        }
    }

    // < 1s at 1GHz
    @Test
    void empty3x3() throws InterruptedException {
        final char[][] inputGrid = new char[][] {
            {'.', '.', '.'},
            {'.', '.', '.'},
            {'.', '.', '.'}
        };

        final Solver.Result result = new Solver(inputGrid, words).solve();

        final var output = assertInstanceOf(Solver.Result.Sat.class, result);
        final char[][] expectedGrid = new char[][] {
            {'B', 'A', 'A'},
            {'A', 'B', 'B'},
            {'B', 'A', 'A'}
        };
        assertArrayEquals(expectedGrid, output.grid());
    }

    // ~ 5s at 1GHz
    @Test
    void empty4x4() throws InterruptedException {
        final char[][] inputGrid = new char[][] {
            {'.', '.', '.', '.'},
            {'.', '.', '.', '.'},
            {'.', '.', '.', '.'},
            {'.', '.', '.', '.'}
        };

        final Solver.Result result = new Solver(inputGrid, words).solve();

        final var output = assertInstanceOf(Solver.Result.Sat.class, result);
        final char[][] expectedGrid = new char[][] {
            {'P', 'E', 'R', 'V'},
            {'H', 'U', 'I', 'A'},
            {'U', 'R', 'E', 'A'},
            {'T', 'O', 'L', 'L'}
        };
        assertArrayEquals(expectedGrid, output.grid());
    }

    // ~5s at 1GHz
    @Test
    void shaded5x5() throws InterruptedException {
        final char[][] inputGrid = new char[][] {
            {'#', '#', '.', '.', '#'},
            {'#', '.', '.', '.', '#'},
            {'.', '.', '.', '.', '.'},
            {'#', '.', '.', '.', '#'},
            {'#', '#', '.', '#', '#'}
        };

        final Solver.Result result = new Solver(inputGrid, words).solve();

        final var output = assertInstanceOf(Solver.Result.Sat.class, result);
        final char[][] expectedGrid = new char[][] {
            {'#', '#', 'A', 'A', '#'},
            {'#', 'T', 'A', 'B', '#'},
            {'D', 'I', 'R', 'A', 'C'},
            {'#', 'D', 'O', 'C', '#'},
            {'#', '#', 'N', '#', '#'}
        };
        assertArrayEquals(expectedGrid, output.grid());
    }

    // ~12s at 1GHz
    @Test
    void shaded9x9() throws InterruptedException {
        final char[][] inputGrid = new char[][] {
            {'#', '#', '#', '.', '.', '.', '#', '#', '#'},
            {'#', '#', '.', '.', '.', '.', '.', '#', '#'},
            {'#', '.', '.', '.', '.', '.', '.', '.', '#'},
            {'.', '.', '.', '.', '#', '.', '.', '.', '.'},
            {'.', '.', '.', '#', '#', '#', '.', '.', '.'},
            {'.', '.', '.', '.', '#', '.', '.', '.', '.'},
            {'#', '.', '.', '.', '.', '.', '.', '.', '#'},
            {'#', '#', '.', '.', '.', '.', '.', '#', '#'},
            {'#', '#', '#', '.', '.', '.', '#', '#', '#'},
        };

        final Solver.Result result = new Solver(inputGrid, words).solve();

        final var output = assertInstanceOf(Solver.Result.Sat.class, result);
        final char[][] expectedGrid = new char[][] {
            {'#', '#', '#', 'Z', 'I', 'A', '#', '#', '#'},
            {'#', '#', 'B', 'E', 'A', 'N', 'S', '#', '#'},
            {'#', 'T', 'A', 'N', 'N', 'A', 'T', 'E', '#'},
            {'H', 'O', 'B', 'O', '#', 'N', 'A', 'J', 'A'},
            {'A', 'R', 'A', '#', '#', '#', 'W', 'E', 'B'},
            {'N', 'I', 'S', 'I', '#', 'M', 'I', 'C', 'A'},
            {'#', 'I', 'S', 'S', 'U', 'A', 'N', 'T', '#'},
            {'#', '#', 'U', 'N', 'D', 'U', 'G', '#', '#'},
            {'#', '#', '#', 'T', 'O', 'N', '#', '#', '#'},
        };
        assertArrayEquals(expectedGrid, output.grid());
    }

    /**
     * Verifies that solver responds to thread interruption.
     *
     * @param delay the delay before interruption (in seconds)
     * @throws InterruptedException if interrupted while waiting before interrupting the solver thread
     */
    @ParameterizedTest
    // Testing with various delays to (try to) test interruption at different solver phases. Exact
    // interruption points will vary between different machines.
    @ValueSource(ints = {0, 1, 2})
    void shaded9x9_interrupted(final int delay) throws InterruptedException {
        // Input grid is large enough not to be solved before interruption
        final char[][] inputGrid = new char[][] {
            {'#', '#', '#', '.', '.', '.', '#', '#', '#'},
            {'#', '#', '.', '.', '.', '.', '.', '#', '#'},
            {'#', '.', '.', '.', '.', '.', '.', '.', '#'},
            {'.', '.', '.', '.', '#', '.', '.', '.', '.'},
            {'.', '.', '.', '#', '#', '#', '.', '.', '.'},
            {'.', '.', '.', '.', '#', '.', '.', '.', '.'},
            {'#', '.', '.', '.', '.', '.', '.', '.', '#'},
            {'#', '#', '.', '.', '.', '.', '.', '#', '#'},
            {'#', '#', '#', '.', '.', '.', '#', '#', '#'},
        };
        final var interruptionTester = new InterruptionTester(new Solver(inputGrid, words)::solve);

        interruptionTester.interruptThreadAfter(delay);

        interruptionTester.assertRunnableThrewInterruptedExceptionWithin(3 /* seconds */);
    }

    // ~34s at 1GHz
    @Test
    @Disabled("too slow to run automatically")
    void shaded13x13() throws InterruptedException {
        final char[][] inputGrid = new char[][] {
            {'.', '.', '.', '.', '#', '.', '.', '.', '#', '.', '.', '.', '.'},
            {'.', '.', '.', '.', '#', '.', '.', '.', '#', '.', '.', '.', '.'},
            {'.', '.', '.', '.', '#', '.', '.', '.', '#', '.', '.', '.', '.'},
            {'.', '.', '.', '.', '.', '.', '#', '.', '.', '.', '.', '.', '.'},
            {'#', '#', '#', '.', '.', '.', '#', '.', '.', '.', '#', '#', '#'},
            {'.', '.', '.', '.', '.', '#', '#', '#', '.', '.', '.', '.', '.'},
            {'.', '.', '.', '#', '#', '#', '#', '#', '#', '#', '.', '.', '.'},
            {'.', '.', '.', '.', '.', '#', '#', '#', '.', '.', '.', '.', '.'},
            {'#', '#', '#', '.', '.', '.', '#', '.', '.', '.', '#', '#', '#'},
            {'.', '.', '.', '.', '.', '.', '#', '.', '.', '.', '.', '.', '.'},
            {'.', '.', '.', '.', '#', '.', '.', '.', '#', '.', '.', '.', '.'},
            {'.', '.', '.', '.', '#', '.', '.', '.', '#', '.', '.', '.', '.'},
            {'.', '.', '.', '.', '#', '.', '.', '.', '#', '.', '.', '.', '.'},
        };

        final Solver.Result result = new Solver(inputGrid, words).solve();

        final var output = assertInstanceOf(Solver.Result.Sat.class, result);
        final char[][] expectedGrid = new char[][] {
            {'S', 'C', 'A', 'T', '#', 'G', 'O', 'A', '#', 'O', 'A', 'H', 'U'},
            {'E', 'I', 'R', 'E', '#', 'R', 'O', 'Z', '#', 'X', 'M', 'A', 'S'},
            {'E', 'A', 'C', 'H', '#', 'A', 'N', 'T', '#', 'T', 'Y', 'R', 'E'},
            {'M', 'O', 'H', 'E', 'L', 'S', '#', 'E', 'Y', 'E', 'L', 'I', 'D'},
            {'#', '#', '#', 'E', 'A', 'S', '#', 'C', 'O', 'R', '#', '#', '#'},
            {'S', 'N', 'A', 'S', 'H', '#', '#', '#', 'B', 'S', 'I', 'D', 'E'},
            {'O', 'E', 'R', '#', '#', '#', '#', '#', '#', '#', 'T', 'O', 'M'},
            {'B', 'O', 'M', 'B', 'O', '#', '#', '#', 'C', 'Z', 'A', 'R', 'S'},
            {'#', '#', '#', 'A', 'B', 'A', '#', 'J', 'U', 'S', '#', '#', '#'},
            {'U', 'J', 'A', 'M', 'A', 'A', '#', 'A', 'Z', 'A', 'L', 'E', 'A'},
            {'M', 'A', 'R', 'A', '#', 'R', 'U', 'C', '#', 'Z', 'I', 'M', 'B'},
            {'B', 'I', 'L', 'K', '#', 'O', 'N', 'O', '#', 'S', 'K', 'U', 'A'},
            {'O', 'L', 'E', 'O', '#', 'N', 'A', 'B', '#', 'A', 'E', 'S', 'C'},
        };
        assertArrayEquals(expectedGrid, output.grid());
    }

    // ~45s at 1GHz
    @Test
    @Disabled("too slow to run automatically")
    void shaded13x13WithLongWords() throws InterruptedException {
        final char[][] inputGrid = new char[][] {
            {'.', '.', '.', '.', '#', '.', '.', '.', '#', '.', '.', '.', '.'},
            {'.', '.', '.', '.', '#', '.', '.', '.', '#', '.', '.', '.', '.'},
            {'.', '.', '.', '.', '#', '.', '.', '.', '#', '.', '.', '.', '.'},
            {'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'},
            {'#', '#', '#', '.', '.', '.', '#', '.', '.', '.', '#', '#', '#'},
            {'.', '.', '.', '.', '.', '#', '#', '#', '.', '.', '.', '.', '.'},
            {'.', '.', '.', '.', '#', '#', '#', '#', '#', '.', '.', '.', '.'},
            {'.', '.', '.', '.', '.', '#', '#', '#', '.', '.', '.', '.', '.'},
            {'#', '#', '#', '.', '.', '.', '#', '.', '.', '.', '#', '#', '#'},
            {'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'},
            {'.', '.', '.', '.', '#', '.', '.', '.', '#', '.', '.', '.', '.'},
            {'.', '.', '.', '.', '#', '.', '.', '.', '#', '.', '.', '.', '.'},
            {'.', '.', '.', '.', '#', '.', '.', '.', '#', '.', '.', '.', '.'},
        };

        final Solver.Result result = new Solver(inputGrid, words).solve();

        final var output = assertInstanceOf(Solver.Result.Sat.class, result);
        final char[][] expectedGrid = new char[][] {
            {'P', 'A', 'T', 'H', '#', 'S', 'A', 'D', '#', 'C', 'H', 'A', 'I'},
            {'E', 'R', 'I', 'E', '#', 'O', 'B', 'A', '#', 'H', 'A', 'I', 'R'},
            {'A', 'B', 'E', 'L', '#', 'D', 'A', 'I', '#', 'A', 'N', 'T', 'I'},
            {'G', 'A', 'S', 'T', 'R', 'O', 'C', 'N', 'E', 'M', 'I', 'U', 'S'},
            {'#', '#', '#', 'E', 'A', 'R', '#', 'T', 'A', 'M', '#', '#', '#'},
            {'S', 'A', 'B', 'R', 'E', '#', '#', '#', 'T', 'Y', 'C', 'H', 'O'},
            {'E', 'Y', 'A', 'S', '#', '#', '#', '#', '#', 'L', 'W', 'O', 'W'},
            {'N', 'E', 'R', 'K', 'A', '#', '#', '#', 'S', 'E', 'M', 'I', 'S'},
            {'#', '#', '#', 'E', 'N', 'E', '#', 'O', 'K', 'A', '#', '#', '#'},
            {'P', 'R', 'O', 'L', 'I', 'F', 'E', 'R', 'A', 'T', 'I', 'V', 'E'},
            {'R', 'O', 'R', 'T', '#', 'I', 'N', 'A', '#', 'H', 'O', 'E', 'R'},
            {'Y', 'O', 'R', 'E', '#', 'T', 'E', 'C', '#', 'E', 'W', 'E', 'R'},
            {'S', 'P', 'A', 'R', '#', 'S', 'W', 'Y', '#', 'R', 'A', 'P', 'S'},
        };
        assertArrayEquals(expectedGrid, output.grid());
    }

    // ~2min24s at 1GHz
    @Test
    @Disabled("too slow to run automatically")
    void shaded15x15() throws InterruptedException {
        final char[][] inputGrid = new char[][] {
            {'.', '.', '.', '.', '#', '.', '.', '.', '.', '.', '#', '.', '.', '.', '.'},
            {'.', '.', '.', '.', '#', '.', '.', '.', '.', '.', '#', '.', '.', '.', '.'},
            {'.', '.', '.', '.', '#', '.', '.', '.', '.', '.', '#', '.', '.', '.', '.'},
            {'.', '.', '.', '.', '.', '.', '.', '.', '#', '.', '.', '.', '.', '.', '.'},
            {'#', '#', '#', '.', '.', '.', '.', '#', '.', '.', '.', '.', '#', '#', '#'},
            {'.', '.', '.', '.', '.', '.', '#', '.', '.', '.', '.', '.', '.', '.', '.'},
            {'.', '.', '.', '.', '.', '#', '.', '.', '.', '.', '.', '#', '.', '.', '.'},
            {'.', '.', '.', '.', '#', '.', '.', '.', '.', '.', '#', '.', '.', '.', '.'},
            {'.', '.', '.', '#', '.', '.', '.', '.', '.', '#', '.', '.', '.', '.', '.'},
            {'.', '.', '.', '.', '.', '.', '.', '.', '#', '.', '.', '.', '.', '.', '.'},
            {'#', '#', '#', '.', '.', '.', '.', '#', '.', '.', '.', '.', '#', '#', '#'},
            {'.', '.', '.', '.', '.', '.', '#', '.', '.', '.', '.', '.', '.', '.', '.'},
            {'.', '.', '.', '.', '#', '.', '.', '.', '.', '.', '#', '.', '.', '.', '.'},
            {'.', '.', '.', '.', '#', '.', '.', '.', '.', '.', '#', '.', '.', '.', '.'},
            {'.', '.', '.', '.', '#', '.', '.', '.', '.', '.', '#', '.', '.', '.', '.'},
        };

        final Solver.Result result = new Solver(inputGrid, words).solve();

        final var output = assertInstanceOf(Solver.Result.Sat.class, result);
        final char[][] expectedGrid = new char[][] {
            {'I', 'P', 'O', 'H', '#', 'Q', 'U', 'A', 'T', 'S', '#', 'M', 'I', 'N', 'A'},
            {'T', 'A', 'R', 'O', '#', 'U', 'P', 'T', 'I', 'E', '#', 'A', 'C', 'E', 'R'},
            {'C', 'R', 'A', 'W', '#', 'A', 'T', 'O', 'L', 'L', '#', 'L', 'E', 'N', 'A'},
            {'H', 'A', 'N', 'D', 'G', 'R', 'I', 'P', '#', 'A', 'W', 'A', 'R', 'E', 'R'},
            {'#', '#', '#', 'Y', 'L', 'K', 'E', '#', 'A', 'D', 'A', 'W', '#', '#', '#'},
            {'M', 'A', 'H', 'D', 'I', 'S', '#', 'A', 'M', 'A', 'R', 'I', 'L', 'L', 'O'},
            {'N', 'A', 'B', 'O', 'B', '#', 'S', 'C', 'E', 'N', 'D', '#', 'O', 'E', 'R'},
            {'E', 'R', 'O', 'S', '#', 'S', 'T', 'U', 'N', 'G', '#', 'P', 'U', 'R', 'R'},
            {'M', 'O', 'M', '#', 'T', 'E', 'A', 'T', 'S', '#', 'B', 'L', 'I', 'N', 'I'},
            {'E', 'N', 'B', 'R', 'O', 'S', 'S', 'E', '#', 'P', 'E', 'A', 'S', 'E', 'S'},
            {'#', '#', '#', 'O', 'A', 'T', 'H', '#', 'K', 'A', 'N', 'T', '#', '#', '#'},
            {'Y', 'A', 'U', 'N', 'D', 'E', '#', 'G', 'O', 'U', 'J', 'E', 'E', 'R', 'S'},
            {'I', 'N', 'R', 'E', '#', 'T', 'R', 'E', 'K', 'S', '#', 'A', 'P', 'I', 'A'},
            {'D', 'O', 'D', 'O', '#', 'T', 'I', 'A', 'R', 'A', '#', 'S', 'E', 'G', 'S'},
            {'S', 'A', 'S', 'S', '#', 'S', 'A', 'L', 'A', 'L', '#', 'M', 'E', 'S', 'H'},
        };
        assertArrayEquals(expectedGrid, output.grid());
    }

    @AfterAll
    static void tearDown() {
        words = null;
    }
}
