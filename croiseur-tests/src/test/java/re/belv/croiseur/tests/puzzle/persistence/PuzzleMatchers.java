/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.tests.puzzle.persistence;

import static org.mockito.ArgumentMatchers.argThat;

import org.mockito.ArgumentMatcher;
import re.belv.croiseur.common.puzzle.SavedPuzzle;

/** Allows creating custom {@link ArgumentMatcher}s related to puzzles. */
final class PuzzleMatchers {

    /** Private constructor to prevent instantiation, static utilities only. */
    private PuzzleMatchers() {
        // Nothing to do.
    }

    /**
     * Allows creating a matcher on a {@link SavedPuzzle} id.
     *
     * @return {@code null}
     */
    static SavedPuzzle withId(final long id) {
        return argThat(actual -> actual.id() == id);
    }
}
