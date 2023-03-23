/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.solver.ginsberg.listener;

import com.gitlab.super7ramp.croiseur.solver.ginsberg.core.Slot;
import com.gitlab.super7ramp.croiseur.solver.ginsberg.grid.Grid;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Prints fine progress information about the solver.
 */
public final class FineProgressPrinter implements SolverListener {

    /** Logger. */
    private static Logger LOGGER = Logger.getLogger(FineProgressPrinter.class.getName());

    /** The grid being solved. */
    private final Grid grid;

    /**
     * Constructs an instance.
     *
     * @param gridArg the grid being solved
     */
    public FineProgressPrinter(final Grid gridArg) {
        grid = gridArg;
    }

    @Override
    public void onAssignment(final Slot slot, final String word) {
        LOGGER.log(Level.FINE, () -> "Assigned " + word + " to slot " + slot);
        LOGGER.log(Level.FINE, grid::toString);
    }

    @Override
    public void onUnassignment(final Slot slot, final String unassignedWord) {
        LOGGER.log(Level.FINE, () -> "Unassigned " + unassignedWord + " from slot " + slot);
        LOGGER.log(Level.FINE, grid::toString);
    }
}
