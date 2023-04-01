/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.solver.ginsberg.dictionary;

import com.gitlab.super7ramp.croiseur.solver.ginsberg.Dictionary;
import com.gitlab.super7ramp.croiseur.solver.ginsberg.core.Slot;
import com.gitlab.super7ramp.croiseur.solver.ginsberg.elimination.EliminationSpace;

import java.util.Collection;

/**
 * Write access to the {@link CachedDictionary}.
 */
public interface CachedDictionaryWriter extends CachedDictionary {

    /**
     * Create a new {@link CachedDictionaryWriter}.
     *
     * @param dictionary       a dictionary
     * @param slots            the variables
     * @param eliminationSpace the eliminated candidates
     * @return the new {@link CachedDictionaryWriter}
     */
    static CachedDictionaryWriter create(final Dictionary dictionary, final Collection<Slot> slots,
                                         final EliminationSpace eliminationSpace) {
        return new CachedDictionaryImpl(dictionary, slots, eliminationSpace);
    }

    /**
     * Invalidates {@link #cachedCandidatesCount(Slot)} upon an assignment/unassignment.
     * <p>
     * Cache will be rebuilt on next call to {@link #cachedCandidatesCount(Slot)}.
     *
     * @param modifiedSlot the assigned/unassigned slot
     */
    void invalidateCacheCount(final Slot modifiedSlot);

}
