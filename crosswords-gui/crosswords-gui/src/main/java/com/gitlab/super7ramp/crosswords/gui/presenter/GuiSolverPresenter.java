package com.gitlab.super7ramp.crosswords.gui.presenter;

import com.gitlab.super7ramp.crosswords.common.GridPosition;
import com.gitlab.super7ramp.crosswords.gui.control.model.CrosswordBox;
import com.gitlab.super7ramp.crosswords.gui.viewmodel.CrosswordGridViewModel;
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
        final Map<GridPosition, Character> resultBoxes = result.boxes();
        final MapProperty<GridPosition, CrosswordBox> viewModelBoxes =
                crosswordGridViewModel.boxes();
        for (final Map.Entry<GridPosition, Character> entry : resultBoxes.entrySet()) {
            final GridPosition key = entry.getKey();
            final CrosswordBox value = viewModelBoxes.computeIfAbsent(key, k -> new CrosswordBox());
            value.contentProperty().set(entry.getValue().toString());
        }
    }

    @Override
    public void presentError(final String error) {
        // TODO really implement
        LOGGER.warning(error);
    }
}
