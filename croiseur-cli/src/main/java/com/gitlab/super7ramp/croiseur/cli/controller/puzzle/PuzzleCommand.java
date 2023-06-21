/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.cli.controller.puzzle;

import com.gitlab.super7ramp.croiseur.api.puzzle.PuzzlePatch;
import com.gitlab.super7ramp.croiseur.api.puzzle.PuzzleService;
import com.gitlab.super7ramp.croiseur.cli.controller.puzzle.converter.Puzzles;
import com.gitlab.super7ramp.croiseur.common.puzzle.Puzzle;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

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
    @Command(aliases = {"ls"})
    void list() {
        puzzleService.list();
    }

    /**
     * Creates a puzzle.
     *
     * @param title the puzzle title, if any
     * @param author the puzzle author, if any
     * @param editor the puzzle editor, if any
     * @param copyright the puzzle copyright, if any
     * @param date the puzzle date, if any
     * @param gridRows the puzzle grid rows
     */
    @Command
    void create(
            @Option(names = {"-t", "--title"}, paramLabel = "TITLE") final Optional<String> title,
            @Option(names = {"-a", "--author"}, paramLabel = "AUTHOR")
            final Optional<String> author,
            @Option(names = {"-e", "--editor"}, paramLabel = "EDITOR")
            final Optional<String> editor,
            @Option(names = {"-c", "--copyright"}, paramLabel = "COPYRIGHT")
            final Optional<String> copyright,
            @Option(names = {"-d", "--date"}, paramLabel = "DATE") final Optional<LocalDate> date,
            @Option(names = {"-r", "--rows"}, paramLabel = "ROWS", required = true)
            final String gridRows) {
        final Puzzle puzzle = Puzzles.puzzleFrom(title, author, editor, copyright, date, gridRows);
        puzzleService.save(puzzle);
    }

    /**
     * Updates a puzzle
     *
     * @param id        the puzzle id
     * @param title     the new title, if any
     * @param author    the new author, if any
     * @param editor    the new editor, if any
     * @param copyright the new copyright, if any
     * @param date      the new date, if any
     * @param gridRows  the new grid rows, if any
     */
    @Command
    void update(@Parameters(arity = "1", paramLabel = "ID") final long id,
                @Option(names = {"-t", "--title"}, paramLabel = "TITLE")
                final Optional<String> title,
                @Option(names = {"-a", "--author"}, paramLabel = "AUTHOR")
                final Optional<String> author,
                @Option(names = {"-e", "--editor"}, paramLabel = "EDITOR")
                final Optional<String> editor,
                @Option(names = {"-c", "--copyright"}, paramLabel = "COPYRIGHT")
                final Optional<String> copyright,
                @Option(names = {"-d", "--date"}, paramLabel = "DATE")
                final Optional<LocalDate> date,
                @Option(names = {"-r", "--rows"}, paramLabel = "ROWS")
                final Optional<String> gridRows) {
        final PuzzlePatch
                puzzlePatch =
                Puzzles.puzzlePatchFrom(id, title, author, editor, copyright, date, gridRows);
        puzzleService.patchAndSave(puzzlePatch);
    }

    /**
     * Deletes a puzzle.
     *
     * @param id the puzzle id
     */
    @Command
    public void delete(@Parameters(arity = "1", paramLabel = "ID") final long id) {
        puzzleService.delete(id);
    }

    /**
     * Displays a puzzle.
     *
     * @param id the puzzle id
     */
    @Command
    public void cat(@Parameters(arity = "1", paramLabel = "ID") final long id) {
        puzzleService.load(id);
    }
}
