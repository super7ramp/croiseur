/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.cli.presenter;

import com.gitlab.super7ramp.croiseur.cli.l10n.ResourceBundles;
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

    /**
     * Returns the localised message with given key.
     *
     * @param key the message key
     * @return the localised message
     */
    private static String $(final String key) {
        return ResourceBundles.$("presenter.puzzle." + key);
    }

    @Override
    public void presentAvailablePuzzles(final List<SavedPuzzle> puzzles) {

        if (puzzles.isEmpty()) {
            System.out.println($("list.none-found"));
            return;
        }

        final String idHeader = $("id");
        final String revisionHeader = $("rev");
        final String titleHeader = $("title");
        final String dateHeader = $("date");

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
        final String confirmation = $("save.success");
        final String formattedPuzzle = PuzzleFormatter.formatSavedPuzzle(puzzle);
        System.out.println(
                confirmation + System.lineSeparator() + System.lineSeparator() + formattedPuzzle);
    }
}