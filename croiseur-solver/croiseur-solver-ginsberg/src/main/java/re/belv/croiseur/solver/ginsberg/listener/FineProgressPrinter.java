/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.solver.ginsberg.listener;

import java.util.logging.Level;
import java.util.logging.Logger;
import re.belv.croiseur.solver.ginsberg.core.Slot;
import re.belv.croiseur.solver.ginsberg.grid.Grid;

/** Prints fine progress information about the solver. */
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
