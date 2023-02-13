/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.solver.ginsberg.grid;

import com.gitlab.super7ramp.croiseur.common.GridPosition;
import com.gitlab.super7ramp.croiseur.solver.ginsberg.core.SlotIdentifier;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Stores a crossword puzzle data.
 */
final class GridData {

    /**
     * The grid.
     * <p>
     * The grid is indexed by row index (y) then by column index (x) so that it is easier to
     * print, i.e. one can access (x,y) = (col_index,row_index) with {@code grid[y][x]}.
     */
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
        for (int y = 0; y < other.grid.length; y++) {
            for (int x = 0; x < other.grid[0].length; x++) {
                grid[y][x] = other.grid[y][x].copy();
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
        for (int y = 0; y < grid.length; y++) {
            final BoxData[] row = grid[y];
            for (int x = 0; x < row.length; x++) {
                final BoxData box = row[x];
                if (!box.isEmpty()) {
                    result.put(new GridPosition(x, y), box.value());
                }
            }
        }
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(System.lineSeparator());
        for (final BoxData[] row : grid) {
            sb.append('|');
            for (final BoxData box : row) {
                sb.append(box.toString());
                sb.append('|');
            }
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }
}
