/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.solver.ginsberg.core.sap;

import java.util.List;

/**
 * Strategy to apply when dead-end is reached.
 *
 * @param <VariableT>          the variable type
 * @param <EliminationReasonT> the elimination reasons type
 */
public interface Backtracker<VariableT, EliminationReasonT> {

    /**
     * Determines the variable(s) to unassign to solve the dead-end reached on given variable,
     * accompanied by a reasons.
     *
     * @param variable unassignable variable
     * @return the variable(s) that should be unassigned and why; An empty list if none could be
     * found
     */
    List<Elimination<VariableT, EliminationReasonT>> backtrackFrom(final VariableT variable);
}
