/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.solver.ginsberg.dictionary;

import com.gitlab.super7ramp.croiseur.solver.ginsberg.core.Slot;
import com.gitlab.super7ramp.croiseur.solver.ginsberg.core.SlotIdentifier;

import java.util.List;
import java.util.stream.Stream;

/**
 * A dictionary caching results of potentially slow external dictionary.
 * <p>
 * The following methods do not modify the dictionary, they are read-only.
 */
public interface CachedDictionary {

    /**
     * Returns the candidates for given variable as a new {@link Stream}.
     *
     * @param wordVariable a variable
     * @return the candidates for given variable
     */
    Stream<String> candidates(final Slot wordVariable);

    /**
     * Returns the number of candidates for given variable.
     * <p>
     * To be preferred over counting stream provided by {@link #candidates(Slot)} if no
     * intermediate stream operation needed, that is to say prefer
     * {@code candidatesCount(variable)} over {@code candidates(variable).count()}.
     *
     * @param wordVariable a variable
     * @return the candidates for given variable
     */
    long candidatesCount(final Slot wordVariable);

    /**
     * Returns the number of candidates for given variable.
     * <p>
     * Similar to {@link #candidatesCount(Slot)} but indicates that the cached count shall be
     * refined.
     * <p>
     * To be used when probing <em>assignment candidates</em>, i.e. when puzzle is not in sync
     * with the puzzle data backing this dictionary.
     *
     * @param wordVariable   the variable for which to get the candidates count
     * @param probedVariable variable which is not in sync with the puzzle data backing this
     *                       {@link CachedDictionary}; connected slots candidates will be refined
     *                       using cached result and current state
     * @return the candidates for given variable
     * @see #reevaluatedCandidatesCount(Slot, List)
     */
    long refinedCandidatesCount(final Slot wordVariable, final SlotIdentifier probedVariable);

    /**
     * Returns the number of candidates for given variable.
     * <p>
     * Similar to {@link #candidatesCount(Slot)} but indicates that the cached count shall not be
     * taken into account and the count shall be completely re-evaluated.
     * <p>
     * To be used when probing <em>unassignment candidates</em>, i.e. when puzzle is not in
     * sync with the puzzle data backing this dictionary.
     *
     * @param wordVariable    the variable for which to get the candidates count
     * @param probedVariables variables which are not in sync with the puzzle data backing this
     *                        {@link CachedDictionary}; connected slots candidates will be
     *                        re-evaluated using initial candidates and current state
     * @return the candidates for given variable
     * @see #refinedCandidatesCount(Slot, SlotIdentifier)
     */
    long reevaluatedCandidatesCount(final Slot wordVariable,
                                    final List<SlotIdentifier> probedVariables);
}
