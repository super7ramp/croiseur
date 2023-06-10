/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.solver.szunami.plugin;

import com.gitlab.super7ramp.croiseur.common.puzzle.GridPosition;
import com.gitlab.super7ramp.croiseur.common.puzzle.PuzzleGrid;
import com.gitlab.super7ramp.croiseur.solver.szunami.Crossword;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for {@link PuzzleAdapter}.
 */
final class PuzzleAdapterTest {

    @Test
    void adapt() {
        final PuzzleGrid puzzleGrid =
                new PuzzleGrid.Builder()
                        .height(4)
                        .width(3)
                        .shade(new GridPosition(1, 2))
                        .fill(new GridPosition(2, 1), 'A')
                        .build();

        final Crossword crossword = PuzzleAdapter.adapt(puzzleGrid);

        assertEquals(4, crossword.height());
        assertEquals(3, crossword.width());
        assertEquals("""
                \s\s\s
                \s\sA
                \s*\s
                \s\s\s
                """, crossword.contents());
    }
}
