/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.cli.controller.puzzle;

import com.gitlab.super7ramp.croiseur.api.puzzle.PuzzleService;
import com.gitlab.super7ramp.croiseur.cli.controller.puzzle.converter.Puzzles;
import com.gitlab.super7ramp.croiseur.common.puzzle.Puzzle;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.time.LocalDate;
import java.util.Optional;

/**
 * The 'puzzle' command: Manage the puzzle repository.
 */
@Command(name = "puzzle")
public final class PuzzleCommand {

    /** The puzzle service. */
    private final PuzzleService puzzleService;

    /**
     * Constructs an instance.
     *
     * @param puzzleServiceArg the puzzle service
     */
    public PuzzleCommand(final PuzzleService puzzleServiceArg) {
        puzzleService = puzzleServiceArg;
    }

    /** Lists the available puzzles. */
    @Command(name = "list", aliases = {"ls"})
    void list() {
        puzzleService.list();
    }

    /** Creates a puzzle. */
    @Command(name = "create")
    void create(@Option(names = {"-t", "--title"}, paramLabel = "TITLE") final Optional<String> title,
              @Option(names = {"-a", "--author"}, paramLabel = "AUTHOR") final Optional<String> author,
              @Option(names = {"-e", "--editor"}, paramLabel = "EDITOR") final Optional<String> editor,
              @Option(names = {"-c", "--copyright"}, paramLabel = "COPYRIGHT") final Optional<String> copyright,
              @Option(names = {"-d", "--date"}, paramLabel = "DATE") final Optional<LocalDate> date,
              @Option(names = {"-r", "--rows"}, paramLabel = "ROWS", required = true) final String gridRows) {
        final Puzzle puzzle = Puzzles.puzzleFrom(title, author, editor, copyright, date, gridRows);
        puzzleService.save(puzzle);
    }

}
