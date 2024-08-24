/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.puzzle.repository.memory.plugin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import re.belv.croiseur.common.puzzle.ChangedPuzzle;
import re.belv.croiseur.common.puzzle.Puzzle;
import re.belv.croiseur.common.puzzle.PuzzleClues;
import re.belv.croiseur.common.puzzle.PuzzleDetails;
import re.belv.croiseur.common.puzzle.PuzzleGrid;
import re.belv.croiseur.common.puzzle.SavedPuzzle;
import re.belv.croiseur.spi.puzzle.repository.WriteException;

/** Tests for {@link InMemoryPuzzleRepository}. */
final class InMemoryPuzzleRepositoryTest {

    /** The repository under tests. */
    private InMemoryPuzzleRepository repo;

    @BeforeEach
    void setUp() {
        repo = new InMemoryPuzzleRepository();
    }

    @Test
    void create() {
        final Puzzle puzzle = puzzleOfSize(4, 3);

        final SavedPuzzle savedPuzzle = repo.create(puzzle);

        assertEquals(savedPuzzle.data(), puzzle);
        assertEquals(savedPuzzle.revision(), 1);
    }

    @Test
    void create_twoDifferentIds() {
        final Puzzle puzzle = puzzleOfSize(4, 3);

        final SavedPuzzle firstPuzzle = repo.create(puzzle);
        final SavedPuzzle secondPuzzle = repo.create(puzzle);

        assertNotEquals(firstPuzzle.id(), secondPuzzle.id());
    }

    @Test
    void update() throws WriteException {
        final Puzzle firstPuzzle = puzzleOfSize(4, 3);
        final SavedPuzzle initialSavedPuzzle = repo.create(firstPuzzle);
        final Puzzle secondPuzzle = puzzleOfSize(5, 4);
        final ChangedPuzzle changedPuzzle = initialSavedPuzzle.modifiedWith(secondPuzzle);

        final SavedPuzzle updatedSavedPuzzle = repo.update(changedPuzzle);

        assertEquals(initialSavedPuzzle.id(), updatedSavedPuzzle.id());
        assertEquals(changedPuzzle.data(), updatedSavedPuzzle.data());
        assertEquals(initialSavedPuzzle.revision() + 1, updatedSavedPuzzle.revision());
    }

    @Test
    void update_noChange() throws WriteException {
        final Puzzle firstPuzzle = puzzleOfSize(4, 3);
        final SavedPuzzle initialSavedPuzzle = repo.create(firstPuzzle);
        final ChangedPuzzle notReallyChangedPuzzle = initialSavedPuzzle.modifiedWith(firstPuzzle);

        final SavedPuzzle updatedSavedPuzzle = repo.update(notReallyChangedPuzzle);

        // Absence of change is detected, revision number is not incremented
        assertEquals(initialSavedPuzzle, updatedSavedPuzzle);
    }

    @Test
    void update_missing() {
        final SavedPuzzle fakeSavedPuzzle = new SavedPuzzle(404, puzzleOfSize(3, 3), 1);
        final ChangedPuzzle changedPuzzle = fakeSavedPuzzle.modifiedWith(puzzleOfSize(3, 4));

        assertThrows(WriteException.class, () -> repo.update(changedPuzzle));
    }

    @Test
    void delete() throws WriteException {
        final Puzzle puzzle = puzzleOfSize(4, 3);
        final SavedPuzzle savedPuzzle = repo.create(puzzle);

        repo.delete(savedPuzzle);

        assertEquals(Optional.empty(), repo.query(savedPuzzle.id()));
    }

    @Test
    void delete_missing() {
        assertThrows(WriteException.class, () -> repo.delete(404));
    }

    // Note: Test likely to break if ID allocation strategy changes
    @Test
    void create_reuseIdAfterDelete() throws WriteException {
        final Puzzle puzzle = puzzleOfSize(4, 3);
        final SavedPuzzle savedPuzzle = repo.create(puzzle);
        repo.delete(savedPuzzle);

        final SavedPuzzle resavedPuzzle = repo.create(puzzle);

        assertEquals(savedPuzzle.id(), resavedPuzzle.id());
    }

    @Test
    void query() {
        final Puzzle puzzle = puzzleOfSize(4, 3);
        final SavedPuzzle savedPuzzle = repo.create(puzzle);

        final Optional<SavedPuzzle> queriedPuzzle = repo.query(savedPuzzle.id());

        assertEquals(Optional.of(savedPuzzle), queriedPuzzle);
    }

    @Test
    void query_absent() {
        assertEquals(Optional.empty(), repo.query(1));
    }

    @Test
    void list() {
        final SavedPuzzle firstSavedPuzzle = repo.create(puzzleOfSize(4, 3));
        final SavedPuzzle secondSavedPuzzle = repo.create(puzzleOfSize(2, 2));
        final SavedPuzzle thirdSavedPuzzle = repo.create(puzzleOfSize(15, 15));

        final Collection<SavedPuzzle> puzzles = repo.list();

        assertEquals(3, puzzles.size());
        assertTrue(puzzles.containsAll(List.of(firstSavedPuzzle, secondSavedPuzzle, thirdSavedPuzzle)));
    }

    private static Puzzle puzzleOfSize(final int numberOfColumns, final int numberOfRows) {
        final var grid = new PuzzleGrid.Builder()
                .height(numberOfRows)
                .width(numberOfColumns)
                .build();
        return new Puzzle(PuzzleDetails.empty(), grid, PuzzleClues.empty());
    }
}
