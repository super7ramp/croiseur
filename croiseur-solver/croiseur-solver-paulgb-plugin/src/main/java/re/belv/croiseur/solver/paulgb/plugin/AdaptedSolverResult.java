/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.solver.paulgb.plugin;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import re.belv.croiseur.common.puzzle.GridPosition;
import re.belv.croiseur.solver.paulgb.Solution;
import re.belv.croiseur.spi.solver.SolverResult;

/**
 * Adapts Crossword Composer solver result into {@link SolverResult}.
 */
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
     * @param idToPosition association between indexes and grid positions
     * @param solution     the solution
     */
    private AdaptedSolverResult(final Map<Integer, GridPosition> idToPosition, final Solution solution) {
        final char[] filledCells = solution.cells();
        if (idToPosition.size() != filledCells.length) {
            throw new IllegalArgumentException("Solver result inconsistent with input grid");
        }
        kind = Kind.SUCCESS;
        filledBoxes = new HashMap<>();
        for (int i = 0; i < filledCells.length; i++) {
            filledBoxes.put(idToPosition.get(i), filledCells[i]);
        }
        unsolvableBoxes = Collections.emptySet();
    }

    private AdaptedSolverResult(final Set<GridPosition> positions) {
        kind = Kind.IMPOSSIBLE;
        filledBoxes = Collections.emptyMap();
        // No fine information returned by solver about problematic slots, just mark all
        // cells as problematic
        unsolvableBoxes = new HashSet<>(positions);
    }

    static AdaptedSolverResult impossible(final Set<GridPosition> positions) {
        return new AdaptedSolverResult(positions);
    }

    static AdaptedSolverResult success(final Map<Integer, GridPosition> idToPosition, final Solution solution) {
        return new AdaptedSolverResult(idToPosition, solution);
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
