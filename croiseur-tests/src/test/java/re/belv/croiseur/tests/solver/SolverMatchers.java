/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.tests.solver;

import static org.mockito.ArgumentMatchers.argThat;

import java.util.Map;
import org.mockito.ArgumentMatcher;
import re.belv.croiseur.common.puzzle.GridPosition;
import re.belv.croiseur.spi.presenter.solver.SolverResult;

/** Allows creating custom {@link ArgumentMatcher}s related to solver result presentation. */
final class SolverMatchers {

    /**
     * {@link ArgumentMatcher} matching a successful solver result.
     *
     * @param solution the expected solution
     */
    private record SuccessSolverResultMatcher(Map<GridPosition, Character> solution)
            implements ArgumentMatcher<SolverResult> {

        @Override
        public boolean matches(final SolverResult actual) {
            return actual.isSuccess()
                    && actual.unsolvableBoxes().isEmpty()
                    && actual.filledBoxes().equals(solution);
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
        return argThat(actual -> !actual.isSuccess());
    }
}
