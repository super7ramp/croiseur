/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.gui.view.model;

import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.util.Callback;

/** The clues view model: Represents all the clues of the grid. */
public final class CluesViewModel {

    /** The names of the available clue providers. */
    private final ListProperty<String> clueProviders;

    /** The across (horizontal) clues. */
    private final ListProperty<ClueViewModel> acrossClues;

    /** The selected across clue index. Value is -1 if no across clue is selected. */
    private final IntegerProperty selectedAcrossClueIndex;

    /** The down (vertical) clues. */
    private final ListProperty<ClueViewModel> downClues;

    /** The selected down clue index. Value is -1 if no down clue is selected. */
    private final IntegerProperty selectedDownClueIndex;

    /** Whether the clue service is running. */
    private final BooleanProperty clueServiceIsRunning;

    /** Constructs an instance. */
    CluesViewModel() {
        clueProviders = new SimpleListProperty<>(this, "clueProviders", FXCollections.observableArrayList());

        final Callback<ClueViewModel, Observable[]> extractor =
                entry -> new Observable[] {entry.userContentProperty(), entry.systemContentProperty()};
        acrossClues = new SimpleListProperty<>(this, "acrossClues", FXCollections.observableArrayList(extractor));
        selectedAcrossClueIndex = new SimpleIntegerProperty(this, "selectedAcrossClueIndex", -1);
        downClues = new SimpleListProperty<>(this, "downClues", FXCollections.observableArrayList(extractor));
        selectedDownClueIndex = new SimpleIntegerProperty(this, "selectedDownClueIndex", -1);

        clueServiceIsRunning = new SimpleBooleanProperty(this, "clueServiceIsRunning");
    }

    /**
     * The names of the available clue providers.
     *
     * @return the names of the available clue providers
     */
    public ListProperty<String> clueProvidersProperty() {
        return clueProviders;
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
     *
     * <p>Value is -1 if no across clue is selected.
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

    /** Sets the value of the selected across clue index property to -1. */
    public void deselectAcrossClue() {
        selectedAcrossClueIndex.set(-1);
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
     *
     * <p>Value is -1 if no down clue is selected.
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

    /** Sets the value of the selected down clue index property to -1. */
    public void deselectDownClue() {
        selectedDownClueIndex.set(-1);
    }

    /** Resets all clues to empty strings. */
    public void reset() {
        acrossClues.forEach(ClueViewModel::reset);
        deselectAcrossClue();
        downClues.forEach(ClueViewModel::reset);
        deselectDownClue();
    }

    /**
     * Whether a task is calling clue service.
     *
     * @return the property denoting whether a task is calling clue service
     */
    public BooleanProperty clueServiceIsRunningProperty() {
        return clueServiceIsRunning;
    }

    /**
     * Sets the value of {@link #clueServiceIsRunningProperty()}.
     *
     * @param value the value to set
     */
    public void clueServiceIsRunning(final boolean value) {
        clueServiceIsRunning.set(value);
    }
}
