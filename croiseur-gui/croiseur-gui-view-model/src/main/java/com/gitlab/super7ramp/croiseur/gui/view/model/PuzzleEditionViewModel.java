/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.view.model;

import com.gitlab.super7ramp.croiseur.gui.view.model.slot.SlotOutline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.IntStream;

/**
 * Puzzle edition view model.
 */
public final class PuzzleEditionViewModel {

    /** The minimal slot length to consider for clues; Slot of a single letter will be ignored. */
    private static final Predicate<SlotOutline> AT_LEAST_TWO_BOXES = s -> s.length() >= 2;

    /** The puzzle details view model. */
    private final PuzzleDetailsViewModel puzzleDetailsViewModel;

    /** The crossword grid view model. */
    private final CrosswordGridViewModel crosswordGridViewModel;

    /** The clues view model. */
    private final CluesViewModel cluesViewModel;

    /** Property following the saving state. */
    private final BooleanProperty savingInProgress;

    /**
     * Constructs an instance.
     */
    PuzzleEditionViewModel() {
        puzzleDetailsViewModel = new PuzzleDetailsViewModel();
        crosswordGridViewModel = CrosswordGridViewModel.welcomeGrid();
        cluesViewModel = new CluesViewModel();
        savingInProgress = new SimpleBooleanProperty(this, "savingInProgress");

        // Bind clue lists to slot lists

        final ObservableList<SlotOutline> acrossSlots =
                crosswordGridViewModel.acrossSlotsProperty().filtered(AT_LEAST_TWO_BOXES);
        final List<ClueViewModel> acrossClues = cluesViewModel.acrossCluesProperty();
        acrossSlots.forEach(slot -> acrossClues.add(new ClueViewModel()));
        acrossSlots.addListener(this::updateAcrossClues);

        final ObservableList<SlotOutline> downSlots =
                crosswordGridViewModel.downSlotsProperty().filtered(AT_LEAST_TWO_BOXES);
        final List<ClueViewModel> downClues = cluesViewModel.downCluesProperty();
        downSlots.forEach(slot -> downClues.add(new ClueViewModel()));
        downSlots.addListener(this::updateDownClues);
    }

    /**
     * Returns the puzzle details view model.
     *
     * @return the puzzle details view model
     */
    public PuzzleDetailsViewModel puzzleDetailsViewModel() {
        return puzzleDetailsViewModel;
    }

    /**
     * Returns the crossword grid view model.
     *
     * @return the crossword grid view model
     */
    public CrosswordGridViewModel crosswordGridViewModel() {
        return crosswordGridViewModel;
    }

    /**
     * Returns the clues view model.
     *
     * @return the clues view model
     */
    public CluesViewModel cluesViewModel() {
        return cluesViewModel;
    }

    /**
     * Returns the property indicating whether the puzzle is being saved.
     *
     * @return the property indicating whether the puzzle is being saved
     */
    public BooleanProperty savingInProgressProperty() {
        return savingInProgress;
    }

    /**
     * Resets edition view model to default.
     */
    public void reset() {
        puzzleDetailsViewModel.reset();
        crosswordGridViewModel.reset();
        cluesViewModel.reset();
    }

    /**
     * Updates the across clue list when the across slot list changes.
     *
     * @param c the across clue list changes
     */
    private void updateAcrossClues(final ListChangeListener.Change<? extends SlotOutline> c) {
        while (c.next()) {
            final var acrossClues = cluesViewModel.acrossCluesProperty();
            if (c.wasReplaced()) {
                IntStream.range(c.getFrom(), c.getTo())
                         .forEach(i -> acrossClues.set(i, new ClueViewModel()));
            } else if (c.wasRemoved()) {
                acrossClues.remove(c.getFrom(), c.getFrom() + c.getRemovedSize());
            } else if (c.wasAdded()) {
                final var newClues = c.getAddedSubList().stream()
                                      .map(slot -> new ClueViewModel())
                                      .toList();
                acrossClues.addAll(c.getFrom(), newClues);
            }
        }
    }

    /**
     * Updates the down clue list when the down slot list changes.
     *
     * @param c the down clue list changes
     */
    private void updateDownClues(final ListChangeListener.Change<? extends SlotOutline> c) {
        while (c.next()) {
            final var downClues = cluesViewModel.downCluesProperty();
            if (c.wasReplaced()) {
                IntStream.range(c.getFrom(), c.getTo())
                         .forEach(i -> downClues.set(i, new ClueViewModel()));
            } else if (c.wasRemoved()) {
                downClues.remove(c.getFrom(), c.getFrom() + c.getRemovedSize());
            } else if (c.wasAdded()) {
                final var newClues = c.getAddedSubList().stream()
                                      .map(slot -> new ClueViewModel())
                                      .toList();
                downClues.addAll(c.getFrom(), newClues);
            }
        }
    }
}
