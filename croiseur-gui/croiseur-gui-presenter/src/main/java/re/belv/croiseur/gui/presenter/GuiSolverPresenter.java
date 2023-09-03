/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.gui.presenter;

import javafx.application.Platform;
import re.belv.croiseur.common.puzzle.GridPosition;
import re.belv.croiseur.gui.view.model.CrosswordBoxViewModel;
import re.belv.croiseur.gui.view.model.CrosswordGridViewModel;
import re.belv.croiseur.gui.view.model.ErrorsViewModel;
import re.belv.croiseur.gui.view.model.GridCoord;
import re.belv.croiseur.gui.view.model.SolverConfigurationViewModel;
import re.belv.croiseur.gui.view.model.SolverItemViewModel;
import re.belv.croiseur.gui.view.model.SolverProgressViewModel;
import re.belv.croiseur.spi.presenter.solver.SolverDescription;
import re.belv.croiseur.spi.presenter.solver.SolverInitialisationState;
import re.belv.croiseur.spi.presenter.solver.SolverPresenter;
import re.belv.croiseur.spi.presenter.solver.SolverProgress;
import re.belv.croiseur.spi.presenter.solver.SolverResult;

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
    private final SolverConfigurationViewModel solverConfigurationViewModel;

    /** The solver progress view mode. */
    private final SolverProgressViewModel solverProgressViewModel;

    /** The errors view model. */
    private final ErrorsViewModel errorsViewModel;

    /**
     * Constructs an instance.
     *
     * @param crosswordGridViewModelArg       the grid view model
     * @param solverConfigurationViewModelArg the solver selection view model
     * @param errorsViewModelArg              the errors view model
     */
    GuiSolverPresenter(final CrosswordGridViewModel crosswordGridViewModelArg,
                       final SolverConfigurationViewModel solverConfigurationViewModelArg,
                       final SolverProgressViewModel solverProgressViewModelArg,
                       final ErrorsViewModel errorsViewModelArg) {
        crosswordGridViewModel = crosswordGridViewModelArg;
        solverConfigurationViewModel = solverConfigurationViewModelArg;
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
                                  solverConfigurationViewModel.availableSolversProperty()
                                                              .addAll(solverNames));
    }

    @Override
    public void presentSolverInitialisationState(final String solverRun,
                                                 final SolverInitialisationState solverInitialisationState) {
        LOGGER.info(() -> "Received solver initialisation state: " + solverInitialisationState);
        // No specific presentation for initialisation progress
    }

    @Override
    public void presentSolverProgress(final String solverRun, final SolverProgress solverProgress) {
        LOGGER.info(() -> "Received solver progress: " + solverProgress);
        final double normalizedSolverProgress = solverProgress.completionPercentage() / 100.0;
        Platform.runLater(() -> solverProgressViewModel.solverProgress(normalizedSolverProgress));
    }

    @Override
    public void presentSolverResult(final String solverRun, final SolverResult result) {
        LOGGER.info(() -> "Received solver result: " + result);
        Platform.runLater(() -> {
            updateBoxContent(result);
            updateBoxSolvableState(result);
        });
    }

    @Override
    public void presentSolverError(final String solverRun, final String error) {
        presentSolverError(error);
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
        final Map<GridCoord, CrosswordBoxViewModel> viewModelBoxes =
                crosswordGridViewModel.boxesProperty();
        final Set<GridPosition> unsolvableBoxes = result.unsolvableBoxes();
        for (final Map.Entry<GridCoord, CrosswordBoxViewModel> entry :
                viewModelBoxes.entrySet()) {
            final CrosswordBoxViewModel box = entry.getValue();
            if (!box.isSelected()) {
                final GridPosition position = gridPositionFrom(entry.getKey());
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
        final Map<GridCoord, CrosswordBoxViewModel> viewModelBoxes =
                crosswordGridViewModel.boxesProperty();
        for (final Map.Entry<GridPosition, Character> entry : resultBoxes.entrySet()) {
            final GridPosition position = entry.getKey();
            final CrosswordBoxViewModel box = viewModelBoxes.get(gridCoordFrom(position));
            box.solverContent(entry.getValue().toString());
        }
    }

    private static GridCoord gridCoordFrom(final GridPosition domainPosition) {
        return new GridCoord(domainPosition.x(), domainPosition.y());
    }

    private static GridPosition gridPositionFrom(final GridCoord viewModelPosition) {
        return new GridPosition(viewModelPosition.column(), viewModelPosition.row());
    }
}
