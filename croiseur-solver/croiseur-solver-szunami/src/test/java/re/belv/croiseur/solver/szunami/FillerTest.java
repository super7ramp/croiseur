/*
 * SPDX-FileCopyrightText: 2025 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.solver.szunami;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.junit.jupiter.api.Test;

/** Tests for {@link Filler}. */
final class FillerTest {

    /**
     * Verifies that native code panic upon a {@code null} {@link Crossword} is correctly caught and turned into a
     * {@link NativePanicException}.
     */
    @Test
    void failureNullCrossword() {
        final Dictionary emptyDictionary = new Dictionary(Collections.emptySet());

        final NativePanicException exception =
                assertThrows(NativePanicException.class, () -> new Filler().fill(null, emptyDictionary));
        assertEquals(
                "Call to re.belv.croiseur.solver.szunami.Crossword method "
                        + "failed: NullPtr(\"call_method obj argument\")",
                exception.getMessage());
    }

    /**
     * Simple 3x3 with a tailored dictionary.
     *
     * @throws InterruptedException should not happen
     */
    @Test
    void possible3x3() throws InterruptedException {
        final Crossword crossword = new Crossword("""
                \s\s\s
                \s\s\s
                \s\s\s
                """, 3, 3);
        final Dictionary dictionary = new Dictionary(List.of("AAA", "BBB", "CDE", "ABC", "ABD", "ABE"));

        final Result result = new Filler().fill(crossword, dictionary);

        assertTrue(result.isOk());
        final Crossword solution = result.solution();
        assertEquals("""
                ABC
                ABD
                ABE
                """, solution.contents());
        assertEquals(3, solution.width());
        assertEquals(3, solution.height());
    }

    /**
     * Simple 3x3 with a tailored dictionary and pre-filled and shaded boxes.
     *
     * @throws InterruptedException should not happen
     */
    @Test
    void possible3x3PreFilled() throws InterruptedException {
        final Crossword crossword = new Crossword("""
                \s\sC
                \s\s*
                \s\sE
                """, 3, 3);
        final Dictionary dictionary = new Dictionary(List.of(
                "AAA", "BBB", "ABC", "AB", "ABE", "C", "E")); // solver considers 1-character slot, hence "C" and "E"

        final Result result = new Filler().fill(crossword, dictionary);

        assertTrue(result.isOk());
        final Crossword solution = result.solution();
        assertEquals("""
                ABC
                AB*
                ABE
                """, solution.contents());
        assertEquals(3, solution.width());
        assertEquals(3, solution.height());
    }

    /**
     * Dictionary is empty so there is no solution.
     *
     * @throws InterruptedException should not happen
     */
    @Test
    void impossible3x3() throws InterruptedException {
        final Crossword crossword = new Crossword("""
                \s\s\s
                \s\s\s
                \s\s\s
                """, 3, 3);
        final Dictionary emptyDictionary = new Dictionary(Collections.emptySet());

        final Result result = new Filler().fill(crossword, emptyDictionary);

        assertTrue(result.isErr());
        assertEquals("We failed" /* :) */, result.error());
    }

    /**
     * Verifies that an {@link InterruptedException} is thrown when filler is interrupted.
     *
     * @throws InterruptedException if test is interrupted, should not happen
     * @throws ExecutionException should not happen
     * @throws TimeoutException should not happen
     */
    @Test
    void interruption() throws InterruptedException, ExecutionException, TimeoutException {
        final Crossword crossword = new Crossword("""
                \s\s\s
                \s\s\s
                \s\s\s
                """, 3, 3);
        final Dictionary dictionary = new Dictionary(List.of("AAA", "BBB", "CDE", "ABC", "ABD", "ABE"));

        final ExecutorService executor = Executors.newSingleThreadExecutor();
        final Future<Result> result = executor.submit(() -> {
            try {
                return new Filler().fill(crossword, dictionary);
            } catch (final InterruptedException e) {
                assertEquals("Filler interrupted", e.getMessage());
                return Result.err("Interrupted");
            }
        });
        executor.shutdownNow();
        assertTrue(result.get(1L, TimeUnit.SECONDS).isErr());
    }
}
