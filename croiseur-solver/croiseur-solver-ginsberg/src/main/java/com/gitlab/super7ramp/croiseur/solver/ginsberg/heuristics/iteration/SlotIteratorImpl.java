/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.solver.ginsberg.heuristics.iteration;

import com.gitlab.super7ramp.croiseur.solver.ginsberg.core.Slot;
import com.gitlab.super7ramp.croiseur.solver.ginsberg.core.sap.VariableIterator;
import com.gitlab.super7ramp.croiseur.solver.ginsberg.dictionary.CachedDictionary;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

import static java.util.function.Predicate.not;

/**
 * Implementation of {@link VariableIterator}.
 */
public final class SlotIteratorImpl implements VariableIterator<Slot> {

    /**
     * Associates a {@link Slot} to its number of candidates.
     */
    private record NumberOfCandidatesPerSlot(Slot slot, long numberOfCandidates) {
        // Nothing to add.
    }

    /**
     * Comparator of {@link Slot} by their openness.
     * <p>
     * Basically, a slot S1 is considered as less open (more constrained) than a slot S2 if S1 has
     * fewer candidates than S2.
     * <p>
     * If the two slots have the same number of candidates, then the percentage of empty boxes is
     * considered: S1 is considered more constrained than S2 if S1 has less empty boxes.
     * <p>
     * If the two slots also have the same ratio of empty boxes, then the slot identifier is
     * used for the sake of reproducibility.
     */
    private static final Comparator<NumberOfCandidatesPerSlot> BY_OPENNESS =
            Comparator.comparingLong(NumberOfCandidatesPerSlot::numberOfCandidates)
                      .thenComparingInt(pair -> pair.slot.emptyBoxRatio())
                      .thenComparingInt(pair -> pair.slot.uid().id());

    /** A predicate that matches not instantiated slots. */
    private static final Predicate<Slot> NOT_INSTANTIATED = not(Slot::isInstantiated);

    /** The dictionary. */
    private final CachedDictionary dictionary;

    /** All variables. */
    private final Collection<Slot> variables;

    /**
     * Constructor.
     *
     * @param slots       the slots
     * @param aDictionary the dictionary
     */
    public SlotIteratorImpl(final Collection<Slot> slots, final CachedDictionary aDictionary) {
        variables = Collections.unmodifiableCollection(slots);
        dictionary = aDictionary;
    }

    @Override
    public boolean hasNext() {
        return variables.stream().anyMatch(NOT_INSTANTIATED);
    }

    @Override
    public Slot next() {
        return variables.stream()
                        .filter(NOT_INSTANTIATED)
                        .map(slot -> new NumberOfCandidatesPerSlot(slot,
                                dictionary.candidatesCount(slot)))
                        .min(BY_OPENNESS)
                        .map(NumberOfCandidatesPerSlot::slot)
                        .orElseThrow(NoSuchElementException::new);
    }

}
