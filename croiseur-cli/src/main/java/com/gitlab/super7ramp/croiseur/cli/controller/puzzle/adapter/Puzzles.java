/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.cli.controller.puzzle.adapter;

import com.gitlab.super7ramp.croiseur.api.puzzle.PuzzlePatch;
import com.gitlab.super7ramp.croiseur.common.puzzle.GridPosition;
import com.gitlab.super7ramp.croiseur.common.puzzle.Puzzle;
import com.gitlab.super7ramp.croiseur.common.puzzle.PuzzleDetails;
import com.gitlab.super7ramp.croiseur.common.puzzle.PuzzleGrid;

import java.time.LocalDate;
import java.util.Optional;

import static com.gitlab.super7ramp.croiseur.common.puzzle.GridPosition.at;

/**
 * Conversions from command-line to domain puzzle types.
 */
public final class Puzzles {

    /** Private constructor to prevent instantiation, static methods only. */
    private Puzzles() {
        // Nothing to do.
    }

    /**
     * Converts command-line options to a {@link Puzzle}.
     *
     * @param title     the title option value
     * @param author    the author option value
     * @param editor    the editor option value
     * @param copyright the copyright option value
     * @param date      the date option value
     * @param gridRows  the grid rows
     * @return the corresponding {@link Puzzle}
     */
    public static Puzzle puzzleFrom(final Optional<String> title, final Optional<String> author,
                                    final Optional<String> editor, final Optional<String> copyright,
                                    final Optional<LocalDate> date, final String gridRows) {
        final PuzzleDetails details =
                new PuzzleDetails(title.orElse(""), author.orElse(""), editor.orElse(""),
                                  copyright.orElse(""), date);
        final PuzzleGrid grid = puzzleGridFrom(gridRows);
        return new Puzzle(details, grid);
    }

    /**
     * Converts command-line options to {@link PuzzlePatch}.
     *
     * @param title     the modified title, if any
     * @param author    the modified author, if any
     * @param editor    the modified editor, if any
     * @param copyright the modified copyright, if any
     * @param date      the modified date, if any
     * @param gridRows  the modified grid rows, if any
     * @return the created {@link PuzzlePatch}
     */
    public static PuzzlePatch puzzlePatchFrom(final Optional<String> title,
                                              final Optional<String> author,
                                              final Optional<String> editor,
                                              final Optional<String> copyright,
                                              final Optional<LocalDate> date,
                                              final Optional<String> gridRows) {
        return new PuzzlePatch() {
            @Override
            public Optional<String> modifiedTitle() {
                return title;
            }

            @Override
            public Optional<String> modifiedAuthor() {
                return author;
            }

            @Override
            public Optional<String> modifiedEditor() {
                return editor;
            }

            @Override
            public Optional<String> modifiedCopyright() {
                return copyright;
            }

            @Override
            public Optional<LocalDate> modifiedDate() {
                return date;
            }

            @Override
            public Optional<PuzzleGrid> modifiedGrid() {
                return gridRows.map(Puzzles::puzzleGridFrom);
            }
        };

    }

    private static PuzzleGrid puzzleGridFrom(final String gridRows) {
        final PuzzleGrid.Builder gridBuilder = new PuzzleGrid.Builder();
        final String[] rows = gridRows.split(",");
        for (int rowIndex = 0; rowIndex < rows.length; rowIndex++) {
            final String line = rows[rowIndex].trim();
            for (int columnIndex = 0; columnIndex < line.length(); columnIndex++) {
                final char cell = line.charAt(columnIndex);
                final GridPosition position = at(columnIndex, rowIndex);
                if (cell == '#') {
                    gridBuilder.shade(position);
                } else if (cell != ' ' && cell != '.') {
                    gridBuilder.fill(position, cell);
                } // else cell is empty
            }
        }
        return gridBuilder.width(rows[0].length()).height(rows.length).build();
    }
}
