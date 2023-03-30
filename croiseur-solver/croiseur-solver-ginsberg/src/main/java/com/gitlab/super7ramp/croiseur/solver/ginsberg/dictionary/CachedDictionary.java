/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.solver.ginsberg.dictionary;

import com.gitlab.super7ramp.croiseur.solver.ginsberg.core.Slot;

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
     * Returns the refined candidates for given variable.
     * <p>
     * Similar to {@link #candidates(Slot)} but indicates that the cached candidates shall be
     * refined.
     * <p>
     * To be used when probing <em>assignment candidates</em>, i.e. when puzzle is not in sync
     * with the puzzle data backing this dictionary.
     *
     * @param wordVariable the variable for which to get the candidates count
     * @return the candidates for given variable
     * @see #reevaluatedCandidates(Slot)
     */
    Stream<String> refinedCandidates(final Slot wordVariable);

    /**
     * Returns the candidates for given variable.
     * <p>
     * Similar to {@link #candidates(Slot)} but indicates that the cached candidates shall not be
     * taken into account at all and the candidates shall be completely re-evaluated from initial
     * dictionary.
     * <p>
     * To be used when probing <em>unassignment candidates</em>, i.e. when puzzle is not in
     * sync with the puzzle data backing this dictionary.
     *
     * @param wordVariable    the variable for which to get the candidates count
     * @return the candidates for given variable
     * @see #refinedCandidates(Slot)
     */
    Stream<String> reevaluatedCandidates(final Slot wordVariable);
}
