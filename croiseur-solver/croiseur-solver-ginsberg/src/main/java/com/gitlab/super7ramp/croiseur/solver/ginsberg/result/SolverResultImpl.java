/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.solver.ginsberg.result;

import com.gitlab.super7ramp.croiseur.common.puzzle.GridPosition;
import com.gitlab.super7ramp.croiseur.solver.ginsberg.SolverResult;

import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Implementation of {@link SolverResult}.
 */
final class SolverResultImpl implements SolverResult {

    /** Not filled/shaded character. */
    private static final char NOT_FILLED = '#';

    /** Column separator. */
    private static final char COLUMN_SEPARATOR = '|';

    /** Line separator. */
    private static final String LINE_SEPARATOR = System.lineSeparator();

    /** The filled boxes, either pre-filled or filled by the solver. */
    private final Map<GridPosition, Character> filledBoxes;

    /** The unsolvable boxes, if any. */
    private final Set<GridPosition> unsolvableBoxes;

    /** The kind of result. */
    private final Kind kind;

    /** Solver statistics. */
    private final Statistics statistics;

    /**
     * Constructs an instance.
     *
     * @param kindArg            the result kind
     * @param filledBoxes        the filled boxes, either pre-filled or filled by the solver
     * @param unsolvableBoxesArg the unsolvable boxes, if any
     * @param statisticsArg      the resolution statistics
     */
    private SolverResultImpl(final Kind kindArg, final Map<GridPosition, Character> filledBoxesArg,
                             final Set<GridPosition> unsolvableBoxesArg,
                             final Statistics statisticsArg) {
        filledBoxes = filledBoxesArg;
        unsolvableBoxes = unsolvableBoxesArg;
        kind = kindArg;
        statistics = statisticsArg;
    }

    /**
     * Builds a {@link SolverResultImpl} denoting a successful resolution.
     *
     * @param solvedBoxes the solved boxes
     * @param statistics  the resolution statistics
     * @return a {@link SolverResultImpl} denoting a successful resolution
     */
    static SolverResultImpl success(final Map<GridPosition, Character> solvedBoxes,
                                    final Statistics statistics) {
        return new SolverResultImpl(Kind.SUCCESS, solvedBoxes, Collections.emptySet(), statistics);
    }

    /**
     * Builds a {@link SolverResultImpl} denoting the impossibility to solve the problem.
     *
     * @param filledBoxes     the filled boxes, if any
     * @param unsolvableBoxes the unsolvable boxes
     * @param statistics      the resolution statistics
     * @return a {@link SolverResultImpl} denoting the impossibility to solve the problem
     */
    static SolverResultImpl impossible(final Map<GridPosition, Character> filledBoxes,
                                       final Set<GridPosition> unsolvableBoxes,
                                       final Statistics statistics) {
        return new SolverResultImpl(Kind.IMPOSSIBLE, filledBoxes, unsolvableBoxes, statistics);
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
        return unsolvableBoxes;
    }

    @Override
    public Statistics statistics() {
        return statistics;
    }

    @Override
    public String toString() {
        final int width = filledBoxes.keySet()
                                     .stream()
                                     .map(coordinate -> coordinate.x() + 1)
                                     .max(Comparator.naturalOrder())
                                     .orElse(0);

        final int height = filledBoxes.keySet()
                                      .stream()
                                      .map(coordinate -> coordinate.y() + 1)
                                      .max(Comparator.naturalOrder())
                                      .orElse(0);

        final StringBuilder sb = new StringBuilder();
        sb.append("Result: ").append(kind).append(LINE_SEPARATOR);
        sb.append("Filled boxes: ").append(filledBoxes).append(LINE_SEPARATOR);
        sb.append("Grid:").append(LINE_SEPARATOR);
        for (int y = 0; y < height; y++) {
            sb.append('|');
            for (int x = 0; x < width; x++) {
                final Character value = filledBoxes.get(new GridPosition(x, y));
                if (value != null) {
                    sb.append(value);
                } else {
                    sb.append(NOT_FILLED);
                }
                sb.append(COLUMN_SEPARATOR);
            }
            sb.append(LINE_SEPARATOR);
        }
        sb.append("Statistics: ").append(statistics).append(LINE_SEPARATOR);
        if (kind == Kind.IMPOSSIBLE) {
            sb.append("Unsolvable boxes: ").append(unsolvableBoxes).append(LINE_SEPARATOR);
        }
        return sb.toString();
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof final SolverResultImpl otherSolverResultImpl)) {
            return false;
        }
        return filledBoxes.equals(otherSolverResultImpl.filledBoxes)
                && unsolvableBoxes.equals(otherSolverResultImpl.unsolvableBoxes)
                && kind == otherSolverResultImpl.kind
                && statistics.equals(otherSolverResultImpl.statistics);
    }

    @Override
    public int hashCode() {
        return Objects.hash(filledBoxes, unsolvableBoxes, kind, statistics);
    }
}
