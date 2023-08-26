/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.view.model.puzzle.selection;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;

import java.util.stream.IntStream;

/**
 * Puzzle selection view model.
 */
public final class PuzzleSelectionViewModel {

    /** The available puzzles. */
    private final ListProperty<SavedPuzzleViewModel> availablePuzzles;

    /** The selected puzzle. Value is {@code null} if no puzzle is selected. */
    private final ObjectProperty<SavedPuzzleViewModel> selectedPuzzle;

    /**
     * Constructs an instance.
     */
    public PuzzleSelectionViewModel() {
        availablePuzzles = new SimpleListProperty<>(this, "availablePuzzles",
                                                    FXCollections.observableArrayList());
        selectedPuzzle = new SimpleObjectProperty<>(this, "selectedPuzzle");
    }

    /**
     * Returns the available puzzles property.
     *
     * @return the available puzzles property
     */
    public ListProperty<SavedPuzzleViewModel> availablePuzzlesProperty() {
        return availablePuzzles;
    }

    /**
     * Updates the available puzzles with given puzzle.
     * <p>
     * If a puzzle with same id already exists, it will be replaced by given puzzle. Otherwise,
     * given puzzle will be added to the available puzzles.
     *
     * @param puzzle a new or updated puzzle
     */
    public void updateAvailablePuzzlesWith(final SavedPuzzleViewModel puzzle) {
        IntStream.range(0, availablePuzzles.size())
                 .filter(i -> availablePuzzles.get(i).id() == puzzle.id())
                 .findFirst()
                 .ifPresentOrElse(i -> availablePuzzles.set(i, puzzle),
                                  () -> availablePuzzles.add(puzzle));
    }

    /**
     * Removes the puzzle with given id from the view model's available puzzles.
     * <p>
     * Does nothing if no such puzzle exists.
     *
     * @param id the id of the puzzle to delete
     */
    public void removeAvailablePuzzleWithId(final long id) {
        final var puzzleIt = availablePuzzles.iterator();
        while (puzzleIt.hasNext()) {
            final var puzzle = puzzleIt.next();
            if (puzzle.id() == id) {
                puzzleIt.remove();
                break;
            }
        }
    }

    /**
     * Returns the selected puzzle property.
     * <p>
     * Value is {@code null} if no puzzle is selected.
     *
     * @return the selected puzzle property
     */
    public ObjectProperty<SavedPuzzleViewModel> selectedPuzzleProperty() {
        return selectedPuzzle;
    }

    /**
     * Returns the value of the selected puzzle property.
     *
     * @return the value of the selected puzzle property; {@code null} if no puzzle selected/saved
     */
    public SavedPuzzleViewModel selectedPuzzle() {
        return selectedPuzzle.get();
    }

    /**
     * Sets the value of the selected puzzle property.
     *
     * @param savedPuzzle the value to set
     */
    public void selectedPuzzle(final SavedPuzzleViewModel savedPuzzle) {
        selectedPuzzle.set(savedPuzzle);
    }
}
