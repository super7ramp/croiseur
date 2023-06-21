/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.cli;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests on 'croiseur-cli puzzle *' commands.
 */
final class CroiseurCliPuzzleTest extends FluentTestHelper {

    @Test
    void puzzle() {
        whenOneRunsCli("puzzle");
        thenCli().doesNotWriteToStdOut()
                 .and().writesToStdErr(
                         """
                         Missing required subcommand
                         Usage: croiseur-cli puzzle COMMAND
                         Manage saved puzzles

                         Commands:
                           cat         Display saved puzzle
                           create      Save a new puzzle
                           delete, rm  Delete a saved puzzle
                           list, ls    List saved puzzles
                           update      Update a saved puzzle
                         """)
                 .and().exitsWithCode(INPUT_ERROR);
    }

    @Test
    void create() {
        whenOneRunsCli("puzzle", "create",
                       "--title", "Example Grid",
                       "--author", "Me",
                       "--editor", "Nobody",
                       "--copyright", "CC-0",
                       "--date", "2023-06-21",
                       "--rows", "...,ABC,#D.");
        thenCli().writesToStdOut("""
                                 Saved puzzle.
                                                                           
                                 Id: 1
                                 Rev: 1
                                 Title: Example Grid
                                 Author: Me
                                 Editor: Nobody
                                 Copyright: CC-0
                                 Date: 2023-06-21
                                 Grid:
                                 | | | |
                                 |A|B|C|
                                 |#|D| |
                                                                           
                                 """)
                 .and().doesNotWriteToStdErr()
                 .and().exitsWithCode(SUCCESS);
    }

    @Test
    void update() {
        givenOneHasRunCli("puzzle", "create", "--rows", "...,ABC,#D.");
        whenOneRunsCli("puzzle", "update", "1", "--title", "Example", "--rows", "XYZ,ABC,#D.");
        thenCli().writesToStdOut("""
                                 Saved puzzle.
                                                                           
                                 Id: 1
                                 Rev: 2
                                 Title: Example
                                 Author:\s
                                 Editor:\s
                                 Copyright:\s
                                 Date:\s
                                 Grid:
                                 |X|Y|Z|
                                 |A|B|C|
                                 |#|D| |
                                                                           
                                 """)
                 .and().doesNotWriteToStdErr()
                 .and().exitsWithCode(SUCCESS);
    }

    @Test
    void update_missing() {
        whenOneRunsCli("puzzle", "update", "1", "--title", "Example", "--rows", "XYZ,ABC,#D.");
        thenCli().doesNotWriteToStdOut()
                 .and().writesToStdErr("Cannot modify puzzle with id #1: No such puzzle exists.\n")
                 .and().exitsWithCode(SUCCESS); // TODO shouldn't it be error?
    }

    @Test
    void delete() {
        givenOneHasRunCli("puzzle", "create", "--rows", "...,ABC,#D.");
        whenOneRunsCli("puzzle", "delete", "1");
        thenCli().doesNotWriteToStdOut() // TODO shouldn't a message be displayed?
                 .and().doesNotWriteToStdErr()
                 .and().exitsWithCode(SUCCESS);
    }

    @Test
    void cat() {
        givenOneHasRunCli("puzzle", "create", "--rows", "...,ABC,#D.");
        whenOneRunsCli("puzzle", "cat", "1");
        thenCli().writesToStdOut(
                         """                                                                     
                         Id: 1
                         Rev: 1
                         Title:\s
                         Author:\s
                         Editor:\s
                         Copyright:\s
                         Date:\s
                         Grid:
                         | | | |
                         |A|B|C|
                         |#|D| |
                                                                   
                         """)
                 .and().doesNotWriteToStdErr()
                 .and().exitsWithCode(SUCCESS);
    }

    private void givenOneHasRunCli(final String... args) {
        cli(args);
        assertFalse(out().isEmpty());
        assertTrue(err().isEmpty());
        assertEquals(SUCCESS, exitCode());
    }

    @Test
    void list() {
        givenOneHasRunCli("puzzle", "create",
                          "--title", "First Example",
                          "--date", "2023-06-21",
                          "--rows", "...,ABC,#D.");
        whenOneRunsCli("puzzle", "list");
        thenCli().writesToStdOut("""
                                 Id  	Rev 	Title           	Date           \s
                                 --  	--- 	-----           	----           \s
                                 1   	1   	First Example   	2023-06-21     \s
                                 """)
                 .and().doesNotWriteToStdErr()
                 .and().exitsWithCode(SUCCESS);
    }

    @Test
    void list_empty() {
        whenOneRunsCli("puzzle", "list");
        thenCli().writesToStdOut("No saved puzzle.\n")
                 .and().doesNotWriteToStdErr()
                 .and().exitsWithCode(SUCCESS);
    }


    @AfterEach
    void cleanRepository() throws IOException {
        try (final Stream<Path> files = puzzleFiles()) {
            files.forEach(this::deletePuzzleFile);
        }
    }

    private Stream<Path> puzzleFiles() throws IOException {
        // TODO we shouldn't do that, croiseur should provide a wat to clear repository
        final Path repositoryPath =
                Path.of(System.getProperty(
                        "com.gitlab.super7ramp.croiseur.puzzle.repository.path"));
        assertTrue(Files.exists(repositoryPath), "No test repository path set, check your setup.");
        return Files.find(repositoryPath, 1, (path, attr) -> attr.isRegularFile() &&
                                                             path.getFileName().toString()
                                                                 .matches("\\d+.xd"));
    }

    private void deletePuzzleFile(final Path path) {
        try {
            Files.delete(path);
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
