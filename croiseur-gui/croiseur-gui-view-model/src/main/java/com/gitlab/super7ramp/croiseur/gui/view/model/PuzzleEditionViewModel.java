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
import java.util.stream.IntStream;

/**
 * Puzzle edition view model.
 */
public final class PuzzleEditionViewModel {

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

        // Bind across clues to across slots
        final ObservableList<SlotOutline> acrossSlots = crosswordGridViewModel.longAcrossSlots();
        final List<ClueViewModel> acrossClues = cluesViewModel.acrossCluesProperty();
        acrossSlots.forEach(slot -> acrossClues.add(new ClueViewModel()));
        acrossSlots.addListener(this::updateAcrossClues);
        cluesViewModel.selectedAcrossClueIndexProperty()
                      .addListener(observable -> updateCurrentAcrossSlot(
                              cluesViewModel.selectedAcrossClueIndex()));

        // Bind down clues to down slots
        final ObservableList<SlotOutline> downSlots = crosswordGridViewModel.longDownSlots();
        final List<ClueViewModel> downClues = cluesViewModel.downCluesProperty();
        downSlots.forEach(slot -> downClues.add(new ClueViewModel()));
        downSlots.addListener(this::updateDownClues);
        cluesViewModel.selectedDownClueIndexProperty()
                      .addListener(observable -> updateCurrentDownSlot(
                              cluesViewModel.selectedDownClueIndex()));

        // Bind current slot to current clue
        crosswordGridViewModel.currentBoxPositionProperty().addListener(
                observable -> updateCurrentClue(crosswordGridViewModel.currentBoxPosition()));
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

    /**
     * Updates current slot upon current down clue change.
     *
     * @param selectedDownClueIndex the current down clue new index
     */
    private void updateCurrentDownSlot(final int selectedDownClueIndex) {
        if (selectedDownClueIndex >= 0) {
            final GridCoord slotFirstBoxPosition =
                    crosswordGridViewModel.longDownSlots()
                                          .get(selectedDownClueIndex)
                                          .firstBoxPosition();
            crosswordGridViewModel.currentSlotVertical();
            crosswordGridViewModel.currentBoxPosition(slotFirstBoxPosition);
        }
    }

    /**
     * Updates current slot upon current across clue change.
     *
     * @param selectedAcrossClueIndex the current across clue new index
     */
    private void updateCurrentAcrossSlot(final int selectedAcrossClueIndex) {
        if (selectedAcrossClueIndex >= 0) {
            final GridCoord slotFirstBoxPosition =
                    crosswordGridViewModel.longAcrossSlots()
                                          .get(selectedAcrossClueIndex)
                                          .firstBoxPosition();
            crosswordGridViewModel.currentSlotHorizontal();
            crosswordGridViewModel.currentBoxPosition(slotFirstBoxPosition);
        }
    }

    /**
     * Updates current clue upon current box position change.
     *
     * @param currentBoxCoord the current box new position
     */
    private void updateCurrentClue(final GridCoord currentBoxCoord) {
        if (currentBoxCoord == null) {
            cluesViewModel.selectedAcrossClueIndex(-1);
            cluesViewModel.selectedDownClueIndex(-1);
        } else if (crosswordGridViewModel.currentSlotVerticalProperty().get()) {
            crosswordGridViewModel.indexOfLongDownSlotContaining(currentBoxCoord)
                                  .ifPresent(cluesViewModel::selectedDownClueIndex);
        } else {
            crosswordGridViewModel.indexOfLongAcrossSlotContaining(currentBoxCoord)
                                  .ifPresent(cluesViewModel::selectedAcrossClueIndex);
        }
    }

}
