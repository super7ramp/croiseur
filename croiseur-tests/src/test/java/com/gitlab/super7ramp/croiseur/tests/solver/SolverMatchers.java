/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.tests.solver;

import com.gitlab.super7ramp.croiseur.common.GridPosition;
import com.gitlab.super7ramp.croiseur.spi.solver.SolverResult;
import org.mockito.ArgumentMatcher;

import java.util.Map;

import static org.mockito.ArgumentMatchers.argThat;

/**
 * Allows creating custom {@link ArgumentMatcher}s related to solver result presentation.
 */
final class SolverMatchers {

    /**
     * {@link ArgumentMatcher} matching a successful solver result.
     *
     * @param solution the expected solution
     */
    private record SuccessSolverResultMatcher(
            Map<GridPosition, Character> solution) implements ArgumentMatcher<SolverResult> {

        @Override
        public boolean matches(final SolverResult actual) {
            return actual.kind() == SolverResult.Kind.SUCCESS &&
                    actual.unsolvableBoxes().isEmpty() &&
                    actual.filledBoxes().equals(solution);
        }
    }

    /** Private constructor to prevent instantiation, static utilities only. */
    private SolverMatchers() {
        // Nothing to do.
    }

    /**
     * Allows creating a successful {@link SolverResult} matcher.
     *
     * @param solution the expected solution
     * @return {@code null}
     */
    static SolverResult success(final Map<GridPosition, Character> solution) {
        return argThat(new SuccessSolverResultMatcher(solution));
    }

    /**
     * Allows creating an "impossible" {@link SolverResult} matcher.
     *
     * @return {@code null}
     */
    static SolverResult impossible() {
        return argThat(actual -> actual.kind() == SolverResult.Kind.IMPOSSIBLE);
    }
}
