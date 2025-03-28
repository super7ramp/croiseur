/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.solver.ginsberg.dictionary;

import java.util.Collection;
import re.belv.croiseur.solver.ginsberg.Dictionary;
import re.belv.croiseur.solver.ginsberg.core.Slot;
import re.belv.croiseur.solver.ginsberg.elimination.EliminationSpace;

/** Write access to the {@link CachedDictionary}. */
public interface CachedDictionaryWriter extends CachedDictionary {

    /**
     * Create a new {@link CachedDictionaryWriter}.
     *
     * @param dictionary a dictionary
     * @param slots the variables
     * @param eliminationSpace the eliminated candidates
     * @return the new {@link CachedDictionaryWriter}
     */
    static CachedDictionaryWriter create(
            final Dictionary dictionary, final Collection<Slot> slots, final EliminationSpace eliminationSpace) {
        return new CachedDictionaryImpl(dictionary, slots, eliminationSpace);
    }

    /**
     * Invalidates {@link #cachedCandidatesCount(Slot)} upon an assignment/unassignment.
     *
     * <p>Cache will be rebuilt on next call to {@link #cachedCandidatesCount(Slot)}.
     *
     * @param modifiedSlot the assigned/unassigned slot
     */
    void invalidateCacheCount(final Slot modifiedSlot);
}
