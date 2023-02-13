/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.solver.ginsberg.grid;

import com.gitlab.super7ramp.croiseur.common.GridPosition;
import com.gitlab.super7ramp.croiseur.solver.ginsberg.core.Slot;
import com.gitlab.super7ramp.croiseur.solver.ginsberg.core.SlotIdentifier;
import com.gitlab.super7ramp.croiseur.solver.ginsberg.lookahead.Assignment;
import com.gitlab.super7ramp.croiseur.solver.ginsberg.lookahead.Unassignment;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toUnmodifiableSet;

/**
 * Implementation of {@link Grid}.
 */
final class GridImpl implements Grid {

    /**
     * Implementation of {@link Puzzle}.
     */
    private static final class PuzzleImpl implements Puzzle, Connectivity {

        /**
         * The underlying data.
         */
        private final GridData data;

        /**
         * Constructor.
         *
         * @param someData actual data model
         */
        PuzzleImpl(final GridData someData) {
            data = someData;
        }

        @Override
        public Collection<Slot> slots() {
            return data.slots()
                       .entrySet()
                       .stream()
                       .map(entry -> new SlotImpl(entry.getKey(), entry.getValue(), this))
                       .collect(toUnmodifiableSet());
        }

        @Override
        public Slot slot(final SlotIdentifier slotIdentifier) {
            return new SlotImpl(slotIdentifier, data.slot(slotIdentifier), this);
        }

        @Override
        public Stream<InternalSlot> connectedSlots(final SlotIdentifier uid) {
            return data.connectedSlots(uid)
                       .entrySet()
                       .stream()
                       .map(entry -> new SlotImpl(entry.getKey(), entry.getValue(), this));
        }

        @Override
        public boolean test(final SlotIdentifier a, final SlotIdentifier b) {
            return data.connectedSlots(a).containsKey(b);
        }

        @Override
        public Collection<Slot> probe(final Assignment assignment) {
            final GridData probedData = data.copy();
            final PuzzleImpl probedPuzzle = new PuzzleImpl(probedData);
            probedPuzzle.slot(assignment.slotUid()).assign(assignment.word());
            return probedPuzzle.slots();
        }

        @Override
        public Collection<Slot> probe(final List<Unassignment> unassignments) {
            final GridData probedData = data.copy();
            final PuzzleImpl probedPuzzle = new PuzzleImpl(probedData);
            for (final Unassignment unassignment : unassignments) {
                probedPuzzle.slot(unassignment.slotUid()).unassign();
            }
            return probedPuzzle.slots();
        }
    }

    /**
     * The {@link Puzzle} implementation.
     */
    private final Puzzle puzzle;

    /**
     * The underlying data.
     */
    private final GridData data;

    /**
     * Constructor.
     *
     * @param someData actual data model
     */
    GridImpl(final GridData someData) {
        data = someData;
        puzzle = new PuzzleImpl(someData);
    }

    @Override
    public Puzzle puzzle() {
        return puzzle;
    }

    @Override
    public Map<GridPosition, Character> boxes() {
        return data.toBoxes();
    }

    @Override
    public Set<GridPosition> slotPositions(final Slot slot) {
        final SlotDefinition slotDefinition = data.slots().get(slot.uid()).definition();
        final Set<GridPosition> positions = new HashSet<>();
        for (int i = slotDefinition.start(); i < slotDefinition.end(); i++) {
            final int row, column;
            if (slotDefinition.type().isHorizontal()) {
                row = slotDefinition.offset();
                column = i;
            } else {
                row = i;
                column = slotDefinition.offset();
            }
            positions.add(new GridPosition(column, row));
        }
        return positions;
    }

    @Override
    public String toString() {
        return data.toString();
    }
}
