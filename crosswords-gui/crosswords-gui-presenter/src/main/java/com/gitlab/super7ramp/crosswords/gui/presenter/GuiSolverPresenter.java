package com.gitlab.super7ramp.crosswords.gui.presenter;

import com.gitlab.super7ramp.crosswords.common.GridPosition;
import com.gitlab.super7ramp.crosswords.gui.view.model.CrosswordBoxViewModel;
import com.gitlab.super7ramp.crosswords.gui.view.model.CrosswordGridViewModel;
import com.gitlab.super7ramp.crosswords.spi.presenter.solver.SolverInitialisationState;
import com.gitlab.super7ramp.crosswords.spi.presenter.solver.SolverPresenter;
import com.gitlab.super7ramp.crosswords.spi.presenter.solver.SolverProgress;
import com.gitlab.super7ramp.crosswords.spi.solver.SolverResult;
import javafx.beans.property.MapProperty;

import java.util.Map;
import java.util.logging.Logger;

/**
 * GUI implementation of {@link SolverPresenter}.
 */
final class GuiSolverPresenter implements SolverPresenter {

    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(GuiSolverPresenter.class.getName());

    /** The grid model. */
    private final CrosswordGridViewModel crosswordGridViewModel;

    /**
     * Constructs an instance.
     */
    GuiSolverPresenter(final CrosswordGridViewModel crosswordGridViewModelArg) {
        crosswordGridViewModel = crosswordGridViewModelArg;
    }

    @Override
    public void presentSolverInitialisationState(SolverInitialisationState solverInitialisationState) {
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
        for (final Map.Entry<GridPosition, CrosswordBoxViewModel> entry : viewModelBoxes.entrySet()) {
            final GridPosition position = entry.getKey();
            final CrosswordBoxViewModel box = entry.getValue();
            box.unsolvableProperty().set(result.unsolvableBoxes().contains(position));
        }
    }

    @Override
    public void presentError(final String error) {
        // TODO really implement
        LOGGER.warning(error);
    }
}
