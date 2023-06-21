/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.cli.presenter;

import com.gitlab.super7ramp.croiseur.common.puzzle.SavedPuzzle;
import com.gitlab.super7ramp.croiseur.spi.presenter.puzzle.PuzzlePresenter;

import java.time.LocalDate;
import java.util.List;

import static com.gitlab.super7ramp.croiseur.cli.presenter.CliPresenterUtil.lineOf;

/**
 * CLI implementation of {@link PuzzlePresenter}.
 */
final class CliPuzzlePresenter implements PuzzlePresenter {

    /** The format to present puzzle list. */
    private static final String PUZZLE_LIST_FORMAT = "%-4s\t%-4s\t%-16s\t%-16s%n";

    /**
     * Constructs an instance.
     */
    CliPuzzlePresenter() {
        // Nothing to do.
    }

    @Override
    public void presentAvailablePuzzles(final List<SavedPuzzle> puzzles) {
        if (puzzles.isEmpty()) {
            // TODO l10n
            System.out.println("No puzzle found.");
            return;
        }
        // TODO l10n
        final String idHeader = "Id";
        final String revisionHeader = "Rev";
        final String titleHeader = "Title";
        final String dateHeader = "Date";

        System.out.printf(PUZZLE_LIST_FORMAT, idHeader, revisionHeader, titleHeader,
                          dateHeader);
        System.out.printf(PUZZLE_LIST_FORMAT, lineOf(idHeader.length()),
                          lineOf(revisionHeader.length()), lineOf(titleHeader.length()),
                          lineOf(dateHeader.length()));
        puzzles.forEach(
                puzzle -> System.out.printf(PUZZLE_LIST_FORMAT, puzzle.id(), puzzle.revision(),
                                            puzzle.details().title(),
                                            puzzle.details().date().map(
                                                    LocalDate::toString).orElse("")));
    }

    @Override
    public void presentLoadedPuzzle(final SavedPuzzle puzzle) {
        final String formattedPuzzle = PuzzleFormatter.formatSavedPuzzle(puzzle);
        System.out.println(formattedPuzzle);
    }

    @Override
    public void presentPuzzleRepositoryError(final String error) {
        System.err.println(error);
    }

    @Override
    public void presentSavedPuzzle(final SavedPuzzle puzzle) {
        // TODO l10n
        final String confirmation = "Successfully saved puzzle.";
        final String formattedPuzzle = PuzzleFormatter.formatSavedPuzzle(puzzle);
        System.out.println(
                confirmation + System.lineSeparator() + System.lineSeparator() + formattedPuzzle);
    }
}
