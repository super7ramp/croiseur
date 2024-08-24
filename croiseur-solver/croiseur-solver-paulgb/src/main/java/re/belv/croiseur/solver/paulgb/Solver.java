/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.solver.paulgb;

import java.util.Optional;

/**
 * A solver wrapping paulgb's crossword-composer written in Rust.
 *
 * @see <a href="https://github.com/paulgb/crossword-composer/blob/master/README.md#auto-filler">The crossword-composer
 *     documentation </a>
 */
public final class Solver {

    static {
        BundledNativeLibLoader.loadLibrary("crossword_composer_jni");
    }

    /**
     * Solves the given puzzle with the given dictionary.
     *
     * @param puzzle the puzzle to solve
     * @param dictionary the dictionary
     * @return the {@link Solution}
     * @throws InterruptedException if interrupted while solving
     * @throws NativePanicException if solver encountered an error
     */
    public native Optional<Solution> solve(final Puzzle puzzle, final Dictionary dictionary)
            throws InterruptedException;
}
