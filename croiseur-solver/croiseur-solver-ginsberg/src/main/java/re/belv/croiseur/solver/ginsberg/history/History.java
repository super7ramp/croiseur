/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.solver.ginsberg.history;

import re.belv.croiseur.solver.ginsberg.core.SlotIdentifier;

/** Assignment history. Useful for backtracking. */
public interface History {

    /**
     * Returns the assignment number of this slot.
     *
     * <p>If slot is not assigned, returns {@link Long#MAX_VALUE}.
     *
     * @param slot the slot
     * @return the assignment age of the given slot, or {@link Long#MAX_VALUE} if not assigned
     */
    long assignmentNumber(final SlotIdentifier slot);
}
