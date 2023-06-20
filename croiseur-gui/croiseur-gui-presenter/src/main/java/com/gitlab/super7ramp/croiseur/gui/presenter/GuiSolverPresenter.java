/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.presenter;

import com.gitlab.super7ramp.croiseur.common.puzzle.GridPosition;
import com.gitlab.super7ramp.croiseur.gui.view.model.CrosswordBoxViewModel;
import com.gitlab.super7ramp.croiseur.gui.view.model.CrosswordGridViewModel;
import com.gitlab.super7ramp.croiseur.gui.view.model.ErrorsViewModel;
import com.gitlab.super7ramp.croiseur.gui.view.model.SolverItemViewModel;
import com.gitlab.super7ramp.croiseur.gui.view.model.SolverProgressViewModel;
import com.gitlab.super7ramp.croiseur.gui.view.model.SolverSelectionViewModel;
import com.gitlab.super7ramp.croiseur.spi.presenter.solver.SolverDescription;
import com.gitlab.super7ramp.croiseur.spi.presenter.solver.SolverInitialisationState;
import com.gitlab.super7ramp.croiseur.spi.presenter.solver.SolverPresenter;
import com.gitlab.super7ramp.croiseur.spi.presenter.solver.SolverProgress;
import com.gitlab.super7ramp.croiseur.spi.solver.SolverResult;
import javafx.application.Platform;

import java.util.List;
import java.util.Map;
import java.util.Set;
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

    /** The solver progress view mode. */
    private final SolverProgressViewModel solverProgressViewModel;

    /** The errors view model. */
    private final ErrorsViewModel errorsViewModel;

    /**
     * Constructs an instance.
     *
     * @param crosswordGridViewModelArg   the grid view model
     * @param solverSelectionViewModelArg the solver selection view model
     * @param errorsViewModelArg          the errors view model
     */
    GuiSolverPresenter(final CrosswordGridViewModel crosswordGridViewModelArg,
                       final SolverSelectionViewModel solverSelectionViewModelArg,
                       final SolverProgressViewModel solverProgressViewModelArg,
                       final ErrorsViewModel errorsViewModelArg) {
        crosswordGridViewModel = crosswordGridViewModelArg;
        solverSelectionViewModel = solverSelectionViewModelArg;
        solverProgressViewModel = solverProgressViewModelArg;
        errorsViewModel = errorsViewModelArg;
    }

    @Override
    public void presentAvailableSolvers(final List<SolverDescription> solverDescriptions) {
        LOGGER.info(() -> "Received solver descriptions: " + solverDescriptions);
        final List<SolverItemViewModel> solverNames =
                solverDescriptions.stream()
                                  .map(s -> new SolverItemViewModel(s.name(),
                                                                    s.description()))
                                  .toList();
        Platform.runLater(() ->
                                  solverSelectionViewModel.availableSolversProperty()
                                                          .addAll(solverNames));
    }

    @Override
    public void presentSolverInitialisationState(
            final SolverInitialisationState solverInitialisationState) {
        LOGGER.info(() -> "Received solver initialisation state: " + solverInitialisationState);
        // No specific presentation for initialisation progress
    }

    @Override
    public void presentSolverProgress(final SolverProgress solverProgress) {
        LOGGER.info(() -> "Received solver progress: " + solverProgress);
        final double normalizedSolverProgress =
                ((double) solverProgress.completionPercentage()) / 100.0;
        Platform.runLater(() -> solverProgressViewModel.solverProgress(normalizedSolverProgress));
    }

    @Override
    public void presentSolverResult(final SolverResult result) {
        LOGGER.info(() -> "Received solver result: " + result);
        Platform.runLater(() -> {
            updateBoxContent(result);
            updateBoxSolvableState(result);
        });
    }

    @Override
    public void presentSolverError(final String error) {
        LOGGER.warning(() -> "Received solver error: " + error);
        Platform.runLater(() -> errorsViewModel.addError(error));
    }

    /**
     * Updates box solvable state.
     * <p>
     * All boxes will be updated with returned result. This ensures that previously set unsolvable
     * status will be cleared.
     *
     * @param result the solver result
     */
    private void updateBoxSolvableState(final SolverResult result) {
        final Map<GridPosition, CrosswordBoxViewModel> viewModelBoxes =
                crosswordGridViewModel.boxesProperty();
        final Set<GridPosition> unsolvableBoxes = result.unsolvableBoxes();
        for (final Map.Entry<GridPosition, CrosswordBoxViewModel> entry :
                viewModelBoxes.entrySet()) {
            final CrosswordBoxViewModel box = entry.getValue();
            if (!box.isSelected()) {
                final GridPosition position = entry.getKey();
                box.unsolvableProperty().set(unsolvableBoxes.contains(position));
            } else {
                /*
                 * Box is part of user-selected slot and its unsolvable status is bound to the
                 * number of suggestions and thus cannot be written.
                 */
            }
        }
    }

    /**
     * Updates box content.
     * <p>
     * Only boxes that have been filled by solver will be updated.
     *
     * @param result the solver result
     */
    private void updateBoxContent(final SolverResult result) {
        final Map<GridPosition, Character> resultBoxes = result.filledBoxes();
        final Map<GridPosition, CrosswordBoxViewModel> viewModelBoxes =
                crosswordGridViewModel.boxesProperty();
        for (final Map.Entry<GridPosition, Character> entry : resultBoxes.entrySet()) {
            final GridPosition position = entry.getKey();
            final CrosswordBoxViewModel box = viewModelBoxes.get(position);
            box.solverContent(entry.getValue().toString());
        }
    }
}
