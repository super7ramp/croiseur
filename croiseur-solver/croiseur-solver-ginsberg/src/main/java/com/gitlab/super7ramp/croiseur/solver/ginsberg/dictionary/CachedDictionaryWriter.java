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
     * Updates cache upon an assignment.
     * <p>
     * This will have the effect to narrow the cache. The more the cache is updated without
     * {@link #invalidateCache invalidation}, the smaller the cache will be, the faster the
     * results will be.
     *
     * @param assignedVariable the assigned variable (or variable connected to the assigned
     *                         variable)
     */
    void updateCache(final Slot assignedVariable);

    /**
     * Invalidates cache candidates upon unassignment.
     * <p>
     * Cache will be reset to its initial state then re-evaluated based on current slot state.
     *
     * @param unassignedVariable the unassigned variable (or variable linked to the unassigned
     *                           variable)
     */
    void invalidateCache(final Slot unassignedVariable);

}
