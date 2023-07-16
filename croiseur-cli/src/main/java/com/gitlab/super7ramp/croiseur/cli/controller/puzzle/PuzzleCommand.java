/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.cli.controller.puzzle;

import com.gitlab.super7ramp.croiseur.api.puzzle.PuzzlePatch;
import com.gitlab.super7ramp.croiseur.api.puzzle.PuzzleService;
import com.gitlab.super7ramp.croiseur.cli.controller.puzzle.adapter.Puzzles;
import com.gitlab.super7ramp.croiseur.common.puzzle.Puzzle;
import picocli.CommandLine.Command;
import picocli.CommandLine.ExitCode;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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

    /** Lists the available puzzle decoders. */
    @Command(name = "list-decoders")
    void listDecoders() {
        puzzleService.listDecoders();
    }

    /**
     * Imports the given puzzle.
     *
     * @param file   the input file
     * @param format the file format; if not given, format is taken from file extension
     * @return {@value ExitCode#OK} if file can be opened; {@value ExitCode#SOFTWARE} otherwise
     */
    @Command(name = "import")
    int importPuzzle(@Parameters(arity = "1", paramLabel = "FILE") final File file,
                     @Option(names = {"-f", "--format"}, paramLabel = "FORMAT")
                     final Optional<String> format) {
        try (final var fis = new FileInputStream(file)) {
            puzzleService.importPuzzle(fis, format.orElseGet(() -> {
                final int lastDot = file.getName().lastIndexOf(".");
                return lastDot >= 0 ? "*" + file.getName().substring(lastDot) : "unknown";
            }));
            return ExitCode.OK;
        } catch (final IOException e) {
            System.err.println("File not found.");
            return ExitCode.SOFTWARE;
        }
    }

    /**
     * Creates a puzzle.
     *
     * @param title     the puzzle title, if any
     * @param author    the puzzle author, if any
     * @param editor    the puzzle editor, if any
     * @param copyright the puzzle copyright, if any
     * @param date      the puzzle date, if any
     * @param gridRows  the puzzle grid rows
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
                Puzzles.puzzlePatchFrom(title, author, editor, copyright, date, gridRows);
        puzzleService.save(id, puzzlePatch);
    }

    /**
     * Deletes a puzzle.
     *
     * @param id the puzzle id
     */
    @Command(aliases = {"rm"})
    public void delete(@Parameters(arity = "1", paramLabel = "ID") final long id) {
        puzzleService.delete(id);
    }

    /** Deletes all puzzle. */
    @Command(name = "delete-all")
    public void deleteAll() {
        puzzleService.deleteAll();
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
