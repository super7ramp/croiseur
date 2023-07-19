/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.cli.controller.puzzle;

import com.gitlab.super7ramp.croiseur.api.puzzle.PuzzleService;
import com.gitlab.super7ramp.croiseur.api.puzzle.exporter.PuzzleExportService;
import com.gitlab.super7ramp.croiseur.api.puzzle.importer.PuzzleImportService;
import com.gitlab.super7ramp.croiseur.api.puzzle.persistence.PuzzlePatch;
import com.gitlab.super7ramp.croiseur.api.puzzle.persistence.PuzzlePersistenceService;
import com.gitlab.super7ramp.croiseur.cli.controller.puzzle.adapter.Puzzles;
import com.gitlab.super7ramp.croiseur.cli.status.Status;
import com.gitlab.super7ramp.croiseur.cli.status.StatusCodes;
import com.gitlab.super7ramp.croiseur.common.puzzle.Puzzle;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

/**
 * The 'puzzle' command: Manage the puzzle repository.
 */
@Command(name = "puzzle")
public final class PuzzleCommand {

    /** The puzzle persistence service. */
    private final PuzzlePersistenceService puzzlePersistenceService;

    /** The puzzle import service. */
    private final PuzzleImportService puzzleImportService;

    /** The puzzle export service. */
    private final PuzzleExportService puzzleExportService;

    /**
     * Constructs an instance.
     *
     * @param puzzleServiceArg the puzzle service
     */
    public PuzzleCommand(final PuzzleService puzzleServiceArg) {
        puzzlePersistenceService = puzzleServiceArg.persistence();
        puzzleImportService = puzzleServiceArg.importer();
        puzzleExportService = puzzleServiceArg.exporter();
    }

    /**
     * Lists the available puzzles.
     *
     * @return the error status
     */
    @Command(aliases = {"ls"})
    int list() {
        puzzlePersistenceService.list();
        return Status.getAndReset();
    }

    /**
     * Lists the available puzzle decoders.
     *
     * @return the error status
     */
    @Command(name = "list-decoders")
    int listDecoders() {
        puzzleImportService.listDecoders();
        return Status.getAndReset();
    }

    /**
     * Lists the available puzzle encoders.
     *
     * @return the error status
     */
    @Command(name = "list-encoders")
    int listEncoders() {
        puzzleExportService.listEncoders();
        return Status.getAndReset();
    }

    /**
     * Imports the given puzzle.
     *
     * @param file   the input file
     * @param format the file format; if not given, format is taken from file extension
     * @return the error status
     */
    @Command(name = "import")
    int importPuzzle(@Parameters(arity = "1", paramLabel = "FILE") final File file,
                     @Option(names = {"-f", "--format"}, paramLabel = "FORMAT")
                     final Optional<String> format) {
        try (final var fis = new FileInputStream(file)) {
            puzzleImportService.importPuzzle(format.orElseGet(() -> inferFormatFrom(file)), fis);
            return Status.getAndReset();
        } catch (final IOException e) {
            System.err.println("File not found.");
            return StatusCodes.IO_ERROR;
        }
    }

    /**
     * Exports the puzzle to given file.
     *
     * @param id     the puzzle id
     * @param file   the output file
     * @param format the file format; if not given, format is taken from file extension
     * @return the error status
     */
    @Command(name = "export")
    int exportPuzzle(@Parameters(arity = "1", paramLabel = "ID") long id,
                     @Parameters(arity = "1", paramLabel = "FILE") final File file,
                     @Option(names = {"-f", "--format"}, paramLabel = "FORMAT")
                     final Optional<String> format) {
        try (final var fos = new FileOutputStream(file)) {
            puzzleExportService.exportPuzzle(id, format.orElseGet(() -> inferFormatFrom(file)),
                                             fos);
            return Status.getAndReset();
        } catch (final IOException e) {
            System.err.println("Could not write export file.");
            return StatusCodes.IO_ERROR;
        }
    }

    /**
     * Infers the puzzle import/export format from the extension of the given file.
     *
     * @param file the file
     * @return the inferred format (basically {@literal "*.<file_extension>"}) or "unknown" if no
     * format could be inferred
     */
    private String inferFormatFrom(final File file) {
        final int lastDot = file.getName().lastIndexOf(".");
        return lastDot >= 0 ? "*" + file.getName().substring(lastDot) : "unknown";
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
     * @return the error status
     */
    @Command
    int create(
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
        puzzlePersistenceService.save(puzzle);
        return Status.getAndReset();
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
     * @return the error status
     */
    @Command
    int update(@Parameters(arity = "1", paramLabel = "ID") final long id,
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
        final PuzzlePatch puzzlePatch =
                Puzzles.puzzlePatchFrom(title, author, editor, copyright, date, gridRows);
        puzzlePersistenceService.save(id, puzzlePatch);
        return Status.getAndReset();
    }

    /**
     * Deletes a puzzle.
     *
     * @param id the puzzle id
     * @return the error status
     */
    @Command(aliases = {"rm"})
    int delete(@Parameters(arity = "1", paramLabel = "ID") final long id) {
        puzzlePersistenceService.delete(id);
        return Status.getAndReset();
    }

    /**
     * Deletes all puzzle.
     *
     * @return the error status
     */
    @Command(name = "delete-all")
    int deleteAll() {
        puzzlePersistenceService.deleteAll();
        return Status.getAndReset();
    }

    /**
     * Displays a puzzle.
     *
     * @param id the puzzle id
     * @return the error status
     */
    @Command
    int cat(@Parameters(arity = "1", paramLabel = "ID") final long id) {
        puzzlePersistenceService.load(id);
        return Status.getAndReset();
    }
}
