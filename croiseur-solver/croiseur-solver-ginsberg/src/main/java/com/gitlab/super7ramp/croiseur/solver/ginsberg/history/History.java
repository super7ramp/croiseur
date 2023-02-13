/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.solver.ginsberg.history;

import com.gitlab.super7ramp.croiseur.solver.ginsberg.core.SlotIdentifier;

/**
 * Assignment history. Useful for backtracking.
 */
public interface History {

    /**
     * Returns the "assignment date" of this slot.
     * <p>
     * The assignment date is a value from 1 to {@link Long#MAX_VALUE} - 1.
     * <p>
     * If slot is not assigned, returns {@link Long#MAX_VALUE}.
     *
     * @param slot the slot
     * @return the assignment age of the given slot, or {@link Long#MAX_VALUE} if not assigned
     */
    long assignmentDate(final SlotIdentifier slot);
}
