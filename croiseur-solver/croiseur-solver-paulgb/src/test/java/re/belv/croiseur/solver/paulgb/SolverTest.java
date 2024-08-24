/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.solver.paulgb;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.junit.jupiter.api.Test;

/** Tests for {@link Solver}. */
final class SolverTest {

    /**
     * Grid is void, so result is present but the encapsulated array is empty.
     *
     * @throws InterruptedException should not happen
     */
    @Test
    void possible0x0() throws InterruptedException {
        final Puzzle puzzle = new Puzzle(new int[0][0]);
        final Dictionary emptyDictionary = new Dictionary(Collections.emptySet());

        final Optional<Solution> solution = new Solver().solve(puzzle, emptyDictionary);

        assertTrue(solution.isPresent());
        assertEquals(0, solution.get().cells().length);
    }

    /**
     * Dictionary is empty so there is no solution.
     *
     * @throws InterruptedException should not happen
     */
    @Test
    void impossible3x3() throws InterruptedException {
        final Puzzle puzzle = new Puzzle(new int[][] {
            // horizontal slots
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8},
            // vertical slots
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8}
        });
        final Dictionary emptyDictionary = new Dictionary(Collections.emptySet());

        final Optional<Solution> solution = new Solver().solve(puzzle, emptyDictionary);

        assertTrue(solution.isEmpty());
    }

    /**
     * Simple 3x3 with a tailored dictionary.
     *
     * @throws InterruptedException should not happen
     */
    @Test
    void possible3x3() throws InterruptedException {
        final Puzzle puzzle = new Puzzle(new int[][] {
            // horizontal slots
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8},
            // vertical slots
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8}
        });
        final Dictionary dictionary = new Dictionary(List.of("AAA", "BBB", "CDE", "ABC", "ABD", "ABE"));

        final Optional<Solution> solution = new Solver().solve(puzzle, dictionary);

        assertTrue(solution.isPresent());
        final char[] cells = solution.get().cells();
        assertEquals(9, cells.length);
        assertArrayEquals(new char[] {'A', 'B', 'C', 'A', 'B', 'D', 'A', 'B', 'E'}, cells);
    }

    /** Verifies that Rust panic upon {@code null} puzzle is turned into {@link NativePanicException}. */
    @Test
    void failureNullPuzzle() {
        final NativePanicException solverError = assertThrows(
                NativePanicException.class, () -> new Solver().solve(null, new Dictionary(Collections.emptySet())));
        assertEquals("Failed to access puzzle slots: NullPtr(\"call_method obj argument\")", solverError.getMessage());
    }

    /** Verifies that Rust panic upon {@code null} dictionary is turned into {@link NativePanicException}. */
    @Test
    void failureNullDictionary() {
        final NativePanicException solverError =
                assertThrows(NativePanicException.class, () -> new Solver().solve(new Puzzle(new int[0][]), null));
        assertEquals(
                "Failed to retrieve dictionary words: NullPtr(\"call_method obj argument\")", solverError.getMessage());
    }

    /**
     * Verifies that an {@link InterruptedException} is thrown when solver is interrupted.
     *
     * @throws InterruptedException if test is interrupted, should not happen
     * @throws ExecutionException should not happen
     * @throws TimeoutException should not happen
     */
    @Test
    void interruption() throws InterruptedException, ExecutionException, TimeoutException {
        final Puzzle puzzle = new Puzzle(new int[][] {
            // horizontal slots
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8},
            // vertical slots
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8}
        });
        final Dictionary dictionary = new Dictionary(Set.of("AAA", "BBB", "CDE", "ABC", "ABD", "ABE"));

        final ExecutorService executor = Executors.newSingleThreadExecutor();
        final Future<Optional<Solution>> solution = executor.submit(() -> {
            try {
                return new Solver().solve(puzzle, dictionary);
            } catch (final InterruptedException e) {
                assertEquals("Solver interrupted", e.getMessage());
                return Optional.empty();
            }
        });
        executor.shutdownNow();
        assertTrue(solution.get(1L, TimeUnit.SECONDS).isEmpty());
    }
}
