/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.crosswords.solver.ginsberg.history;

import com.gitlab.super7ramp.crosswords.solver.ginsberg.core.Slot;

/**
 * Write access to {@link History}.
 */
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
