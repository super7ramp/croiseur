/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.presenter;

import com.gitlab.super7ramp.croiseur.common.GridPosition;
import com.gitlab.super7ramp.croiseur.gui.view.model.CrosswordBoxViewModel;
import com.gitlab.super7ramp.croiseur.gui.view.model.CrosswordGridViewModel;
import com.gitlab.super7ramp.croiseur.gui.view.model.SolverItemViewModel;
import com.gitlab.super7ramp.croiseur.gui.view.model.SolverSelectionViewModel;
import com.gitlab.super7ramp.croiseur.spi.presenter.solver.SolverDescription;
import com.gitlab.super7ramp.croiseur.spi.presenter.solver.SolverInitialisationState;
import com.gitlab.super7ramp.croiseur.spi.presenter.solver.SolverPresenter;
import com.gitlab.super7ramp.croiseur.spi.presenter.solver.SolverProgress;
import com.gitlab.super7ramp.croiseur.spi.solver.SolverResult;
import javafx.beans.property.MapProperty;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * GUI implementation of {@link SolverPresenter}.
 */
final class GuiSolverPresenter implements SolverPresenter {

    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(GuiSolverPresenter.class.getName());

    /** The grid view model. */
    private final CrosswordGridViewModel crosswordGridViewModel;

    /** The solver selection view model. */
    private final SolverSelectionViewModel solverSelectionViewModel;

    /**
     * Constructs an instance.
     *
     * @param crosswordGridViewModelArg   the grid view model
     * @param solverSelectionViewModelArg the solver selection view model
     */
    GuiSolverPresenter(final CrosswordGridViewModel crosswordGridViewModelArg,
                       final SolverSelectionViewModel solverSelectionViewModelArg) {
        crosswordGridViewModel = crosswordGridViewModelArg;
        solverSelectionViewModel = solverSelectionViewModelArg;
    }

    @Override
    public void presentAvailableSolvers(final List<SolverDescription> solverDescriptions) {
        final List<SolverItemViewModel> solverNames =
                solverDescriptions.stream()
                                  .map(s -> new SolverItemViewModel(s.name(),
                                          s.description()))
                                  .toList();
        solverSelectionViewModel.availableSolversProperty().addAll(solverNames);
    }

    @Override
    public void presentSolverInitialisationState(final SolverInitialisationState solverInitialisationState) {
        // TODO really implement
        LOGGER.info(() -> "Solver initialisation: " + solverInitialisationState);
    }

    @Override
    public void presentProgress(final SolverProgress solverProgress) {
        // TODO really implement
        LOGGER.info(() -> "Completion: " + solverProgress.completionPercentage() + " %");
    }

    @Override
    public void presentResult(final SolverResult result) {
        LOGGER.info(() -> "Received result: " + result);
        final Map<GridPosition, Character> resultBoxes = result.filledBoxes();
        final MapProperty<GridPosition, CrosswordBoxViewModel> viewModelBoxes =
                crosswordGridViewModel.boxes();
        for (final Map.Entry<GridPosition, Character> entry : resultBoxes.entrySet()) {
            final GridPosition position = entry.getKey();
            final CrosswordBoxViewModel box = viewModelBoxes.get(position);
            box.contentProperty().set(entry.getValue().toString());
        }
        for (final Map.Entry<GridPosition, CrosswordBoxViewModel> entry :
                viewModelBoxes.entrySet()) {
            final GridPosition position = entry.getKey();
            final CrosswordBoxViewModel box = entry.getValue();
            box.unsolvableProperty().set(result.unsolvableBoxes().contains(position));
        }
    }

    @Override
    public void presentSolverError(final String error) {
        // TODO really implement
        LOGGER.warning(error);
    }
}
