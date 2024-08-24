/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.solver.szunami.plugin;

import java.util.Map;
import java.util.Set;
import re.belv.croiseur.common.puzzle.GridPosition;
import re.belv.croiseur.common.puzzle.PuzzleGrid;
import re.belv.croiseur.solver.szunami.Crossword;

/** Adapts {@link PuzzleGrid} into xwords-rs' {@link Crossword}. */
final class PuzzleAdapter {

    /** Private constructor - static utilities only. */
    private PuzzleAdapter() {
        // Nothing to do.
    }

    /**
     * Adapts {@link PuzzleGrid} into xwords-rs' {@link Crossword}.
     *
     * @param puzzleGrid a {@link PuzzleGrid}
     * @return xwords-rs' {@link Crossword}
     */
    static Crossword adapt(final PuzzleGrid puzzleGrid) {

        final int width = puzzleGrid.width();
        final int height = puzzleGrid.height();
        final Set<GridPosition> shaded = puzzleGrid.shaded();
        final Map<GridPosition, Character> filled = puzzleGrid.filled();

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
