/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.solver.ginsberg.dictionary;

import re.belv.croiseur.solver.ginsberg.core.Slot;

import java.util.stream.Stream;

/**
 * A dictionary caching results of potentially slow external dictionary and taking into account
 * words eliminated by search.
 */
public interface CachedDictionary {

    /**
     * Returns the current candidates for given slot as a new {@link Stream}, taking into account
     * current elimination space.
     *
     * @param slot a slot
     * @return the candidates for given slot
     */
    Stream<String> candidates(final Slot slot);

    /**
     * Returns the cached number of candidates for given slot.
     * <p>
     * To be preferred over counting stream provided by {@link #candidates(Slot)} if no
     * intermediate stream operation needed, that is to say prefer {@code candidatesCount(slot)}
     * over {@code candidates(slot).count()}.
     *
     * @param slot a slot
     * @return the candidates for given slot
     */
    long cachedCandidatesCount(final Slot slot);

    /**
     * Returns the reevaluated candidates for given slot, ignoring current elimination space.
     * <p>
     * Similar to {@link #candidates(Slot)} but indicates that the words eliminated by search so
     * far shall be taken into account again and the candidates shall be completely reevaluated
     * from initial dictionary.
     * <p>
     * To be used when probing <em>unassignment candidates</em>.
     *
     * @param slot a slot
     * @return the candidates for given variable
     */
    Stream<String> reevaluatedCandidates(final Slot slot);
}
