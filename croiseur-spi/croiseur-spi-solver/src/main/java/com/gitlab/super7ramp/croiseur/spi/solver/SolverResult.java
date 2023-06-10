/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.spi.solver;

import com.gitlab.super7ramp.croiseur.common.puzzle.GridPosition;

import java.util.Map;
import java.util.Set;

/**
 * Puzzle solving result.
 */
public interface SolverResult {

    /**
     * Kind of result.
     */
    enum Kind {
        /** The grid has been successfully solved. */
        SUCCESS,
        /** No solution exists. */
        IMPOSSIBLE;

        /**
         * Return {@code true} if result is a success
         *
         * @return {@code true} if result is a success
         */
        public boolean isSuccess() {
            return this == SUCCESS;
        }
    }

    /**
     * @return the result kind
     */
    Kind kind();

    /**
     * The filled boxes.
     * <p>
     * Contains the entire grid filled if {@link #kind()} is {@link Kind#SUCCESS}.
     * <p>
     * When {@link #kind()} is {@link Kind#IMPOSSIBLE}, the returned map contains only the boxes
     * that have been successfully filled, either by the solver or pre-filled. A special situation
     * is when a pre-filled box is not in the dictionary: In this case, the box will be here as well
     * as in {@link #unsolvableBoxes()}.
     *
     * @return the filled boxes
     */
    Map<GridPosition, Character> filledBoxes();

    /**
     * Returns the boxes for which no solution could be found or an empty set if {@link #kind()} is
     * {@link Kind#SUCCESS}
     *
     * @return the boxes for which no solution could be found
     */
    Set<GridPosition> unsolvableBoxes();

}
