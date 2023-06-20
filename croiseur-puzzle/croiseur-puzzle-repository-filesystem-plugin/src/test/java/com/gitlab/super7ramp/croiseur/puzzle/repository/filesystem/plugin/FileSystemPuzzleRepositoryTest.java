/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.puzzle.repository.filesystem.plugin;

import com.gitlab.super7ramp.croiseur.common.puzzle.SavedPuzzle;
import com.gitlab.super7ramp.croiseur.spi.puzzle.repository.WriteException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for {@link FileSystemPuzzleRepository}.
 */
final class FileSystemPuzzleRepositoryTest {

    @TempDir
    private Path repositoryPath;

    @BeforeEach
    void setUp() {
        System.setProperty("com.gitlab.super7ramp.croiseur.puzzle.repository.path",
                           repositoryPath.toString());
    }

    @Test
    void delete() throws IOException, WriteException {
        final Path puzzlePath = repositoryPath.resolve("1.xd");
        Files.writeString(puzzlePath, xdPuzzle());
        final FileSystemPuzzleRepository repo = new FileSystemPuzzleRepository();

        repo.delete(1L);

        assertFalse(Files.exists(puzzlePath));
    }

    @Test
    void delete_missing() {
        final FileSystemPuzzleRepository repo = new FileSystemPuzzleRepository();
        assertThrows(WriteException.class, () -> repo.delete(1L));
    }

    @Test
    void query() throws IOException {
        Files.writeString(repositoryPath.resolve("1.xd"), xdPuzzle());
        final FileSystemPuzzleRepository repo = new FileSystemPuzzleRepository();

        final Optional<SavedPuzzle> puzzle = repo.query(1L);

        assertTrue(puzzle.isPresent());
    }

    @Test
    void query_empty() {
        final FileSystemPuzzleRepository repo = new FileSystemPuzzleRepository();
        final Optional<SavedPuzzle> puzzle = repo.query(1L);
        assertEquals(Optional.empty(), puzzle);
    }

    @Test
    void list() throws IOException {
        Files.writeString(repositoryPath.resolve("1.xd"), xdPuzzle());
        Files.writeString(repositoryPath.resolve("2.xd"), xdPuzzle());
        Files.writeString(repositoryPath.resolve("3.xd"), xdPuzzle());
        final FileSystemPuzzleRepository repo = new FileSystemPuzzleRepository();

        final Collection<SavedPuzzle> puzzles = repo.list();

        assertEquals(3, puzzles.size());
    }

    @Test
    void list_empty() {
        final FileSystemPuzzleRepository repo = new FileSystemPuzzleRepository();
        final Collection<SavedPuzzle> puzzles = repo.list();
        assertEquals(Collections.emptyList(), puzzles);
    }

    @Test
    void list_invalidFormat() throws IOException {
        Files.createFile(repositoryPath.resolve("1.xd"));
        Files.createFile(repositoryPath.resolve("2.xd"));
        Files.createFile(repositoryPath.resolve("3.xd"));
        final FileSystemPuzzleRepository repo = new FileSystemPuzzleRepository();

        final Collection<SavedPuzzle> puzzles = repo.list();

        assertEquals(Collections.emptyList(), puzzles);
    }

    @Test
    void list_ignoredFiles() throws IOException {
        Files.writeString(repositoryPath.resolve("test.xd"), xdPuzzle());
        Files.writeString(repositoryPath.resolve("1.xdd"), xdPuzzle());
        final FileSystemPuzzleRepository repo = new FileSystemPuzzleRepository();

        final Collection<SavedPuzzle> puzzles = repo.list();

        assertEquals(Collections.emptyList(), puzzles);
    }

    @Test
    void list_ignoredSubdirectory() throws IOException {
        final Path subdirPath = repositoryPath.resolve("subdir");
        Files.createDirectory(subdirPath);
        Files.writeString(subdirPath.resolve("1.xd"), xdPuzzle());
        final FileSystemPuzzleRepository repo = new FileSystemPuzzleRepository();

        final Collection<SavedPuzzle> puzzles = repo.list();

        assertEquals(Collections.emptyList(), puzzles);
    }

    @Test
    void creationFailureNoPath() {
        System.clearProperty("com.gitlab.super7ramp.croiseur.puzzle.repository.path");
        assertThrows(IllegalStateException.class, FileSystemPuzzleRepository::new);
    }

    private static String xdPuzzle() {
        return """
               Title: Example Grid
               Author: Me
               Editor: Croiseur
               Date: 2023-06-19
               x-croiseur-revision: 1


               ABC
               DEF
               GHI


               A1. Start. ~ ABC
               A2. Middle. ~ DEF
               A3. End. ~ GHI

               D1. Some Very. ~ ADG
               D2. Dummy. ~ BEH
               D3. Clues. ~ CFI
               """;
    }
}
