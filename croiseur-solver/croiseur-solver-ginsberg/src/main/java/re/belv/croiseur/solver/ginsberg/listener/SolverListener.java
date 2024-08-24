/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.solver.ginsberg.listener;

import re.belv.croiseur.solver.ginsberg.core.Slot;

/** Listener on puzzle modification. */
public interface SolverListener {

    /**
     * Called when a slot which was entirely filled is cleared.
     *
     * @param slot the cleared slot
     * @param unassignedWord the value unset
     */
    default void onUnassignment(final Slot slot, final String unassignedWord) {
        // Do nothing by default.
    }

    /**
     * Called when a slot is filled.
     *
     * @param slot the assigned slot
     * @param word the value set
     */
    default void onAssignment(final Slot slot, final String word) {
        // Do nothing by default.
    }
}
