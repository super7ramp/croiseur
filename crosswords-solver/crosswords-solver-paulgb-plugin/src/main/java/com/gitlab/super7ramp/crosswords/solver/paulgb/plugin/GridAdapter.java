package com.gitlab.super7ramp.crosswords.solver.paulgb.plugin;

import com.gitlab.super7ramp.crosswords.common.GridPosition;
import com.gitlab.super7ramp.crosswords.common.PuzzleDefinition;
import com.gitlab.super7ramp.crosswords.solver.paulgb.Grid;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Adapts {@link PuzzleDefinition} into Crossword Composer's {@link Grid}.
 */
// TODO simplify (just like GridDataBuilder in ginsberg solver, code is too convoluted)
final class GridAdapter {

    /** Private constructor - static utilities only. */
    private GridAdapter() {
        // Nothing to do.
    }

    /**
     * Adapts {@link PuzzleDefinition} into Crossword Composer's {@link Grid}.
     *
     * @param puzzleDefinition a {@link PuzzleDefinition}
     * @return Crossword Composer's {@link Grid}
     * @throws UnsupportedOperationException if the grid contains pre-filled cells: It is not
     *                                       supported by Crossword Composer
     */
    static Grid adapt(final NumberedPuzzleDefinition puzzleDefinition) {

        if (!puzzleDefinition.filled().isEmpty()) {
            throw new UnsupportedOperationException("paulgb's solver doesn't support pre-filled " +
                    "grid");
        }

        final int width = puzzleDefinition.width();
        final int height = puzzleDefinition.height();
        final Set<GridPosition> shaded = puzzleDefinition.shaded();

        final Map<GridPosition, Long> labeledBoxes = puzzleDefinition.positionToId();
        final List<List<Long>> horizontalSlots = identifyHorizontalSlots(labeledBoxes, width,
                height, shaded);
        final List<List<Long>> verticalSlots = identifyVerticalSlots(labeledBoxes, width,
                height, shaded);

        final long[][] slots = concatSlots(horizontalSlots, verticalSlots);

        return new Grid(slots);
    }

    private static long[][] concatSlots(final List<List<Long>> horizontalSlots,
                                        final List<List<Long>> verticalSlots) {
        return Stream.concat(horizontalSlots.stream(), verticalSlots.stream())
                     .map(slot -> slot.stream().mapToLong(Long::longValue).toArray())
                     .toArray(long[][]::new);
    }

    private static List<List<Long>> identifyVerticalSlots(final Map<GridPosition, Long> labeledBoxes, final int width, final int height, final Set<GridPosition> shaded) {
        final List<List<Long>> verticalSlots = new ArrayList<>();
        for (int column = 0; column < width; column++) {
            for (int startRow = 0, endRow = nextShadedOnColumn(column, 0, height, shaded); startRow < height; startRow =
                    nextVerticalSlot(column, endRow, height, shaded), endRow = nextShadedOnColumn(column, startRow,
                    height, shaded)) {
                if (endRow - startRow > 1) {
                    final List<Long> verticalSlot = new ArrayList<>();
                    for (int row = startRow; row < endRow; row++) {
                        final long id = labeledBoxes.get(new GridPosition(column, row));
                        verticalSlot.add(id);
                    }
                    verticalSlots.add(verticalSlot);
                } // else: Ignore empty slot (row starting by a shaded box) or single-letter slot
            }
        }
        return verticalSlots;
    }

    private static List<List<Long>> identifyHorizontalSlots(final Map<GridPosition, Long> labeledBoxes,
                                                            final int width, final int height,
                                                            final Set<GridPosition> shaded) {
        final List<List<Long>> horizontalSlots = new ArrayList<>();
        for (int row = 0; row < height; row++) {
            for (int startColumn = 0, endColumn = nextShadedOnLine(row, 0, width, shaded); startColumn < width; startColumn =
                    nextHorizontalSlot(row, endColumn, width, shaded), endColumn = nextShadedOnLine(row, startColumn,
                    width, shaded)) {
                if (endColumn - startColumn > 1) {
                    final List<Long> horizontalSlot = new ArrayList<>();
                    for (int column = startColumn; column < endColumn; column++) {
                        final Long id = labeledBoxes.get(new GridPosition(column, row));
                        horizontalSlot.add(id);
                    }
                    horizontalSlots.add(horizontalSlot);
                } // else: Ignore empty slot (line starting by a shaded box) or single-letter slot
            }
        }
        return horizontalSlots;
    }

    private static int nextShadedOnLine(final int row, final int startColumn, final int width,
                                        final Set<GridPosition> shaded) {
        for (int column = startColumn; column < width; column++) {
            if (shaded.contains(new GridPosition(column, row))) {
                return column;
            }
        }
        return width;
    }

    private static int nextShadedOnColumn(final int column, final int startRow, final int height,
                                          final Set<GridPosition> shaded) {
        for (int row = startRow; row < height; row++) {
            if (shaded.contains(new GridPosition(column, row))) {
                return row;
            }
        }
        return height;
    }

    private static int nextVerticalSlot(final int column, final int startRow, final int height,
                                        final Set<GridPosition> shaded) {
        for (int row = startRow; row < height; row++) {
            if (!shaded.contains(new GridPosition(column, row))) {
                return row;
            }
        }
        return height;
    }

    private static int nextHorizontalSlot(final int row, final int startColumn, final int width,
                                          final Set<GridPosition> shaded) {
        for (int column = startColumn; column < width; column++) {
            if (!shaded.contains(new GridPosition(column, row))) {
                return column;
            }
        }
        return width;
    }
}
