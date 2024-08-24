/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.solver.szunami.plugin;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import re.belv.croiseur.common.puzzle.GridPosition;
import re.belv.croiseur.common.puzzle.PuzzleGrid;
import re.belv.croiseur.solver.szunami.Result;
import re.belv.croiseur.spi.solver.SolverResult;

/** Adapts xwords-rs filler {@link Result} into {@link SolverResult}. */
final class AdaptedSolverResult implements SolverResult {

    /** The result kind. */
    private final Kind kind;

    /** The filled boxes. */
    private final Map<GridPosition, Character> filledBoxes;

    /** The unsolvable boxes. */
    private final Set<GridPosition> unsolvableBoxes;

    /**
     * Constructs an instance.
     *
     * @param result the result returned by xwords-rs
     * @param original the original puzzle
     */
    AdaptedSolverResult(final Result result, final PuzzleGrid original) {
        if (result.isOk()) {
            kind = Kind.SUCCESS;
            filledBoxes = new HashMap<>();
            final String contents = result.solution().contents();
            final String[] lines = contents.split("\n");
            for (int row = 0; row < lines.length; row++) {
                final String line = lines[row];
                for (int column = 0; column < line.length(); column++) {
                    final GridPosition position = new GridPosition(column, row);
                    final char character = line.charAt(column);
                    if (character != '*') {
                        filledBoxes.put(position, character);
                    }
                }
            }
            unsolvableBoxes = Collections.emptySet();
        } else {
            kind = Kind.IMPOSSIBLE;
            filledBoxes = Collections.emptyMap();
            unsolvableBoxes = new HashSet<>();
            for (int row = 0; row < original.height(); row++) {
                for (int column = 0; column < original.width(); column++) {
                    final GridPosition position = new GridPosition(column, row);
                    if (!original.shaded().contains(position)) {
                        unsolvableBoxes.add(position);
                    }
                }
            }
        }
    }

    @Override
    public Kind kind() {
        return kind;
    }

    @Override
    public Map<GridPosition, Character> filledBoxes() {
        return Collections.unmodifiableMap(filledBoxes);
    }

    @Override
    public Set<GridPosition> unsolvableBoxes() {
        return Collections.unmodifiableSet(unsolvableBoxes);
    }

    @Override
    public String toString() {
        return "AdaptedSolverResult{" + "kind="
                + kind + ", filledBoxes="
                + filledBoxes + ", unsolvableBoxes="
                + unsolvableBoxes + '}';
    }
}
