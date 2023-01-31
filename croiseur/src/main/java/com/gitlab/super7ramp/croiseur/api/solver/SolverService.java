/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.api.solver;

/**
 * Services pertaining to solving crossword puzzle.
 */
public interface SolverService {

    /**
     * Lists available solver providers.
     */
    void listProviders();

    /**
     * Solve a puzzle.
     *
     * @param event details about the puzzle to solve
     */
    void solve(final SolveRequest event);
}
