/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.solver.ginsberg.history;

import re.belv.croiseur.solver.ginsberg.core.Slot;

/** Write access to {@link History}. */
public interface HistoryWriter extends History {

    /**
     * Creates an instance.
     *
     * @return a new writer
     */
    static HistoryWriter create() {
        return new HistoryImpl();
    }

    /**
     * Records an assignment.
     *
     * @param variable the assigned variable
     */
    void addAssignmentRecord(final Slot variable);

    /**
     * Removes the recorded assignment.
     *
     * @param variable the unassigned variable
     */
    void removeAssignmentRecord(final Slot variable);
}
