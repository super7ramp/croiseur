/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.view.model;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

/**
 * The clues view model: Represents all the clues of the grid.
 */
public final class CluesViewModel {

    /** The across (horizontal) clues. */
    private final ListProperty<ClueViewModel> acrossClues;

    /** The down (vertical) clues. */
    private final ListProperty<ClueViewModel> downClues;

    /**
     * Constructs an instance.
     */
    CluesViewModel() {
        acrossClues = new SimpleListProperty<>(FXCollections.observableArrayList());
        downClues = new SimpleListProperty<>(FXCollections.observableArrayList());
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
     * Resets all clues to empty string.
     */
    public void reset() {
        acrossClues.forEach(ClueViewModel::reset);
        downClues.forEach(ClueViewModel::reset);
    }
}
