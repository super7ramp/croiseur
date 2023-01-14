/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.crosswords.solver.ginsberg.core.sap;

import java.util.Optional;

/**
 * Encapsulates the heuristics for selecting the value of a variable.
 *
 * @param <VariableT> the variable type
 * @param <ValueT>    the value type
 */
public interface CandidateChooser<VariableT, ValueT> {

    /**
     * Chooses a candidate for the given variable.
     *
     * @param wordVariable word variable to choose a candidate for
     * @return the appropriate candidate, or {@link Optional#empty()} if no suitable candidate
     * can be found
     */
    Optional<ValueT> find(final VariableT wordVariable);
}
