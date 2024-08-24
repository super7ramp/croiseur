/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.gui.view.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;

/** The solver selection view model. */
public final class SolverConfigurationViewModel {

    /** The available solvers. */
    private final ListProperty<SolverItemViewModel> availableSolvers;

    /** The selected solver (name). */
    private final StringProperty selectedSolverName;

    /** Whether the clues should be automatically for the solution returned by solver. */
    private final BooleanProperty fillCluesOnSuccess;

    /** Constructs an instance. */
    public SolverConfigurationViewModel() {
        selectedSolverName = new SimpleStringProperty(this, "selectedSolver");
        availableSolvers = new SimpleListProperty<>(this, "availableSolvers", FXCollections.observableArrayList());
        fillCluesOnSuccess = new SimpleBooleanProperty(this, "fillCluesOnSuccess");
    }

    /**
     * Returns the selected solver property.
     *
     * <p>Value is {@code null} if no solver is selected.
     *
     * @return the selected solver property
     */
    public StringProperty selectedSolverProperty() {
        return selectedSolverName;
    }

    /**
     * Returns the value of the selected solver property.
     *
     * @return the value of the selected solver property
     */
    public String selectedSolver() {
        return selectedSolverName.get();
    }

    /**
     * Returns the available solvers property.
     *
     * @return the available solvers property
     */
    public ListProperty<SolverItemViewModel> availableSolversProperty() {
        return availableSolvers;
    }

    /**
     * Returns the "fill clues on success" property.
     *
     * @return the "fill clues on success" property
     */
    public BooleanProperty fillCluesOnSuccessProperty() {
        return fillCluesOnSuccess;
    }

    /**
     * Returns the value of the "fill clues on success" property.
     *
     * @return the value of the "fill clues on success" property.
     */
    public boolean fillCluesOnSuccess() {
        return fillCluesOnSuccess.get();
    }
}
