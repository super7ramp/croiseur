/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.solver.ginsberg.grid;

import com.gitlab.super7ramp.croiseur.common.GridPosition;
import com.gitlab.super7ramp.croiseur.solver.ginsberg.core.SlotIdentifier;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Stores a crossword puzzle data.
 */
final class GridData {

    /** The grid. */
    private final BoxData[][] grid;

    /** The word slots. */
    private final Map<SlotIdentifier, SlotData> slots;

    /**
     * Constructor.
     *
     * @param aGrid     the grid
     * @param someSlots the word slots
     */
    GridData(final BoxData[][] aGrid, final Map<SlotIdentifier, SlotData> someSlots) {
        grid = aGrid;
        slots = someSlots;
    }

    /**
     * Copy constructor.
     *
     * @param other other data
     */
    private GridData(final GridData other) {
        grid = new BoxData[other.grid.length][other.grid[0].length];
        for (int x = 0; x < other.grid.length; x++) {
            for (int y = 0; y < other.grid[0].length; y++) {
                grid[x][y] = other.grid[x][y].copy();
            }
        }

        slots = new HashMap<>(other.slots.size());
        for (final Map.Entry<SlotIdentifier, SlotData> slotEntry : other.slots.entrySet()) {
            final SlotIdentifier slotIdentifier = slotEntry.getKey();
            final SlotData data = slotEntry.getValue();
            slots.put(slotIdentifier, new SlotData(data.definition(), grid, data.isInstantiated()));
        }
    }

    /**
     * Returns a deep-copy of this {@link GridData}.
     *
     * @return a deep-copy of this {@link GridData}
     */
    GridData copy() {
        return new GridData(this);
    }

    /**
     * Returns the {@link SlotData} for given {@link SlotIdentifier}.
     *
     * @param slotId the {@link SlotIdentifier}
     * @return the corresponding {@link SlotData}
     */
    SlotData slot(final SlotIdentifier slotId) {
        return slots.get(slotId);
    }

    /**
     * Returns all the {@link SlotData}.
     *
     * @return all the {@link SlotData}
     */
    Map<SlotIdentifier, SlotData> slots() {
        return Collections.unmodifiableMap(slots);
    }

    /**
     * Returns the slots connected to given slot identifier
     */
    Map<SlotIdentifier, SlotData> connectedSlots(final SlotIdentifier slotId) {
        final SlotData slotData = slots.get(slotId);
        final SlotDefinition slotDefinition = slotData.definition();

        return slots.entrySet()
                    .stream()
                    .filter(entry -> entry.getValue().definition().isConnected(slotDefinition))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    Map<GridPosition, Character> toBoxes() {
        final Map<GridPosition, Character> result = new HashMap<>();
        for (int x = 0; x < grid.length; x++) {
            final BoxData[] row = grid[x];
            for (int y = 0; y < row.length; y++) {
                // FIXME why 2 checks on shaded?
                if (!row[y].isShaded()) {
                    final char boxValue = row[y].value();
                    if (boxValue != BoxData.EMPTY_VALUE && boxValue != BoxData.SHADED_VALUE) {
                        result.put(new GridPosition(x, y), boxValue);
                    }
                }
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return Arrays.deepToString(grid);
    }
}
