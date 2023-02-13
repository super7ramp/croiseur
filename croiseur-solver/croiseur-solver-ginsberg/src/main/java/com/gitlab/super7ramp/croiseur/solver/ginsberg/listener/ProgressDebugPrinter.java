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
 * Prints debug information
 */
public final class ProgressDebugPrinter implements SolverListener {

    /** Logger. */
    private static Logger LOGGER = Logger.getLogger(ProgressDebugPrinter.class.getName());

    /** The grid being solved. */
    private final Grid grid;

    /**
     * Constructs an instance.
     *
     * @param gridArg the grid being solved
     */
    public ProgressDebugPrinter(final Grid gridArg) {
        grid = gridArg;
    }

    @Override
    public void onAssignment(final Slot slot, final String word) {
        // TODO log level to be put at FINE and FINER
        LOGGER.log(Level.FINE, () -> "Assigned " + word + " to slot " + slot);
        LOGGER.log(Level.FINE, grid::toString);
    }

    @Override
    public void onUnassignment(final Slot slot, final String unassignedWord) {
        // TODO log level to be put at FINE and FINER
        LOGGER.log(Level.FINE, () -> "Unassigned " + unassignedWord + " from slot " + slot);
        LOGGER.log(Level.FINE, grid::toString);
    }
}
