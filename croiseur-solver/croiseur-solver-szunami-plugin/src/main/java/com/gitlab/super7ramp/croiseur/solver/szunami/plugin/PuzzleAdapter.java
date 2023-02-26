/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.solver.szunami.plugin;

import com.gitlab.super7ramp.croiseur.common.GridPosition;
import com.gitlab.super7ramp.croiseur.common.PuzzleDefinition;
import com.gitlab.super7ramp.croiseur.solver.szunami.Crossword;

import java.util.Map;
import java.util.Set;

/**
 * Adapts {@link PuzzleDefinition} into xwords-rs' {@link Crossword}.
 */
final class PuzzleAdapter {

    /** Private constructor - static utilities only. */
    private PuzzleAdapter() {
        // Nothing to do.
    }

    /**
     * Adapts {@link PuzzleDefinition} into xwords-rs' {@link Crossword}.
     *
     * @param puzzleDefinition a {@link PuzzleDefinition}
     * @return xwords-rs' {@link Crossword}
     */
    static Crossword adapt(final PuzzleDefinition puzzleDefinition) {

        final int width = puzzleDefinition.width();
        final int height = puzzleDefinition.height();
        final Set<GridPosition> shaded = puzzleDefinition.shaded();
        final Map<GridPosition, Character> filled = puzzleDefinition.filled();

        final StringBuilder contentsBuilder = new StringBuilder();
        for (int row = 0; row < height; row++) {
            for (int column = 0; column < width; column++) {
                final GridPosition position = new GridPosition(column, row);
                if (shaded.contains(position)) {
                    contentsBuilder.append('*');
                } else if (filled.containsKey(position)) {
                    contentsBuilder.append(filled.get(position));
                } else {
                    contentsBuilder.append(' ');
                }
            }
            contentsBuilder.append('\n');
        }

        return new Crossword(contentsBuilder.toString(), width, height);
    }

}
