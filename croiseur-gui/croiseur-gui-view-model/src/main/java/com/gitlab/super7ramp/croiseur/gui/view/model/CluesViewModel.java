/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.view.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

/**
 * The clues view model: Represents all the clues of the grid.
 */
public final class CluesViewModel {

    /** The across (horizontal) clues. */
    private final ListProperty<ClueViewModel> acrossClues;

    /** The selected across clue index. Value is -1 if no across clue is selected. */
    private final IntegerProperty selectedAcrossClueIndex;

    /** The down (vertical) clues. */
    private final ListProperty<ClueViewModel> downClues;

    /** The selected down clue index. Value is -1 if no down clue is selected. */
    private final IntegerProperty selectedDownClueIndex;

    /**
     * Constructs an instance.
     */
    CluesViewModel() {
        acrossClues =
                new SimpleListProperty<>(this, "acrossClues", FXCollections.observableArrayList());
        selectedAcrossClueIndex = new SimpleIntegerProperty(this, "selectedAcrossClueIndex", -1);
        downClues =
                new SimpleListProperty<>(this, "downClues", FXCollections.observableArrayList());
        selectedDownClueIndex = new SimpleIntegerProperty(this, "selectedDownClueIndex", -1);
    }

    /**
     * The across (horizontal) clues.
     *
     * @return across (horizontal) clues
     */
    public ListProperty<ClueViewModel> acrossCluesProperty() {
        return acrossClues;
    }

    /**
     * Returns the across clue at given index in the across clue list.
     *
     * @param index the clue index
     * @return the across clue at given index in the across clue list
     * @throws IndexOutOfBoundsException if index is not in clue list range
     */
    public ClueViewModel acrossClue(final int index) {
        return acrossClues.get(index);
    }

    /**
     * The selected across clue index property.
     * <p>
     * Value is -1 if no across clue is selected.
     *
     * @return the selected across clue index property
     */
    public IntegerProperty selectedAcrossClueIndexProperty() {
        return selectedAcrossClueIndex;
    }

    /**
     * Returns the value of the selected across clue index property.
     *
     * @return the value of the selected across clue index property
     */
    public int selectedAcrossClueIndex() {
        return selectedAcrossClueIndex.get();
    }

    /**
     * Sets the value of the selected across clue index property.
     *
     * @param value the value to set
     */
    public void selectedAcrossClueIndex(final int value) {
        selectedAcrossClueIndex.set(value);
    }

    /**
     * The down (vertical) clues.
     *
     * @return the down (vertical) clues.
     */
    public ListProperty<ClueViewModel> downCluesProperty() {
        return downClues;
    }

    /**
     * Returns the down clue at given index in the down clue list.
     *
     * @param index the clue index
     * @return the down clue at given index in the down clue list
     * @throws IndexOutOfBoundsException if index is not in clue list range
     */
    public ClueViewModel downClue(final int index) {
        return downClues.get(index);
    }

    /**
     * The selected down clue index property.
     * <p>
     * Value is -1 if no down clue is selected.
     *
     * @return the selected down clue index property
     */
    public IntegerProperty selectedDownClueIndexProperty() {
        return selectedDownClueIndex;
    }

    /**
     * Returns the value of the selected down clue index property.
     *
     * @return the value of the selected down clue index property
     */
    public int selectedDownClueIndex() {
        return selectedDownClueIndex.get();
    }

    /**
     * Sets the value of the selected down clue index property.
     *
     * @param value the value to set
     */
    public void selectedDownClueIndex(final int value) {
        selectedDownClueIndex.set(value);
    }

    /**
     * Resets all clues to empty string.
     */
    public void reset() {
        acrossClues.forEach(ClueViewModel::reset);
        selectedAcrossClueIndex.set(-1);
        downClues.forEach(ClueViewModel::reset);
        selectedDownClueIndex.set(-1);
    }
}
