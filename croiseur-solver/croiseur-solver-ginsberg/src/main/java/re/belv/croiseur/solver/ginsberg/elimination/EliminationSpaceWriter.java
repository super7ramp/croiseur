/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.solver.ginsberg.elimination;

import java.util.Collection;
import re.belv.croiseur.solver.ginsberg.core.SlotIdentifier;

/** Write access to the {@link EliminationSpace}. */
public interface EliminationSpaceWriter extends EliminationSpace {

    /**
     * Create a new instance.
     *
     * @return a new instance of {@link EliminationSpaceWriter}
     */
    static EliminationSpaceWriter create() {
        return new EliminationSpaceImpl();
    }

    /**
     * Eliminate the given no-good.
     *
     * @param unassigned the unassigned slot
     * @param reasons the reasons of the elimination (other relevant slots)
     * @param eliminated the eliminated value
     */
    void eliminate(final SlotIdentifier unassigned, final Collection<SlotIdentifier> reasons, final String eliminated);
}
