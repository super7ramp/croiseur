/*
 * SPDX-FileCopyrightText: 2025 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.puzzle.repository.filesystem.plugin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static re.belv.croiseur.common.puzzle.GridPosition.at;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import re.belv.croiseur.common.puzzle.ChangedPuzzle;
import re.belv.croiseur.common.puzzle.Puzzle;
import re.belv.croiseur.common.puzzle.PuzzleClues;
import re.belv.croiseur.common.puzzle.PuzzleDetails;
import re.belv.croiseur.common.puzzle.PuzzleGrid;
import re.belv.croiseur.common.puzzle.SavedPuzzle;
import re.belv.croiseur.spi.puzzle.repository.WriteException;

/** Tests for {@link FileSystemPuzzleRepository}. */
final class FileSystemPuzzleRepositoryTest {

    /** An example xd puzzle used for tests. */
    private static final String XD_PUZZLE = """
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

    @TempDir
    private Path repositoryPath;

    /** The repository under tests. */
    private FileSystemPuzzleRepository repo;

    @BeforeEach
    void setUp() throws IOException {
        System.setProperty("re.belv.croiseur.puzzle.path", repositoryPath.toString());
        repo = new FileSystemPuzzleRepository();
    }

    @Test
    void create() throws WriteException {
        final PuzzleDetails details = new PuzzleDetails("A Title", "An author", "", "", Optional.empty());
        final PuzzleGrid grid = new PuzzleGrid.Builder().width(2).height(2).build();
        final PuzzleClues clues = PuzzleClues.empty();
        final Puzzle puzzle = new Puzzle(details, grid, clues);

        final SavedPuzzle savedPuzzle = repo.create(puzzle);

        assertEquals(puzzle, savedPuzzle.data());
        assertEquals(1, savedPuzzle.revision());
        assertTrue(Files.exists(repositoryPath.resolve(savedPuzzle.id() + ".xd")));
    }

    @Test
    void create_consistencyWithQuery() throws WriteException {
        final PuzzleDetails details = new PuzzleDetails("A Title", "An author", "", "", Optional.empty());
        final PuzzleGrid grid = new PuzzleGrid.Builder().width(2).height(2).build();
        final PuzzleClues clues = PuzzleClues.empty();
        final Puzzle puzzle = new Puzzle(details, grid, clues);

        final SavedPuzzle savedPuzzle = repo.create(puzzle);
        final SavedPuzzle queriedPuzzle = repo.query(savedPuzzle.id()).orElseThrow(AssertionError::new);

        assertEquals(savedPuzzle, queriedPuzzle);
    }

    @Test
    void update() throws WriteException {
        final PuzzleDetails details = new PuzzleDetails("A Title", "An author", "", "", Optional.empty());
        final PuzzleGrid grid = new PuzzleGrid.Builder().width(2).height(2).build();
        final PuzzleClues clues = PuzzleClues.empty();
        final Puzzle puzzle = new Puzzle(details, grid, clues);
        final SavedPuzzle savedPuzzle = repo.create(puzzle);
        final PuzzleGrid changedGrid =
                new PuzzleGrid.Builder().width(2).height(2).fill(at(0, 0), 'A').build();
        final Puzzle changedPuzzleData = new Puzzle(details, changedGrid, clues);
        final ChangedPuzzle changedPuzzle = savedPuzzle.modifiedWith(changedPuzzleData);

        final SavedPuzzle updatedSavedPuzzle = repo.update(changedPuzzle);

        assertEquals(changedPuzzleData, updatedSavedPuzzle.data());
        assertEquals(2, updatedSavedPuzzle.revision());
        assertTrue(Files.exists(repositoryPath.resolve(updatedSavedPuzzle.id() + ".xd")));
    }

    @Test
    void update_noRealChange() throws WriteException {
        final PuzzleDetails details = new PuzzleDetails("A Title", "An author", "", "", Optional.empty());
        final PuzzleGrid grid = new PuzzleGrid.Builder().width(2).height(2).build();
        final PuzzleClues clues = PuzzleClues.empty();
        final Puzzle puzzle = new Puzzle(details, grid, clues);
        final SavedPuzzle savedPuzzle = repo.create(puzzle);
        final ChangedPuzzle noReallyChangedPuzzle = savedPuzzle.modifiedWith(puzzle);

        final SavedPuzzle updatedSavedPuzzle = repo.update(noReallyChangedPuzzle);

        assertEquals(1, updatedSavedPuzzle.revision());
    }

    @Test
    void delete() throws IOException, WriteException {
        final Path puzzlePath = repositoryPath.resolve("1.xd");
        Files.writeString(puzzlePath, XD_PUZZLE);

        repo.delete(1L);

        assertFalse(Files.exists(puzzlePath));
    }

    @Test
    void delete_missing() {
        assertThrows(WriteException.class, () -> repo.delete(1L));
    }

    @Test
    void deleteAll() throws IOException, WriteException {
        Files.writeString(repositoryPath.resolve("1.xd"), XD_PUZZLE);
        Files.writeString(repositoryPath.resolve("2.xd"), XD_PUZZLE);
        Files.writeString(repositoryPath.resolve("3.xd"), XD_PUZZLE);

        repo.deleteAll();

        assertFalse(Files.exists(repositoryPath.resolve("1.xd")));
        assertFalse(Files.exists(repositoryPath.resolve("2.xd")));
        assertFalse(Files.exists(repositoryPath.resolve("3.xd")));
    }

    @Test
    void query() throws IOException {
        Files.writeString(repositoryPath.resolve("1.xd"), XD_PUZZLE);

        final Optional<SavedPuzzle> puzzle = repo.query(1L);

        assertTrue(puzzle.isPresent());
    }

    @Test
    void query_empty() {
        final Optional<SavedPuzzle> puzzle = repo.query(1L);
        assertEquals(Optional.empty(), puzzle);
    }

    @Test
    void list() throws IOException {
        Files.writeString(repositoryPath.resolve("1.xd"), XD_PUZZLE);
        Files.writeString(repositoryPath.resolve("2.xd"), XD_PUZZLE);
        Files.writeString(repositoryPath.resolve("3.xd"), XD_PUZZLE);

        final Collection<SavedPuzzle> puzzles = repo.list();

        assertEquals(3, puzzles.size());
    }

    @Test
    void list_empty() {
        final Collection<SavedPuzzle> puzzles = repo.list();
        assertEquals(Collections.emptyList(), puzzles);
    }

    @Test
    void list_invalidFormat() throws IOException {
        Files.createFile(repositoryPath.resolve("1.xd"));
        Files.createFile(repositoryPath.resolve("2.xd"));
        Files.createFile(repositoryPath.resolve("3.xd"));

        final Collection<SavedPuzzle> puzzles = repo.list();

        assertEquals(Collections.emptyList(), puzzles);
    }

    @Test
    void list_ignoredFiles() throws IOException {
        Files.writeString(repositoryPath.resolve("test.xd"), XD_PUZZLE);
        Files.writeString(repositoryPath.resolve("1.xdd"), XD_PUZZLE);

        final Collection<SavedPuzzle> puzzles = repo.list();

        assertEquals(Collections.emptyList(), puzzles);
    }

    @Test
    void list_ignoredSubdirectory() throws IOException {
        final Path subdirPath = repositoryPath.resolve("subdir");
        Files.createDirectory(subdirPath);
        Files.writeString(subdirPath.resolve("1.xd"), XD_PUZZLE);

        final Collection<SavedPuzzle> puzzles = repo.list();

        assertEquals(Collections.emptyList(), puzzles);
    }
}
