/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.gui.view.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Tests for {@link PuzzleSelectionViewModel}. */
final class PuzzleSelectionViewModelTest {

    /** The model under tests. */
    private PuzzleSelectionViewModel puzzleSelection;

    @BeforeEach
    void setup() {
        puzzleSelection = new PuzzleSelectionViewModel();
    }

    @Test
    void removeAvailablePuzzleWithId() {
        final var puzzle1 = newPuzzleWithId(1);
        final var puzzle2 = newPuzzleWithId(2);
        final var puzzle3 = newPuzzleWithId(3);
        puzzleSelection.availablePuzzlesProperty().setAll(List.of(puzzle1, puzzle2, puzzle3));

        puzzleSelection.removeAvailablePuzzleWithId(2);

        assertEquals(List.of(puzzle1, puzzle3), puzzleSelection.availablePuzzlesProperty());
    }

    /**
     * Verifies that {@link PuzzleSelectionViewModel#removeAvailablePuzzleWithId(long)} does not change the available
     * puzzle list nor raise an exception when given is is not present in the list.
     */
    @Test
    void removeAvailablePuzzleWithId_noSuchPuzzle() {
        final var puzzles = List.of(newPuzzleWithId(1), newPuzzleWithId(2), newPuzzleWithId(3));
        puzzleSelection.availablePuzzlesProperty().setAll(puzzles);

        puzzleSelection.removeAvailablePuzzleWithId(404);

        assertEquals(puzzles, puzzleSelection.availablePuzzlesProperty());
    }

    private static SavedPuzzleViewModel newPuzzleWithId(final long id) {
        return new SavedPuzzleViewModel.Builder()
                .numberOfColumns(1)
                .numberOfRows(1)
                .id(id)
                .build();
    }
}
