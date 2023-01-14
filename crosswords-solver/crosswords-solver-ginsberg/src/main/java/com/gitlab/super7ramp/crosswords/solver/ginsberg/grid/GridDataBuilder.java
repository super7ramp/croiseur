/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.crosswords.solver.ginsberg.grid;

import com.gitlab.super7ramp.crosswords.common.GridPosition;
import com.gitlab.super7ramp.crosswords.common.PuzzleDefinition;
import com.gitlab.super7ramp.crosswords.solver.ginsberg.core.SlotIdentifier;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A {@link GridData} builder.
 */
public final class GridDataBuilder {

    /** The shaded boxes. */
    private final Set<GridPosition> shaded;

    /** The prefilled (not shaded) boxes. */
    private final Map<GridPosition, Character> prefilled;

    /** The height. */
    private int height;

    /** The width. */
    private int width;

    /**
     * Constructor.
     */
    public GridDataBuilder() {
        shaded = new HashSet<>();
        prefilled = new HashMap<>();
    }

    /**
     * Specify a height for {@link GridData} to build.
     *
     * @param anHeight the height
     * @return this builder
     */
    public GridDataBuilder withHeight(final int anHeight) {
        height = anHeight;
        return this;
    }

    /**
     * Specify a width for the {@link GridData} to build.
     *
     * @param aWidth the width
     * @return this builder
     */
    public GridDataBuilder withWidth(final int aWidth) {
        width = aWidth;
        return this;
    }

    /**
     * Specify the position of a shaded box.
     *
     * @param gridPosition coordinate
     * @return this builder
     */
    public GridDataBuilder withShaded(final GridPosition gridPosition) {
        shaded.add(gridPosition);
        return this;
    }

    /**
     * Use a {@link PuzzleDefinition} to build the grid.
     *
     * @param puzzle the {@link PuzzleDefinition}
     * @return this {@link GridDataBuilder}
     */
    public GridDataBuilder from(final PuzzleDefinition puzzle) {
        width = puzzle.width();
        height = puzzle.height();
        shaded.addAll(puzzle.shaded());
        prefilled.putAll(puzzle.filled());
        return this;
    }

    /**
     * Actually builds the data.
     *
     * @return the built data
     * @throws IllegalArgumentException if given specifications are not valid
     */
    public GridData build() {
        validate();
        final BoxData[][] grid = buildGrid();
        return new GridData(grid, buildSlots(grid));
    }

    private Map<SlotIdentifier, SlotData> buildSlots(final BoxData[][] grid) {
        final Map<SlotIdentifier, SlotData> slots = new HashMap<>();
        int id = 1;
        for (int x = 0; x < width; x++) {
            // Vertical slots
            for (int yStart = 0, yEnd = nextShadedOnColumn(x, 0); yStart < height; yStart =
                    nextVerticalSlot(x, yEnd), yEnd = nextShadedOnColumn(x, yStart), id++) {
                if (yEnd - yStart > 1) {
                    slots.put(new SlotIdentifier(id), new SlotData(new SlotDefinition(x, yStart,
                            yEnd, SlotDefinition.Type.VERTICAL), grid));
                } else {
                    // Ignore empty slot (row starting by a shaded box) or single-letter slot
                }
            }
        }
        for (int y = 0; y < height; y++) {
            // Horizontal slots
            for (int xStart = 0, xEnd = nextShadedOnLine(y, 0); xStart < width; xStart =
                    nextHorizontalSlot(y, xEnd), xEnd = nextShadedOnLine(y, xStart), id++) {
                if (xEnd - xStart > 1) {
                    slots.put(new SlotIdentifier(id), new SlotData(new SlotDefinition(y, xStart,
                            xEnd, SlotDefinition.Type.HORIZONTAL), grid));
                } else {
                    // Ignore empty slot (line starting by a shaded box) or single-letter slot
                }
            }
        }

        return slots;
    }

    private int nextShadedOnLine(final int y, final int xStart) {
        for (int x = xStart; x < width; x++) {
            if (shaded.contains(new GridPosition(x, y))) {
                return x;
            }
        }
        return width;
    }

    private int nextShadedOnColumn(final int x, final int yStart) {
        for (int y = yStart; y < height; y++) {
            if (shaded.contains(new GridPosition(x, y))) {
                return y;
            }
        }
        return height;
    }

    private int nextVerticalSlot(final int x, final int yStart) {
        for (int y = yStart; y < height; y++) {
            if (!shaded.contains(new GridPosition(x, y))) {
                return y;
            }
        }
        return height;
    }

    private int nextHorizontalSlot(final int y, final int xStart) {
        for (int x = xStart; x < width; x++) {
            if (!shaded.contains(new GridPosition(x, y))) {
                return x;
            }
        }
        return width;
    }

    private BoxData[][] buildGrid() {
        final BoxData[][] result = new BoxData[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                final GridPosition coord = new GridPosition(x, y);
                if (shaded.contains(coord)) {
                    result[x][y] = Boxes.shaded();
                } else if (prefilled.containsKey(coord)) {
                    result[x][y] = Boxes.prefilled(prefilled.get(coord));
                } else {
                    result[x][y] = Boxes.computed();
                }
            }
        }
        return result;
    }

    private void validate() {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Invalid dimensions");
        }
        if (shaded.stream()
                  .anyMatch(s -> s.x() < 0 || s.x() >= width || s.y() < 0 || s.y() >= height)) {
            throw new IllegalArgumentException("Invalid shaded definitions");
        }
    }
}
