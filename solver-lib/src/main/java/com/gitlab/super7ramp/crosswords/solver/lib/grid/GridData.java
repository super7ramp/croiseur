package com.gitlab.super7ramp.crosswords.solver.lib.grid;

import com.gitlab.super7ramp.crosswords.solver.api.GridPosition;
import com.gitlab.super7ramp.crosswords.solver.lib.core.SlotIdentifier;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Stores a crossword puzzle data.
 */
final class GridData {

    /** The grid. */
    private final BoxData[][] grid;

    /** The word slots. */
    private final Map<SlotIdentifier, SlotDefinition> slots;

    /**
     * Constructor.
     *
     * @param aGrid     the grid
     * @param someSlots the word slots
     */
    GridData(final BoxData[][] aGrid, final Map<SlotIdentifier, SlotDefinition> someSlots) {
        grid = aGrid;
        slots = Collections.unmodifiableMap(someSlots);
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

        // Unmodifiable map of immutable objects, no need to copy
        slots = other.slots;
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
        return new SlotData(slots.get(slotId), grid);
    }

    /**
     * Returns all the {@link SlotData}.
     *
     * @return all the {@link SlotData}
     */
    Map<SlotIdentifier, SlotData> slots() {
        final Map<SlotIdentifier, SlotData> result = new HashMap<>();
        slots.entrySet()
             .stream()
             .map(entry -> Map.entry(entry.getKey(), new SlotData(entry.getValue(), grid)))
             .forEach(entry -> result.put(entry.getKey(), entry.getValue()));
        return result;
    }

    /**
     * Returns the slots connected to given slot identifier
     */
    Map<SlotIdentifier, SlotData> connectedSlots(final SlotIdentifier slotId) {
        final SlotDefinition slotDefinition = slots.get(slotId);
        final Map<SlotIdentifier, SlotData> result = new HashMap<>();
        slots.entrySet()
             .stream()
             .filter(entry -> entry.getValue().isConnected(slotDefinition))
             .map(entry -> Map.entry(entry.getKey(), new SlotData(entry.getValue(), grid)))
             .forEach(entry -> result.put(entry.getKey(), entry.getValue()));
        return result;
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
}
