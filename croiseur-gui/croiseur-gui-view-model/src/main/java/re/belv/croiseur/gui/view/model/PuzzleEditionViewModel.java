/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.gui.view.model;

import java.util.List;
import java.util.stream.IntStream;
import javafx.beans.InvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import re.belv.croiseur.gui.view.model.slot.SlotOutline;

/** Puzzle edition view model. */
public final class PuzzleEditionViewModel {

    /** The puzzle details view model. */
    private final PuzzleDetailsViewModel puzzleDetailsViewModel;

    /** The crossword grid view model. */
    private final CrosswordGridViewModel crosswordGridViewModel;

    /** The clues view model. */
    private final CluesViewModel cluesViewModel;

    /** Property following the saving state. */
    private final BooleanProperty savingInProgress;

    /** Constructs an instance. */
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
        cluesViewModel
                .selectedAcrossClueIndexProperty()
                .addListener(observable -> updateCurrentAcrossSlot(cluesViewModel.selectedAcrossClueIndex()));

        // Bind down clues to down slots
        final ObservableList<SlotOutline> downSlots = crosswordGridViewModel.longDownSlots();
        final List<ClueViewModel> downClues = cluesViewModel.downCluesProperty();
        downSlots.forEach(slot -> downClues.add(new ClueViewModel()));
        downSlots.addListener(this::updateDownClues);
        cluesViewModel
                .selectedDownClueIndexProperty()
                .addListener(observable -> updateCurrentDownSlot(cluesViewModel.selectedDownClueIndex()));

        // Bind current slot to current clue
        crosswordGridViewModel.currentSlotPositionsProperty().addListener((InvalidationListener)
                observable -> updateCurrentClue(crosswordGridViewModel.currentSlotPositions()));
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
     * <p>Note: The clue lists are synchronized with {@link #crosswordGridViewModel()}'s slots so that there is a 1:1
     * mapping between clues and slots. The clue position in a clue list is the same as a slot in a slot list.
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

    /** Resets edition view model to default. */
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
                        .forEach(i -> acrossClues.get(i).reset());
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
                        .forEach(i -> downClues.get(i).reset());
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
            final List<GridCoord> slotPositions = crosswordGridViewModel
                    .longDownSlots()
                    .get(selectedDownClueIndex)
                    .boxPositions();
            crosswordGridViewModel.currentSlotVertical();
            if (!slotPositions.contains(crosswordGridViewModel.currentBoxPosition())) {
                crosswordGridViewModel.currentBoxPosition(slotPositions.get(0));
            }
        }
    }

    /**
     * Updates current slot upon current across clue change.
     *
     * @param selectedAcrossClueIndex the current across clue new index
     */
    private void updateCurrentAcrossSlot(final int selectedAcrossClueIndex) {
        if (selectedAcrossClueIndex >= 0) {
            final List<GridCoord> slotPositions = crosswordGridViewModel
                    .longAcrossSlots()
                    .get(selectedAcrossClueIndex)
                    .boxPositions();
            crosswordGridViewModel.currentSlotHorizontal();
            if (!slotPositions.contains(crosswordGridViewModel.currentBoxPosition())) {
                crosswordGridViewModel.currentBoxPosition(slotPositions.get(0));
            }
        }
    }

    /**
     * Updates current clue upon current slot positions change.
     *
     * @param currentSlotPositions the current slot positions change
     */
    private void updateCurrentClue(final List<GridCoord> currentSlotPositions) {
        if (currentSlotPositions.isEmpty()) {
            cluesViewModel.deselectAcrossClue();
            cluesViewModel.deselectDownClue();
        } else if (crosswordGridViewModel.currentSlotVerticalProperty().get()) {
            crosswordGridViewModel
                    .indexOfLongDownSlotContaining(currentSlotPositions.get(0))
                    .ifPresentOrElse(cluesViewModel::selectedDownClueIndex, cluesViewModel::deselectDownClue);
        } else {
            crosswordGridViewModel
                    .indexOfLongAcrossSlotContaining(currentSlotPositions.get(0))
                    .ifPresentOrElse(cluesViewModel::selectedAcrossClueIndex, cluesViewModel::deselectAcrossClue);
        }
    }
}
