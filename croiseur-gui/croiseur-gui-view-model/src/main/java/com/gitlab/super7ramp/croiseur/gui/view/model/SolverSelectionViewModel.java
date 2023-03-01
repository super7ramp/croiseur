/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.view.model;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;

/**
 * The solver selection view model.
 */
public final class SolverSelectionViewModel {

    /** The available solvers. */
    private final ListProperty<SolverItemViewModel> availableSolvers;

    /** The selected solver (name). */
    private final StringProperty selectedSolverName;

    /**
     * Constructs an instance.
     */
    public SolverSelectionViewModel() {
        selectedSolverName = new SimpleStringProperty(this, "selectedSolver");
        availableSolvers = new SimpleListProperty<>(this, "availableSolvers",
                FXCollections.observableArrayList());
    }

    /**
     * Returns the selected solver property.
     * <p>
     * Value is {@code null} if no solver is selected.
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
    public String getSelectedSolver() {
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
}
