/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.solver.ginsberg.elimination;

import java.util.Map;
import java.util.Set;
import re.belv.croiseur.solver.ginsberg.core.SlotIdentifier;

/** The eliminated candidates. */
public interface EliminationSpace {

    /**
     * Return the eliminated values for given slot.
     *
     * @param slot the slot for which return the eliminated values
     * @return the eliminated values for given slot
     */
    default Set<String> eliminatedValues(final SlotIdentifier slot) {
        return eliminations(slot).keySet();
    }

    /**
     * Returns the eliminations for a given slot, i.e. the eliminated values associated with the reasons of their
     * eliminations.
     *
     * @param slot the slot for which return the elimination
     * @return the eliminations for a given slot
     */
    Map<String, Set<SlotIdentifier>> eliminations(final SlotIdentifier slot);
}
