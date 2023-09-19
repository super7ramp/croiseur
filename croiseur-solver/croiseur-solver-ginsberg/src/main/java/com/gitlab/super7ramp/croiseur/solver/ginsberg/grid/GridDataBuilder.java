/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.solver.ginsberg.grid;

import com.gitlab.super7ramp.croiseur.common.puzzle.GridPosition;
import com.gitlab.super7ramp.croiseur.common.puzzle.PuzzleGrid;
import com.gitlab.super7ramp.croiseur.solver.ginsberg.core.SlotIdentifier;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A {@link GridData} builder.
 */
final class GridDataBuilder {

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
    GridDataBuilder() {
        shaded = new HashSet<>();
        prefilled = new HashMap<>();
    }

    /**
     * Uses a {@link PuzzleGrid} to build the grid.
     *
     * @param puzzle the {@link PuzzleGrid}
     * @return this {@link GridDataBuilder}
     */
    static GridDataBuilder from(final PuzzleGrid puzzle) {
        final var builder = new GridDataBuilder();
        builder.width = puzzle.width();
        builder.height = puzzle.height();
        builder.shaded.addAll(puzzle.shaded());
        builder.prefilled.putAll(puzzle.filled());
        return builder;
    }

    /**
     * Specify a height for {@link GridData} to build.
     *
     * @param anHeight the height
     * @return this builder
     */
    GridDataBuilder withHeight(final int anHeight) {
        height = anHeight;
        return this;
    }

    /**
     * Specify a width for the {@link GridData} to build.
     *
     * @param aWidth the width
     * @return this builder
     */
    GridDataBuilder withWidth(final int aWidth) {
        width = aWidth;
        return this;
    }

    /**
     * Specify the position of a shaded box.
     *
     * @param gridPosition coordinate
     * @return this builder
     */
    GridDataBuilder withShaded(final GridPosition gridPosition) {
        shaded.add(gridPosition);
        return this;
    }

    /**
     * Actually builds the data.
     *
     * @return the built data
     * @throws IllegalArgumentException if given specifications are not valid
     */
    GridData build() {
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
        final BoxData[][] result = new BoxData[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                final GridPosition coord = new GridPosition(x, y);
                if (shaded.contains(coord)) {
                    result[y][x] = Boxes.shaded();
                } else if (prefilled.containsKey(coord)) {
                    result[y][x] = Boxes.prefilled(prefilled.get(coord));
                } else {
                    result[y][x] = Boxes.computed();
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
