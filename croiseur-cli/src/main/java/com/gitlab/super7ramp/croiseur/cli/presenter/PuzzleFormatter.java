/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.cli.presenter;

import com.gitlab.super7ramp.croiseur.cli.l10n.ResourceBundles;
import com.gitlab.super7ramp.croiseur.common.puzzle.GridPosition;
import com.gitlab.super7ramp.croiseur.common.puzzle.PuzzleDetails;
import com.gitlab.super7ramp.croiseur.common.puzzle.PuzzleGrid;
import com.gitlab.super7ramp.croiseur.common.puzzle.SavedPuzzle;
import com.gitlab.super7ramp.croiseur.spi.presenter.solver.SolverResult;
import picocli.CommandLine;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Formats crossword puzzles.
 */
final class PuzzleFormatter {

    /** The format used to display unsolvable boxes (red background). */
    private static final String UNSOLVABLE_FORMAT = "bg(red)";

    /** The default format used to display boxes. */
    private static final String DEFAULT_FORMAT = "reset";

    /** Line separator. */
    private static final String LINE_SEPARATOR = System.lineSeparator();

    /** Shaded/empty box. */
    private static final char EMPTY = ' ';

    /** Column separator. */
    private static final char COLUMN_SEPARATOR = '|';

    /**
     * Private constructor, static utility methods only.
     */
    private PuzzleFormatter() {
        // Nothing to do.
    }

    /**
     * Formats the given saved puzzle.
     *
     * @param savedPuzzle the saved puzzle
     * @return the formatted saved puzzle
     */
    static String formatSavedPuzzle(final SavedPuzzle savedPuzzle) {
        final PuzzleDetails details = savedPuzzle.details();
        return "Id: " + savedPuzzle.id() + LINE_SEPARATOR +
               "Rev: " + savedPuzzle.revision() + LINE_SEPARATOR +
               "Title: " + details.title() + LINE_SEPARATOR +
               "Author: " + details.author() + LINE_SEPARATOR +
               "Editor: " + details.editor() + LINE_SEPARATOR +
               "Copyright: " + details.copyright() + LINE_SEPARATOR +
               "Date: " + details.date().map(LocalDate::toString).orElse("") + LINE_SEPARATOR +
               "Grid:" + LINE_SEPARATOR + formatPuzzleGrid(savedPuzzle.grid());
    }

    /**
     * Formats the given puzzle grid.
     *
     * @param puzzle the puzzle grid
     * @return the formatted puzzle grid
     */
    static String formatPuzzleGrid(final PuzzleGrid puzzle) {
        return formatPuzzleGrid(puzzle, Collections.emptySet());
    }

    /**
     * Formats the given puzzle grid.
     * <p>
     * Given unsolvable positions will be formatted using {@link #UNSOLVABLE_FORMAT}.
     *
     * @param grid            the puzzle grid
     * @param unsolvableBoxes optionally, the unsolvable boxes
     * @return the formatted puzzle
     */
    static String formatPuzzleGrid(final PuzzleGrid grid, final Set<GridPosition> unsolvableBoxes) {
        final Map<GridPosition, Character> filledBoxes = grid.filled();
        final Set<GridPosition> shadedBoxes = grid.shaded();
        final Set<GridPosition> positions = new HashSet<>(filledBoxes.keySet());
        positions.addAll(unsolvableBoxes);
        positions.addAll(grid.shaded());
        final int width = width(positions);
        final int height = height(positions);
        final StringBuilder sb = new StringBuilder();
        for (int y = 0; y < height; y++) {
            sb.append(COLUMN_SEPARATOR);
            for (int x = 0; x < width; x++) {
                final GridPosition position = new GridPosition(x, y);
                final Character value;
                if (shadedBoxes.contains(position)) {
                    value = '#';
                } else {
                    value = filledBoxes.getOrDefault(position, EMPTY);
                }
                final String format = unsolvableBoxes.contains(position) ? UNSOLVABLE_FORMAT :
                        DEFAULT_FORMAT;
                final String box =
                        CommandLine.Help.Ansi.AUTO.string("@|" + format + " " + value + "|@");
                sb.append(box);
                sb.append(COLUMN_SEPARATOR);
            }
            sb.append(LINE_SEPARATOR);
        }
        return sb.toString();
    }

    /**
     * Formats the solver result.
     *
     * @return the formatted solver result
     */
    static String formatSolverResult(final SolverResult result) {
        final boolean success = result.isSuccess();
        final String resultHeaderKey = $("result.header");
        final String resultHeaderValue = $(toL10nKey(success));
        final String formattedPuzzle = formatPuzzleGrid(result.grid(), result.unsolvableBoxes());
        return resultHeaderKey + resultHeaderValue + LINE_SEPARATOR + LINE_SEPARATOR +
               formattedPuzzle;
    }

    /**
     * Returns the localised message with given key.
     *
     * @param key the message key
     * @return the localised message
     */
    private static String $(final String key) {
        return ResourceBundles.$("presenter.solver." + key);
    }

    private static int width(final Set<GridPosition> positions) {
        return positions.stream()
                        .map(GridPosition::x)
                        .max(Comparator.naturalOrder())
                        .orElse(-1) + 1;
    }

    private static int height(final Set<GridPosition> positions) {
        return positions.stream()
                        .map(GridPosition::y)
                        .max(Comparator.naturalOrder())
                        .orElse(-1) + 1;
    }

    private static String toL10nKey(final boolean success) {
        return success ? "result.success" : "result.impossible";
    }
}
